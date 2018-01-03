package com.appit.listit.General;

import com.orm.SugarRecord;

/**
 * Created by איתי פלדמן on 03/01/2018.
 */

public class Category extends SugarRecord{

    private String categoryName;

    public Category(){
        this.categoryName = "";
    }

    public Category(String categoryName){
        this.categoryName = categoryName;
    }

    public String getCategoryName(){
        return categoryName;
    }

}
