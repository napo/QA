/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbUtils;

import java.io.File;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.sqlite.SQLiteConfig;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import nickan.Dataset;
import nickan.Organization;
import nickan.Resource;
import utils.ResourceControls;

/**
 *
 * @author nicola
 */
public class DBManager {
    
    String classpath;
    
    public DBManager() throws URISyntaxException{
        classpath = new File(DBManager.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getParent();
    }
        
    
    private Connection connect() {
        
        SQLiteConfig config = new SQLiteConfig();
        config.enableLoadExtension(true);
        
        //url di connessione al database
        String url = "jdbc:sqlite:" + classpath + "/db.sqlite";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, config.toProperties());
            Statement stmt = conn.createStatement();
            stmt.execute("SELECT load_extension(\"mod_spatialite\")");
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    
    
    public void insertDataset(Dataset d) {
        String sql = "INSERT INTO dataset VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 
        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, d.getId());//id
                pstmt.setString(2, d.getLicense_title());//license_title
                pstmt.setString(3, d.getMaintainer());//maintainer
                pstmt.setString(4, d.getEncoding());//encoding
                pstmt.setString(5, d.getIssued());//issued
                pstmt.setString(6, d.getTemporal_start());//temporal_start
                pstmt.setString(7, d.isPrivate());//private
                pstmt.setString(8, d.getCreation_date());//creation_date
                pstmt.setInt(9, d.getNum_tags());//num_tags
                pstmt.setString(10, d.getFrequency());//frequency
                pstmt.setString(11, d.getPublisher_name());//publisher_name
                pstmt.setString(12, d.getMetadata_created());//metadata_created
                pstmt.setString(13, d.getTemporal_end());//temporal_end
                pstmt.setString(14, d.getMetadata_modified());//metadata_modified
                pstmt.setString(15, d.getAuthor());//author
                pstmt.setString(16, d.getAuthor_email());//author_email
                pstmt.setString(17, d.getTheme());//theme
                pstmt.setString(18, d.getSite_url());//site_url
                pstmt.setString(19, d.getState());//state
                pstmt.setString(20, d.getVersion());//version
                pstmt.setString(21, d.getLicense_id());//license_id
                pstmt.setString(22, d.getType());//type
                pstmt.setString(23, d.getHolder_name());//holder_name
                pstmt.setString(24, d.getHolder_identifier());//holder_identifier
                pstmt.setString(25, d.getFields_description());//fields_description
                pstmt.setString(26, d.getCreator_user_id());//creator_user_id
                pstmt.setString(27, d.getMaintainer_email());//maintainer_email
                pstmt.setInt(28, d.getNum_resources());//num_resources
                pstmt.setString(29, d.getName());//name
                pstmt.setString(30, d.isopen());//isopen
                pstmt.setString(31, d.getUrl());//url
                pstmt.setString(32, d.getNotes());//notes
                pstmt.setString(33, d.getOwner_org());//owner_org
                pstmt.setString(34, d.getModified());//modified
                pstmt.setString(35, d.getPublisher_identifier());//publisher_identifier
                pstmt.setString(36, d.getGeographical_name());//geographical_name
                pstmt.setString(37, d.getLicense_url());//license_url
                pstmt.setString(38, d.getTitle());//title
                pstmt.setString(39, d.getRevision_id());//revision_id
                pstmt.setString(40, d.getIdentifier());//identifier
                pstmt.setString(41, d.getCreator_name());//creator_name
                pstmt.setString(42, d.getCreator_identifier());//creator_identifier
                pstmt.setString(43, d.getConforms_to());//conforms_to
                pstmt.setString(44, d.getLanguage());//language                
                pstmt.setString(45, d.getAlternate_identifier());//alternate_identifier
                pstmt.setString(46, d.getIs_version_of());//is_version_of
                pstmt.setString(47, d.getContact());//contact
                pstmt.setString(48, d.getGeographical_geonames_url());//geographical_geonames_url
                pstmt.setString(49, d.getPortalUrl());//portalUrl
                pstmt.executeUpdate();
            }
        catch (SQLException e) {
            System.out.println("DATASET " + e.getMessage());
        }
    }
    
    public void insertResource(Resource r){
        String sql = "INSERT INTO resource VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 
        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, r.getId());//id
                pstmt.setString(2, r.getCache_last_updated());//cache_last_updated
                pstmt.setString(3, r.getPackage_id());//package_id
                pstmt.setString(4, r.getWebstore_last_updated());//webstore_last_updated
                pstmt.setString(5, r.getDatastore_active());//datastore_active
                pstmt.setString(6, r.getSize());//size
                pstmt.setString(7, r.getState());//state
                pstmt.setString(8, r.getHash());//hash
                pstmt.setString(9, r.getDescription());//description
                pstmt.setString(10, r.getFormat());//format
                pstmt.setString(11, r.getLast_modified());//last_modified
                pstmt.setString(12, r.getUrl_type());//url_type
                pstmt.setString(13, r.getMimetype());//mimetype
                pstmt.setString(14, r.getCache_url());//cache_url
                pstmt.setString(15, r.getName());//name
                pstmt.setString(16, r.getCreated());//created
                pstmt.setString(17, r.getUrl());//url
                pstmt.setString(18, r.getWebstore_url());//webstore_url
                pstmt.setString(19, r.getMimetype_inner());//mimetype_inner                
                pstmt.setInt(20, r.getPosition());//position
                pstmt.setString(21, r.getRevision_id());//revision_id
                pstmt.setString(22, r.getResource_type());//resource_type
                pstmt.setString(23, r.getDistribution_format());//distribution_format
                pstmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("RESOURCE " + e.getMessage());
        }
    }
    
    public void insertRes_in_dataset(String datasetId, String resouceId) {
        String sql = "INSERT INTO res_in_dataset VALUES(?,?)";
        
        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, datasetId);//dataset_id
                pstmt.setString(2, resouceId);//resource_id
                pstmt.executeUpdate();
            }
        catch (SQLException e) {
            System.out.println("res_in_dataset " + e.getMessage());
        }
    }
    
    public Dataset getDatasetFromId(String id){
        Dataset d = null;
        String sql = "SELECT * FROM dataset WHERE id=?";
        
        try (Connection conn = this.connect();
            PreparedStatement pstmt  = conn.prepareStatement(sql)){
                pstmt.setString(1,id);
                ResultSet rs  = pstmt.executeQuery();
                
                while (rs.next()) {
                    d = new Dataset();
                    d.setId(rs.getString("id"));
                    d.setLicense_title(rs.getString("license_title"));                    
                    d.setMaintainer(rs.getString("maintainer"));
                    d.setEncoding(rs.getString("encoding"));
                    d.setIssued(rs.getString("issued"));
                    d.setTemporal_start(rs.getString("temporal_start"));
                    d.setIsPrivate(rs.getString("private"));
                    d.setCreation_date(rs.getString("creation_date"));
                    d.setNum_tags(rs.getInt("num_tags"));
                    d.setFrequency(rs.getString("frequency"));
                    d.setPublisher_name(rs.getString("publisher_name"));
                    d.setMetadata_created(rs.getString("metadata_created"));
                    d.setTemporal_end(rs.getString("temporal_end"));
                    d.setMetadata_modified(rs.getString("metadata_modified"));
                    d.setAuthor(rs.getString("author"));
                    d.setAuthor_email(rs.getString("author_email"));
                    d.setTheme(rs.getString("theme"));
                    d.setSite_url(rs.getString("site_url"));
                    d.setState(rs.getString("state"));
                    d.setVersion(rs.getString("version"));
                    d.setType(rs.getString("type"));
                    d.setHolder_name(rs.getString("holder_name"));
                    d.setHolder_identifier(rs.getString("holder_identifier"));
                    d.setFields_description(rs.getString("fields_description"));
                    d.setCreator_user_id(rs.getString("creator_user_id"));                    
                    d.setMaintainer_email(rs.getString("maintainer_email"));
                    d.setNum_resources(rs.getInt("num_resources"));
                    d.setName(rs.getString("name"));
                    d.setIsopen(rs.getString("isopen"));
                    d.setUrl(rs.getString("url"));
                    d.setNotes(rs.getString("notes"));
                    d.setOwner_org(rs.getString("owner_org"));
                    d.setModified(rs.getString("modified"));
                    d.setPublisher_identifier(rs.getString("publisher_identifier"));
                    d.setGeographical_name(rs.getString("geographical_name"));
                    d.setLicense_url(rs.getString("license_url"));
                    d.setTitle(rs.getString("title"));
                    d.setRevision_id(rs.getString("revision_id"));
                    d.setIdentifier(rs.getString("identifier"));
                    d.setCreator_name(rs.getString("creator_name"));
                    d.setCreator_identifier(rs.getString("creator_identifier"));
                    d.setConforms_to(rs.getString("conforms_to"));
                    d.setLanguage(rs.getString("language"));
                    d.setAlternate_identifier(rs.getString("alternate_identifier"));
                    d.setIs_version_of(rs.getString("is_version_of"));
                    d.setContact(rs.getString("contact"));
                    d.setGeographical_geonames_url(rs.getString("geographical_geonames_url"));
                    d.setPortalUrl(rs.getString("portalUrl"));
                    
                    d.removeNullKeys();
                }
                
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        
        d.setOrganization(getOrganizationFromId(getOrganizationIdFromDatasetId(d.getId())));
        
        List<String> l = getResourcesIdFromDatasetId(d.getId());
        List <Resource> resources = new LinkedList<>();
        for(String s : l){
            resources.add(getResourceFromId(s));
        }
        d.setResources(resources);
        
        return d;
    }
    
    public Dataset getDatasetFromName(String name){
        Dataset d;
        String sql = "SELECT * FROM dataset WHERE name=?";
        
        try (Connection conn = this.connect();
            PreparedStatement pstmt  = conn.prepareStatement(sql)){
                pstmt.setString(1,name);
                ResultSet rs  = pstmt.executeQuery();
                
                if(rs.next()) {
                    d = new Dataset();
                    d.setId(rs.getString("id"));
                    d.setLicense_title(rs.getString("license_title"));                    
                    d.setMaintainer(rs.getString("maintainer"));
                    d.setEncoding(rs.getString("encoding"));
                    d.setIssued(rs.getString("issued"));
                    d.setTemporal_start(rs.getString("temporal_start"));
                    d.setIsPrivate(rs.getString("private"));
                    d.setCreation_date(rs.getString("creation_date"));
                    d.setNum_tags(rs.getInt("num_tags"));
                    d.setFrequency(rs.getString("frequency"));
                    d.setPublisher_name(rs.getString("publisher_name"));
                    d.setMetadata_created(rs.getString("metadata_created"));
                    d.setTemporal_end(rs.getString("temporal_end"));
                    d.setMetadata_modified(rs.getString("metadata_modified"));
                    d.setAuthor(rs.getString("author"));
                    d.setAuthor_email(rs.getString("author_email"));
                    d.setTheme(rs.getString("theme"));
                    d.setSite_url(rs.getString("site_url"));
                    d.setState(rs.getString("state"));
                    d.setVersion(rs.getString("version"));
                    d.setType(rs.getString("type"));
                    d.setHolder_name(rs.getString("holder_name"));
                    d.setHolder_identifier(rs.getString("holder_identifier"));
                    d.setFields_description(rs.getString("fields_description"));
                    d.setCreator_user_id(rs.getString("creator_user_id"));                    
                    d.setMaintainer_email(rs.getString("maintainer_email"));
                    d.setNum_resources(rs.getInt("num_resources"));
                    d.setName(rs.getString("name"));
                    d.setIsopen(rs.getString("isopen"));
                    d.setUrl(rs.getString("url"));
                    d.setNotes(rs.getString("notes"));
                    d.setOwner_org(rs.getString("owner_org"));
                    d.setModified(rs.getString("modified"));
                    d.setPublisher_identifier(rs.getString("publisher_identifier"));
                    d.setGeographical_name(rs.getString("geographical_name"));
                    d.setLicense_url(rs.getString("license_url"));
                    d.setTitle(rs.getString("title"));
                    d.setRevision_id(rs.getString("revision_id"));
                    d.setIdentifier(rs.getString("identifier"));
                    d.setCreator_name(rs.getString("creator_name"));
                    d.setCreator_identifier(rs.getString("creator_identifier"));
                    d.setConforms_to(rs.getString("conforms_to"));
                    d.setLanguage(rs.getString("language"));
                    d.setAlternate_identifier(rs.getString("alternate_identifier"));
                    d.setIs_version_of(rs.getString("is_version_of"));
                    d.setContact(rs.getString("contact"));
                    d.setGeographical_geonames_url(rs.getString("geographical_geonames_url"));
                    d.setPortalUrl(rs.getString("portalUrl"));
                    
                    d.removeNullKeys();
                }
                else{
                    return null;
                }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        
        d.setOrganization(getOrganizationFromId(getOrganizationIdFromDatasetId(d.getId())));
        
        List<String> l = getResourcesIdFromDatasetId(d.getId());
        List <Resource> resources = new LinkedList<>();
        for(String s : l){
            resources.add(getResourceFromId(s));
        }
        d.setResources(resources);
        
        return d;
    }
    
    public Resource getResourceFromId(String id){
        Resource r = null;
        String sql = "SELECT * FROM resource WHERE id=?";
        
        try (Connection conn = this.connect();
            PreparedStatement pstmt  = conn.prepareStatement(sql)){
                pstmt.setString(1,id);
                ResultSet rs  = pstmt.executeQuery();
                
                if(rs.next()) {
                    r = new Resource();
                    r.setId(rs.getString("id"));//id
                    r.setCache_last_updated(rs.getString("cache_last_updated"));//cache_last_updated
                    r.setPackage_id(rs.getString("package_id"));//package_id
                    r.setWebstore_last_updated(rs.getString("webstore_last_updated"));//webstore_last_updated
                    r.setDatastore_active(rs.getString("datastore_active"));//datastore_active
                    r.setSize(rs.getString("size"));//size
                    r.setState(rs.getString("state"));//state
                    r.setHash(rs.getString("hash"));//hash
                    r.setDescription(rs.getString("description"));//description
                    r.setFormat(rs.getString("format"));//format
                    r.setLast_modified(rs.getString("last_modified"));//last_modified
                    r.setUrl_type(rs.getString("url_type"));//url_type
                    r.setMimetype(rs.getString("mimetype"));//mimetype
                    r.setCache_url(rs.getString("cache_url"));//cache_url
                    r.setName(rs.getString("name"));//name
                    r.setCreated(rs.getString("created"));//created
                    r.setUrl(rs.getString("url"));//url
                    r.setWebstore_url(rs.getString("webstore_url"));//webstore_url
                    r.setMimetype_inner(rs.getString("mimetype_inner"));//mimetype_inner
                    r.setPosition(rs.getInt("position"));//position
                    r.setRevision_id(rs.getString("revision_id"));//revision_id
                    r.setResource_type(rs.getString("resource_type"));//resource_type
                    r.setDistribution_format(rs.getString("distribution_format"));// distribution_format 
                    
                    r.removeNullKeys();
                }
                
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }        
        
        return r;
    }
    
    public List<String> getResourcesIdFromDatasetId(String dataset_id){
        String sql = "SELECT * FROM res_in_dataset WHERE dataset_id=?";
        
        List <String> l = new LinkedList<>();
        
        try (Connection conn = this.connect();
            PreparedStatement pstmt  = conn.prepareStatement(sql)){
                pstmt.setString(1, dataset_id);
                ResultSet rs  = pstmt.executeQuery();
                
                while(rs.next()) {
                    l.add(rs.getString("resource_id"));
                }
            
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        
        return l;
    }
    
    public String getOrganizationIdFromDatasetId(String dataset_id){
        String sql = "SELECT organization_id FROM org_in_dataset WHERE dataset_id=?";
        
        String organization_id = null;
        
        try (Connection conn = this.connect();
            PreparedStatement pstmt  = conn.prepareStatement(sql)){
                pstmt.setString(1, dataset_id);
                ResultSet rs  = pstmt.executeQuery();
                
                if(rs.next()) {
                    organization_id = rs.getString("organization_id");
                }
            
        }
        catch (SQLException e) {
            System.out.println("org_in_dataset " + e.getMessage());
            return null;
        }
        
        return organization_id;
    }
    
    public Organization getOrganizationFromId(String id){
        Organization o = null;
        String sql = "SELECT * FROM organization WHERE id=?";
        
        try (Connection conn = this.connect();
            PreparedStatement pstmt  = conn.prepareStatement(sql)){
                pstmt.setString(1, id);
                ResultSet rs  = pstmt.executeQuery();
                
                while (rs.next()) {
                    o = new Organization();
                    o.setId(rs.getString("id"));
                    o.setDescription(rs.getString("description"));
                    o.setCreated(rs.getString("created"));
                    o.setTitle(rs.getString("title"));
                    o.setName(rs.getString("name"));
                    o.setIs_organization(rs.getString("is_organization"));
                    o.setState(rs.getString("state"));
                    o.setImage_url(rs.getString("image_url"));
                    o.setRevision_id(rs.getString("revision_id"));
                    o.setType(rs.getString("type"));
                    o.setApproval_status(rs.getString("approval_status"));                    
                }                
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        
        return o;        
    }
    
    public void insertDataset_is_updated(String id, String result){
        String sql = "INSERT INTO dataset_is_updated VALUES(?,?)";
        
        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, id);//dataset_id
                pstmt.setString(2, result);//result
                pstmt.executeUpdate();
            }
        catch (SQLException e) {
            System.out.println("dataset_is_updated " + e.getMessage());
        }
    }
    
    public void insertOrganization(Organization o){
        String sql = "INSERT INTO organization VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        
        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, o.getId());//id
                pstmt.setString(2, o.getDescription());//description
                pstmt.setString(3, o.getCreated());//created
                pstmt.setString(4, o.getTitle());//title
                pstmt.setString(5, o.getName());//name
                pstmt.setString(6, o.getIs_organization());//is_organization
                pstmt.setString(7, o.getState());//state
                pstmt.setString(8, o.getImage_url());//image_url
                pstmt.setString(9, o.getRevision_id());//revision_id
                pstmt.setString(10, o.getType());//type
                pstmt.setString(11, o.getApproval_status());//approval_status
                pstmt.executeUpdate();
            }
        catch (SQLException e) {
            //System.out.println("organization " + e.getMessage());
        }
    }
    
    public void insertOrg_in_dataset(String datasetId, String organizationId) {
        String sql = "INSERT INTO org_in_dataset VALUES(?,?)";
        
        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, datasetId);//dataset_id
                pstmt.setString(2, organizationId);//organization_id
                pstmt.executeUpdate();
            }
        catch (SQLException e) {
            System.out.println("org_in_dataset " + e.getMessage());
        }
    }
    
    public void insertResource_responsecode(String resource_id, String response){
        String sql = "INSERT INTO resource_responsecode VALUES(?,?)";
        
        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, resource_id);//resource_id
                pstmt.setString(2, response);//response
                pstmt.executeUpdate();
            }
        catch (SQLException e) {
            System.out.println("resource_responsecode " + e.getMessage());
        }
    }
    
    public void insertEmailVerification(String dataset_id, String author_email_result, String maintainer_email_result, String contact_result){
        String sql = "INSERT INTO email_verification VALUES(?,?,?,?)";
        
        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, dataset_id);//dataset_id
                pstmt.setString(2, author_email_result);//author_email
                pstmt.setString(3, maintainer_email_result);//maintainer_email
                pstmt.setString(4, contact_result);//contact
                pstmt.executeUpdate();
            }
        catch (SQLException e) {
            System.out.println("dataset_is_updated " + e.getMessage());
        }
    }
    
    public void insertCompleteness(Dataset d){
        //Max dataset points M:48
        //Max dataset points R:13
        //Max dataset points O:13
        //Max resource points M:10
        //Max resource points R:3
        //Max resource points O:3
        //score=somma max points
        int score=90;
        String result = "OK";
        
        for(Map.Entry<String, String> entry : d.getNullKeys().entrySet()){
            switch (entry.getValue()) {
                case "M":
                    score-=5;
                    result="M";
                    break;
                case "R":
                    score-=3;
                    if(!result.equals("M"))
                        result="R";
                    break;
                case "O":
                    if(!result.equals("M") && !result.equals("R"))
                        result="O";
                    score-=1;
                    break;
                default:
                    break;
            }
        }
        
        int resScore=0;
        for(Resource r: d.getResources()){
            for(Map.Entry<String, String> entry : r.getNullKeys().entrySet()){
                switch (entry.getValue()) {
                    case "M":
                        resScore+=5;
                        result="M";
                        break;
                    case "R":
                        resScore+=3;
                        if(!result.equals("M"))
                            result="R";
                        break;
                    case "O":
                        if(!result.equals("M") && !result.equals("R"))
                            result="O";
                        resScore+=1;
                        break;
                    default:
                        break;
                }
            }
        }
        
        float tmp=0;
        if(d.getNum_resources()!=0)
            tmp = (float) resScore/d.getNum_resources();
        
        int avg = (int) Math.round(tmp);
        
        score-=avg;
        
        String sql = "INSERT INTO completeness VALUES(?,?,?)";
        
        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, d.getId());//dataset_id
                pstmt.setInt(2, score);//score
                pstmt.setString(3, result);//result
                pstmt.executeUpdate();
            }
        catch (SQLException e) {
            System.out.println("completeness " + e.getMessage());
        }
    }
    
    public void insertResource_controls(ResourceControls rc){
        String sql = "INSERT INTO resource_controls VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        
        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, rc.getResource_id());//resource_id
                pstmt.setInt(2, rc.getResponse_code());//response_code
                pstmt.setString(3, Boolean.toString(rc.isDownloadable()));//is_downloadable
                pstmt.setString(4, Boolean.toString(rc.isFormat_correspondence()));//format_correspondence
                pstmt.setString(5, Boolean.toString(rc.isEmpty()));//is_empty
                pstmt.setString(6, Boolean.toString(rc.isCorrect()));//is_correct
                pstmt.setString(7, rc.getLog());//log
                pstmt.setString(8, Boolean.toString(rc.isCorrect_encoding()));//correct_encoding
                pstmt.setString(9, rc.getDeclared_format());//declared_format
                pstmt.setString(10, rc.getFound_format());//find_format
                pstmt.setString(11, Boolean.toString(rc.isProcessed()));//processed
                pstmt.setString(12, Boolean.toString(rc.isDiretto()));//diretto
                pstmt.setString(13, Boolean.toString(rc.isGeo_processed()));//geo_processed
                pstmt.setString(14, Boolean.toString(rc.isGeo_valid()));//geo_valid
                pstmt.setString(15, rc.getMd5sum());//md5sum
                pstmt.executeUpdate();
            }
        catch (SQLException e) {
            System.out.println("resource_controls " + e.getMessage());
        }
    }
    
    public List<String> getResourcesUrlsFromPackageId(String package_id){
        List<String> l = new LinkedList<>();
        
        String sql = "SELECT url FROM res_in_dataset, resource WHERE dataset_id=? AND resource_id=id";
        
        try (Connection conn = this.connect();
            PreparedStatement pstmt  = conn.prepareStatement(sql)){
                pstmt.setString(1, package_id);
                ResultSet rs  = pstmt.executeQuery();
                
                while (rs.next()) {
                    l.add(rs.getString("url"));
                }                
        }
        catch (SQLException e) {
            System.out.println("getResourcesUrlsFromPackageId " + e.getMessage());
        }
        
        return l;
    }
    
    public List<String> getNames(){
        List<String> l = new LinkedList<>();
        
        //query per ottenere tutti i nomi dei dataset che contengono file XMl
        //String sql = "SELECT DISTINCT dataset.name FROM res_in_dataset, resource, dataset WHERE resource.id = res_in_dataset.resource_id AND resource.format LIKE '%XML%' AND res_in_dataset.dataset_id = dataset.id";
        
        //query per ottenere i primi 100 nomi dei dataset
        //String sql = "SELECT name FROM (SELECT * FROM dataset LIMIT 200 OFFSET 1023) WHERE geographical_geonames_url is not null";
        
        //query per ottenere i primi 100 nomi dei dataset partendo dal 500esimo
        //String sql = "SELECT name FROM dataset LIMIT 300 OFFSET 6000";
        
        //String sql = "select distinct dataset.name from resource_controls, res_in_dataset, dataset where geo_processed = 'true' AND resource_controls.resource_id = res_in_dataset.resource_id AND res_in_dataset.dataset_id = dataset.id";
        //String sql = "SELECT name FROM dataset LIMIT 5000 OFFSET 1600";
        String sql = "SELECT dataset.name FROM res_in_dataset, resource, dataset WHERE resource.id = res_in_dataset.resource_id AND resource.format LIKE '%ZIP%' AND res_in_dataset.dataset_id = dataset.id LIMIT 5000 OFFSET 158";
        
        //String sql = "SELECT DISTINCT dataset.name FROM res_in_dataset, resource, dataset WHERE resource.id = res_in_dataset.resource_id AND resource.format LIKE '%zip%' AND res_in_dataset.dataset_id = dataset.id";
        
        //ottenere tutti i nomi dei dataset che contengono risorse di tipo zip
        //String sql = "SELECT DISTINCT dataset.name FROM res_in_dataset, resource, dataset WHERE resource.id = res_in_dataset.resource_id AND resource.format LIKE '%zip%' AND res_in_dataset.dataset_id = dataset.id";
        
        //String sql = "SELECT DISTINCT dataset.name FROM res_in_dataset, resource, dataset WHERE resource.id = res_in_dataset.resource_id AND resource.format NOT LIKE '%CSV%' AND resource.format NOT LIKE '%JSON%' AND res_in_dataset.dataset_id = dataset.id LIMIT 20 OFFSET 200";
        
        try (Connection conn = this.connect();
            PreparedStatement pstmt  = conn.prepareStatement(sql)){
                ResultSet rs  = pstmt.executeQuery();
                
                while (rs.next()) {
                    l.add(rs.getString("name"));
                }                
        }
        catch (SQLException e) {
            System.out.println("getNames " + e.getMessage());
        }
        
        return l;
    }
    
    public List<String> getDatasetNames(){
        List<String> l = new LinkedList<>();

        String sql = "SELECT dataset.name FROM dataset";

        try (Connection conn = this.connect();
            PreparedStatement pstmt  = conn.prepareStatement(sql)){
                ResultSet rs  = pstmt.executeQuery();
                
                while (rs.next()) {
                    l.add(rs.getString("name"));
                }                
        }
        catch (SQLException e) {
            System.err.println("getDatasetNames " + e.getMessage());
        }
        
        return l;
    }
    
    
    public List<String> getDatasetValidated(){
        List<String> l = new LinkedList<>();

        String sql = "SELECT DISTINCT dataset.name FROM res_in_dataset, resource_controls, dataset WHERE resource_controls.resource_id = res_in_dataset.resource_id AND dataset.id = res_in_dataset.dataset_id";

        try (Connection conn = this.connect();
            PreparedStatement pstmt  = conn.prepareStatement(sql)){
                ResultSet rs  = pstmt.executeQuery();
                
                while (rs.next()) {
                    l.add(rs.getString("name"));
                }                
        }
        catch (SQLException e) {
            System.err.println("getDatasetValidated " + e.getMessage());
        }
        
        return l;
    }
    
    
    public Integer[] getCODS(String lat, String lon){
        
        String sql = "SELECT COD_REG, COD_PRO, PRO_COM FROM adm3 WHERE Contains(geometry,Transform(MakePoint(?,?,4326),32632)) = 1";
        
        Integer[] CODS = new Integer[3];
        
        try (Connection conn = this.connect();
            PreparedStatement pstmt  = conn.prepareStatement(sql)){
                pstmt.setDouble(1, Double.parseDouble(lon));
                pstmt.setDouble(2, Double.parseDouble(lat));
                ResultSet rs  = pstmt.executeQuery();
                
                if(rs.next()) {
                    CODS[0] = rs.getInt("COD_REG");
                    CODS[1] = rs.getInt("COD_PRO");
                    CODS[2] = rs.getInt("PRO_COM");
                }
        }
        catch (SQLException e) {
            System.out.println("getCODS " + e.getMessage());
        }
        
        return CODS;
    }
    
    public String getGeometry(String ADM, String IDCOD, Integer COD){
        String sql = "SELECT asWKT(geometry) FROM " + ADM + " WHERE " + IDCOD + "=?";
                
        String result = "";
        try (Connection conn = this.connect();
            PreparedStatement pstmt  = conn.prepareStatement(sql)){
                pstmt.setDouble(1, COD);
                ResultSet rs  = pstmt.executeQuery();
                
                if(rs.next()) {
                    result = rs.getString("asWKT(geometry)");
                }
        }
        catch (SQLException e) {
            System.out.println("getGeometry " + e.getMessage());
        }
        
        return result;
    }
    
    public void deleteTables() throws SQLException{
        deleteTable("dataset");
        deleteTable("resource");
        deleteTable("res_in_dataset");
        deleteTable("organization");
        deleteTable("org_in_dataset");
        deleteTable("dataset_is_updated");
        deleteTable("email_verification");
        deleteTable("resource_controls");
    }
    
    public void deleteTable(String table) throws SQLException{
        String sql = "DELETE FROM " + table + ";";        
        
        try {
            Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        }
        catch (SQLException e) {
            System.out.println("deleteTable " + e.getMessage());
        }
    }
    
    public List<String> getNamesEmailChecked() throws SQLException{
        String sql = "SELECT name FROM email_verification, dataset WHERE dataset.id = email_verification.dataset_id";        
        
        List<String> l = new LinkedList<>();

        try (Connection conn = this.connect();
            PreparedStatement pstmt  = conn.prepareStatement(sql)){
                ResultSet rs  = pstmt.executeQuery();
                
                while (rs.next()) {
                    l.add(rs.getString("name"));
                }                
        }
        catch (SQLException e) {
            System.err.println("getNamesEmailChecked " + e.getMessage());
        }
        return l;
    }
}