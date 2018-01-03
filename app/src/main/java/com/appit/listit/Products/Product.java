package com.appit.listit.Products;

import com.orm.SugarRecord;

public class Product extends SugarRecord {

    private long productOnlineId;
    private String productName;
    private boolean productDone;
    private int quantity;
    //private Bitmap productImage;
    private long listId;
    private long categoryId;

    public Product(){
    }

    public Product(String productName, long listId, long categoryId){
        this.productName = productName;
        this.listId = listId;
        this.quantity = 1;
        this.productDone = false;
        //this.productImage = null;
        this.categoryId = categoryId;
    }

    public void setProductOnlineId(long productOnlineId){
        this.productOnlineId = productOnlineId;
    }

    public long getProductOnlineId(){
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

    public void setListId(long listId){
        this.listId = listId;
    }

    public long getListId(){
        return this.listId;
    }

    public long getCategorytId(){
        return this.categoryId;
    }

}