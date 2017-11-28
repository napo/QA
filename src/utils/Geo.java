/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.WKTReader;
import dbUtils.DBManager;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.apache.commons.io.FileUtils;
import org.geotools.feature.FeatureIterator;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 *
 * @author nicola
 */
public class Geo {
    
    double lat;
    double lon;
    
    String ADM1;
    String ADM2;
    String ADM3;
    String ADM4;
    
    private final String geonamesUrl;
    private boolean correct = true;
    String jsonDir = new File(DBManager.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getParent() + "/json/";
    String rdfDir = new File(DBManager.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getParent() + "/rdf/";;
    
    DBManager manager;
    private String log = "";
    
    private boolean trasformed = false;
    
    Geometry multiPolygonWGS84;
    
    CoordinateReferenceSystem targetCRS;
    
    Geometry MultiPolygonGeometry;
    
    Geometry rdfGeometryWGS84;
    
    GeometryFactory geometryFactory;
    
    
    public Geo(String url) throws Exception{
        
        geonamesUrl = url;
        geometryFactory = JTSFactoryFinder.getGeometryFactory();
    }
    
    /**
     * @param points
     * @return true se tutti i punti della lista sono interni al confine, false altrimenti.
     */
    public boolean validateCoordinates(List<Coordinate> points) throws IOException, FactoryException, MismatchedDimensionException, TransformException, MalformedURLException, com.vividsolutions.jts.io.ParseException, ParseException, URISyntaxException{
        
        
        if(multiPolygonWGS84 == null)
            getBorders();
        
        boolean valid = true;
        
        boolean contains00 = false;
        Geometry tmp;

        if(targetCRS != null && targetCRS != CRS.decode("EPSG:4326")){
            tmp = (Geometry) MultiPolygonGeometry.clone();
        }
        
        else{
            tmp = (Geometry) multiPolygonWGS84.clone();
        }
        
        List<Integer> distanze = new LinkedList<>();
        
        for(Coordinate c: points){
            Point point = geometryFactory.createPoint(c);
            
            if(!tmp.contains(point)){
                if(!contains00 && c.x==0 && c.y==0){
                    log+="- Il file contiene una o più coordinate 0,0 che non è contenuta nei confini di riferimento\n";
                    contains00=true;
                }
                else if(c.x!=0 && c.y!=0){
                    int distanceMeters = (int) getDistance(point);
                    if(distanceMeters>0){
                        distanze.add(distanceMeters);
                    }
                        //log+="- Il punto: " + c.x + "," + c.Y + " è fuori dai confini di riferimento di " + distanceMeters + "m\n";
                    //System.out.println("- Il punto: " + c.x + "," + c.y + " è fuori dai confini di riferimento di " + distanceMeters + "m");
                    //System.out.println("- Il punto: " + getPointWGS84(c).x + "," + getPointWGS84(c).y + " è fuori dai confini di riferimento di " + distanceMeters + "m");
                }
                valid=false;
            }
        }
        if(!distanze.isEmpty()){
            if(distanze.size()==1){
                log += "- Il file contiene un oggetto fuori dal confine di riferimento di " + distanze.get(0) + "m\n";
            }
            else{
                log += Utils.stats(distanze);
            }
        }
        setCorrect(valid);
        return valid;
    }
    
    /**
     * @param c
     * @return coordinata nella proiezione WGS84, partendo dal sistema di riferimento con cui e' stato creato l'oggetto Geo.
     */
    public Coordinate getPointWGS84(Coordinate c) throws FactoryException, MismatchedDimensionException, TransformException{
        Point point = geometryFactory.createPoint(c);
        
        MathTransform transform = CRS.findMathTransform(targetCRS, CRS.decode("EPSG:4326"), true);
        Point tmp = (Point) JTS.transform(point, transform);            
        return new Coordinate(tmp.getCoordinate());
    }
    
    
    public boolean validateGeoJson(String filepath) throws IOException, FactoryException, MismatchedDimensionException, TransformException, MalformedURLException, com.vividsolutions.jts.io.ParseException, ParseException, URISyntaxException{
        boolean wellformed = true;
        if(geonamesUrl!=null)
            getBorders();
        else{
            setCorrect(false);
        }
        
        FeatureJSON io = new FeatureJSON();
        File file = new File(filepath);
        String json = FileUtils.readFileToString(file);
        List<Coordinate> points = new LinkedList<>();
        
        FeatureIterator<SimpleFeature> features = io.streamFeatureCollection(json);
        try {
            while(features.hasNext()) {
                SimpleFeature feature = (SimpleFeature) features.next();
                Geometry geom = (Geometry) feature.getDefaultGeometry();

                if(geonamesUrl!=null){
                    boolean insideGeometry = multiPolygonWGS84.contains(geom);
                    if(!insideGeometry){
                        boolean intersectGeometry = multiPolygonWGS84.intersects(geom);
                        if(!intersectGeometry){     
                            for(Coordinate c : geom.getCoordinates()){
                                points.add(new Coordinate(c.y, c.x));                                
                            }
                        }
                    }
                }
            }
        }
        catch (Exception ex) {
            log +="- " + ex.toString() + "\n";
            wellformed = false;
        }
        if(!points.isEmpty())
            validateCoordinates(points);
        
        return wellformed;
    }
    
    
    
    private void readGeoJson(String filepath) throws IOException, FactoryException, MismatchedDimensionException, TransformException, MalformedURLException, com.vividsolutions.jts.io.ParseException, ParseException, URISyntaxException{
        if(geonamesUrl!=null)
            getBorders();
        else{
            setCorrect(false);
        }
        
        FeatureJSON io = new FeatureJSON();
        File file = new File(filepath);
        String json = FileUtils.readFileToString(file);
        List<Coordinate> points = new LinkedList<>();
        
        FeatureIterator<SimpleFeature> features = io.streamFeatureCollection(json);
        try {
            while(features.hasNext()) {
                SimpleFeature feature = (SimpleFeature) features.next();
                Geometry geom = (Geometry) feature.getDefaultGeometry();

                if(geonamesUrl!=null){
                    //boolean insideGeometry = containsGeometry(geom, crs);
                    boolean insideGeometry = multiPolygonWGS84.contains(geom);
                    if(!insideGeometry){
                        boolean intersectGeometry = multiPolygonWGS84.intersects(geom);
                        if(!intersectGeometry){     
                            for(Coordinate c : geom.getCoordinates()){
                                points.add(new Coordinate(c.y, c.x));                                
                            }
                        }
                    }
                }
            }
        }
        catch (Exception ex) {
            log +="- " + ex.toString() + "\n";
            correct = false;
        }
        if(!points.isEmpty())
            validateCoordinates(points);
    }
    
    
    
    
    
    
//    private void readGeoJson(String filepath) throws IOException, FactoryException, MismatchedDimensionException, TransformException{
//        
//        FeatureJSON io = new FeatureJSON();
//        File file = new File(filepath);
//        String json = FileUtils.readFileToString(file);
//        List<Coordinate> points = new LinkedList<>();
//        
//        FeatureIterator<SimpleFeature> features = io.streamFeatureCollection(json);
//        
//        //io.readFeatureCollectionSchema(json, false);
//        
//        FeatureCollection actual = io.readFeatureCollection;
//        FeatureIterator a = actual.features();
//        
//        while(a.hasNext()){
//            SimpleFeature feature = (SimpleFeature)a.next();
//        }
//        
//        boolean iter = features.hasNext();
//        boolean isFirst = true;
//        
//        while(iter){
//            try {
//                if(!isFirst){
//                    iter = features.hasNext();
//                }
//                SimpleFeature feature = (SimpleFeature) features.next();
//                Geometry geom = (Geometry) feature.getDefaultGeometry();
//                System.out.println(geom.toString());
//                isFirst=false;
//                //iter = features.hasNext();
//            }
//            catch (RuntimeException e) {
//                
//                System.out.println(e);
//            }
//            
//        }
//        
//        try {
//            while(features.hasNext()) {
//                SimpleFeature feature = (SimpleFeature) features.next();
//                Geometry geom = (Geometry) feature.getDefaultGeometry();
//                System.out.println(geom.toString());
//
//                if(geonamesUrl!=null){
//                    //boolean insideGeometry = containsGeometry(geom, crs);
//                    boolean insideGeometry = multiPolygonWGS84.contains(geom);
//                    if(!insideGeometry){
//                        boolean intersectGeometry = multiPolygonWGS84.intersects(geom);
//                        if(!intersectGeometry){     
//                            for(Coordinate c : geom.getCoordinates()){
//                                points.add(new Coordinate(c.x, c.y));
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        catch (Exception ex) {
//            
//            System.out.println(ex);
//                //Logger.getLogger(ShapeFile.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        validateCoordinates(points);
//    }
    
    
    /**
     * @param point
     * @return distanza tra point e il confine di riferimento
     */
    private double getDistance(Point point) throws FactoryException, MismatchedDimensionException, TransformException{
        double distance = -1;
        if(targetCRS == null){
            CoordinateReferenceSystem toCrs = CRS.decode("EPSG:3857");
            MathTransform transform = CRS.findMathTransform(CRS.decode("EPSG:4326"), toCrs, true);
            Geometry tmp3857 = JTS.transform(multiPolygonWGS84, transform);
            
            /*
            CoordinateReferenceSystem toCrs1 = CRS.decode("EPSG:32632");
            MathTransform transform1 = CRS.findMathTransform(CRS.decode("EPSG:4326"), toCrs1, true);
            Geometry tmp38571 = JTS.transform(multiPolygonWGS84, transform1);
            */
            
            try{
                Geometry distancePoint = JTS.transform(point, transform);
                distance = distancePoint.distance(tmp3857);
            }
            catch(MismatchedDimensionException | TransformException e){
                log += "- Error: " + e.getMessage() + "\n";
            }
            return distance;
        }
        
        else if(targetCRS==CRS.decode("EPSG:4326")){
            CoordinateReferenceSystem toCrs = CRS.decode("EPSG:3857");
            MathTransform transform = CRS.findMathTransform(targetCRS, toCrs, true);            
            Geometry tmp3857 = JTS.transform(multiPolygonWGS84, transform);  
            try{
                Geometry distancePoint = JTS.transform(point, transform);
                distance = distancePoint.distance(tmp3857);
            }
            catch(MismatchedDimensionException | TransformException e){
                log += "- Error: " + e.getMessage() + "\n";
            }
            return distance;            
        }
        else{
            return point.distance(MultiPolygonGeometry);
        }
    }
    
    
    
    /**
     * @param geom la geometria da verificare se e' contenuta
     * @param targetCRS e' il CRS di geom
     * @return true se geom e' contenuta nel confine di riferimento, false altrimenti
     */
    public boolean containsGeometry(Geometry geom, CoordinateReferenceSystem targetCRS) throws FactoryException, IOException, MismatchedDimensionException, TransformException, MalformedURLException, com.vividsolutions.jts.io.ParseException, ParseException, URISyntaxException{
        if(multiPolygonWGS84 == null){
            getBorders();
            this.targetCRS = targetCRS;
        }
        
        if(!trasformed){
            CoordinateReferenceSystem crsWGS84 = CRS.decode("EPSG:4326");
            MathTransform transform = CRS.findMathTransform(crsWGS84, targetCRS, true);        
            MultiPolygonGeometry = JTS.transform(multiPolygonWGS84, transform);
            //MultiPolygonGeometry = MultiPolygonGeometry.buffer(300.0);
            trasformed = true;
        }
        
        //return MultiPolygonGeometry.buffer(300.0).contains(geom);
        return MultiPolygonGeometry.contains(geom);
    }
    
    
    
    /**
     * @param geom
     * @return true se geom interseca il confine di riferimento
     */
    public boolean intersectGeometry(Geometry geom){
        return MultiPolygonGeometry.intersects(geom);
    }
    
    public int getDistanceGeometry(Geometry geom){
        return (int) MultiPolygonGeometry.distance(geom);
    }
    
    /**
     * 
     * @param url
     * @return estrapola le informazioni necessarie per la creazione dei confini dal file indicato in url, verificando prima se e' gia' presente in memoria
     */
    private JsonObject getGeoJson(String url) throws MalformedURLException, IOException{
        
        URL geographical_geonames_url = new URL(url);        
        String geonameId = geographical_geonames_url.getFile().replace("/", "");
        
        File destDir = new File(jsonDir);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        List<File> fileList = new LinkedList<>();
        Utils.listFiles(jsonDir, fileList);
        
        File f = new File(jsonDir + geonameId + ".json");
        JsonObject json;
        JsonObject geoJson = new JsonObject();
        
        if(!f.exists()){
            String jsonUrl = "http://www.geonames.org/servlet/geonames?&srv=780&geonameId=" + geonameId + "&type=json";
            Utils.download(jsonUrl, f.getName(), jsonDir);            
        }
        
        try{
            json = Json.parse(FileUtils.readFileToString(f)).asObject();
            JsonArray shapes = json.get("shapes").asArray();
            geoJson = shapes.get(0).asObject().get("geoJson").asObject();
        }
        catch(IOException e){}
        
        return geoJson;
    }
    
    private String getRDF() throws MalformedURLException, IOException{
        
        String geonameId = new URL(getGeonamesUrl()).getFile().replace("/", "");
        
        File destDir = new File(rdfDir);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        List<File> fileList = new LinkedList<>();
        Utils.listFiles(rdfDir, fileList);
        
        File f = new File(rdfDir + geonameId + ".rdf");
        
        if(!f.exists()){
            String rdfUrl = "http://sws.geonames.org/" + geonameId + "/about.rdf";
            Utils.download(rdfUrl, f.getName(), rdfDir);
        }
        return f.getAbsolutePath();
    }
    
    public void getBorders() throws MalformedURLException, IOException, com.vividsolutions.jts.io.ParseException, FactoryException, TransformException, ParseException, URISyntaxException{
        
        File f = new File(getRDF());
        
        //se non esiste
        if(!f.exists()){
            
        }
        
        RDFParse rdfParse = new RDFParse();
        Map<String, String> data = new HashMap<>();        
        
        rdfParse.getCoords(f.getAbsolutePath(), data);
        
        manager = new DBManager();        
        Integer[] CODS = manager.getCODS(data.get("lat"), data.get("lon"));
        
        String resultWKT;
        
        if(data.get("ADM2")!=null){
            resultWKT = manager.getGeometry("adm3", "PRO_COM", CODS[2]);
        }
        else if(data.get("ADM1")!=null){
            resultWKT = manager.getGeometry("adm2", "COD_PRO", CODS[1]);
        }
        else{
            resultWKT = manager.getGeometry("adm1", "COD_REG", CODS[0]);
        }
        
        

        WKTReader wkt = new WKTReader();
        Geometry polygon = wkt.read(resultWKT);
        

        CoordinateReferenceSystem toCrs1 = CRS.decode("EPSG:32632");
        MathTransform transform1 = CRS.findMathTransform(toCrs1, CRS.decode("EPSG:4326"), true);
        multiPolygonWGS84 = JTS.transform(polygon, transform1);
        
        
    }
    
    
    
    
    
    /**
     * inizializza gli oggetti che servono per identificare il confine a cui si fa riferimento
     * @throws IOException 
     */
//    public void getBorders() throws IOException{
//        JsonObject geoJson = getGeoJson(getGeonamesUrl());
//        
//        JsonArray coordinates = geoJson.get("coordinates").asArray();
//        String type = geoJson.get("type").asString();        
//        
//        Polygon[] polygons = null;
//        
//        LinearRing ring = null;
//        LinearRing[] holes;
//               
//        if(type.equals("Polygon")){
//            polygons = new Polygon[1];
//            holes = new LinearRing[coordinates.size()-1];
//            
//            for(int i=0; i<coordinates.size(); i++){
//                JsonArray p = coordinates.get(i).asArray();
//                if(i==0){
//                    Coordinate[] coords = new Coordinate[p.size()];
//                    for(int countCoords = 0; countCoords<coords.length; countCoords++){
//                        //coords[countCoords] = new Coordinate(p.get(countCoords).asArray().get(0).asDouble(), p.get(countCoords).asArray().get(1).asDouble());
//                        coords[countCoords] = new Coordinate(p.get(countCoords).asArray().get(1).asDouble(), p.get(countCoords).asArray().get(0).asDouble());
//                    }
//                    ring = geometryFactory.createLinearRing(coords);
//                }
//                else{
//                    //hole                    
//                    Coordinate[] coords = new Coordinate[p.size()];
//                    for(int countCoords = 0; countCoords<coords.length; countCoords++){
//                        coords[countCoords] = new Coordinate(p.get(countCoords).asArray().get(1).asDouble(), p.get(countCoords).asArray().get(0).asDouble());
//                    }
//                    holes[i-1] = geometryFactory.createLinearRing(coords);
//                }
//            }
//            polygons[0] = geometryFactory.createPolygon(ring, holes);
//        }
//        
//        else if(type.equals("MultiPolygon")){
//            polygons  = new Polygon[coordinates.size()];
//            for(int countPoly=0; countPoly<coordinates.size(); countPoly++){
//                holes = new LinearRing[coordinates.get(countPoly).asArray().size()-1];
//                for(int i=0; i<coordinates.get(countPoly).asArray().size(); i++){
//                    JsonArray p = coordinates.get(countPoly).asArray().get(i).asArray();                    
//                    if(i==0){
//                        Coordinate[] coords = new Coordinate[p.size()];
//                        for(int countCoords = 0; countCoords<coords.length; countCoords++){
//                            coords[countCoords] = new Coordinate(p.get(countCoords).asArray().get(1).asDouble(), p.get(countCoords).asArray().get(0).asDouble());
//                        }
//                        ring = geometryFactory.createLinearRing(coords);
//                    }
//                    else{
//                        //hole                    
//                        Coordinate[] coords = new Coordinate[p.size()];
//                        Coordinate[] revcoords = new Coordinate[p.size()];
//                        for(int countCoords = 0; countCoords<coords.length; countCoords++){
//                            coords[countCoords] = new Coordinate(p.get(countCoords).asArray().get(1).asDouble(), p.get(countCoords).asArray().get(0).asDouble());
//                            revcoords[countCoords] = new Coordinate(p.get(countCoords).asArray().get(0).asDouble(), p.get(countCoords).asArray().get(1).asDouble());
//                        }
//                        holes[i-1] = geometryFactory.createLinearRing(coords);
//                    }
//                }
//                polygons[countPoly] = geometryFactory.createPolygon(ring, holes);
//            }
//        }
//        
//        multiPolygonWGS84 = geometryFactory.createMultiPolygon(polygons);
//    }
    
    

    /**
     * @return the log
     */
    public String getLog() {
        return log;
    }

    /**
     * @return the correct
     */
    public boolean isCorrect() {
        return correct;
    }

    /**
     * @return the geonamesUrl
     */
    public String getGeonamesUrl() {
        return geonamesUrl;
    }

    /**
     * @param correct the correct to set
     */
    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
