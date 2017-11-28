/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import com.eclipsesource.json.Json;
import com.vividsolutions.jts.geom.Coordinate;
import dbUtils.DBManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipException;
import javax.script.ScriptException;
import javax.xml.parsers.ParserConfigurationException;
import nickan.Resource;
import org.apache.commons.io.FileUtils;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import org.xml.sax.SAXException;

/*
 * @author nicola
 */
public class ResourceControls{
    
    private String resource_id;
    private int response_code;
    private boolean is_downloadable;
    private boolean format_correspondence;
    private boolean is_empty;
    private boolean is_correct;
    private String log = "";
    //private final String path = "/home/ospite/analyzer/resources/";
    private final String path = new File(DBManager.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getParent() + "/resources/";
    private boolean correct_encoding;
    private String declared_format;
    private String found_format;
    DBManager manager;
    private boolean processed;
    private boolean diretto;
    private boolean geo_valid;
    private boolean geo_processed;
    private String md5sum;
    
    private long size;
    
    String extractPath;
    
    PapaParse papa;
    Geo geo;
    
    public ResourceControls(Resource r, String dataset_encoding, String geonames_url) throws Exception{
        processed = false;
        diretto = true;
        
        manager = new DBManager();
        papa = new PapaParse();
        geo = new Geo(geonames_url);
        
        this.resource_id = r.getId();
        
        if(r.getFormat()!= null){
            this.declared_format = r.getFormat();
        }
        
        
        ///////////Download solo se non e' gia stato scaricato
        File file = new File(path+resource_id);
        size = file.length();
        
        if(!file.exists()){
            response_code = Utils.download(r.getUrl(), resource_id, path);
        }
        else{
            response_code = 200;
        }
        
        
        //response_code = Utils.download(r.getUrl(), resource_id, path);
        
        if(response_code == 408){
            log += "- Connection timed out\n";
        }
        
        is_downloadable = response_code==200;
        
        URL url = new URL(r.getUrl());
        
        if(url.getFile().equals(""))
            is_downloadable = false;
            
//        if(url.getHost().equals(r.getUrl()) || url.getHost().equals(r.getUrl().substring(0, r.getUrl().length()-2))){
//            is_downloadable = false;
//        }
        
        if(is_downloadable){
            //ContentInfoUtil util = new ContentInfoUtil();
            //ContentInfo info = util.findMatch(path + resource_id);
            
            
            
            java.nio.file.Path p = java.nio.file.Paths.get(path + resource_id);
            this.found_format = java.nio.file.Files.probeContentType(p);
            
            if(declared_format == null){
                
            }
            
            else if(found_format.toUpperCase().contains(declared_format.toUpperCase())){
                format_correspondence = true;
            }
            
            else if(found_format.toUpperCase().contains("ZIP") && declared_format.toUpperCase().contains("ZIP")){
                format_correspondence = true;
            }
            
            else if(found_format.equals("text/plain") && (declared_format.toUpperCase().contains("CSV") || declared_format.toUpperCase().contains("JSON") || declared_format.toUpperCase().contains("XML"))){
                format_correspondence = true;
            }
            else if(found_format.equals("application/xml") && declared_format.toUpperCase().contains("RDF")){
                format_correspondence = true;
            }
            
            try (FileInputStream fis = new FileInputStream(file)) {
                md5sum = org.apache.commons.codec.digest.DigestUtils.md5Hex(fis);
            }
            
            if(found_format.contains("zip")){
                extractPath = path+"extract-"+resource_id;
                try{
                    Utils.unzip(path+resource_id, extractPath);
                }
                catch(ZipException e){
                    log += e.getMessage() +"\n";
                    is_correct = false;
                    
                    insertAndDelete();
                    
                    return;
                }
                
                List<File> files = new LinkedList<>();
                
                Utils.listFiles(extractPath, files);
                
                diretto = Utils.unzipAll(files);
                
                
                ShapeFile shp = new ShapeFile();
                //verifica presenza elementi shape file (.shp, .dbf, .prj, .shx)
                shp.checkDir(extractPath);
                
                //verifica correttezza geometrie e verifica che i punti siano nei confini stabiliti
                if(shp.isShapefile()){
                    shp.checkShape(geonames_url);
                    is_correct = shp.isCorrect() && shp.isAllFiles();
                    processed = shp.isShapefile();
                    if(processed && declared_format!= null && declared_format.toUpperCase().contains("SHP")){
                        format_correspondence = true;
                    }
                    
                    diretto = shp.isDiretto();
                    log += shp.getLog();
                    geo_processed = geonames_url!=null;
                    geo_valid = shp.isCorrect() && geonames_url!=null;
                }
                //
                
                if(files.size()==1 && declared_format != null && declared_format.toUpperCase().contains("CSV")){
//TROPPO DISPENDIOSO
                    if(java.nio.file.Files.probeContentType(Paths.get(files.get(0).getAbsolutePath())).equals("text/plain"))
                        format_correspondence = true;
                                        
                    //Verifico encoding
                    java.io.FileInputStream fis = new java.io.FileInputStream(path + resource_id);
                    String tikaEnc = Utils.detectEncoding(fis);
                    correct_encoding = Utils.correctEncoding(dataset_encoding, tikaEnc);
                    if(!correct_encoding)
                        log += "- Declared encoding: " + dataset_encoding + ", found encoding: " + tikaEnc + "\n";
                    
                    //Verifico correttezza CSV e in points inserisco eventuali lon-lat
                    List<Coordinate> points = new LinkedList<>();
                    is_correct = papa.Parse(files.get(0).getAbsolutePath(), points);
                    log += papa.getLog();
                    is_empty = papa.isIs_empty();
                    
                    if(!points.isEmpty() && geonames_url!= null){
                        geo_valid = geo.validateCoordinates(points);
                        log+=geo.getLog();
                        geo_processed = true;
                    }
                    processed=true;
                }
                
                //se la lista di file contiene XML
                else if(Utils.containsSubString(files, ".xml") != -1){
                    is_correct = true;
                    boolean tmp;
                    List<Integer> index = Utils.getIndexesFromExtension(files, ".xml");
                    for(int i : index){
                        String filepath = files.get(i).getAbsolutePath();
                        
                        //Verifico encoding
                        java.io.FileInputStream fis = new java.io.FileInputStream(filepath);
                        String tikaEnc = Utils.detectEncoding(fis);
                        correct_encoding = Utils.correctEncoding(dataset_encoding, tikaEnc);
                        log += "- Declared encoding: " + dataset_encoding + ", found encoding: " + tikaEnc + "\n";

                        //verifico correttezza XML
                        XMLValidator xmlValidator = new XMLValidator(r.getPackage_id());                        
                        tmp = xmlValidator.validXML(filepath, files.get(i).getParent());
                        if(!tmp)
                            is_correct = false;
                        log += xmlValidator.getLog();
                        
                    }
                    diretto = false;
                    processed=true;
                }
                
                else if (!processed){
                    String s = "";
                    if(!files.isEmpty()){
                        for(File f: files){
                            s+=f.getName() + " - ";
                        }
                        s = s.substring(0, s.length()-2);
                        log += "- Lo zip estratto contiene i file: " + s + "\n";
                    }
                }
                
                
                //cannatissima
//                else if(setOfNames.contains("agency") && setOfNames.contains("stops") && setOfNames.contains("routes") && setOfNames.contains("trips") && setOfNames.contains("stop_times") && setOfNames.contains("calendar")){
//                    log+="- Il file è in formato GTFS in quanto sono presenti tutti i file necessari a comporlo\n";
//                    is_correct=true;
//                }
                
                
            }


















            
            //CSV
/*riga da eliminare, processo solo CSV <1 MB*/            //else if(declared_format != null && declared_format.toUpperCase().contains("CSV") && size < 1000000){
            else if(declared_format != null && declared_format.toUpperCase().contains("CSV")){
                //viene scaricato un file html invece che un csv, che è pagina intermedia di export
                if(found_format.contains("html")){
                    is_downloadable = false;
                }
                
                else{
                    //Verifico encoding
                    java.io.FileInputStream fis = new java.io.FileInputStream(path + resource_id);
                    String tikaEnc = Utils.detectEncoding(fis);
                    correct_encoding = Utils.correctEncoding(dataset_encoding, tikaEnc);
                    if(!correct_encoding)
                        log += "- Declared encoding: " + dataset_encoding + ", found encoding: " + tikaEnc + "\n";
                    
                    //Verifico correttezza CSV e in points inserisco eventuali lon-lat
                    List<Coordinate> points = new LinkedList<>();
                    is_correct = papa.Parse(path+resource_id, points);
                    log += papa.getLog();
                    is_empty = papa.isIs_empty();
                    
                    if(!points.isEmpty() && geonames_url!= null){
                        geo_valid = geo.validateCoordinates(points);
                        log+=geo.getLog();
                        geo_processed = true;
                    }
                    processed=true;
                }
                
            }
            //GeoJSON
            else if(declared_format != null && declared_format.toUpperCase().equals("GEOJSON")){
                //Verifico encoding
                java.io.FileInputStream fis = new java.io.FileInputStream(path + resource_id);
                String tikaEnc = Utils.detectEncoding(fis);
                correct_encoding = Utils.correctEncoding(dataset_encoding, tikaEnc);
                log += "- Declared encoding: " + dataset_encoding + ", found encoding: " + tikaEnc + "\n";
                
                //verifico correttezza JSON
                //is_correct = JSONParse(path+resource_id);
                geo = new Geo(geonames_url);
                
                is_correct = geo.validateGeoJson(file.getAbsolutePath());
                
                geo_valid = geo.isCorrect();
                log+=geo.getLog();
                geo_processed = geonames_url!=null;
                   
                
                processed=true;
            }
            //JSON
            else if(declared_format != null && declared_format.toUpperCase().contains("JSON")){
                //Verifico encoding
                java.io.FileInputStream fis = new java.io.FileInputStream(path + resource_id);
                String tikaEnc = Utils.detectEncoding(fis);
                correct_encoding = Utils.correctEncoding(dataset_encoding, tikaEnc);
                log += "- Declared encoding: " + dataset_encoding + ", found encoding: " + tikaEnc + "\n";
                
                //verifico correttezza JSON
                is_correct = JSONParse(path+resource_id);
                
                processed=true;
            }
            
            //XML
/*riga da eliminare, processo solo XML <1 MB*/            //else if(declared_format != null && declared_format.toUpperCase().contains("XML") && !found_format.contains("html") && size < 1000000){
            else if(declared_format != null && declared_format.toUpperCase().contains("XML") && !found_format.contains("html")){
                //Verifico encoding
                java.io.FileInputStream fis = new java.io.FileInputStream(path + resource_id);
                String tikaEnc = Utils.detectEncoding(fis);
                correct_encoding = Utils.correctEncoding(dataset_encoding, tikaEnc);
                log += "- Declared encoding: " + dataset_encoding + ", found encoding: " + tikaEnc + "\n";
                
                //verifico correttezza XML
                XMLValidator xmlValidator = new XMLValidator(r.getPackage_id());
                is_correct = xmlValidator.validXML(path + resource_id, path);
                log += xmlValidator.getLog();
                diretto = xmlValidator.isDiretto();
                //is_correct = validXML(path+resource_id, path);
                
                processed = true;
            }
//RDF
            //else if(declared_format != null && declared_format.toUpperCase().contains("RDF") && size < 1000000){
            else if(declared_format != null && declared_format.toUpperCase().contains("RDF")){
                //Verifico encoding
                java.io.FileInputStream fis = new java.io.FileInputStream(path + resource_id);
                String tikaEnc = Utils.detectEncoding(fis);
                correct_encoding = Utils.correctEncoding(dataset_encoding, tikaEnc);
                log += "- Declared encoding: " + dataset_encoding + ", found encoding: " + tikaEnc + "\n";
                //valido RDF
                RDFParse rdfParse = new RDFParse();
                is_correct = rdfParse.validateRDF(file.getAbsolutePath());
                processed = true;
            }
            
            //else if((declared_format != null && !found_format.contains("html")) && (declared_format.toUpperCase().contains("XML") || declared_format.toUpperCase().contains("CSV")) && size > 1000000)
              //  log+="- Risorsa non processata perché CSV o XML > 1MB \n";
            
            if(declared_format != null && !format_correspondence){
                log += "- Expected " + declared_format.toUpperCase() + " file but found " + found_format + " file format\n";
            }
        }
        
        //Se la risorsa non viene scaricata:
        else{
            //if(size > 10000000)
                //log+="- Non è stata elaborata la risorsa, file > 10MB\n";
            //else{
                log+="- Non è stato possibile scaricare la risorsa\n";
            //}
        }
        
        System.out.println(log);
        insertAndDelete();
    }
    
    private void insertAndDelete(){
        manager.insertResource_controls(this);
        
        //elimino i file
        FileUtils.deleteQuietly(new File(path+resource_id));
        FileUtils.deleteQuietly(new File(path+"extract-"+resource_id));
        
    }
    
    private boolean JSONParse(String path) throws MalformedURLException, IOException{
        
        File file = new File(path);
	String json = FileUtils.readFileToString(file);
        
        try{
            Json.parse(json);
            return true;
        }
        catch(Exception e){
            log += "- " + e.getMessage() + "\n";
            return false;
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    

    /**
     * @return the resource_id
     */
    public String getResource_id() {
        return resource_id;
    }

    /**
     * @param resource_id the resource_id to set
     */
    public void setResource_id(String resource_id) {
        this.resource_id = resource_id;
    }

    /**
     * @return the response_code
     */
    public int getResponse_code() {
        return response_code;
    }

    /**
     * @param response_code the response_code to set
     */
    public void setResponse_code(int response_code) {
        this.response_code = response_code;
    }

    /**
     * @return the is_downloadable
     */
    public boolean isDownloadable() {
        return is_downloadable;
    }

    /**
     * @param is_downloadable the is_downloadable to set
     */
    public void setIs_downloadable(boolean is_downloadable) {
        this.is_downloadable = is_downloadable;
    }

    /**
     * @return the format_correspondence
     */
    public boolean isFormat_correspondence() {
        return format_correspondence;
    }

    /**
     * @param format_correspondence the format_correspondence to set
     */
    public void setFormat_correspondence(boolean format_correspondence) {
        this.format_correspondence = format_correspondence;
    }

    /**
     * @return the isEmpty
     */
    public boolean isEmpty() {
        return is_empty;
    }

    /**
     * @param isEmpty the isEmpty to set
     */
    public void setIsEmpty(boolean isEmpty) {
        this.is_empty = isEmpty;
    }

    /**
     * @return the isCorrect
     */
    public boolean isCorrect() {
        return is_correct;
    }

    /**
     * @param isCorrect the isCorrect to set
     */
    public void setIsCorrect(boolean isCorrect) {
        this.is_correct = isCorrect;
    }

    /**
     * @return the log
     */
    public String getLog() {
        return log;
    }

    /**
     * @param log the log to set
     */
    public void setLog(String log) {
        this.log = log;
    }

    /**
     * @return the correct_encoding
     */
    public boolean isCorrect_encoding() {
        return correct_encoding;
    }

    /**
     * @param correct_encoding the correct_encoding to set
     */
    public void setCorrect_encoding(boolean correct_encoding) {
        this.correct_encoding = correct_encoding;
    }

    /**
     * @return the declared_format
     */
    public String getDeclared_format() {
        return declared_format;
    }

    /**
     * @param declared_format the declared_format to set
     */
    public void setDeclared_format(String declared_format) {
        this.declared_format = declared_format;
    }

    /**
     * @return the found_format
     */
    public String getFound_format() {
        return found_format;
    }

    /**
     * @param found_format the found_format to set
     */
    public void setFound_format(String found_format) {
        this.found_format = found_format;
    }

    /**
     * @return the size
     */
    public long getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(long size) {
        this.size = size;
    }

    /**
     * @return the processed
     */
    public boolean isProcessed() {
        return processed;
    }

    /**
     * @param processed the processed to set
     */
    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    /**
     * @return the diretto
     */
    public boolean isDiretto() {
        return diretto;
    }

    /**
     * @param diretto the diretto to set
     */
    public void setDiretto(boolean diretto) {
        this.diretto = diretto;
    }

    /**
     * @return the geo_valid
     */
    public boolean isGeo_valid() {
        return geo_valid;
    }

    /**
     * @param geo_valid the geo_valid to set
     */
    public void setGeo_valid(boolean geo_valid) {
        this.geo_valid = geo_valid;
    }

    /**
     * @return the geo_processed
     */
    public boolean isGeo_processed() {
        return geo_processed;
    }

    /**
     * @param geo_processed the geo_processed to set
     */
    public void setGeo_processed(boolean geo_processed) {
        this.geo_processed = geo_processed;
    }

    /**
     * @return the md5sum
     */
    public String getMd5sum() {
        return md5sum;
    }
}
