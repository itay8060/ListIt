package com.appit.listit.Lists;

import com.orm.SugarRecord;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by איתי פלדמן on 07/12/2017.
 */

public class List extends SugarRecord {

    private long listOnlineId;
    private String listName;
    private String userName;
    private String listCreateDate;
    private boolean listDone;

    public List(){
    }

    public List(String listName, String createrName){
        this.listName = listName;
        this.userName = createrName;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        this.listCreateDate = formattedDate;
        this.listDone = false;
    }

    public void setListOnlineId(long listOnlineId){
        this.listOnlineId = listOnlineId;
    }

    public long getListOnlineId(){
        return this.listOnlineId;
    }

    public void setListName(String listName){
        this.listName=listName;
    }

    public String getListName(){
        return this.listName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getUserName(){
        return this.userName;
    }

    public boolean listIsDone(){
        return this.listDone;
    }

    public void listToggleChecked(){
        this.listDone = !this.listDone;
    }

    public String getListDate(){
        return this.listCreateDate;
    }

}
