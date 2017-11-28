/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFHandlerException;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.UnsupportedRDFormatException;
import org.eclipse.rdf4j.rio.helpers.BasicParserSettings;
import org.eclipse.rdf4j.rio.helpers.RDFHandlerBase;

/**
 *
 * @author ospite
 */
public class RDFParse {
    
    
    public void getCoords(String rdfPath, Map<String, String> data) throws FileNotFoundException{
        class StatementParser extends RDFHandlerBase {
            String lat;
            String lon;
    
            String ADM1;
            String ADM2;
            String ADM3;
            String ADM4;
            
            @Override
            public void handleStatement(Statement st) {

                String string;
                String localName = st.getPredicate().getLocalName();

                if(localName.equals("lat")){
                    string = st.getObject().toString().replace("\"", "");
                    String[] parts = string.split("\\^\\^");
                    lat = parts[0];
                }

                if(localName.equals("long")){
                    string = st.getObject().toString().replace("\"", "");
                    String[] parts = string.split("\\^\\^");
                    lon = parts[0];
                }

                if(localName.equals("parentADM1")){
                    ADM1 = st.getObject().toString();
                }

                if(localName.equals("parentADM2")){
                    ADM2 = st.getObject().toString();
                }

                if(localName.equals("parentADM3")){
                    ADM3 = st.getObject().toString();
                }

                if(localName.equals("parentADM4")){
                    ADM4 = st.getObject().toString();
                }
            }
        }
        
        
        try {
            
            FileInputStream in = new FileInputStream(rdfPath);
            RDFParser parser;
            parser = Rio.createParser(RDFFormat.RDFXML);

            StatementParser statementParser = new StatementParser();
            parser.setRDFHandler(statementParser);
            parser.getParserConfig().set(BasicParserSettings.VERIFY_DATATYPE_VALUES, false);
            parser.getParserConfig().set(BasicParserSettings.NORMALIZE_DATATYPE_VALUES, false);
                        
            parser.parse(in, "http://RDFERROR");
            
            data.put("lat", statementParser.lat);
            data.put("lon", statementParser.lon);
            data.put("ADM1", statementParser.ADM1);
            data.put("ADM2", statementParser.ADM2);
            data.put("ADM3", statementParser.ADM3);
            data.put("ADM4", statementParser.ADM4);
            

            
        }
        catch (Exception e) {
        }
        
        
        
        
        
    }
    
    public boolean validateRDF(String rdfPath){
        class StatementParser extends RDFHandlerBase {
            
            @Override
            public void handleStatement(Statement st) {
            }
            
            
        }
        
        try {
            
            FileInputStream in = new FileInputStream(rdfPath);
            RDFParser parser;
            parser = Rio.createParser(RDFFormat.RDFXML);

            StatementParser statementParser = new StatementParser();
            parser.setRDFHandler(statementParser);
            parser.getParserConfig().set(BasicParserSettings.VERIFY_DATATYPE_VALUES, false);
            parser.getParserConfig().set(BasicParserSettings.NORMALIZE_DATATYPE_VALUES, false);
                        
            parser.parse(in, "http://RDFERROR");
        }
        catch (IOException | RDFHandlerException | RDFParseException | UnsupportedRDFormatException e) {
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }
}
