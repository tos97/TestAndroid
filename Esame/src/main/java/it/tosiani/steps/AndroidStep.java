package it.tosiani.steps;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Properties;

public class AndroidStep {
    private AndroidDriver<?> androidDriver = null;
    private Properties prop = null;

    /**
     * Costruttore con come parametri in ingresso le istanze di oggetti precedentemente dichiarati
     * @param androidDriver
     * @param prop
     */
    public AndroidStep(AndroidDriver<?> androidDriver, Properties prop) {
        this.androidDriver = androidDriver;
        this.prop = prop;
    }

    /**
     * Gestione della login dell'app
     * @param utente nome dell'utente da inserire
     * @param pwd password da inserire
     */
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
        androidDriver.findElement(By.id(prop.getProperty("id.btn.login"))).click();
    }

    /**
     * @return ritorna la lista dei nomi nell'app di un determinato utente
     */
    public ArrayList<String> listaUtenti(){
        ArrayList<String> listTmp = new ArrayList<>();

        for(WebElement i: androidDriver.findElements(By.className(prop.getProperty("class.lista.nomi")))){
            listTmp.add(i.getText());
        }

        return listTmp;
    }

    /**
     * @return ritorna il numero di nomi nella lista +2 elementi che non sono nomi ma si trovano nella stessa finestra
     */
    public int totaleNomi(){
        return listaUtenti().size();
    }

    /**
     * Serve per controllare la presenza di un nome nella lista
     * @param nomeConfronto nome da confrontare
     * @param indice posizione al quale si trova un elemento che non si deve controllare (nel caso il chiamante sia controlloNomiUguali)
     * @return ritorna un booleano risultante dall'operazione
     */
    public boolean controlloNomi(String nomeConfronto, int indice){
        ArrayList<String> listTmp = listaUtenti();
            for (int i = 0;i < listTmp.size();i++) {
                if(i != indice)
                    if (listTmp.get(i).equals(nomeConfronto))
                        return false;
            }
        return true;
    }

    /**
     * controla se i nomi si ripetono richiamando il metodo controlloNomi
     * @return restituise un risultato booleano
     */
    public boolean controlloNomiUguali(){
        ArrayList<String> listTmp = listaUtenti();
        for (int i = 0;i < listTmp.size();i++) {
            if(!controlloNomi(listTmp.get(i),i))
                return false;
        }
        return true;
    }

    /**
     * Aggiunge un utente alla lista di utenti
     * @param nome nome da aggiungere
     * @throws InterruptedException per poter usare Thread.sleep senza problemi o try/catch
     */
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

    /**
     * Tenta di rimuovere tutti gli elementi
     * @return true se compare pop-up di conferma elminazione false se non lo fa
     * @throws InterruptedException per poter usare Thread.sleep senza problemi o try/catch
     */
    public boolean removeAll() throws InterruptedException {
        Thread.sleep(1000);
        androidDriver.findElement(By.id(prop.getProperty("id.btn.final.reset"))).click();
        Thread.sleep(1000);
        try{
            if (androidDriver.findElement(By.id(prop.getProperty("id.btn.accettazione"))).isDisplayed())
                return true;
        } catch (NoSuchElementException e){ }
        return false;
    }

    /**
     * clicca sul si per accettare quello che viene chiesto nel pop-up a schermo
     */
    public void accettazione(){
        androidDriver.findElement(By.id(prop.getProperty("id.btn.accettazione"))).click();
    }
}
