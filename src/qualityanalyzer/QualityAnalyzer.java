/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qualityanalyzer;

import utils.DateAnalyzer;
import dbUtils.DBManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.script.ScriptException;
import javax.xml.parsers.ParserConfigurationException;
import nickan.Client;
import nickan.Dataset;
import nickan.Resource;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import org.xml.sax.SAXException;
import utils.EmailChecker;
import utils.ResourceControls;

/**
 *
 * @author nicola
 */
public class QualityAnalyzer {

    static String link = "http://dati.trentino.it";
    static String modified;
    static DateAnalyzer da;
    public static  Map<String, String> tagsRules = new HashMap<>();
    static Client c;
    
    
    public static void main(String[] args) throws Exception {

        c = new Client(link);
        DBManager manager = new DBManager();
        
        Scanner sc = new Scanner(System.in);
        String info = FileUtils.readFileToString(new File(new File(QualityAnalyzer.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getParent()+"/comandi.txt")).trim();
        
        System.out.println(info);
        System.out.println();
        
//        URL url1 = new URL("http://dati.trentino.it/api/3/action/resource_show?id=0faeaa67-2a81-4de1-8e39-3670809cde1f");
//        Resource r1 = new Resource(url1);
//        ResourceControls rc1 = new ResourceControls(r1, "utf8", "http://www.geonames.org/6539506");

//        List<String> ll = manager.getDatasetNames();
//        initTableEmailVerification(ll);

        
        
        
        while(sc.hasNextLine()){
            String input = sc.nextLine();
            if(input.equals("initDB")){
                manager.deleteTables();
                List<String> l = c.getDatasetList();
                initDB(l);
                System.out.println("DB inizializzato");
                System.out.println();
            }
            
            else if(input.equals("continue-initDB")){
                List<String> l = c.getDatasetList();
                l.removeAll(manager.getDatasetNames());
                initDB(l);
                System.out.println("DB inizializzato");
                System.out.println();
            }
            
            else if(input.equals("validate-email")){
                manager.deleteTable("email_verification");                
                List<String> l = manager.getDatasetNames();
                initTableEmailVerification(l);
                System.out.println("Validazione completata");
                System.out.println();
            }
            
            else if(input.equals("continue-validate-email")){
                List<String> l = manager.getDatasetNames();
                l.removeAll(manager.getNamesEmailChecked());
                initTableEmailVerification(l);
                System.out.println("Validazione completata");
                System.out.println();
            }
            
            else if(input.contains("validate-package ")){
                String dataset_name = input.replace("validate-package ", "");
                Dataset d = manager.getDatasetFromName(dataset_name);
                if(d!=null){
                    validateDataset(dataset_name);
                    System.out.println("Validazione completata");
                    System.out.println();
                }
                
            }
            
            else if(input.equals("validate-all-resources")){
                manager.deleteTable("resource_controls");
                List<String> l = manager.getDatasetNames();
                validateAllDatasets(l);
                System.out.println("Validazione completata");
                System.out.println();
            }
            
            else if(input.equals("continue-validate-all-resources")){
                List<String> l = manager.getDatasetNames();
                l.removeAll(manager.getDatasetValidated());
                validateAllDatasets(l);
                System.out.println("Validazione completata");
                System.out.println();
            }
            
            else if(input.equals("exit")){
                System.exit(0);
            }
            
            else if(input.equals("info")){
                System.out.println(info);
                System.out.println();
            }
            else{
                System.out.println("Command not found");
                System.out.println();
            }
        }
        
        
        
        
        
        
        /*
        //CICLO PER TUTTI I DATASET E TUTTE LE RISORSE
        for(String s : l){
            try{
                Dataset d = manager.getDatasetFromName(s);
                for(Resource r : d.getResources()){
                }
            }
            catch(Exception e){
                System.err.println(s + " ERRORE \n" + e.toString());
            }
        }
        */
        
        
        //DOWNLOAD ALL resources
//        String dir = "/home/ospite/analyzer/resources/";
//        int i=1;
//        List<String> names = manager.getNames();
//        for(String s : names){
//            try{
//                Dataset d = manager.getDatasetFromName(s);
//                System.out.println(i++ + " - " + s);
//                
//                for(Resource r : d.getResources()){
//                    File f = new File(dir + r.getId());
//                    if(!f.exists()){
//                        int resp = Utils.download(r.getUrl(), r.getId(), dir);
//                        if(resp != 200){
//                            System.err.println(s + " - " + resp);
//                        }
//                    }
//                }
//            }
//            catch(Exception e){
//                System.err.println(s + " ERRORE \n" + e.toString());
//            }
//        }
        
        
        //Test ResourceControls per una lista di dataset (vedere quanti dataset restituiti da manager.getNames perche' e' stato impostato LIMIT)
//        List<String> names = manager.getNames();
//        
//        int i=1;
//        for(String s : names){
//            try{
//                Dataset d = manager.getDatasetFromName(s);
//                System.out.println(i + " - " + s + " - " + d.getNum_resources());
//                i++;
//                for(Resource r : d.getResources()){
//                    ResourceControls rc = new ResourceControls(r, d.getEncoding(), d.getGeographical_geonames_url());
//                }
//            }
//            catch(IOException | ScriptException | ParserConfigurationException | SAXException e){
//                System.err.println(s + " ERRORE \n" + e.toString());
//            }
//        }
        
        //Test ResourceControls per singolo dataset
//        Dataset d = manager.getDatasetFromName("linee-impianti-a-fune");//corpi-glaciali-minori-2003
//        
//        for(Resource r : d.getResources()){
//            try{
//                System.out.println(d.getName() + " " + d.getNum_resources());
//                ResourceControls rc = new ResourceControls(r, d.getEncoding(), d.getGeographical_geonames_url());
//            }
//            catch(IOException | ScriptException | ParserConfigurationException | SAXException e){
//                System.err.println(r.getId() + e);                
//            }
//        }

        //Test ResourceControls per singola risorsa
        //URL url = new URL("http://dati.trentino.it/api/3/action/resource_show?id=b20830c9-1a05-45a7-9121-91937bb0e320");            // zip file con mille shape
        //URL url = new URL("http://dati.trentino.it/api/3/action/resource_show?id=f22d4990-e49d-4b16-a3c9-19fc0772d7e7");        //catasto shape, mille mega
        //URL url = new URL("http://dati.trentino.it/api/3/action/resource_show?id=6e255a37-59f5-4c3d-ae65-abea0e0214fe");//strade rovereto
        //URL url = new URL("http://dati.trentino.it/api/3/action/resource_show?id=cbea5b96-5fb4-4efd-8bc6-22814daf66cc");//punti rovereto
//        URL url = new URL("http://dati.trentino.it/api/3/action/resource_show?id=9990f652-7233-4d50-aac0-507892574c42");//csv predaia
        //URL url = new URL("http://dati.trentino.it/api/3/action/resource_show?id=8ded8cd2-b04b-46e5-92e3-e16d33bf1894");//csv associazioni arco
        //URL url = new URL("http://dati.trentino.it/api/3/action/resource_show?id=2e9e2317-d609-449e-9653-b493ae052b2f");//csv eventi giovo
        //URL url = new URL("http://dati.trentino.it/api/3/action/resource_show?id=5eafcab9-b838-47ed-9d08-90a8109394c8");//csv zambana
        //URL url = new URL("http://dati.trentino.it/api/3/action/resource_show?id=0f6ecc2a-c4b7-4231-bf3d-3025b1c62921");
//        Resource r = new Resource(url);
        //ResourceControls rc = new ResourceControls(r, "utf8", "http://www.geonames.org/6539916");
        //ResourceControls rc = new ResourceControls(r, "utf8", "http://www.geonames.org/6540653");//geonames besenello
        //ResourceControls rc = new ResourceControls(r, "utf8", "http://www.geonames.org/6542626");//geonames giovo
        //ResourceControls rc = new ResourceControls(r, "utf8", "http://www.geonames.org/6539916");//geonames rovereto
        //ResourceControls rc = new ResourceControls(r, "utf8", "http://www.geonames.org/6539794");//geonames arco
//        ResourceControls rc = new ResourceControls(r, "utf8", "http://www.geonames.org/11287665");//geonames predaia
        //ResourceControls rc = new ResourceControls(r, "utf8", "http://www.geonames.org/6542077");//geonames zambana
        //ResourceControls rc = new ResourceControls(r, "utf8", "http://www.geonames.org/6540650");//ala
        
//        URL url1 = new URL("http://dati.trentino.it/api/3/action/resource_show?id=9990f652-7233-4d50-aac0-507892574c42");
//        Resource r1 = new Resource(url1);
//        ResourceControls rc1 = new ResourceControls(r1, "utf8", "http://www.geonames.org/11287665");
        
    }
    
    public static void validateAllDatasets(List <String> listDataset) throws Exception{
        DBManager manager = new DBManager();
        int index=1, listSize = listDataset.size();
        for(String s : listDataset){
            try{
                Dataset d = manager.getDatasetFromName(s);
                System.out.println("Package [" +index++ +"/"+ listSize + "] " + " - " + s);
                
                int resIndex = 1, resSize=d.getResources().size();
                for(Resource r : d.getResources()){
                    System.out.println("Risorsa [" +resIndex++ +"/"+ resSize + "] " + " - " + r.getName());
                    ResourceControls rc = new ResourceControls(r, d.getEncoding(), d.getGeographical_geonames_url());
                }
            }
            catch(IOException | ScriptException | ParserConfigurationException | SAXException e){
                System.err.println(s + " ERRORE \n" + e.toString());
            }
        }
    }
    
    public static void validateDataset(String dataset_name) throws Exception{
        DBManager manager = new DBManager();
        Dataset d = manager.getDatasetFromName(dataset_name);
        
        for(Resource r : d.getResources()){
            try{
                System.out.println("Validazione risorsa: " + r.getName());
                ResourceControls rc = new ResourceControls(r, d.getEncoding(), d.getGeographical_geonames_url());
            }
            catch(IOException | ScriptException | ParserConfigurationException | SAXException e){
                System.err.println(r.getId() + e);
            }
        }
    }
    
    public static void initDB(List <String> listDataset) throws MalformedURLException, JSONException, IOException, URISyntaxException{
        da = new DateAnalyzer();
        int index=1;
        int listSize = listDataset.size();
        for(String s:listDataset){
            Dataset d;
            
            try{
                d = new Dataset(c.getPortalUrl(), c.getApi3UrlDatasetShow(), s);
                
            }
            catch(IOException e){
                //API2
                d = new Dataset(c.getPortalUrl(), c.getApi2UrlDatasetShow(), s);
            }
            
            if(d.getType().equals("dataset")){
                System.out.println("Package [" +index++ +"/"+ listSize + "] " + s);
                
                DBManager manager = new DBManager();
                //popolamento tabella dataset
                manager.insertDataset(d);

                //popolamento tabella organization
                manager.insertOrganization(d.getOrganization());

                //popolamento tabella org_in_dataset
                manager.insertOrg_in_dataset(d.getId(), d.getOrganization().getId());

                //popolamento tabella dataset_is_updated
                String result = da.isUpdated(d);
                manager.insertDataset_is_updated(d.getId(), result);

                //popolamento tabelle resource e res_in_dataset
                for(Resource r : d.getResources()){
                    manager.insertResource(r);
                    manager.insertRes_in_dataset(d.getId(), r.getId());
                }
            }
            else{
                System.err.println("Package [" +index++ +"/"+ listSize + "] " + s + " - " + d.getType() + ", non aggiunto.");
            }
        }
    }
    
    public static void initTableCompleteness(List <String> l) throws URISyntaxException{
        DBManager manager = new DBManager();
        for(String s : l){
            System.out.println(s);
            try{
                Dataset d = manager.getDatasetFromName(s);
                if(d!=null && d.getType().equals("dataset")){
                    manager.insertCompleteness(d);
                }
            }
            catch(Exception e){
                System.err.println("initTableCompleteness "+ s + " ERRORE \n" + e.toString());
            }
        }
    }
    
    public static void initTableResourceResponsecode(List <String> l) throws URISyntaxException{
        //POPOLAMENTO TABELLA resource_responsecode per ogni datset
        URL url;
        DBManager manager = new DBManager();
        int responseCode;
        for(String s : l){
            try{
                Dataset d = manager.getDatasetFromName(s);
                for(Resource r : d.getResources()){
                    try{
                        int responseCodeRedirect = -1;
                        url = new URL(r.getUrl().replace(" ", "%20"));
                        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                        responseCode = httpConn.getResponseCode();
                        if(responseCode == 301 || responseCode == 302){
                            String newUrl = httpConn.getHeaderField("Location");
                            httpConn = (HttpURLConnection) new URL(newUrl).openConnection();
                            responseCodeRedirect = httpConn.getResponseCode();
                        }
                        if(responseCodeRedirect == -1){
                            manager.insertResource_responsecode(r.getId(), Integer.toString(responseCode));
                        }
                        else{
                            manager.insertResource_responsecode(r.getId(), responseCode + " responseRedirect: " + responseCodeRedirect);
                        }
                    }
                    catch(IOException e){
                        manager.insertResource_responsecode(r.getId(), "ERROR " + e.toString());
                    }
                }
            }
            catch(Exception e){
                System.out.println(s + " " + e.toString());
            }
        }
    }
    
    public static void initTableEmailVerification(List <String> l) throws URISyntaxException{
        DBManager manager = new DBManager();
        EmailChecker ec = new EmailChecker();
        
        String maintainer_result;
        String author_result;
        String contact_result;
        int index=1, listSize = l.size();
        for(String s : l){
            System.out.println("Package [" +index++ +"/"+ listSize + "] " + s);
            try{
                maintainer_result="NOT VALID";
                author_result="NOT VALID";
                contact_result="NOT VALID";
                
                Dataset d = manager.getDatasetFromName(s);
                
                if(d.getMaintainer_email()!=null){
                    if(ec.isValidEmailAddress(d.getMaintainer_email())){
                        maintainer_result = "OK";
                    }
                }
                else{
                    maintainer_result = "NULL";
                }
                
                if(d.getAuthor_email()!=null){
                    if(ec.isValidEmailAddress(d.getAuthor_email())){
                        author_result = "OK";
                    }
                }
                else{
                    author_result = "NULL";
                }
                
                if(d.getContact()!=null){
                    if(ec.isValidEmailAddress(d.getContact())){
                        contact_result = "OK";
                    }
                }  
                else{
                    contact_result = "NULL";
                }
                
                manager.insertEmailVerification(d.getId(), author_result, maintainer_result, contact_result);
            }
            catch(Exception e){
                //System.err.println(s + " ERRORE \n" + e.toString());
            }
        }
    }
    
    
    public static void UpgradeTableFrequency(List <String> datasetlist){
        da = new DateAnalyzer();
        for(String s : datasetlist){
            try{
                DBManager manager = new DBManager();
                Dataset d = manager.getDatasetFromName(s);
                if(d.getType().equals("dataset")){
                    String result = da.isUpdated(d);
                    manager.insertDataset_is_updated(d.getId(), result);
                }
            }
            catch(Exception e){
                System.err.println("ERRORE: " + s + "\n " + e.toString());                
            }
        }
    }
}
