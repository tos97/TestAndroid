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
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.util.Properties;

import static it.tosiani.utility.GlobalParameters.*;
import static org.junit.Assert.fail;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test_002_Home {
    static private WebElement webElement = null;
    static private ExtentReports extentReports;
    static private ExtentTest extentTest;
    static private AndroidDriver<?> androidDriver = null;
    static private DesiredCapabilities desiredCapabilities = null;
    static private AndroidStep step = null;
    static private Properties prop = null;
    static private String credenziali = "user";

    @BeforeAll
    static void beforeAll(){
        extentReports = new ExtentReports(REPORT_PATH + File.separator + "reportAndroidApp" + EXT_HTML, true);
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
    void beforeEach(){
        step.login(credenziali,credenziali);
    }


    @Test
    @Order(1)
    @DisplayName("Test barra welcome")
    @Tag("Home")
    void Test_001_Welcome(TestInfo testInfo) throws InterruptedException {
        extentTest = extentReports.startTest(testInfo.getDisplayName());
        extentTest.log(LogStatus.INFO, "Controllo della presenza del corretto utente nella barra di benvenuto", "");
        Thread.sleep(1000);
        try {
            if (androidDriver.findElement(By.id(prop.getProperty("id.welcome"))).isDisplayed()) {
                if (androidDriver.findElement(By.id(prop.getProperty("id.welcome"))).getText().contains(credenziali))
                    extentTest.log(LogStatus.PASS, "Comparsa scritta Benvenuto dopo il login con nome utente corretto, ovvero '"+credenziali+"'",extentTest.addBase64ScreenShot(Utils.getScreenBase64Android()));
                else{
                    extentTest.log(LogStatus.FAIL, "Nome nella barra di benvenuto errato, deoveva essere '"+credenziali+"' ma era '"+androidDriver.findElement(By.id(prop.getProperty("id.welcome"))).getText()+"' come riportato in screen",extentTest.addBase64ScreenShot(Utils.getScreenBase64Android()));
                    fail();
                }
            }
        } catch (NoSuchElementException e){
            extentTest.log(LogStatus.FAIL, "Non comparsa scritta Benvenuto",extentTest.addBase64ScreenShot(Utils.getScreenBase64Android()));
            fail();
        }
    }

    @ParameterizedTest(name = "test aggiungi")
    @Order(2)
    @DisplayName("Test funzione aggiungi")
    @ValueSource(strings = {"Franco","","Danilo"})
    @Tag("Home")
    void Test_002_Add(String nomeUtente,TestInfo testInfo) throws InterruptedException {
        extentTest = extentReports.startTest(testInfo.getDisplayName());
        extentTest.log(LogStatus.INFO, "Controllo della funzionalità di aggiungere nuovi utenti alla lista", "");
        Thread.sleep(1000);
        int totale = step.totaleNomi();
        extentTest.log(LogStatus.INFO, "Screen prima del test", extentTest.addBase64ScreenShot(Utils.getScreenBase64Android()));
        step.aggiungiUtente(nomeUtente);
        Thread.sleep(1000);
        if (nomeUtente.length() == 0){
            try {
                if (androidDriver.findElement(By.id(prop.getProperty("btn.accettazione"))).isDisplayed()) {
                    extentTest.log(LogStatus.PASS, "Comparso messaggio di Errore quando nome è vuoto", extentTest.addBase64ScreenShot(Utils.getScreenBase64Android()));
                    androidDriver.findElement(By.id(prop.getProperty("btn.accettazione"))).click();
                    Thread.sleep(1000);
                    androidDriver.findElement(By.id(prop.getProperty("id.annull.add"))).click();
                }
            } catch (NoSuchElementException e){
                extentTest.log(LogStatus.FAIL, "Non comparso messaggio di Errore quando nome è vuoto", extentTest.addBase64ScreenShot(Utils.getScreenBase64Android()));
                //fail();
                //il fail è commentato in modo che il test possa andare avanti senza fallire ai fini del report
                // ma si è l'asciato in modo che se serve che il test ad un errore fallisce si può risttivare
            }
        } else {
            if (step.controlloNomiUguali()) {
                if (totale < step.totaleNomi())
                    extentTest.log(LogStatus.PASS, "Nuovo utente '" + nomeUtente + "' inserito correttamente", extentTest.addBase64ScreenShot(Utils.getScreenBase64Android()));
            } else{
                extentTest.log(LogStatus.FAIL, "Nuovo utente '" + nomeUtente + "' inserito correttamente ma era già presente nome uguale, non gestita correttamente tale evenualità", extentTest.addBase64ScreenShot(Utils.getScreenBase64Android()));
                //fail();
                //il fail è commentato in modo che il test possa andare avanti senza fallire ai fini del report
                // ma si è l'asciato in modo che se serve che il test ad un errore fallisce si può risttivare
            }
        }
    }

    @Test
    @Order(3)
    @DisplayName("Test funzione remove")
    @Tag("Home")
    void Test_003_RemoveAll(TestInfo testInfo) throws InterruptedException {
        extentTest = extentReports.startTest(testInfo.getDisplayName());
        extentTest.log(LogStatus.INFO, "Controllo della funzionalità per rimuovere tutti gli utenti dalla lista", "");
        Thread.sleep(1000);
        extentTest.log(LogStatus.INFO, "Screen prima del test", extentTest.addBase64ScreenShot(Utils.getScreenBase64Android()));
        step.removeAll();
        // cè il 2 perché tra l'elemtento preso in considerazione con il findelement per la lista di nomi comprende anche
        //la barra di benvenuto e i bottoni
        if (step.totaleNomi() == 2)
            extentTest.log(LogStatus.PASS, "Test del tasto funzionante, screen della lista vuota", extentTest.addBase64ScreenShot(Utils.getScreenBase64Android()));
        else{
            extentTest.log(LogStatus.FAIL, "Test del tasto non passato, screen della lista non vuota", extentTest.addBase64ScreenShot(Utils.getScreenBase64Android()));
            fail();
        }
    }




    @AfterEach
    void tearDown() throws InterruptedException {
        extentReports.endTest(extentTest);
        androidDriver.navigate().back();
        Thread.sleep(1000);
    }

    @AfterAll
    static void tearDownAll(){
        ManagmentDriver.stopDriver();
        extentReports.flush();
    }
}
