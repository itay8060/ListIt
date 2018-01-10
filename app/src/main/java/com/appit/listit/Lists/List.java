package com.appit.listit.Lists;

import com.orm.SugarRecord;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by itay feldman on 07/12/2017.
 */

public class List extends SugarRecord {

    private String listOnlineId;
    private String listName;
    private String userId;
    private String listCreateDate;
    private boolean listDone;

    public List(){
    }

    public List(String userId, String listName){
        this.listName = listName;
        this.userId = userId;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        this.listCreateDate = formattedDate;
        this.listDone = false;
    }

    public void setListOnlineId(String listOnlineId){
        this.listOnlineId = listOnlineId;
        this.save();
    }

    public String getListOnlineId(){
        return this.listOnlineId;
    }

    public void setListName(String listName){
        this.listName=listName;
    }

    public String getListName(){
        return this.listName;
    }

    public void setUserId(String userId){
        this.userId = userId;
        this.save();
    }

    public String getUserId(){
        return this.userId;
    }

    public boolean listIsDone(){
        return this.listDone;
    }

    public void listToggleChecked(){
        this.listDone = !this.listDone;
    }

    public String getListCreateDate() {
        return listCreateDate;
    }
}
