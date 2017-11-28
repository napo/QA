/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nickan;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author nicola
 */
public class Organization {
    
    private String description;
    private String created;
    private String title;
    private String name;
    private String is_organization;
    private String state;
    private String image_url;
    private String revision_id;
    private String type;
    private String id;
    private String approval_status;

    public Organization(JSONObject organization) throws JSONException{
        
        if(organization.has("id")){
            if(organization.getString("id")!=null && !organization.getString("id").equals("")){
                this.id=organization.getString("id");
            }
        }
        
        if(organization.has("description")){
            if(organization.getString("description")!=null && !organization.getString("description").equals("")){
                this.description=organization.getString("description");
            }
        }
        
        if(organization.has("created")){
            if(organization.getString("created")!=null && !organization.getString("created").equals("")){
                this.created=organization.getString("created");
            }
        }
        
        if(organization.has("title")){
            if(organization.getString("title")!=null && !organization.getString("title").equals("")){
                this.title=organization.getString("title");
            }
        }
        
        if(organization.has("name")){
            if(organization.getString("name")!=null && !organization.getString("name").equals("")){
                this.name=organization.getString("name");
            }
        }
        
        if(organization.has("is_organization")){
            if(organization.getString("is_organization")!=null && !organization.getString("is_organization").equals("")){
                this.is_organization=organization.getString("is_organization");
            }
        }
        
        if(organization.has("state")){
            if(organization.getString("state")!=null && !organization.getString("state").equals("")){
                this.state=organization.getString("state");
            }
        }
        
        if(organization.has("image_url")){
            if(organization.getString("image_url")!=null && !organization.getString("image_url").equals("")){
                this.image_url=organization.getString("image_url");
            }
        }
        
        if(organization.has("revision_id")){
            if(organization.getString("revision_id")!=null && !organization.getString("revision_id").equals("")){
                this.revision_id=organization.getString("revision_id");
            }
        }
        
        if(organization.has("type")){
            if(organization.getString("type")!=null && !organization.getString("type").equals("")){
                this.type=organization.getString("type");
            }
        }
        
        if(organization.has("approval_status")){
            if(organization.getString("approval_status")!=null && !organization.getString("approval_status").equals("")){
                this.approval_status=organization.getString("approval_status");
            }
        }
    }
    
    public Organization(){};

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
     * @return the is_organization
     */
    public String getIs_organization() {
        return is_organization;
    }

    /**
     * @param is_organization the is_organization to set
     */
    public void setIs_organization(String is_organization) {
        this.is_organization = is_organization;
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
     * @return the image_url
     */
    public String getImage_url() {
        return image_url;
    }

    /**
     * @param image_url the image_url to set
     */
    public void setImage_url(String image_url) {
        this.image_url = image_url;
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
     * @return the approval_status
     */
    public String getApproval_status() {
        return approval_status;
    }

    /**
     * @param approval_status the approval_status to set
     */
    public void setApproval_status(String approval_status) {
        this.approval_status = approval_status;
    }

    
}
