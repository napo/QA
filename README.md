#### About

Applicazione JAVA per la validazione dei dati presenti su dati.trentino.it


**Pre-requisiti:**

  * libreria spatialite
  * spatialite_tool
  * unzip
  * java
  * ant

**INSTALLAZIONE**

Da una bash shell eseguire le seguenti istruzioni:

 * sh install.sh

Questo comado esegue uno script di installazione con il quale :
 * viene creato il database sqlite e inizializzate le relative tabelle, su cui verranno salvati i risultati delle analisi
 * viene scaricato e aggiunto al database lo shape file che descrive i confini delle regioni, province e comuni del territorio italiano

Una volta completata l'installazione, proseguire digitando:

 * ant -buildfile build.xml

Il quale compila il progetto e genera il file .jar eseguibile.

A questo punto eseguire il programma digitando:

 * java -jar dist/QualityAnalyzer.jar
