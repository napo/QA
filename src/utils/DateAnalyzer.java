/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import nickan.Dataset;

/**
 *
 * @author nicola
 */
public class DateAnalyzer{
        
    //Mappa chiave_frequenza,num_giorni_per_chiave
    public static  Map<String, Integer> FREQUENCY_MAP;
    
    public DateAnalyzer(){
        FREQUENCY_MAP = new HashMap<>();
        FREQUENCY_MAP.put("ANNUALE", 366);
        FREQUENCY_MAP.put("ANNUAL", 366);
        FREQUENCY_MAP.put("WEEKLY", 7);
        FREQUENCY_MAP.put("TRIENNALE", 1096);
        FREQUENCY_MAP.put("TRIMESTRALE", 92);
        FREQUENCY_MAP.put("QUARTERLY", 92);
        FREQUENCY_MAP.put("DAILY", 1);
        FREQUENCY_MAP.put("BIENNALE", 731);
        FREQUENCY_MAP.put("BIENNIAL", 731);
        FREQUENCY_MAP.put("MONTHLY", 31);
        FREQUENCY_MAP.put("BIWEEKLY", 14);
        FREQUENCY_MAP.put("QUINQUENNALE", 1827);
        FREQUENCY_MAP.put("DECENNALE", 3652);
        FREQUENCY_MAP.put("TRIMESTRALE", 92);
    }
    
    public String isUpdated(Dataset dataset){
        String result;
        try{
            Timestamp tModified = dataset.getModified_date();
            Timestamp today = new Timestamp(System.currentTimeMillis());

            long diff = today.getTime() - tModified.getTime();
            int dayDiff = (int)((diff/1000)/(60)/(60)/(24));
            if(FREQUENCY_MAP.containsKey(dataset.getFrequency())){
                if(dayDiff <= FREQUENCY_MAP.get(dataset.getFrequency())){
                    result = "YES";
                }
                else{
                    result = "NO";
                }
            }
            else{
                result = "NON VERIFICABILE";
            }
        }
        
        catch(NullPointerException e){
            result = "NON VERIFICABILE";
        }
        return result;
    }
}
