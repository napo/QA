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
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
/**
 *
 * @author nicola
 */
public class Resource {
    private String cache_last_updated;
    private String package_id;
    private String distribution_format;
    private String webstore_last_updated;
    private String datastore_active;
    private String id;
    private String size;
    private String state;
    private String hash;
    private String description;
    private String format;
    private String last_modified;
    private String url_type;
    private String mimetype;
    private String cache_url;
    private String name;
    private String created;
    private String url;
    private String webstore_url;
    private String mimetype_inner;
    private int position;
    private String revision_id;
    private String resource_type;
    
    private Map<String,String> nullKeys = new HashMap<>();
    
    public Resource(JSONObject resource) throws JSONException{
        
        initNullKeys();
        
        //cache_last_updated
        if(resource.has("cache_last_updated")){            
            if(!resource.getString("cache_last_updated").equals("null") && !resource.getString("cache_last_updated").equals("")){
                this.cache_last_updated=resource.getString("cache_last_updated");
            }            
        }
        
        //package_id
        if(resource.has("package_id")){
            if(!resource.getString("package_id").equals("null") && !resource.getString("package_id").equals("")){
                this.package_id=resource.getString("package_id");
            }            
        }
        
        //distribution_format
        if(resource.has("distribution_format")){
            if(!resource.getString("distribution_format").equals("null") && !resource.getString("distribution_format").equals("")){
                this.distribution_format=resource.getString("distribution_format");
            }            
        }
        
        //webstore_last_updated
        if(resource.has("webstore_last_updated")){
            if(!resource.getString("webstore_last_updated").equals("null") && !resource.getString("webstore_last_updated").equals("")){
                this.webstore_last_updated=resource.getString("webstore_last_updated");
            }            
        }
        
        //datastore_active
        if(resource.has("datastore_active")){
            if(!resource.getString("datastore_active").equals("null") && !resource.getString("datastore_active").equals("")){
                this.datastore_active=resource.getString("datastore_active");
            }            
        }
        
        //id
        if(resource.has("id")){
            if(!resource.getString("id").equals("null") && !resource.getString("id").equals("")){
                this.id=resource.getString("id");
            }            
        }
        
        //size
        if(resource.has("size")){
            if(!resource.getString("size").equals("null") && !resource.getString("size").equals("")){
                this.size=resource.getString("size");
            }            
        }
        
        //state
        if(resource.has("state")){
            if(!resource.getString("state").equals("null") && !resource.getString("state").equals("")){
                this.state=resource.getString("state");
            }            
        }
        
        //hash
        if(resource.has("hash")){
            if(!resource.getString("hash").equals("null") && !resource.getString("hash").equals("")){
                this.hash=resource.getString("hash");
            }            
        }
        
        //description
        if(resource.has("description")){
            if(!resource.getString("description").equals("null") && !resource.getString("description").equals("")){
                this.description=resource.getString("description");
            }            
        }
        
        //format
        if(resource.has("format")){
            if(!resource.getString("format").equals("null") && !resource.getString("format").equals("")){
                this.format=resource.getString("format");
            }            
        }
        
        //last_modified
        if(resource.has("last_modified")){
            if(!resource.getString("last_modified").equals("null") && !resource.getString("last_modified").equals("")){
                this.last_modified=resource.getString("last_modified");
            }            
        }
        
        //url_type
        if(resource.has("url_type")){
            if(!resource.getString("url_type").equals("null") && !resource.getString("url_type").equals("")){
                this.url_type=resource.getString("url_type");
            }            
        }
        
        //mimetype
        if(resource.has("mimetype")){
            if(!resource.getString("mimetype").equals("null") && !resource.getString("mimetype").equals("")){
                this.mimetype=resource.getString("mimetype");
            }            
        }
        
        //cache_url
        if(resource.has("cache_url")){
            if(!resource.getString("cache_url").equals("null") && !resource.getString("cache_url").equals("")){
                this.cache_url=resource.getString("cache_url");
            }            
        }
        
        //name
        if(resource.has("name")){
            if(!resource.getString("name").equals("null") && !resource.getString("name").equals("")){
                this.name=resource.getString("name");
            }            
        }
        
        //created
        if(resource.has("created")){
            if(!resource.getString("created").equals("null") && !resource.getString("created").equals("")){
                this.created=resource.getString("created");
            }            
        }
        
        //url
        if(resource.has("url")){
            if(!resource.getString("url").equals("null") && !resource.getString("url").equals("")){
                this.url=resource.getString("url");
            }            
        }
        
        //webstore_url
        if(resource.has("webstore_url")){
            if(!resource.getString("webstore_url").equals("null") && !resource.getString("webstore_url").equals("")){
                this.webstore_url=resource.getString("webstore_url");
            }            
        }
        
        //mimetype_inner
        if(resource.has("mimetype_inner")){
            if(!resource.getString("mimetype_inner").equals("null") && !resource.getString("mimetype_inner").equals("")){
                this.mimetype_inner=resource.getString("mimetype_inner");
            }            
        }
        
        //position
        if(resource.has("position")){
            this.position=resource.getInt("position");
        }
        
        //revision_id
        if(resource.has("revision_id")){
            if(!resource.getString("revision_id").equals("null") && !resource.getString("revision_id").equals("")){
                this.revision_id=resource.getString("revision_id");
            }            
        }
        
        //resource_type
        if(resource.has("resource_type")){
            if(!resource.getString("resource_type").equals("null") && !resource.getString("resource_type").equals("")){
                this.resource_type=resource.getString("resource_type");
            }            
        }
        removeNullKeys();
    }
    
    public Resource(URL jsonUrl) throws JSONException, IOException{
        
        initNullKeys();
        
        HttpURLConnection httpConn = (HttpURLConnection) jsonUrl.openConnection();
        JSONObject result;
        
        try (BufferedReader in = new BufferedReader(new InputStreamReader(httpConn.getInputStream()))) {
            result = new JSONObject(new JSONObject(in.readLine()).getString("result"));
        }
        
        if(result.has("cache_last_updated")){
            if (!result.getString("cache_last_updated").equals("") && !result.getString("cache_last_updated").equals("null")){
                cache_last_updated = result.getString("cache_last_updated");
            }
        }
        
        if(result.has("package_id")){
            if (!result.getString("package_id").equals("") && !result.getString("package_id").equals("null")){
                package_id = result.getString("package_id");
            }
        }
        
        //distribution_format
        if(result.has("distribution_format")){
            if (!result.getString("distribution_format").equals("") && !result.getString("distribution_format").equals("null")){
                distribution_format = result.getString("distribution_format");
            }
        }
        
        
        //webstore_last_updated
        if(result.has("webstore_last_updated")){
            if (!result.getString("webstore_last_updated").equals("") && !result.getString("webstore_last_updated").equals("null")){
                webstore_last_updated = result.getString("webstore_last_updated");
            }
        }
            
        //datastore_active
        if(result.has("datastore_active")){
            if (!result.getString("datastore_active").equals("") && !result.getString("datastore_active").equals("null")){
                datastore_active = result.getString("datastore_active");
            }
        }
        
        //id
        if(result.has("id")){
            if (!result.getString("id").equals("") && !result.getString("id").equals("null")){
                id = result.getString("id");
            }
        }
        
        //size
        if(result.has("size")){
            if (!result.getString("size").equals("") && !result.getString("size").equals("null")){
                size = result.getString("size");
            }
        }
        
        //state
        if(result.has("state")){
            if (!result.getString("state").equals("") && !result.getString("state").equals("null")){
                state = result.getString("state");
            }
        }
        
        //hash
        if(result.has("hash")){
            if (!result.getString("hash").equals("") && !result.getString("hash").equals("null")){
                hash = result.getString("hash");
            }
        }
        
        //description
        if(result.has("description")){
            if (!result.getString("description").equals("") && !result.getString("description").equals("null")){
                description = result.getString("description");
            }
        }
        
        //format
        if(result.has("format")){
            if (!result.getString("format").equals("") && !result.getString("format").equals("null")){
                format = result.getString("format");
            }
        }
        
        //last_modified
        if(result.has("last_modified")){
            if (!result.getString("last_modified").equals("") && !result.getString("last_modified").equals("null")){
                last_modified = result.getString("last_modified");
            }
        }

        //url_type
        if(result.has("url_type")){
            if (!result.getString("url_type").equals("") && !result.getString("url_type").equals("null")){
                url_type = result.getString("url_type");
            }
        }
        
        //mimetype
        if(result.has("mimetype")){
            if (!result.getString("mimetype").equals("") && !result.getString("mimetype").equals("null")){
                mimetype = result.getString("mimetype");
            }
        }
        
        //cache_url
        if(result.has("cache_url")){
            if (!result.getString("cache_url").equals("") && !result.getString("cache_url").equals("null")){
                cache_url = result.getString("cache_url");
            }
        }

        //name
        if(result.has("name")){
            if (!result.getString("name").equals("") && !result.getString("name").equals("null")){
                name = result.getString("name");
            }
        }

        //created
        if(result.has("created")){
            if (!result.getString("created").equals("") && !result.getString("created").equals("null")){
                created = result.getString("created");
            }
        }

        //url
        if(result.has("url")){
            if (!result.getString("url").equals("") && !result.getString("url").equals("null")){
                url = result.getString("url");
            }
        }

        //webstore_url
        if(result.has("webstore_url")){
            if (!result.getString("webstore_url").equals("") && !result.getString("webstore_url").equals("null")){
                webstore_url = result.getString("webstore_url");
            }
        }

        //mimetype_inner
        if(result.has("mimetype_inner")){
            if (!result.getString("mimetype_inner").equals("") && !result.getString("mimetype_inner").equals("null")){
                mimetype_inner = result.getString("mimetype_inner");
            }
        }

        //position
        if(result.has("position")){
            position = result.getInt("position");
        }
        
        //revision_id
        if(result.has("revision_id")){
            if (!result.getString("revision_id").equals("") && !result.getString("revision_id").equals("null")){
                revision_id = result.getString("revision_id");
            }
        }

        //resource_type
        if(result.has("resource_type")){
            if (!result.getString("resource_type").equals("") && !result.getString("resource_type").equals("null")){
                resource_type = result.getString("resource_type");
            }
        }     
        
        removeNullKeys();
    }
    
    public Resource(){
        initNullKeys();
    }
    
    private void initNullKeys(){
        nullKeys.put("format", "M");
        nullKeys.put("url", "M");
        nullKeys.put("description", "R");
        nullKeys.put("name", "O");
        nullKeys.put("size", "O");
        nullKeys.put("last_modified", "O");        
    }
    
    public void removeNullKeys(){
        if(format!=null){
            nullKeys.remove("format");
        }
        if(url!=null){
            nullKeys.remove("url");
        }
        if(description!=null){
            nullKeys.remove("description");
        }
        if(name!=null){
            nullKeys.remove("name");
        }
        if(size!=null){
            nullKeys.remove("size");
        }
        if(last_modified!=null){
            nullKeys.remove("last_modified");
        }
    }
    
    /**
     * @return the cache_last_updated
     */
    public String getCache_last_updated() {
        return cache_last_updated;
    }

    /**
     * @param cache_last_updated the cache_last_updated to set
     */
    public void setCache_last_updated(String cache_last_updated) {
        this.cache_last_updated = cache_last_updated;
    }

    /**
     * @return the package_id
     */
    public String getPackage_id() {
        return package_id;
    }

    /**
     * @param package_id the package_id to set
     */
    public void setPackage_id(String package_id) {
        this.package_id = package_id;
    }

    /**
     * @return the webstore_last_updated
     */
    public String getWebstore_last_updated() {
        return webstore_last_updated;
    }

    /**
     * @param webstore_last_updated the webstore_last_updated to set
     */
    public void setWebstore_last_updated(String webstore_last_updated) {
        this.webstore_last_updated = webstore_last_updated;
    }

    /**
     * @return the datastore_active
     */
    public String getDatastore_active() {
        return datastore_active;
    }

    /**
     * @param datastore_active the datastore_active to set
     */
    public void setDatastore_active(String datastore_active) {
        this.datastore_active = datastore_active;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the size
     */
    public String getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(String size) {
        this.size = size;
    }

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return the hash
     */
    public String getHash() {
        return hash;
    }

    /**
     * @param hash the hash to set
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the format
     */
    public String getFormat() {
        return format;
    }

    /**
     * @param format the format to set
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * @return the last_modified
     */
    public String getLast_modified() {
        return last_modified;
    }

    /**
     * @param last_modified the last_modified to set
     */
    public void setLast_modified(String last_modified) {
        this.last_modified = last_modified;
    }

    /**
     * @return the url_type
     */
    public String getUrl_type() {
        return url_type;
    }

    /**
     * @param url_type the url_type to set
     */
    public void setUrl_type(String url_type) {
        this.url_type = url_type;
    }

    /**
     * @return the mimetype
     */
    public String getMimetype() {
        return mimetype;
    }

    /**
     * @param mimetype the mimetype to set
     */
    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    /**
     * @return the cache_url
     */
    public String getCache_url() {
        return cache_url;
    }

    /**
     * @param cache_url the cache_url to set
     */
    public void setCache_url(String cache_url) {
        this.cache_url = cache_url;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the created
     */
    public String getCreated() {
        return created;
    }

    /**
     * @param created the created to set
     */
    public void setCreated(String created) {
        this.created = created;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the webstore_url
     */
    public String getWebstore_url() {
        return webstore_url;
    }

    /**
     * @param webstore_url the webstore_url to set
     */
    public void setWebstore_url(String webstore_url) {
        this.webstore_url = webstore_url;
    }

    /**
     * @return the mimetype_inner
     */
    public String getMimetype_inner() {
        return mimetype_inner;
    }

    /**
     * @param mimetype_inner the mimetype_inner to set
     */
    public void setMimetype_inner(String mimetype_inner) {
        this.mimetype_inner = mimetype_inner;
    }

    /**
     * @return the position
     */
    public int getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * @return the revision_id
     */
    public String getRevision_id() {
        return revision_id;
    }

    /**
     * @param revision_id the revision_id to set
     */
    public void setRevision_id(String revision_id) {
        this.revision_id = revision_id;
    }

    /**
     * @return the resource_type
     */
    public String getResource_type() {
        return resource_type;
    }

    /**
     * @param resource_type the resource_type to set
     */
    public void setResource_type(String resource_type) {
        this.resource_type = resource_type;
    }    
    
    /**
     * @return the distribution_format
     */
    public String getDistribution_format() {
        return distribution_format;
    }

    /**
     * @param distribution_format the distribution_format to set
     */
    public void setDistribution_format(String distribution_format) {
        this.distribution_format = distribution_format;
    }

    /**
     * @return the nullKeys
     */
    public Map<String,String> getNullKeys() {
        return nullKeys;
    }

    /**
     * @param nullKeys the nullKeys to set
     */
    public void setNullKeys(Map<String,String> nullKeys) {
        this.nullKeys = nullKeys;
    }
    
    
    
}
