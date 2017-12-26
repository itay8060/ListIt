package com.appit.listit.Users;

import com.orm.SugarRecord;

/**
 * Created by איתי פלדמן on 11/12/2017.
 */

public class User extends SugarRecord {

    private long userOnlineId;
    private String userName;
    private String userEmail;
    private String userPassword;

    public User(){
    }

    public User(String userName, String userEmail, String userPassword){
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    public void setUserOnlineId(long userOnlineId){
        this.userOnlineId = userOnlineId;
    }

    public long getUserOnlineId(){
        return this.userOnlineId;
    }

    public void setUserName(String name){
        this.userName = name;
    }

    public String getUserName(){
        return userName;
    }

    public void setUserEmail(String userEmail){
        this.userEmail = userEmail;
    }

    public String getUserEmail(){
        return this.userEmail;
    }

    public void setUserPassword(String userPassword){
        this.userPassword = userPassword;
    }

    public String getUserPassword(){
        return this.userPassword;
    }

}
