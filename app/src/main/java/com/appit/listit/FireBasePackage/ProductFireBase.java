package com.appit.listit.FireBasePackage;

/**
 * Created by itay feldman on 09/01/2018.
 */

public class ProductFireBase {

    private String productOnlineId;
    private String productName;
    private boolean productDone;
    private int quantity;
    //private Bitmap productImage;
    private String listId;
    private String categoryId;

    public String getCategoryId() {
        return categoryId;
    }

    public ProductFireBase(String productOnlineId, String productName, boolean productDone, int quantity, String listId, String categoryId){
        this.productOnlineId = productOnlineId;
        this.productName = productName;
        this.listId = listId;
        this.quantity = quantity;
        this.productDone = productDone;
        //this.productImage = null;
        this.categoryId = categoryId;
    }

    public void setProductOnlineId(String productOnlineId) {
        this.productOnlineId = productOnlineId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductDone(boolean productDone) {
        this.productDone = productDone;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public ProductFireBase(){
    }

    public String getProductOnlineId() {
        return productOnlineId;
    }

    public String getProductName() {
        return productName;
    }

    public boolean isProductDone() {
        return productDone;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getListId() {
        return listId;
    }

}
