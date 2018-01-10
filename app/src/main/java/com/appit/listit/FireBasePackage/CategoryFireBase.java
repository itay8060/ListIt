package com.appit.listit.FireBasePackage;

/**
 * Created by itay feldman on 09/01/2018.
 */

public class CategoryFireBase {

    private String categoryOnlineId;
    private String categoryName;

    public CategoryFireBase(){
    }

    public CategoryFireBase(String categoryOnlineId, String categoryName){
        this.categoryOnlineId = categoryOnlineId;
        this.categoryName = categoryName;
    }

    public String getCategoryOnlineId() {
        return categoryOnlineId;
    }

    public void setCategoryOnlineId(String categoryOnlineId) {
        this.categoryOnlineId = categoryOnlineId;
    }

    public String getCategoryName(){
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
