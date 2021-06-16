package it.tosiani.steps;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Properties;

public class AndroidStep {
    private AndroidDriver<?> androidDriver = null;
    private Properties prop = null;

    public AndroidStep(AndroidDriver<?> androidDriver, Properties prop) {
        this.androidDriver = androidDriver;
        this.prop = prop;
    }

    public void login(String utente,String pwd){
        if (utente == null)
            androidDriver.findElement(By.id(prop.getProperty("id.username"))).sendKeys("");
        else {
            androidDriver.findElement(By.id(prop.getProperty("id.username"))).sendKeys(utente);
        }
        if (pwd == null)
            androidDriver.findElement(By.id(prop.getProperty("id.pwd"))).sendKeys("");
        else {
            androidDriver.findElement(By.id(prop.getProperty("id.pwd"))).sendKeys(pwd);
        }
        androidDriver.findElement(By.id(prop.getProperty("btn.login"))).click();
    }

    public ArrayList<String> listaUtenti(){
        ArrayList<String> listTmp = new ArrayList<>();

        for(WebElement i: androidDriver.findElements(By.className(prop.getProperty("class.lista.nomi")))){
            listTmp.add(i.getText());
        }

        return listTmp;
    }

    public int totaleNomi(){
        return listaUtenti().size();
    }

    public boolean nomiUguali(String nomeConfronto, int indice){
        ArrayList<String> listTmp = listaUtenti();
            for (int i = 0;i < listTmp.size();i++) {
                if(i != indice)
                    if (listTmp.get(i).equals(nomeConfronto))
                        return false;
            }
        return true;
    }

    public boolean controlloNomiUguali(){
        ArrayList<String> listTmp = listaUtenti();
        for (int i = 0;i < listTmp.size();i++) {
            if(!nomiUguali(listTmp.get(i),i))
                return false;
        }
        return true;
    }

    public void aggiungiUtente(String nome) throws InterruptedException {
        androidDriver.findElement(By.id(prop.getProperty("id.btn.add"))).click();
        Thread.sleep(1000);
        if (nome == null){
            androidDriver.findElement(By.id(prop.getProperty("id.edit.name"))).sendKeys("");
        } else {
            androidDriver.findElement(By.id(prop.getProperty("id.edit.name"))).sendKeys(nome);
        }
        androidDriver.findElement(By.id(prop.getProperty("id.add.utente"))).click();
    }

    public void removeAll() throws InterruptedException {
        Thread.sleep(1000);
        androidDriver.findElement(By.id(prop.getProperty("id.btn.final.reset"))).click();
        Thread.sleep(1000);
        androidDriver.findElement(By.id(prop.getProperty("btn.accettazione"))).click();
        Thread.sleep(1000);
    }
}
