package com.appit.listit.Lists;

import com.appit.listit.Products.Product;

import java.util.ArrayList;

/**
 * Created by itay feldman on 31/12/2017.
 */

public class SubList{

    private String subListTitle;
    private String categoryId;
    private ArrayList<Product> productsList;

    public SubList(){

    }

    public SubList(String listTitle, String categoryId){
        this.subListTitle = listTitle;
        this.categoryId = categoryId;
    }

    public String getSubListTitle(){
        return this.subListTitle;
    }

    public void setSubListTitle(String subListTitle){
        this.subListTitle = subListTitle;
    }

    public String getCategoryId() {
        return categoryId;
    }
}
