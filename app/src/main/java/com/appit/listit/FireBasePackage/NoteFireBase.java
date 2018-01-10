package com.appit.listit.FireBasePackage;

/**
 * Created by itay feldman on 09/01/2018.
 */

public class NoteFireBase {

    private String noteOnlineId;
    private String noteTxt;
    private String userName;
    private String productId;

    public String getProductId() {
        return productId;
    }

    public NoteFireBase(String noteOnlineId, String noteTxt, String userName, String productId){
        this.noteOnlineId = noteOnlineId;
        this.noteTxt = noteTxt;
        this.userName = userName;
        this.productId = productId;
    }

    public void setNoteOnlineId(String noteOnlineId) {
        this.noteOnlineId = noteOnlineId;
    }

    public void setNoteTxt(String noteTxt) {
        this.noteTxt = noteTxt;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public NoteFireBase(){
    }

    public String getNoteOnlineId() {
        return noteOnlineId;
    }

    public String getNoteTxt() {
        return noteTxt;
    }

    public String getUserName() {
        return userName;
    }
}
