package com.appit.listit.Products;

import android.graphics.Bitmap;

import com.orm.SugarRecord;

/**
 * Created by אitay feldman on 08/12/2017.
 */

public class Product extends SugarRecord {

    private String productOnlineId;
    private String productName;
    private Bitmap productImage;
    private String categoryId;

    public Product(){
    }

    public Product(String productName, String categoryId){
        this.productName = productName;
        this.categoryId = categoryId;
    }

    public void setProductOnlineId(String productOnlineId){
        this.productOnlineId = productOnlineId;
        this.save();
    }

    public String getProductOnlineId(){
        return this.productOnlineId;
    }

    public void setProductName(String productName){
        this.productName=productName;
    }

    public String getProductName(){
        return this.productName;
    }



    public void setProductImage(Bitmap bm){
        this.productImage = bm;
        this.save();
    }

    public Bitmap getProductImage(){
        return this.productImage;
    }

    public String getCategorytId(){
        return this.categoryId;
    }

    public void setCategoryId(String categoryId){
        this.categoryId = categoryId;
        this.save();
    }

}