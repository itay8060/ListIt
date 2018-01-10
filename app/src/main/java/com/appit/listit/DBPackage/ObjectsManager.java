package com.appit.listit.DBPackage;

import android.util.Log;

import com.appit.listit.FireBasePackage.FireBaseDataManager;
import com.appit.listit.General.Category;
import com.appit.listit.Notes.Note;
import com.appit.listit.Products.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import static com.orm.SugarRecord.findWithQuery;

/**
 * Created by itay feldman on 08/01/2018.
 */

public class ObjectsManager {

    // region user manager

    public static boolean checkIfUserLoggedIn(){
        if (getUser() != null){
            return true;
        }
        return false;
    }

    public static FirebaseUser getUser(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    // endregion user manager

    // region list manager

    public static boolean userHasList(){
        List<com.appit.listit.Lists.List> listsList;
        listsList = getUserListsList();
        Log.e("list of lists from user", listsList.toString());
        if(listsList.isEmpty()){
            return false;
        }
        else {
            return true;
        }
    }

    public static com.appit.listit.Lists.List getUserList(){
        if (userHasList()){
            Log.e("getUserList", "got existing list");
            return getUserListsList().get(0);
        }
        else{
            com.appit.listit.Lists.List list = new com.appit.listit.Lists.List(getUser().getUid(), getUser().getDisplayName());
            list.save();
            FireBaseDataManager.addFireBaseList(list);
            Log.e("getUserList", "made new list");
            return list;
        }
    }

    public static List<com.appit.listit.Lists.List> getUserListsList(){
        List<com.appit.listit.Lists.List> listsList;
        listsList = findWithQuery(com.appit.listit.Lists.List.class, "Select * from List where user_id = ?", getUser().getUid());
        Log.e("UManager user lists", String.valueOf(listsList.size()));
        return listsList;
    }

    // endregion list manager

    // region products manager

    public static List<Product> getProductsList(String listId){
        List<Product> productsList;
        productsList = findWithQuery(Product.class, "Select * from Product where list_id = ?", listId);
        if (productsList.isEmpty()){
            productsList = getDemoProducts(listId);
            return productsList;
        }
        else{
        }
        return productsList;
    }

    public static List<Product> getDemoProducts(String listId){
        List<Product> productsList = new ArrayList<>();
        Product product1 = new Product("Chicken breast", listId, getCategoryList().get(1).getCategoryOnlineId());
        productsList.add(product1);
        product1.save();
        FireBaseDataManager.addFireBaseProduct(product1, listId);
        Product product2 = new Product("Ice cream", listId, getCategoryList().get(2).getCategoryOnlineId());
        productsList.add(product2);
        product2.save();
        FireBaseDataManager.addFireBaseProduct(product2, listId);
        Product product3 = new Product("Oranges", listId, getCategoryList().get(3).getCategoryOnlineId());
        productsList.add(product3);
        product3.save();
        FireBaseDataManager.addFireBaseProduct(product3, listId);

        Note.deleteAll(Note.class);
        Note note1 = new Note("ERGENT!", ObjectsManager.getUser().getEmail(), product1.getProductOnlineId());
        note1.save();
        FireBaseDataManager.addFireBaseNote(note1, product1.getProductOnlineId());
        Note note2 = new Note("We're all outt!", ObjectsManager.getUser().getEmail(), product1.getProductOnlineId());
        note2.save();
        FireBaseDataManager.addFireBaseNote(note2, product1.getProductOnlineId());
        Note note3 = new Note("Anyone getting?!", ObjectsManager.getUser().getEmail(), product2.getProductOnlineId());
        note3.save();
        FireBaseDataManager.addFireBaseNote(note3, product2.getProductOnlineId());

        return productsList;
    }

    // endregion product manager


    public static List<Note> getnotesList(String productId){
        List<Note> notesList;
        notesList = findWithQuery(Note.class, "Select * from Note where product_id = ?", productId);

        return notesList;
    }


   /* public static List<Note> getDemoNotes(String productId) {
        List<Note> notesList = new ArrayList<>();
        Note.deleteAll(Note.class);
        Note note1 = new Note("ERGENT!", ObjectsManager.getUser().getEmail(), productId);
        note1.save();
        FireBaseDataManager.addFireBaseNote(note1, productId);
        Note note2 = new Note("We're all outt!", ObjectsManager.getUser().getEmail(), productId);
        note2.save();
        FireBaseDataManager.addFireBaseNote(note2, productId);
        Note note3 = new Note("Anyone getting?!", ObjectsManager.getUser().getEmail(), productId);
        note3.save();
        FireBaseDataManager.addFireBaseNote(note3, productId);

        return notesList;
    }*/



    //region Category Manager
    public static List<Category> getCategoryList(){
        List<Category> categoriesList = new ArrayList<>();
        categoriesList = Category.listAll(Category.class);
        if (!categoriesList.isEmpty()){
            return categoriesList;
        }
        else{
            Category.deleteAll(Category.class);
            Category category1 = new Category("Others");
            category1.save();
            category1.setCategoryOnlineId(String.valueOf(category1.getId()));
            categoriesList.add(category1);
            Category category2 = new Category("Meat");
            category2.save();
            category2.setCategoryOnlineId(String.valueOf(category2.getId()));
            categoriesList.add(category2);
            Category category3 = new Category("Milk");
            category3.save();
            category3.setCategoryOnlineId(String.valueOf(category3.getId()));
            categoriesList.add(category3);
            Category category4 = new Category("Fruits");
            category4.save();
            category4.setCategoryOnlineId(String.valueOf(category4.getId()));
            categoriesList.add(category4);
            return categoriesList;
        }
    }

    public static ArrayList<String> getCategoryAsStrings(){
        List<Category> categoriesList = getCategoryList();
        ArrayList<String> listContent = new ArrayList<>();
        for (int i = 0; i < categoriesList.size(); i++) {
             listContent.add(categoriesList.get(i).getCategoryName());
        }
        return listContent;
    }

    public static void setNewCategoryToProduct(String productId, String categoryString){
        java.util.List<Product> productsListTemp = new ArrayList();
        productsListTemp = findWithQuery(Product.class, "Select * from Product where product_online_id = ?", productId);
        Product product = productsListTemp.get(0);
        List<Category> categoryList = getCategoryList();
        for (int i = 0; i < categoryList.size(); i++) {
             if (categoryList.get(i).getCategoryName().equals(categoryString)){
                 product.setCategoryId(categoryList.get(i).getId().toString());
                 Log.e("categoryUpdate", "update success");
             }
            else Log.e("categoryUpdate", "update failed");
        }
    }

    public static String getCategoryNameById(String categoryId){
        java.util.List<Category> categoryListTemp = new ArrayList();
        categoryListTemp = findWithQuery(Category.class, "Select * from Category where category_online_id = ?", categoryId);
        Category category = categoryListTemp.get(0);
        return category.getCategoryName();
    }

    //endregion Category Manager
}
