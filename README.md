# Test Esame Android
Testo:
Creare un nuovo progetto Maven android appium che effettui tutti i test che ritenere opportuni sull app precedentemente inviata.
oltre al progetto dovete realizzare un report HTML dettagliato che dimostri il corretto funzionamento dei test da voi realizzati ed un file excel che rappresenti il vostro test book che contenga almeno i seguenti campi:
  - Codice test
  - Descrizione
  - Esito

NB: questi cambi faranno riferimento ai vostri test presenti nel progetto.

Mi aspetto che il progetto sia fatto rispettando il paradigma OOP con Read Me e descrizione dei metodi da voi creati.

Il tutto dovra essere caricato su Git caricando soltanto i file necessari. Successivamente scaricare in formato zip il progetto ed inviarlo alla mia email insieme al report, excel e json di capability che avete usato per appium.

Data inizio 9:15 data max di consegna 12:50

- Creato progetto a nome it.tosiani, app inserita nelle resouces, fatto 2 classi di test divise una per login e l'altra per home, creante anche classi con metodi per funzionamento Test.
# Test Login
- controllo errori in caso di credenziali errate o vuote
- controllo in caso di credenziali corrette se si entra nella home
- conrtrollo del funzionamento del tasto reset
# Test Home
- test su barra welcome e correttezza dati
- test su funzionamento dei vari pulsanti e pop-up
  - vari tipi di test fatti su add per casi:
    - vuoto
    - pieno con nome nuovo
    - pieno con nuome già presente in precedenza
  - test su funzionalità di rimozione e su suo pop-up
  - test su funzionalità Errore per aggiunta di riga vuota
- Controllo salvataggio modifiche  