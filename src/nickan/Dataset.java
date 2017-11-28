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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author nicola
 */
public class Dataset {
    
    private String id;
    private String license_title;
    private String maintainer;
    private String encoding;
    private String issued;
    private String temporal_start;
    private String isPrivate;
    private String creation_date;
    private int num_tags;
    private String frequency;
    private String publisher_name;
    private String metadata_created;
    private String temporal_end;
    private String metadata_modified;
    private String author;
    private String author_email;
    private String theme;
    private String site_url;
    private String state;
    private String version;
    private String license_id;
    private String type;
    private String holder_name;
    private String holder_identifier;
    private String fields_description;
    private String creator_user_id;
    private String maintainer_email;
    private int num_resources;
    private String name;
    private String isopen;
    private String url;
    private String notes;
    private String owner_org;
    private String modified;
    private String publisher_identifier;
    private String geographical_name;
    private String license_url;
    private String title;
    private String revision_id;
    private String identifier;
    private String creator_name;
    private String creator_identifier;
    private String conforms_to;
    private String language;
    private String alternate_identifier;
    private String is_version_of;
    private String contact;
    private String geographical_geonames_url;
    
    private String portalUrl;
    private String apiUrl;
    
    private Timestamp modified_date;
    
    private List<Tag> tags;
    private List<Resource> resources;
    private Organization organization;
    
    private Map<String,String> nullKeys = new HashMap<>();
    
    /*
    DA IMPLEMENTARE:
    extras
    groups
    relationships_as_object
    relationships_as_subject
    */
    
    public Dataset(String portalUrl, String apiUrl, String name) throws MalformedURLException, IOException, JSONException{
                
        initNullKeys();
        
        this.portalUrl = portalUrl;
        this.apiUrl = apiUrl;
        
        URL queryUrl = new URL(portalUrl + apiUrl + name);
        JSONObject result, extrasApi2 = null;
        JSONArray j;
        
        boolean isApi3 = true;
        
        try{
            HttpURLConnection httpConn = (HttpURLConnection) queryUrl.openConnection();

            try (BufferedReader in = new BufferedReader(new InputStreamReader(httpConn.getInputStream()))) {
                String line = in.readLine();
                if(new JSONObject(line).has("result")){
                    result = new JSONObject(new JSONObject(line).getString("result"));
                }
                else{
                    result = new JSONObject(line);
                    isApi3 = false;
                    extrasApi2 = result.getJSONObject("extras");
                }
            }    
            
            //type
            if(result.has("type")){
                if(!result.getString("type").equals("") && !result.getString("type").equals("null")){
                    this.type=result.getString("type");
                }
            }
            
            //inizializza tutti i valori solo se e' effettivamente un dataset
            if(type.equals("dataset")){            
                
                //tags                
                tags = new LinkedList<>();
                if(isApi3){
                    if(result.has("tags")){
                        j=result.getJSONArray("tags");
                        for(int i=0; i<j.length();i++){
                            JSONObject tag = j.getJSONObject(i);
                            tags.add(new Tag(tag));            
                        }
                    }
                }

                //resources
                if(result.has("resources")){
                    resources = new LinkedList<>();        
                    j=result.getJSONArray("resources");
                    for(int i=0; i<j.length();i++){
                        JSONObject resource = j.getJSONObject(i);
                        resources.add(new Resource(resource));
                    }
                }

                //organization
                if(result.has("organization")){
                    organization = new Organization(result.getJSONObject("organization"));
                }

                //id
                if(result.has("id")){
                    if(!result.getString("id").equals("") && !result.getString("id").equals("null")){
                        this.id=result.getString("id");
                    }
                }

                //license_title
                if(result.has("license_title")){
                    if(!result.getString("license_title").equals("") && !result.getString("license_title").equals("null")){
                        this.license_title=result.getString("license_title");
                    }
                }

                //maintainer
                if(result.has("maintainer")){
                    if(!result.getString("maintainer").equals("") && !result.getString("maintainer").equals("null")){
                        this.maintainer=result.getString("maintainer");
                    }
                }

                //encoding
                if(result.has("encoding")){
                    if(!result.getString("encoding").equals("") && !result.getString("encoding").equals("null")){
                        this.encoding=result.getString("encoding");
                    }
                }
                else if(!isApi3 && extrasApi2.has("encoding")){
                    if(!extrasApi2.getString("encoding").equals("") && !extrasApi2.getString("encoding").equals("null")){
                        this.encoding=extrasApi2.getString("encoding");
                    }
                }

                //issued
                if(result.has("issued")){
                    if(!result.getString("issued").equals("") && !result.getString("issued").equals("null")){
                        this.issued=result.getString("issued");
                    }
                }
                else if(!isApi3 && extrasApi2.has("issued")){
                    if(!extrasApi2.getString("issued").equals("") && !extrasApi2.getString("issued").equals("null")){
                        this.issued=extrasApi2.getString("issued");
                    }
                }

                //temporal_start
                if(result.has("temporal_start")){
                    if(!result.getString("temporal_start").equals("") && !result.getString("temporal_start").equals("null")){
                        this.temporal_start=result.getString("temporal_start");
                    }
                }
                else if(!isApi3 && extrasApi2.has("temporal_start")){
                    if(!extrasApi2.getString("temporal_start").equals("") && !extrasApi2.getString("temporal_start").equals("null")){
                        this.temporal_start=extrasApi2.getString("temporal_start");
                    }
                }

                //isPrivate
                if(result.has("isPrivate")){
                    this.isPrivate=result.getString("isPrivate");
                }

                //creation_date
                if(result.has("creation_date")){
                    if(!result.getString("creation_date").equals("") && !result.getString("creation_date").equals("null")){
                        this.creation_date=result.getString("creation_date");
                    }
                }

                //num_tags
                if(result.has("num_tags")){
                    this.num_tags=result.getInt("num_tags");
                }

                //frequency
                if(result.has("frequency")){
                    if(!result.getString("frequency").equals("") && !result.getString("frequency").equals("null")){
                        this.frequency=result.getString("frequency").toUpperCase();
                    }
                }
                else if(!isApi3 && extrasApi2.has("frequency")){
                    if(!extrasApi2.getString("frequency").equals("") && !extrasApi2.getString("frequency").equals("null")){
                        this.frequency=extrasApi2.getString("frequency").toUpperCase();
                    }
                }

                //publisher_name
                if(result.has("publisher_name")){
                    if(!result.getString("publisher_name").equals("") && !result.getString("publisher_name").equals("null")){
                        this.publisher_name=result.getString("publisher_name");
                    }
                }

                //metadata_created
                if(result.has("metadata_created")){
                    if(!result.getString("metadata_created").equals("") && !result.getString("metadata_created").equals("null")){
                        this.metadata_created=result.getString("metadata_created");
                    }
                }

                //temporal_end
                if(result.has("temporal_end")){
                    if(!result.getString("temporal_end").equals("") && !result.getString("temporal_end").equals("null")){
                        this.temporal_end=result.getString("temporal_end");
                    }
                }
                else if(!isApi3 && extrasApi2.has("temporal_end")){
                    if(!extrasApi2.getString("temporal_end").equals("") && !extrasApi2.getString("temporal_end").equals("null")){
                        this.temporal_end=extrasApi2.getString("temporal_end");
                    }
                }                

                //metadata_modified
                if(result.has("metadata_modified")){
                    if(!result.getString("metadata_modified").equals("") && !result.getString("metadata_modified").equals("null")){
                        this.metadata_modified=result.getString("metadata_modified");
                    }
                }

                //author
                if(result.has("author")){
                    if(!result.getString("author").equals("") && !result.getString("author").equals("null")){
                        this.author=result.getString("author");
                    }
                }

                //author_email
                if(result.has("author_email")){
                    if(!result.getString("author_email").equals("") && !result.getString("author_email").equals("null")){
                        this.author_email=result.getString("author_email");
                    }
                }

                //theme
                if(result.has("theme")){
                    if(!result.getString("theme").equals("") && !result.getString("theme").equals("null")){
                        this.theme=result.getString("theme");
                    }
                }

                //site_url
                if(result.has("site_url")){
                    if(!result.getString("site_url").equals("") && !result.getString("site_url").equals("null")){
                        this.site_url=result.getString("site_url");
                    }
                }

                //state
                if(result.has("state")){
                    if(!result.getString("state").equals("") && !result.getString("state").equals("null")){
                        this.state=result.getString("state");
                    }
                }

                //version
                if(result.has("version")){
                    if(!result.getString("version").equals("") && !result.getString("version").equals("null")){
                        this.version=result.getString("version");
                    }
                }

                //license_id
                if(result.has("license_id")){
                    if(!result.getString("license_id").equals("") && !result.getString("license_id").equals("null")){
                        this.license_id=result.getString("license_id");
                    }
                }

                //holder_name
                if(result.has("holder_name")){
                    if(!result.getString("holder_name").equals("") && !result.getString("holder_name").equals("null")){
                        this.holder_name=result.getString("holder_name");
                    }
                }
                else if(!isApi3 && extrasApi2.has("holder_name")){
                    if(!extrasApi2.getString("holder_name").equals("") && !extrasApi2.getString("holder_name").equals("null")){
                        this.holder_name=extrasApi2.getString("holder_name");
                    }
                }

                //holder_identifier
                if(result.has("holder_identifier")){
                    if(!result.getString("holder_identifier").equals("") && !result.getString("holder_identifier").equals("null")){
                        this.holder_identifier=result.getString("holder_identifier");
                    }
                }
                else if(!isApi3 && extrasApi2.has("holder_identifier")){
                    this.holder_identifier = extrasApi2.getString("holder_identifier");
                }

                //fields_description
                if(result.has("fields_description")){
                    if(!result.getString("fields_description").equals("") && !result.getString("fields_description").equals("null")){
                        this.fields_description=result.getString("fields_description");
                    }
                }

                //creator_user_id
                if(result.has("creator_user_id")){
                    if(!result.getString("creator_user_id").equals("") && !result.getString("creator_user_id").equals("null")){
                        this.creator_user_id=result.getString("creator_user_id");
                    }
                }

                //maintainer_email
                if(result.has("maintainer_email")){
                    if(!result.getString("maintainer_email").equals("") && !result.getString("maintainer_email").equals("null")){
                        this.maintainer_email=result.getString("maintainer_email");
                    }
                }

                //num_resources
                if(result.has("num_resources")){
                    this.num_resources=result.getInt("num_resources");  
                }

                //name
                if(result.has("name")){
                    if(!result.getString("name").equals("") && !result.getString("name").equals("null")){
                        this.name=result.getString("name");
                    }
                }

                //isopen
                if(result.has("isopen")){
                    if(!result.getString("isopen").equals("") && !result.getString("isopen").equals("null")){
                        this.isopen=result.getString("isopen");
                    }                
                }

                //url
                if(result.has("url")){
                    if(!result.getString("url").equals("") && !result.getString("url").equals("null")){
                        this.url=result.getString("url");
                    }
                }

                //owner_org
                if(result.has("owner_org")){
                    if(result.getString("owner_org")!=null && !result.getString("owner_org").equals("") && !result.getString("owner_org").equals("null")){
                        this.owner_org=result.getString("owner_org");
                    }
                }

                //modified
                if(result.has("modified")){
                    if(!result.getString("modified").equals("") && !result.getString("modified").equals("null")){
                        this.modified=result.getString("modified");
                    }
                }
                else if(!isApi3 && extrasApi2.has("holder_name")){
                    if(!extrasApi2.getString("modified").equals("") && !extrasApi2.getString("modified").equals("null")){
                        this.modified=extrasApi2.getString("modified");
                    }
                }
                if(this.modified!=null){
                    SimpleDateFormat dateFormat;
                    Date parsedDate;

                    //catch date format yyyy-MM-dd
                    try{
                        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        parsedDate = dateFormat.parse(modified);
                        this.modified_date = new Timestamp(parsedDate.getTime());
                    }
                    //catch date format dd/MM/yyyy
                    catch(Exception e){
                        try{
                            dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            parsedDate = dateFormat.parse(modified);
                            this.modified_date = new Timestamp(parsedDate.getTime());
                        }
                        //catch date format yyyy
                        catch(Exception ex){
                            try{
                                dateFormat = new SimpleDateFormat("yyyy");
                                parsedDate = dateFormat.parse(modified);
                                this.modified_date = new Timestamp(parsedDate.getTime());
                            }
                            catch(Exception exe){
                                System.err.println(this.name + " " + ex.toString());
                            }
                        }
                    }                        
                }

                //publisher_identifier
                if(result.has("publisher_identifier")){
                    if(!result.getString("publisher_identifier").equals("") && !result.getString("publisher_identifier").equals("null")){
                        this.publisher_identifier=result.getString("publisher_identifier");
                    }
                }

                //geographical_name
                if(result.has("geographical_name")){
                    if(!result.getString("geographical_name").equals("") && !result.getString("geographical_name").equals("null")){
                        this.geographical_name=result.getString("geographical_name");
                    }
                }
                else if(!isApi3 && extrasApi2.has("geographical_name")){
                    if(!extrasApi2.getString("geographical_name").equals("") && !extrasApi2.getString("geographical_name").equals("null")){
                        this.geographical_name=extrasApi2.getString("geographical_name");
                    }
                }

                //license_url
                if(result.has("license_url")){
                    if(!result.getString("license_url").equals("") && !result.getString("license_url").equals("null")){
                        this.license_url=result.getString("license_url");
                    }
                }

                //title
                if(result.has("title")){
                    if(!result.getString("title").equals("") && !result.getString("title").equals("null")){
                        this.title=result.getString("title");
                    }
                }

                //revision_id
                if(result.has("revision_id")){
                    if(!result.getString("revision_id").equals("") && !result.getString("revision_id").equals("null")){
                        this.revision_id=result.getString("revision_id");
                    }
                }

                //identifier
                if(result.has("identifier")){
                    if(!result.getString("identifier").equals("") && !result.getString("identifier").equals("null")){
                        this.identifier=result.getString("identifier");
                    }
                }

                //creator_name
                if(result.has("creator_name")){
                    if(!result.getString("creator_name").equals("") && !result.getString("creator_name").equals("null")){
                        this.creator_name=result.getString("creator_name");
                    }
                }

                //creator_identifier
                if(result.has("creator_identifier")){
                    if(!result.getString("creator_identifier").equals("") && !result.getString("creator_identifier").equals("null")){
                        this.creator_identifier=result.getString("creator_identifier");
                    }
                }

                //conforms_to
                if(result.has("conforms_to")){
                    if(!result.getString("conforms_to").equals("") && !result.getString("conforms_to").equals("null")){
                        this.conforms_to=result.getString("conforms_to");
                    }
                }

                //language
                if(result.has("language")){
                    if(!result.getString("language").equals("") && !result.getString("language").equals("null")){
                        this.language=result.getString("language");
                    }
                }

                //alternate_identifier
                if(result.has("alternate_identifier")){
                   if(!result.getString("alternate_identifier").equals("") && !result.getString("alternate_identifier").equals("null")){
                        this.alternate_identifier=result.getString("alternate_identifier");
                    }
                }

                //is_version_of
                if(result.has("is_version_of")){
                    if(!result.getString("is_version_of").equals("") && !result.getString("is_version_of").equals("null")){
                        this.is_version_of=result.getString("is_version_of");
                    }
                }

                //contact
                if(result.has("contact")){
                    if(!result.getString("contact").equals("") && !result.getString("contact").equals("null")){
                        this.contact=result.getString("contact");
                    }
                }

                //notes
                if(result.has("notes")){
                    if(!result.getString("notes").equals("") && !result.getString("notes").equals("null")){
                        this.notes=result.getString("notes");
                    }
                }
                
                //geographical_geonames_url
                if(result.has("geographical_geonames_url")){
                    if(!result.getString("geographical_geonames_url").equals("") && !result.getString("geographical_geonames_url").equals("null")){
                        this.geographical_geonames_url=result.getString("geographical_geonames_url");
                    }
                }
                else if(!isApi3 && extrasApi2.has("geographical_geonames_url")){
                    if(!extrasApi2.getString("geographical_geonames_url").equals("") && !extrasApi2.getString("geographical_geonames_url").equals("null")){
                        this.geographical_geonames_url=extrasApi2.getString("geographical_geonames_url");
                    }
                }
            }
            removeNullKeys();
        }        
        
        catch(NullPointerException e){
            System.err.println(e.toString());
        }
    }
    
    public Dataset(){
        initNullKeys();
    }
    
    private void initNullKeys(){
        nullKeys.put("id", "M");
        nullKeys.put("title", "M");
        nullKeys.put("notes", "M");
        nullKeys.put("modified", "M");
        nullKeys.put("theme", "M");
        nullKeys.put("holder_name", "M");
        nullKeys.put("holder_identifier", "M");
        nullKeys.put("frequency", "M");
        nullKeys.put("license_id", "M");
        nullKeys.put("license_title", "M");
        nullKeys.put("license_url", "M");
        nullKeys.put("resources", "M");
        nullKeys.put("publisher_name", "R");
        nullKeys.put("publisher_identifier", "R");
        nullKeys.put("author_email", "R");
        nullKeys.put("maintainer_email", "R");
        nullKeys.put("contact", "R");
        nullKeys.put("issued", "O");
        nullKeys.put("temporal_start", "O");
        nullKeys.put("temporal_end", "O");
        nullKeys.put("conforms_to", "O");
        nullKeys.put("alternate_identifier", "O");
        nullKeys.put("is_version_of", "O");
        nullKeys.put("tags", "O");
        nullKeys.put("geographical_name", "O");
        nullKeys.put("geographical_geonames_url", "O");
        nullKeys.put("creator_name", "O");
        nullKeys.put("creator_identifier", "O");
        nullKeys.put("version", "O");
        nullKeys.put("language", "O");
        
    }
    
    public void removeNullKeys(){
        if(id!=null){
            nullKeys.remove("id");
        }
        if(frequency!=null){
            nullKeys.remove("frequency");
        }
        if(title!=null){
            nullKeys.remove("title");
        }
        if(notes!=null){
            nullKeys.remove("notes");
        }
        if(modified!=null){
            nullKeys.remove("modified");
        }
        if(theme!=null){
            nullKeys.remove("theme");
        }
        if(holder_name!=null){
            nullKeys.remove("holder_name");
        }
        if(holder_identifier!=null){
            nullKeys.remove("holder_identifier");
        }
        if(license_id!=null){
            nullKeys.remove("license_id");
        }
        if(license_title!=null){
            nullKeys.remove("license_title");
        }
        if(license_url!=null){
            nullKeys.remove("license_url");
        }
        if(num_resources!=0){
            nullKeys.remove("resources");
        }
        if(publisher_name!=null){
            nullKeys.remove("publisher_name");
        }
        if(publisher_identifier!=null){
            nullKeys.remove("publisher_identifier");
        }
        if(maintainer_email!=null){
            nullKeys.remove("maintainer_email");
        }
        if(author_email!=null){
            nullKeys.remove("author_email");
        }        
        if(contact!=null){
            nullKeys.remove("contact");
        }
        if(issued!=null){
            nullKeys.remove("issued");
        }
        if(temporal_start!=null){
            nullKeys.remove("temporal_start");
        }
        if(temporal_end!=null){
            nullKeys.remove("temporal_end");
        }
        if(conforms_to!=null){
            nullKeys.remove("conforms_to");
        }
        if(alternate_identifier!=null){
            nullKeys.remove("alternate_identifier");
        }
        if(is_version_of!=null){
            nullKeys.remove("is_version_of");
        }
        if(tags!=null){
            nullKeys.remove("tags");
        }
        if(geographical_name!=null){
            nullKeys.remove("geographical_name");
        } 
        if(geographical_geonames_url!=null){
            nullKeys.remove("geographical_geonames_url");
        }
        if(creator_name!=null){
            nullKeys.remove("creator_name");
        }
        if(creator_identifier!=null){
            nullKeys.remove("creator_identifier");
        } 
        if(version!=null){
            nullKeys.remove("version");
        } 
        if(language!=null){
            nullKeys.remove("language");
        }
        
        if(nullKeys.containsKey("holder_name") && !nullKeys.containsKey("holder_identifier")){
            nullKeys.remove("holder_name");
            nullKeys.put("holder_name", "O");
        }
        else if(!nullKeys.containsKey("holder_name") && nullKeys.containsKey("holder_identifier")){
            nullKeys.remove("holder_identifier");
            nullKeys.put("holder_identifier", "O");
        }
        else if(nullKeys.containsKey("holder_name") && nullKeys.containsKey("holder_identifier")){
            nullKeys.remove("holder_identifier");
            nullKeys.put("holder_identifier", "O");
        }
        
        if(nullKeys.containsKey("author_email") && !nullKeys.containsKey("contact")){
            nullKeys.remove("author_email");
            nullKeys.put("author_email", "O");
        }
        else if(!nullKeys.containsKey("author_email") && nullKeys.containsKey("contact")){
            nullKeys.remove("contact");
            nullKeys.put("contact", "O");
        }
        else if(nullKeys.containsKey("author_email") && nullKeys.containsKey("contact")){
            nullKeys.remove("contact");
            nullKeys.put("contact", "O");
        }
        
        if(!nullKeys.containsKey("license_id")){
            if(nullKeys.containsKey("license_title")){
                nullKeys.remove("license_title");
                nullKeys.put("license_title", "O");
            }
            if(nullKeys.containsKey("license_url")){
                nullKeys.remove("license_url");
                nullKeys.put("license_url", "O");
            }
        }
        else if(!nullKeys.containsKey("license_title")){
            if(nullKeys.containsKey("license_id")){
                nullKeys.remove("license_id");
                nullKeys.put("license_id", "O");
            }
            if(nullKeys.containsKey("license_url")){
                nullKeys.remove("license_url");
                nullKeys.put("license_url", "O");
            }
        }
        else if(!nullKeys.containsKey("license_url")){
            if(nullKeys.containsKey("license_id")){
                nullKeys.remove("license_id");
                nullKeys.put("license_id", "O");
            }
            if(nullKeys.containsKey("license_title")){
                nullKeys.remove("license_title");
                nullKeys.put("license_title", "O");
            }
        }
        else{
            nullKeys.remove("license_title");
            nullKeys.put("license_title", "O");
            nullKeys.remove("license_url");
            nullKeys.put("license_url", "O");
        }
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
     * @return the license_title
     */
    public String getLicense_title() {
        return license_title;
    }

    /**
     * @param license_title the license_title to set
     */
    public void setLicense_title(String license_title) {
        this.license_title = license_title;
    }

    /**
     * @return the maintainer
     */
    public String getMaintainer() {
        return maintainer;
    }

    /**
     * @param maintainer the maintainer to set
     */
    public void setMaintainer(String maintainer) {
        this.maintainer = maintainer;
    }

    /**
     * @return the encoding
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * @param encoding the encoding to set
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * @return the issued
     */
    public String getIssued() {
        return issued;
    }

    /**
     * @param issued the issued to set
     */
    public void setIssued(String issued) {
        this.issued = issued;
    }

    /**
     * @return the temporal_start
     */
    public String getTemporal_start() {
        return temporal_start;
    }

    /**
     * @param temporal_start the temporal_start to set
     */
    public void setTemporal_start(String temporal_start) {
        this.temporal_start = temporal_start;
    }

    /**
     * @return the isPrivate
     */
    public String isPrivate() {
        return isPrivate;
    }

    /**
     * @param isPrivate the isPrivate to set
     */
    public void setIsPrivate(String isPrivate) {
        this.isPrivate = isPrivate;
    }

    /**
     * @return the creation_date
     */
    public String getCreation_date() {
        return creation_date;
    }

    /**
     * @param creation_date the creation_date to set
     */
    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    /**
     * @return the num_tags
     */
    public int getNum_tags() {
        return num_tags;
    }

    /**
     * @param num_tags the num_tags to set
     */
    public void setNum_tags(int num_tags) {
        this.num_tags = num_tags;
    }

    /**
     * @return the frequency
     */
    public String getFrequency() {
        return frequency;
    }

    /**
     * @param frequency the frequency to set
     */
    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    /**
     * @return the publisher_name
     */
    public String getPublisher_name() {
        return publisher_name;
    }

    /**
     * @param publisher_name the publisher_name to set
     */
    public void setPublisher_name(String publisher_name) {
        this.publisher_name = publisher_name;
    }

    /**
     * @return the metadata_created
     */
    public String getMetadata_created() {
        return metadata_created;
    }

    /**
     * @param metadata_created the metadata_created to set
     */
    public void setMetadata_created(String metadata_created) {
        this.metadata_created = metadata_created;
    }

    /**
     * @return the temporal_end
     */
    public String getTemporal_end() {
        return temporal_end;
    }

    /**
     * @param temporal_end the temporal_end to set
     */
    public void setTemporal_end(String temporal_end) {
        this.temporal_end = temporal_end;
    }

    /**
     * @return the metadata_modified
     */
    public String getMetadata_modified() {
        return metadata_modified;
    }

    /**
     * @param metadata_modified the metadata_modified to set
     */
    public void setMetadata_modified(String metadata_modified) {
        this.metadata_modified = metadata_modified;
    }

    /**
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return the author_email
     */
    public String getAuthor_email() {
        return author_email;
    }

    /**
     * @param author_email the author_email to set
     */
    public void setAuthor_email(String author_email) {
        this.author_email = author_email;
    }

    /**
     * @return the theme
     */
    public String getTheme() {
        return theme;
    }

    /**
     * @param theme the theme to set
     */
    public void setTheme(String theme) {
        this.theme = theme;
    }

    /**
     * @return the site_url
     */
    public String getSite_url() {
        return site_url;
    }

    /**
     * @param site_url the site_url to set
     */
    public void setSite_url(String site_url) {
        this.site_url = site_url;
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
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return the license_id
     */
    public String getLicense_id() {
        return license_id;
    }

    /**
     * @param license_id the license_id to set
     */
    public void setLicense_id(String license_id) {
        this.license_id = license_id;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the holder_name
     */
    public String getHolder_name() {
        return holder_name;
    }

    /**
     * @param holder_name the holder_name to set
     */
    public void setHolder_name(String holder_name) {
        this.holder_name = holder_name;
    }

    /**
     * @return the holder_identifier
     */
    public String getHolder_identifier() {
        return holder_identifier;
    }

    /**
     * @param holder_identifier the holder_identifier to set
     */
    public void setHolder_identifier(String holder_identifier) {
        this.holder_identifier = holder_identifier;
    }

    /**
     * @return the fields_description
     */
    public String getFields_description() {
        return fields_description;
    }

    /**
     * @param fields_description the fields_description to set
     */
    public void setFields_description(String fields_description) {
        this.fields_description = fields_description;
    }

    /**
     * @return the creator_user_id
     */
    public String getCreator_user_id() {
        return creator_user_id;
    }

    /**
     * @param creator_user_id the creator_user_id to set
     */
    public void setCreator_user_id(String creator_user_id) {
        this.creator_user_id = creator_user_id;
    }

    /**
     * @return the maintainer_email
     */
    public String getMaintainer_email() {
        return maintainer_email;
    }

    /**
     * @param maintainer_email the maintainer_email to set
     */
    public void setMaintainer_email(String maintainer_email) {
        this.maintainer_email = maintainer_email;
    }

    /**
     * @return the num_resources
     */
    public int getNum_resources() {
        return num_resources;
    }

    /**
     * @param num_resources the num_resources to set
     */
    public void setNum_resources(int num_resources) {
        this.num_resources = num_resources;
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
     * @return the isopen
     */
    public String isopen() {
        return isopen;
    }

    /**
     * @param isopen the isopen to set
     */
    public void setIsopen(String isopen) {
        this.isopen = isopen;
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
     * @return the notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * @param notes the notes to set
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * @return the owner_org
     */
    public String getOwner_org() {
        return owner_org;
    }

    /**
     * @param owner_org the owner_org to set
     */
    public void setOwner_org(String owner_org) {
        this.owner_org = owner_org;
    }

    /**
     * @return the modified
     */
    public String getModified() {
        return modified;
    }

    /**
     * @param modified the modified to set
     */
    public void setModified(String modified) {
        this.modified = modified;
    }

    /**
     * @return the publisher_identifier
     */
    public String getPublisher_identifier() {
        return publisher_identifier;
    }

    /**
     * @param publisher_identifier the publisher_identifier to set
     */
    public void setPublisher_identifier(String publisher_identifier) {
        this.publisher_identifier = publisher_identifier;
    }

    /**
     * @return the geographical_name
     */
    public String getGeographical_name() {
        return geographical_name;
    }

    /**
     * @param geographical_name the geographical_name to set
     */
    public void setGeographical_name(String geographical_name) {
        this.geographical_name = geographical_name;
    }

    /**
     * @return the license_url
     */
    public String getLicense_url() {
        return license_url;
    }

    /**
     * @param license_url the license_url to set
     */
    public void setLicense_url(String license_url) {
        this.license_url = license_url;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
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
     * @return the identifier
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * @param identifier the identifier to set
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * @return the creator_name
     */
    public String getCreator_name() {
        return creator_name;
    }

    /**
     * @param creator_name the creator_name to set
     */
    public void setCreator_name(String creator_name) {
        this.creator_name = creator_name;
    }

    /**
     * @return the creator_identifier
     */
    public String getCreator_identifier() {
        return creator_identifier;
    }

    /**
     * @param creator_identifier the creator_identifier to set
     */
    public void setCreator_identifier(String creator_identifier) {
        this.creator_identifier = creator_identifier;
    }

    /**
     * @return the conforms_to
     */
    public String getConforms_to() {
        return conforms_to;
    }

    /**
     * @param conforms_to the conforms_to to set
     */
    public void setConforms_to(String conforms_to) {
        this.conforms_to = conforms_to;
    }

    /**
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @param language the language to set
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * @return the alternate_identifier
     */
    public String getAlternate_identifier() {
        return alternate_identifier;
    }

    /**
     * @param alternate_identifier the alternate_identifier to set
     */
    public void setAlternate_identifier(String alternate_identifier) {
        this.alternate_identifier = alternate_identifier;
    }

    /**
     * @return the is_version_of
     */
    public String getIs_version_of() {
        return is_version_of;
    }

    /**
     * @param is_version_of the is_version_of to set
     */
    public void setIs_version_of(String is_version_of) {
        this.is_version_of = is_version_of;
    }

    /**
     * @return the contact
     */
    public String getContact() {
        return contact;
    }

    /**
     * @param contact the contact to set
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * @return the tags
     */
    public List<Tag> getTags() {
        return tags;
    }

    /**
     * @param tags the tags to set
     */
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    /**
     * @return the resources
     */
    public List<Resource> getResources() {
        return resources;
    }

    /**
     * @param resources the resources to set
     */
    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    /**
     * @return the organization
     */
    public Organization getOrganization() {
        return organization;
    }

    /**
     * @param organization the organization to set
     */
    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    /**
     * @return the modified_date
     */
    public Timestamp getModified_date() {
        return modified_date;
    }

    /**
     * @param modified_date the modified_date to set
     */
    public void setModified_date(Timestamp modified_date) {
        this.modified_date = modified_date;
    }

    /**
     * @return the geographical_geonames_url
     */
    public String getGeographical_geonames_url() {
        return geographical_geonames_url;
    }

    /**
     * @param geographical_geonames_url the geographical_geonames_url to set
     */
    public void setGeographical_geonames_url(String geographical_geonames_url) {
        this.geographical_geonames_url = geographical_geonames_url;
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
     * @return the apiUrl
     */
    public String getApiUrl() {
        return apiUrl;
    }

    /**
     * @param apiUrl the apiUrl to set
     */
    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
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
