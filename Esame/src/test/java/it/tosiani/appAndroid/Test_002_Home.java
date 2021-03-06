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
        extentTest.log(LogStatus.INFO, "Controllo della funzionalit?? di aggiungere nuovi utenti alla lista", "");
        Thread.sleep(1000);
        int totale = step.totaleNomi();
        extentTest.log(LogStatus.INFO, "Screen prima del test", extentTest.addBase64ScreenShot(Utils.getScreenBase64Android()));
        step.aggiungiUtente(nomeUtente);
        Thread.sleep(1000);
        if (nomeUtente.length() == 0){
            try {
                if (androidDriver.findElement(By.id(prop.getProperty("id.btn.accettazione"))).isDisplayed()) {
                    extentTest.log(LogStatus.PASS, "Comparso messaggio di Errore quando nome ?? vuoto", extentTest.addBase64ScreenShot(Utils.getScreenBase64Android()));
                    androidDriver.findElement(By.id(prop.getProperty("id.btn.accettazione"))).click();
                    Thread.sleep(1000);
                    androidDriver.findElement(By.id(prop.getProperty("id.annull.add"))).click();
                }
            } catch (NoSuchElementException e){
                extentTest.log(LogStatus.FAIL, "Non comparso messaggio di Errore quando nome ?? vuoto", extentTest.addBase64ScreenShot(Utils.getScreenBase64Android()));
                //fail();
                //il fail ?? commentato in modo che il test possa andare avanti senza fallire ai fini del report
                // ma si ?? l'asciato in modo che se serve che il test ad un errore fallisce si pu?? risttivare
            }
        } else {
            if (step.controlloNomiUguali()) {
                if (totale < step.totaleNomi())
                    extentTest.log(LogStatus.PASS, "Nuovo utente '" + nomeUtente + "' inserito correttamente", extentTest.addBase64ScreenShot(Utils.getScreenBase64Android()));
            } else{
                extentTest.log(LogStatus.FAIL, "Nuovo utente '" + nomeUtente + "' inserito correttamente ma era gi?? presente nome uguale, non gestita correttamente tale evenualit??", extentTest.addBase64ScreenShot(Utils.getScreenBase64Android()));
                //fail();
                //il fail ?? commentato in modo che il test possa andare avanti senza fallire ai fini del report
                // ma si ?? l'asciato in modo che se serve che il test ad un errore fallisce si pu?? risttivare
            }
        }
    }

    @Test
    @Order(3)
    @DisplayName("Test funzione remove")
    @Tag("Home")
    void Test_003_RemoveAll(TestInfo testInfo) throws InterruptedException {
        extentTest = extentReports.startTest(testInfo.getDisplayName());
        extentTest.log(LogStatus.INFO, "Controllo della funzionalit?? per rimuovere tutti gli utenti dalla lista", "");
        Thread.sleep(1000);
        extentTest.log(LogStatus.INFO, "Screen prima del test", extentTest.addBase64ScreenShot(Utils.getScreenBase64Android()));
        if(step.removeAll()){
            extentTest.log(LogStatus.INFO, "Comparso banner per richiesta di accettazione rimozione", extentTest.addBase64ScreenShot(Utils.getScreenBase64Android()));
            step.accettazione();
            // c?? il 2 perch?? tra l'elemtento preso in considerazione con il findelement per la lista di nomi comprende anche
            //la barra di benvenuto e i bottoni
            if (step.totaleNomi() == 2)
                extentTest.log(LogStatus.PASS, "Test del tasto funzionante, screen della lista vuota", extentTest.addBase64ScreenShot(Utils.getScreenBase64Android()));
            else{
                extentTest.log(LogStatus.FAIL, "Test del tasto non passato, screen della lista non vuota", extentTest.addBase64ScreenShot(Utils.getScreenBase64Android()));
                fail();
            }
        } else{
            extentTest.log(LogStatus.FAIL, "Test del tasto non passato, non ?? comparso il banner dell'accettazione dell'eliminazione", extentTest.addBase64ScreenShot(Utils.getScreenBase64Android()));
            fail();
        }
    }

    @Test
    @Order(4)
    @DisplayName("Test funzione Error")
    @Tag("Home")
    void Test_004_ErrorBtn(TestInfo testInfo) throws InterruptedException {
        extentTest = extentReports.startTest(testInfo.getDisplayName());
        extentTest.log(LogStatus.INFO, "Controllo della funzionalit?? per aggiungere una linea vuota alla lista dei nomi", "");
        Thread.sleep(1000);
        extentTest.log(LogStatus.INFO, "Screen prima del test", extentTest.addBase64ScreenShot(Utils.getScreenBase64Android()));
        int totale = step.totaleNomi();
        androidDriver.findElement(By.id(prop.getProperty("id.btn.error"))).click();
        if (totale < step.totaleNomi()){
            if (!step.controlloNomi("",step.totaleNomi()))
                extentTest.log(LogStatus.PASS, "Test passato elemento vuoto aggiunto", extentTest.addBase64ScreenShot(Utils.getScreenBase64Android()));
            else{
                extentTest.log(LogStatus.FAIL, "Test non passato elemento aggiunto non vuoto", extentTest.addBase64ScreenShot(Utils.getScreenBase64Android()));
                fail();
            }
        } else{
            extentTest.log(LogStatus.FAIL, "Test non passato elemento vuoto non aggiunto", extentTest.addBase64ScreenShot(Utils.getScreenBase64Android()));
            fail();
        }
    }

    @Test
    @Order(5)
    @DisplayName("Test Salvataggio modifiche")
    @Tag("Home")
    void Test_005_Save(TestInfo testInfo) throws InterruptedException {
        extentTest = extentReports.startTest(testInfo.getDisplayName());
        extentTest.log(LogStatus.INFO, "Controllo per verificare se dopo qualche modifica apportata dopo la login di un utente uscendo e rientrando tali modifiche restino invariate oppure non vengano salvate", "");
        Thread.sleep(1000);
        extentTest.log(LogStatus.INFO, "Screen prima del test", extentTest.addBase64ScreenShot(Utils.getScreenBase64Android()));
        int totale1 = step.totaleNomi();
        step.removeAll();
        step.accettazione();
        Thread.sleep(1000);
        step.aggiungiUtente("Pippo");
        Thread.sleep(1000);
        extentTest.log(LogStatus.INFO, "Screen delle modifiche apportate", extentTest.addBase64ScreenShot(Utils.getScreenBase64Android()));
        int totale2 = step.totaleNomi();
        androidDriver.navigate().back();
        Thread.sleep(1000);
        step.login(credenziali,credenziali);
        Thread.sleep(1000);
        if(step.totaleNomi() == totale1)
            extentTest.log(LogStatus.PASS, "Test passato uscendo e rientrando non vengono salvate le modifiche quindi non ci sono rischi", extentTest.addBase64ScreenShot(Utils.getScreenBase64Android()));
        if(step.totaleNomi() == totale2){
            extentTest.log(LogStatus.FAIL, "Test non passato uscendo e rientrando vengono salvate le modifiche quindi ci potrebbero essere errori se in pi?? usano la stessa app", extentTest.addBase64ScreenShot(Utils.getScreenBase64Android()));
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
