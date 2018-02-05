package com.appit.listit.Products;

import com.orm.SugarRecord;

/**
 * Created by אitay feldman on 13/12/2017.
 */

public class Note extends SugarRecord {

    private String noteOnlineId;
    private String noteTxt;
    private String userName;
    private String productId;

    public Note(){
    }

    public Note(String noteTxt, String userName, String productId){
        this.noteTxt = noteTxt;
        this.userName = userName;
        this.productId = productId;
    }

    public void setNoteOnlineId(String noteOnlineId){
        this.noteOnlineId = noteOnlineId;
        this.save();
    }

    public String getNoteOnlineId(){
        return this.noteOnlineId;
    }

    public void setNoteTxt(String noteTxt){
        this.noteTxt = noteTxt;
        this.save();
    }

    public String getNoteTxt(){
        return this.noteTxt;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getUserName(){
        return this.userName;
    }

    public void setProductId(String productId){
        this.productId = productId;
        this.save();
    }

    public String getProductId(){
        return this.productId;
    }
}
