/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.ScriptUtils;
import org.apache.commons.lang3.StringUtils;
import com.vividsolutions.jts.geom.Coordinate;
import java.io.File;
import java.net.URISyntaxException;
import org.apache.commons.io.FileUtils;



/**
 *
 * @author nicola
 */
public class PapaParse {
    
    private boolean is_empty;
    private String log = "";
    //i seguenti servono?
    //data
    //meta
    //errors
    //JSObject result
    
    public boolean Parse(String filename, List<Coordinate> coordinates) throws FileNotFoundException, ScriptException, URISyntaxException, IOException{
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine jsEngine = mgr.getEngineByName("JavaScript");
        //jsEngine.eval(new java.io.FileReader("/home/ospite/usr/rhino1.7.7.2/papaparse.js"));
        jsEngine.eval(new java.io.FileReader(new File(PapaParse.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getParent() + "/papaparse.js"));
                
        String csv = fileToString(filename);
        if(isIs_empty()){
            return false;
        }
        
        String fun = "Papa.parse(\"" + csv + "\",{header: true, skipEmptyLines: true})";//skipEmptyLines: true
        
        JSObject result =  (JSObject) jsEngine.eval(fun);
        
        //farsi dare valore del codice asci per capire se CR o LR o CRLR (ritorno a capo)
        ScriptObjectMirror meta = (ScriptObjectMirror) result.getMember("meta");
        String meta_delimiter =(String) ScriptUtils.convert(meta.getMember("delimiter"),String.class);
        String meta_linebreak =(String) ScriptUtils.convert(meta.getMember("linebreak"),String.class);
        Boolean meta_aborted =(Boolean) ScriptUtils.convert(meta.getMember("aborted"),Boolean.class);
        Boolean meta_truncated =(Boolean) ScriptUtils.convert(meta.getMember("truncated"),Boolean.class);
        String meta_cursor =(String) ScriptUtils.convert(meta.getMember("cursor"),String.class);
        ScriptObjectMirror fields = (ScriptObjectMirror) meta.getMember("fields");
        
        Set<String> fieldsKeys = new HashSet<>();
        for(int i=0; i<fields.size(); i++){
            fieldsKeys.add(fields.getSlot(i).toString());
        }
        
        ScriptObjectMirror data = (ScriptObjectMirror) result.getMember("data");
        
        //ciclo di tutti i dati all'interno del CSV        
        if(fieldsKeys.contains("lat") && fieldsKeys.contains("lon")){
            //ciclo di tutti i dati all'interno del CSV        
            int data_length = (int) ScriptUtils.convert(data.get("length"), Integer.class);
            for(int i=0; i<data_length; i++){
                ScriptObjectMirror d = (ScriptObjectMirror) data.getSlot(i);
                try{
                    double x = Double.parseDouble(d.get("lon").toString());
                    double y = Double.parseDouble(d.get("lat").toString());
                    coordinates.add(new Coordinate(y,x));
                }
                catch(NullPointerException e){
                    
                }
            }
        }
        
        //cattura dei messaggi di errore
        ScriptObjectMirror errors = (ScriptObjectMirror) result.getMember("errors");
        int errors_length = (int) ScriptUtils.convert(errors.get("length"), Integer.class);
        if(errors_length!=0){
            ScriptObjectMirror error = (ScriptObjectMirror) errors.getSlot(0);
            
            String code = (String) ScriptUtils.convert(error.get("code"),String.class);
            String message = (String) ScriptUtils.convert(error.get("message"),String.class);
            String row = (String) ScriptUtils.convert(error.get("row"),String.class);
            String type = (String) ScriptUtils.convert(error.get("type"),String.class);
            
            setLog(getLog() + "- CSV file errors: " + errors_length + "\n");
            setLog(getLog() + "- CSV first error code: " + code + "\n");
            setLog(getLog() + "- CSV first error message: " + message + "\n");
            setLog(getLog() + "- CSV first error row: " + row + "\n");
            setLog(getLog() + "- CSV first error type: " + type + "\n");
            
            return false;
        }
        else{            
            setLog("- CSV file is correct\n");
            return true;
        }
        
        //ciclo per tutti gli errori trovati nel CSV        
//        for(int i=0; i<errors_length; i++){
//            ScriptObjectMirror error = (ScriptObjectMirror) errors.getSlot(i);
//
//            String code = (String) ScriptUtils.convert(error.get("code"),String.class);
//            String message = (String) ScriptUtils.convert(error.get("message"),String.class);
//            String row = (String) ScriptUtils.convert(error.get("row"),String.class);
//            String type = (String) ScriptUtils.convert(error.get("type"),String.class);
//
//            
//            System.out.println(code);
//            System.out.println(message);
//            System.out.println(row);
//            System.out.println(type);
//            
//        }
    }
    
    private String fileToString(String path){
        String csv="";
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String sCurrentLine;
            
            if((sCurrentLine = br.readLine()) != null){
                csv +=sCurrentLine.replace("\\", "\\\\")+"\\n";
                
                while ((sCurrentLine = br.readLine()) != null) {
                    
//                    sCurrentLine=StringUtils.trim(sCurrentLine);
//                    sCurrentLine=StringUtils.chomp(sCurrentLine);
                    csv +=sCurrentLine.replace("\\", "\\\\") + "\\n";
                    
                }
                csv=csv.substring(0, csv.length()-2);
                csv = csv.replace("\u2028", "\\n");
                csv=csv.replace("\"", "\\\"");
            }
            else{
                setIs_empty(true);
            }            
        }
        catch (IOException e) {}
        
        return csv;
    }

    /**
     * @return the is_empty
     */
    public boolean isIs_empty() {
        return is_empty;
    }

    /**
     * @param is_empty the is_empty to set
     */
    public void setIs_empty(boolean is_empty) {
        this.is_empty = is_empty;
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
}
