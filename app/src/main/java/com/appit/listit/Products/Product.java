package com.appit.listit.Products;

import com.orm.SugarRecord;

/**
 * Created by אitay feldman on 08/12/2017.
 */

public class Product extends SugarRecord {

    private String productOnlineId;
    private String productName;
    private boolean productDone;
    private int quantity;
    //private Bitmap productImage;
    private String listId;
    private String categoryId;

    public Product(){
    }

    public Product(String productName, String listId, String categoryId){
        this.productName = productName;
        this.listId = listId;
        this.quantity = 1;
        this.productDone = false;
        //this.productImage = null;
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

    public boolean productIsDone(){
        return this.productDone;
    }

    public void productToggleChecked(){
        this.productDone = !this.productDone;
        this.save();
    }

    public void increaseQuantity() {
        this.quantity += 1;
        this.save();
    }

    public void decreaseQuantity() {
        this.quantity -= 1;
        this.save();
    }

    public int getQuantity() {
        return this.quantity;
    }

    /*public void setProductImage(Bitmap bm){
        this.productImage = bm;
    }

    public Bitmap getProductImage(){
        return this.productImage;
    }*/

    public void setListId(String listId){
        this.listId = listId;
        this.save();
    }

    public String getListId(){
        return this.listId;
    }

    public String getCategorytId(){
        return this.categoryId;
    }

    public void setCategoryId(String categoryId){
        this.categoryId = categoryId;
        this.save();
    }

}