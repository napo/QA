/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.tika.parser.txt.CharsetDetector;
import org.apache.tika.parser.txt.CharsetMatch;

/**
 *
 * @author nicola
 */
public class Utils {
    
    private static final int BUFFER_SIZE = 4096;
    
    public static int download(String fileurl, String fileName, String dirPath) throws IOException{
        
        int response_code;
        String saveFilePath;
        
        try{
            //verifica del response HTTP
            URL url = new URL(fileurl.replace(" ", "%20"));
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            response_code = httpConn.getResponseCode();
            //always check HTTP response code first
            if( response_code == HttpURLConnection.HTTP_OK || (response_code >= 300 && response_code < 400)) {
                if(response_code >= 300 && response_code < 400){
                    String newUrl = httpConn.getHeaderField("Location");
                    httpConn.disconnect();                
                    httpConn = (HttpURLConnection) new URL(newUrl).openConnection();
                    response_code = httpConn.getResponseCode();
                }
                if( response_code == HttpURLConnection.HTTP_OK){
                    // opens input stream from the HTTP connection
                    InputStream inputStream = httpConn.getInputStream();
                    saveFilePath = dirPath + fileName;

                    //creo la cartella se non esiste
                    if (!new File(dirPath).exists()) {
                        new File(dirPath).mkdir();
                    }

                    // opens an output stream to save into file
                    FileOutputStream outputStream = new FileOutputStream(saveFilePath);

                    int bytesRead;
                    byte[] buffer = new byte[BUFFER_SIZE];

                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    } 

                    outputStream.close();
                    inputStream.close();
                }
                httpConn.disconnect();
            }
            else{
                httpConn.disconnect();
            }
        }
        catch(ConnectException e){
            //connection timed out
            response_code = 408;
        }
        return  response_code;
    }
    
    public static void printStringInFile(String filepath, String txt) throws FileNotFoundException{
        try(  PrintWriter out = new PrintWriter(filepath)){
            out.println(txt);
        }
    }
    
    
    
    public static boolean unzipAll(List<File> files) throws IOException{
        boolean diretto = true;
        for(File f: files){
            Path p = Paths.get(f.getAbsolutePath());
            if(Files.probeContentType(p).contains("zip")){
                diretto = false;
                unzip(f.getAbsolutePath(), f.getParent()+ File.separator + "extract-" + f.getName().replace(".zip", ""));
                //elimino il file zip
                FileUtils.deleteQuietly(f);
            }
        }
        return diretto;
    }
    
    public static List<File> unzipAllForXML(List<File> files) throws IOException{
        List<File> newFiles = new LinkedList<>();
        for(File f: files){
            Path p = Paths.get(f.getAbsolutePath());
            if(Files.probeContentType(p).contains("zip")){
                String destDirectory = f.getParent()+ File.separator + "extract-" + f.getName().replace(".zip", "");
                unzip(f.getAbsolutePath(), destDirectory);
                //elimino il file zip
                FileUtils.deleteQuietly(f);
                
                //rimuovo il file dalla lista e aggiungo i nuovi files
                List<File> tmp = new LinkedList<>();
                listFiles(destDirectory, tmp);
                tmp.forEach((t) -> {
                    newFiles.add(t);
                });
            }
        }
        return newFiles;
    }
    
    public static void unzip(String zipFilePath, String destDirectory) throws IOException {
       
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        
        if(entry==null){
            //log+="- Nothing to unzip\n";
        }
        
        // iterates over entries in the zip file
        while (entry != null) {
            
            String filePath = destDirectory + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                extractFile(zipIn, filePath);
            } else {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                dir.mkdir();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }
    
    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        Path p = Paths.get(filePath);
        File f = new File(p.getParent().toString());
        if (!f.exists()) {
            f.mkdir();
        }
        
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }
    
    /**
     * @param directoryName è la directory da verificare
     * @param files è la lista che viene popolata
     */
    public static void listFiles(String directoryName, List<File> files) {
        File directory = new File(directoryName);

        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                files.add(file);
            }
            else if (file.isDirectory()) {
                listFiles(file.getAbsolutePath(), files);
            }
        }
    }
    
    public static String stats(List<Integer> distanze){
        Collections.sort(distanze);
        double media;
        int mediana;
        if(distanze.size()%2==0){
            mediana = (distanze.get((distanze.size()/2)-1) + distanze.get((distanze.size()/2)))/2;
        }
        
        else{
            mediana = (distanze.get((distanze.size()/2)+1))/2;
        }

        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;

        int sum = 0;
        for(int i: distanze){
            if(i<min)
                min=i;
            if(i>max)
                max=i;
            sum+=i;
        }

        media = (double)sum/distanze.size();

        double variance = 0;
        for(double d :distanze)
            variance += (d-media)*(d-media);

        variance = variance/(distanze.size());

        double stdDev = Math.sqrt(variance);
        return "- Il file contiene " + distanze.size() + " oggetti fuori dal confine di riferimento, le cui distanze hanno:\nMedia: " + media + "\nMediana: " + mediana + "\nDistanza massima: "+ max +  "\nDistanza minima: "+ min +  "\nScarto quadratico medio: "+ stdDev + "\n";
    }
    
    public static int containsSubString(List<File> files, String sub_string){
        int index = 0;
        for(File f : files){
            String s = f.getName();
            int lastPointIndex = s.lastIndexOf(".");
            if(lastPointIndex!=-1){
                if(s.substring(lastPointIndex).equals(sub_string)){
                    return index;
                }
            }
            index++;
        }
        return -1;
    }
    
    public static List<Integer> getIndexesFromExtension(List<File> files, String sub_string){
        List<Integer> indexes = new LinkedList<>();
        int index = 0;
        for(File f : files){
            String s = f.getName();
            int lastPointIndex = s.lastIndexOf(".");
            if(lastPointIndex!=-1){
                if(s.substring(lastPointIndex).equals(sub_string)){
                    indexes.add(index);
                }
            }
            index++;
        }
        return indexes;
    }
    
    public static int containsFileName(List<File> files, String filename){
        int index = 0;
        for(File f : files){
            String s = f.getName();
            
            if(s.contains(filename)){
                return index;
            }
            index++;
        }
        return -1;
    }
    
    
    public static String detectEncoding(InputStream is) throws IOException {
        CharsetDetector charsetDetector=new CharsetDetector();
        charsetDetector.setText(is instanceof BufferedInputStream ? is : new BufferedInputStream(is));
        charsetDetector.enableInputFilter(true);
        CharsetMatch cm=charsetDetector.detect();
        return cm.getName();
    }
    
    public static boolean correctEncoding(String dataset_encoding, String tika_encoding){
        if(dataset_encoding == null){
            return false;
        }
        
        else if(dataset_encoding.equals(tika_encoding)){
            return true;
        }
        
        else if(dataset_encoding.toUpperCase().equals("UTF8") && tika_encoding.equals("UTF-8")){
            return true;
        }
        
        else if(tika_encoding.equals("ISO-8859-1") && (dataset_encoding.toUpperCase().equals("LATIN-1") || dataset_encoding.toUpperCase().equals("ISO LATIN1") || dataset_encoding.toUpperCase().equals("PC-850"))){
            return true;
        }
        
        else if((tika_encoding.equals("UTF-16LE") || tika_encoding.equals("UTF-16BE")) && dataset_encoding.equals("ucs2")){
            return true;
        }
        
        return false;
    }
}
