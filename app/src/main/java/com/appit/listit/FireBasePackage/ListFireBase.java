package com.appit.listit.FireBasePackage;

/**
 * Created by itay feldman on 09/01/2018.
 */

public class ListFireBase {

    private String listOnlineId;
    private String listName;
    private String userName;
    private String listCreateDate;
    private boolean listDone;

    public ListFireBase(){
    }

    public ListFireBase(String listOnlineId, String listName, String userName, String listCreateDate){
        this.listOnlineId = listOnlineId;
        this.listName = listName;
        this.userName = userName;
        this.listCreateDate = listCreateDate;
        this.listDone = false;
    }

    public void setListOnlineId(String listOnlineId) {
        this.listOnlineId = listOnlineId;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setListCreateDate(String listCreateDate) {
        this.listCreateDate = listCreateDate;
    }

    public void setListDone(boolean listDone) {
        this.listDone = listDone;
    }

    public String getListOnlineId() {
        return listOnlineId;
    }

    public String getListName() {
        return listName;
    }

    public String getUserName() {
        return userName;
    }

    public String getListCreateDate() {
        return listCreateDate;
    }

    public boolean isListDone() {
        return listDone;
    }
}
