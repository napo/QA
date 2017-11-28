/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nickan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 *
 * @author nicola
 */
public class Client {
    
    private String portalUrl;
    private final String api3Url = "/api/3/action/";
    private final String api3UrlDatasetShow = "/api/3/action/package_show?id=";
    private final String api2UrlDatasetShow = "/api/2/rest/dataset/";
    private final String package_list = "package_list";
    
    public Client(String portalUrl){
        this.portalUrl = portalUrl;        
    }
    
    public List<String> getDatasetList() throws MalformedURLException, IOException, JSONException{
        URL queryUrl = new URL(getPortalUrl() + getApi3Url() + getPackage_list());
        HttpURLConnection httpConn = (HttpURLConnection) queryUrl.openConnection();
        
        JSONObject result;
        JSONArray j;
        List<String> datasetList=new LinkedList<>();
        
        try (BufferedReader in = new BufferedReader(new InputStreamReader(httpConn.getInputStream()))) {
            
            result = new JSONObject(in.readLine());
            j=result.getJSONArray("result");
            
            for(int i=0; i<j.length();i++){
                datasetList.add(j.getString(i));
            }            
        }        
        return datasetList;
    }

    /**
     * @return the portalUrl
     */
    public String getPortalUrl() {
        return portalUrl;
    }

    /**
     * @param portalUrl the portalUrl to set
     */
    public void setPortalUrl(String portalUrl) {
        this.portalUrl = portalUrl;
    }

    /**
     * @return the api3Url
     */
    public String getApi3Url() {
        return api3Url;
    }

    /**
     * @return the package_list
     */
    public String getPackage_list() {
        return package_list;
    }

    /**
     * @return the api3UrlDatasetShow
     */
    public String getApi3UrlDatasetShow() {
        return api3UrlDatasetShow;
    }

    /**
     * @return the api2UrlDatasetShow
     */
    public String getApi2UrlDatasetShow() {
        return api2UrlDatasetShow;
    }
    
}
