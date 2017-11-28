#### About

Software java da command line per l'analisi dei dati presenti su un catalogo open data basato su ckan.
Viene usato dati.trentino.it come test.


**Pre-requisiti:**

  * libreria spatialite >= 4.3.0
  * spatialite_tool
  * unzip
  * java >= 1.7
  * ant >= ?

**INSTALLAZIONE**

Da una bash shell eseguire le seguenti istruzioni:

 * sh setupdb.sh

Lo script genera un file sqlite, con estensioni spaziali, arricchito dagli shapefile ISTAT dei limiti amministrativi (regioni, province e comuni) al 2016 e il setup delle tabelle usate dal programma.
Nello specifico:
 * viene creato il database sqlite e inizializzate le relative tabelle, su cui verranno salvati i risultati delle analisi
 * viene scaricato e aggiunto al database lo shape file che descrive i confini delle regioni, province e comuni del territorio italiano

Una volta che il file sqlite è stato generato si può procedere alla compilazione della parte java attraverso questo comando:
 * ant -buildfile build.xml

Il programma va eseguito usando questo comando

 * java -jar dist/QualityAnalyzer.jar
