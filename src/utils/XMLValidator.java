/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import dbUtils.DBManager;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author nicola
 */
public class XMLValidator {
    
    private String log = "";
    private final String package_id;
    private boolean diretto = true;
    
    public XMLValidator(String package_id){
        this.package_id = package_id;
    }
    
    public boolean validXML(String filePath, String dirPath) throws ParserConfigurationException, SAXException, IOException, URISyntaxException{
        Path p = Paths.get(filePath);
        
        
        //Checking Wellformed
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        //builder.setErrorHandler(new SimpleErrorHandler());
        try{
            Document document = builder.parse(new InputSource(filePath));
            setLog(getLog() + "- Il file " + filePath + " è wellformed\n");
        }
        catch(SAXParseException e){
            //il documento non è wellformed
            setLog(getLog() + "- File " + filePath + " isn't wellformed, error: " + e.getMessage() + "\n");
            return false;
        }
        
        //trovo link per scaricare file xsd
        String schemaLink="";
        List<String> lines = Files.readAllLines(p, StandardCharsets.ISO_8859_1);
        int tmp =0;
        for(String line:lines){
            //schemaLocation, se presente, deve essere tra le prime righe del file
            if(tmp==10)
                break;
            
            if(line.contains("schemaLocation")){
                schemaLink=line.substring(line.indexOf("schemaLocation"));
                schemaLink=schemaLink.substring(schemaLink.indexOf("\"")+1);
                schemaLink=schemaLink.substring(0, schemaLink.indexOf("\""));
                break;
            }
            tmp++;
        }
        ///
        
        if(schemaLink.equals("")){
            
            List<File> files = new LinkedList<>();
            Utils.listFiles(dirPath, files);
            List<File> oldFiles = new LinkedList<>(files);
            
            int xsdIndex = Utils.containsSubString(files, ".xsd");
            
            if (xsdIndex != -1){
                return ValidateXML_XSD(files.get(xsdIndex).getAbsolutePath(), filePath);
            }
            else{
                DBManager manager = new DBManager();
                List<String> urls = manager.getResourcesUrlsFromPackageId(package_id);
                List <File> newFiles = new LinkedList<>();
                tmp = 0;
                for(String s : urls){
                    String extension = "";
                    int lastIndex = s.lastIndexOf(".");
                    if(lastIndex!=-1){
                        extension = s.substring(lastIndex);
                        if(!extension.equals(".xsd")){
                            extension = "";
                        }
                    }
                    Utils.download(s, Integer.toString(tmp)+extension, dirPath);
                    newFiles.add(new File(dirPath+Integer.toString(tmp)+extension));
                    tmp++;
                }
                
                files.clear();
                files.addAll(newFiles);
                
                files.addAll(Utils.unzipAllForXML(newFiles));
                
                xsdIndex = Utils.containsSubString(files, ".xsd");
                if(xsdIndex != -1){
                    setDiretto(false);                    
                    boolean validXML = ValidateXML_XSD(files.get(xsdIndex).getAbsolutePath(), filePath);
                    //files.removeAll(oldFiles);
                    for(File f:files){
                        FileUtils.deleteQuietly(f);
                    }
                    return validXML;
                }
                else{
                    setLog(getLog() + "- File " + filePath + " without schema\n");
                    files.removeAll(oldFiles);
                    for(File f:files){
                        FileUtils.deleteQuietly(f);
                    }
                    return false;
                }                
            }
            
            //setLog(getLog() + "- File XML without schema\n");
            //return false;
        }
        else{
            //scarico il file xsd
            String xsdName = schemaLink.substring(schemaLink.lastIndexOf("/")+1);
            Utils.download(schemaLink, xsdName, dirPath);
            
            return ValidateXML_XSD(filePath, dirPath + xsdName);            
        }
    }
    
    private boolean ValidateXML_XSD(String xmlPath, String xsdPath){
        try {
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(new File(xsdPath));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xmlPath)));
            return true;
        }
        catch (IOException | SAXException e) {
            setLog(getLog() + "- File XML error parsing schema: " + e.getMessage() + "\n");
            return false;
        }
    }
    
//    public boolean isValidXML(String filePath, String dirPath) throws ParserConfigurationException, SAXException, IOException{
//        
//        Path p = Paths.get(filePath);
//        
//        //Checking Wellformed
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        factory.setValidating(false);
//        factory.setNamespaceAware(true);
//        DocumentBuilder builder = factory.newDocumentBuilder();
//        //builder.setErrorHandler(new SimpleErrorHandler());
//        try{
//            Document document = builder.parse(new InputSource(filePath));
//            log+="- Il file XML è wellformed\n";
//        }
//        catch(SAXParseException e){
//            //il documento non è wellformed
//            log+="- File XML isn't wellformed, error: " + e.getMessage() + "\n";
//            return false;
//        }
//        
//        //trovo link per scaricare file xsd
//        String schemaLink="";
//        List<String> lines = Files.readAllLines(p, StandardCharsets.ISO_8859_1);
//        for(String line:lines){
//            if(line.contains("schemaLocation")){
//                schemaLink=line.substring(line.indexOf("schemaLocation"));
//                schemaLink=schemaLink.substring(schemaLink.indexOf("\"")+1);
//                schemaLink=schemaLink.substring(0, schemaLink.indexOf("\""));
//                break;
//            }
//        }
//        ///
//        
//        if(schemaLink.equals("")){
//           log+="- File XML without schema\n";
//           return false;
//        }
//        else{
//            //scarico il file xsd
//            String xsdName = schemaLink.substring(schemaLink.lastIndexOf("/")+1);
//            Utils.download(schemaLink, xsdName, dirPath);
//            
//            try {
//                SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//                Schema schema = schemaFactory.newSchema(new File(dirPath + xsdName));
//                Validator validator = schema.newValidator();
//                validator.validate(new StreamSource(new File(filePath)));
//                return true;
//            }
//            catch (IOException | SAXException e) {
//                log+="- File XML error parsing schema: " + e.getMessage() + "\n";
//                return false;
//            }
//        }
//    }

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
}
