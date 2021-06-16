package it.tosiani.appAndroid;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import it.tosiani.drivers.ManagmentDriver;
import it.tosiani.steps.AndroidStep;
import it.tosiani.utility.Utils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.util.Properties;

import static it.tosiani.utility.GlobalParameters.*;
import static org.junit.Assert.fail;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test_001_Login {
    static private WebElement webElement = null;
    static private ExtentReports extentReports;
    static private ExtentTest extentTest;
    static private AndroidDriver<?> androidDriver = null;
    static private DesiredCapabilities desiredCapabilities = null;
    static private AndroidStep step = null;
    static private Properties prop = null;

    @BeforeAll
    static void beforeAll(){
        extentReports = new ExtentReports(REPORT_PATH + File.separator + "reportAndroidApp" + EXT_HTML, false);
        extentReports.loadConfig(new File(REPORT_CONFIG_XML));

        desiredCapabilities = new DesiredCapabilities();

        desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        desiredCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, " emulator-5554 ");
        desiredCapabilities.setCapability(MobileCapabilityType.APP, RESOURCES_PATH + File.separator + "app" + EXT_ANDROID);

        ManagmentDriver.startAndroidDriver(desiredCapabilities);
        androidDriver = ManagmentDriver.getAndroidDriver();
        prop = Utils.loadProp("appAndroid");
        step = new AndroidStep(androidDriver, prop);
    }

    @BeforeEach
    void beforeEach(){}


    @ParameterizedTest(name = "test con credenziali: {0}  {1}")
    @Order(1)
    @DisplayName("Test Login Errate")
    @CsvSource({"user,",",user",",","admin,user"})
    @Tag("Login")
    void Test_001_Errori(String utente,String pwd,TestInfo testInfo) throws InterruptedException {
        extentTest = extentReports.startTest(testInfo.getDisplayName());
        extentTest.log(LogStatus.INFO, "Apertura App per controllare se scrivendo cose errate nella login appare il messaggio di errore", "");
        step.login(utente,pwd);
        Thread.sleep(1000);
        try {
            if (androidDriver.findElement(By.id(prop.getProperty("btn.accettazione"))).isDisplayed()) {
                extentTest.log(LogStatus.PASS, "Comparso messaggio di errore: '"+androidDriver.findElement(By.id(prop.getProperty("error.message"))).getText()+"' con inserimento di utente: '"+utente+"' e password: "+pwd+"'",extentTest.addBase64ScreenShot(Utils.getScreenBase64Android()));
                androidDriver.findElement(By.id(prop.getProperty("btn.accettazione"))).click();
            }
        } catch (NoSuchElementException e){
            extentTest.log(LogStatus.FAIL, "Non è comparso il messaggio di errore desiderato oppure tasto reset non funzionante",extentTest.addBase64ScreenShot(Utils.getScreenBase64Android()));
            fail();
        }
    }

    @ParameterizedTest(name = "test con credenziali: {0}  {1}")
    @Order(2)
    @DisplayName("Test Login corretta")
    @CsvSource({"admin,admin","user,user"})
    @Tag("Login")
    void Test_002_Corretta(String utente,String pwd,TestInfo testInfo) throws InterruptedException {
        extentTest = extentReports.startTest(testInfo.getDisplayName());
        extentTest.log(LogStatus.INFO, "Apertura App con controllo se con credenziali esatte entra nella home", "");
        step.login(utente,pwd);
        Thread.sleep(1000);
        try {
            if (androidDriver.findElement(By.id(prop.getProperty("id.welcome"))).isDisplayed()) {
                extentTest.log(LogStatus.PASS, "Comparsa scritta Benvenuto",extentTest.addBase64ScreenShot(Utils.getScreenBase64Android()));
            }
        } catch (NoSuchElementException e){
            extentTest.log(LogStatus.FAIL, "Non comparsa scritta Benvenuto",extentTest.addBase64ScreenShot(Utils.getScreenBase64Android()));
            fail();
        }
        androidDriver.navigate().back();
    }

    @ParameterizedTest(name = "test reset")
    @Order(3)
    @DisplayName("Test bottone reset")
    @CsvSource({"admin,admin"})
    @Tag("Login")
    void Test_003_Reset(String utente,String pwd,TestInfo testInfo) throws InterruptedException {
        extentTest = extentReports.startTest(testInfo.getDisplayName());
        extentTest.log(LogStatus.INFO, "Controllo del bottone reset", "");
        androidDriver.findElement(By.id(prop.getProperty("id.username"))).sendKeys(utente);
        androidDriver.findElement(By.id(prop.getProperty("id.pwd"))).sendKeys(pwd);
        Thread.sleep(1000);
        extentTest.log(LogStatus.PASS, "screen prima di premere pulsante",extentTest.addBase64ScreenShot(Utils.getScreenBase64Android()));
        androidDriver.findElement(By.id(prop.getProperty("btn.reset"))).click();
        Thread.sleep(1000);
        if (androidDriver.findElement(By.id(prop.getProperty("id.username"))).getText().length() != utente.length()
        && androidDriver.findElement(By.id(prop.getProperty("id.pwd"))).getText().length() != pwd.length()) {
            extentTest.log(LogStatus.PASS, "Tasto reset funzionante",extentTest.addBase64ScreenShot(Utils.getScreenBase64Android()));
        }else{
            extentTest.log(LogStatus.FAIL, "Tasto reset non funzionante",extentTest.addBase64ScreenShot(Utils.getScreenBase64Android()));
            fail();
        }
    }


    @AfterEach
    void tearDown(){
        extentReports.endTest(extentTest);
    }

    @AfterAll
    static void tearDownAll(){
        ManagmentDriver.stopDriver();
        extentReports.flush();
    }
}
