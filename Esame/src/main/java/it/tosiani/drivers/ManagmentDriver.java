package it.tosiani.drivers;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

import static it.tosiani.utility.GlobalParameters.SERVER_APPIUM;

public class ManagmentDriver {
    private static AndroidDriver<?> androidDriver;

    /**
     * Start Android driver
     * @param desiredCapabilities opzioni di funzionamento del driver
     */
    public static void startAndroidDriver(DesiredCapabilities desiredCapabilities){
        try{
            androidDriver = new AndroidDriver<MobileElement>(new URL(SERVER_APPIUM), desiredCapabilities);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return serve per restituire il driver creato in questa classe
     */
    public static AndroidDriver<?> getAndroidDriver(){
        return androidDriver;
    }

    /**
     * Termina l'esecuzione del driver
     */
    public static void stopDriver(){
            androidDriver.quit();
    }
}
