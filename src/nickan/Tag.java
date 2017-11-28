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
public class Tag {
    private String vocabulary_id;
    private String display_name;
    private String id;
    private String name;

    /**
     * @return the vocabulary_id
     */
    public String getVocabulary_id() {
        return vocabulary_id;
    }

    /**
     * @param vocabulary_id the vocabulary_id to set
     */
    public void setVocabulary_id(String vocabulary_id) {
        this.vocabulary_id = vocabulary_id;
    }

    /**
     * @return the display_name
     */
    public String getDisplay_name() {
        return display_name;
    }

    /**
     * @param display_name the display_name to set
     */
    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
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
    
    public Tag(String id, String vocabulary_id, String display_name, String name){
        this.id = id;
        this.vocabulary_id = vocabulary_id;
        this.display_name = display_name;
        this.name = name;
    }
    
    public Tag(JSONObject tag) throws JSONException{
        
        if(tag.has("id")){
            if(tag.getString("id")!=null && !tag.getString("id").equals("")){
                this.id=tag.getString("id");
            }
        }
        else{
            this.id="MISSING";
        }
        
        if(tag.has("vocabulary_id")){
            if(tag.getString("vocabulary_id")!=null && !tag.getString("vocabulary_id").equals("")){
                this.vocabulary_id=tag.getString("vocabulary_id");
            }
        }
        else{
            this.vocabulary_id="MISSING";
        }
        
        if(tag.has("display_name")){
            if(tag.getString("display_name")!=null && !tag.getString("display_name").equals("")){
                this.display_name=tag.getString("display_name");
            }
        }
        else{
            this.display_name="MISSING";
        }
        
        if(tag.has("name")){
            if(tag.getString("name")!=null && !tag.getString("name").equals("")){
                this.name=tag.getString("name");
            }
        }
        else{
            this.name="MISSING";
        }
    }
}
