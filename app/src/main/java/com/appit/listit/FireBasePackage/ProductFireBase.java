package com.appit.listit.FireBasePackage;

import android.graphics.Bitmap;

/**
 * Created by itay feldman on 09/01/2018.
 */

public class ProductFireBase {

    private String productOnlineId;
    private String productName;
    private Bitmap productImage;
    private String categoryId;

    public ProductFireBase(){
    }

    public ProductFireBase(String productOnlineId, String productName,  String categoryId){
        this.productOnlineId = productOnlineId;
        this.productName = productName;
        this.productImage = null;
        this.categoryId = categoryId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setProductOnlineId(String productOnlineId) {
        this.productOnlineId = productOnlineId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }


    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductOnlineId() {
        return productOnlineId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductImage(Bitmap productImage) {
        this.productImage = productImage;
    }

    public Bitmap getProductImage() {
        return productImage;
    }

}
