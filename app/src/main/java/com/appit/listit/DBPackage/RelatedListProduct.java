package com.appit.listit.DBPackage;

import com.orm.SugarRecord;

/**
 * Created by איתי פלדמן on 28/01/2018.
 */

public class RelatedListProduct extends SugarRecord {

    private String relatedListOnlineId;
    private String relatedProductOnlineId;
    private int quantity;
    private boolean productDone;

    public RelatedListProduct(){

    }

    public RelatedListProduct (String listId, String productId){
        this.relatedListOnlineId = listId;
        this.relatedProductOnlineId = productId;
        this.productDone = false;
        this.quantity=1;
    }

    public void setRelatedListId(String listId){
        this.relatedListOnlineId = listId;
    }

    public String getRelatedListId(){
        return this.relatedListOnlineId;
    }

    public String getRelatedProductOnlineId(){
        return this.relatedProductOnlineId;
    }

    public void increaseQuantity() {
        this.quantity += 1;
        this.save();
    }

    public void decreaseQuantity() {
        if (getQuantity()>1){
            this.quantity -= 1;
            this.save();
        }
    }

    public int getQuantity() {
        return this.quantity;
    }

    public boolean productIsDone(){
        return this.productDone;
    }

    public void productToggleChecked(){
        this.productDone = !this.productDone;
        this.save();
    }

    public String getProductName(){
        return ObjectsManager.getProductFromOnlineId(getRelatedProductOnlineId()).getProductName();
    }

    public String getCategorytId(){
        return ObjectsManager.getProductFromOnlineId(getRelatedProductOnlineId()).getCategorytId();
    }
}
