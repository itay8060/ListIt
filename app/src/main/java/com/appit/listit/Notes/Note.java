package com.appit.listit.Notes;

import com.orm.SugarRecord;

/**
 * Created by איתי פלדמן on 13/12/2017.
 */

public class Note extends SugarRecord {

    private long noteOnlineId;
    private String noteTxt;
    private String userName;
    private long productId;

    public Note(){
    }

    public Note(String noteTxt, String userName, long productId){
        this.noteOnlineId = 0;
        this.noteTxt = noteTxt;
        this.userName = userName;
        this.productId = productId;
    }

    public void setNoteOnlineId(long noteOnlineId){
        this.noteOnlineId = noteOnlineId;
    }

    public long getNoteOnlineId(){
        return this.noteOnlineId;
    }

    public void setNoteTxt(String noteTxt){
        this.noteTxt = noteTxt;
        this.save();
    }

    public String getNoteTxt(){
        return this.noteTxt;
    }

    public void setUseId(String userName){
        this.userName = userName;
    }

    public String getUserName(){
        return this.userName;
    }

    public void setProductId(long productId){
        this.productId = productId;
    }

    public long getProductId(){
        return this.productId;
    }
}
