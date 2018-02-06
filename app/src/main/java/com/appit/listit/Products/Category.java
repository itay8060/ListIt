package com.appit.listit.Products;

import com.orm.SugarRecord;

/**
 * Created by itay feldman on 03/01/2018.
 */

public class Category extends SugarRecord{


    private String categoryOnlineId;
    private String categoryName;

    public Category(){
        this.categoryName = "";
    }

    public Category(String categoryName){
        this.categoryName = categoryName;
    }

    public void setCategoryOnlineId(String categoryOnlineId) {
        this.categoryOnlineId = categoryOnlineId;
        this.save();
    }


    public String getCategoryOnlineId() {
        return categoryOnlineId;
    }

    public String getCategoryName(){
        return categoryName;
    }

}
