package com.appit.listit.DBPackage;

import android.util.Log;

import com.appit.listit.FireBasePackage.FireBaseDataManager;
import com.appit.listit.Products.Category;
import com.appit.listit.General.PrefsManager;
import com.appit.listit.ListItApplication;
import com.appit.listit.Products.Note;
import com.appit.listit.Products.Product;
import com.appit.listit.R;
import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

import static com.orm.SugarRecord.findWithQuery;

/**
 * Created by itay feldman on 08/01/2018.
 */

public class ObjectsManager  {

    private static List<com.appit.listit.Lists.List> listsList = new ArrayList<>();
    private static List<Product> productsList = new ArrayList<>();
    private static com.appit.listit.Lists.List cList = new com.appit.listit.Lists.List();

    // region list manager

    public static com.appit.listit.Lists.List getUserList() {
        if (userHasList()) {
            Log.e("getUserList", "got existing list");
            for (com.appit.listit.Lists.List list : listsList) {
                if (list.getListOnlineId().equals(PrefsManager.getCurrentList())) {
                    cList = list;
                    return list;
                } else continue;
            }
            //return getUserListsList().get(0);
        }
        else{
            com.appit.listit.Lists.List list = new com.appit.listit.Lists.List(FireBaseDataManager.getUser().getUid(), ListItApplication.getContext().getString(R.string.mainpage_defaultlist));
            list.save();
            FireBaseDataManager.addFireBaseList(list);
            PrefsManager.setCurrentList(list.getListOnlineId());
            //default empty list for user
            //productsList = getDemoProducts(list.getListOnlineId());
            cList = list;
            Log.e("getUserList", "made new list");
            return list;
        }
        return cList;
    }

    public static boolean userHasList(){
        listsList = getUserListsList();
        Log.e("list of lists from user", listsList.toString());
        if(listsList.isEmpty()){
            return false;
        }
        else {
            return true;
        }
    }

    public static List<com.appit.listit.Lists.List> getUserListsList(){
        List<com.appit.listit.Lists.List> listsList;
        listsList = findWithQuery(com.appit.listit.Lists.List.class, "Select * from List where user_id = ?", FireBaseDataManager.getUser().getUid());
        Log.e("UManager user lists", String.valueOf(listsList.size()));
        return listsList;
    }

    // endregion list manager

    // region products manager

    public static List<RelatedListProduct> getRelatedProductsList(String listId){
        SugarRecord.executeQuery("CREATE TABLE IF NOT EXISTS RELATED_LIST_PRODUCT (ID INTEGER PRIMARY KEY AUTOINCREMENT, related_list_online_id TEXT, related_product_online_id TEXT, category_id TEXT, quantity INTEGER, product_done BOOL)");
        List<RelatedListProduct> productsList;
        productsList = findWithQuery(RelatedListProduct.class, "Select * from RELATED_LIST_PRODUCT where related_list_online_id = ?", listId);
        if (productsList.isEmpty()){
            return productsList;
        }
        else{
        }
        return productsList;
    }

    public static long getRelatedProduct(String listId, String productId){
        long id = 0;
        List<RelatedListProduct> productsList;
        productsList = findWithQuery(RelatedListProduct.class, "Select * from RELATED_LIST_PRODUCT where related_list_online_id = ? and related_product_online_id = ?", listId, productId);
        if (productsList.isEmpty()){
            Log.e("Empty", "List is empty");
            return id;
        }
        else{
            Log.e("Not empty", "List is not empty");
            id = productsList.get(0).getId();
            return id;
        }
    }



    // endregion product manager


    public static List<Note> getnotesList(String productId){
        List<Note> notesList;
        notesList = findWithQuery(Note.class, "Select * from Note where product_id = ?", productId);

        return notesList;
    }


    //region Category Manager
    public static List<Category> getCategoryList(){
        List<Category> categoriesList = new ArrayList<>();
        categoriesList = Category.listAll(Category.class);
        if (!categoriesList.isEmpty()){
            return categoriesList;
        }

        for (String name : ListItApplication.getContext().getResources().getStringArray(R.array.category_names)) {
            Category category = new Category(name);
            category.save();
            category.setCategoryOnlineId(String.valueOf(category.getId()));
            categoriesList.add(category);
        }
            return categoriesList;
        }

    public static Category getCategoryByName(String categoryName){
        java.util.List<Category> categoryListTemp = new ArrayList();
        categoryListTemp = findWithQuery(Category.class, "Select * from Category where category_name = ?", categoryName);
        Category category = categoryListTemp.get(0);
        return category;
    }


    public static ArrayList<String> getCategoriesAsStrings(){
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

    public static Product getProductFromOnlineId(String id){
        List<Product> productsListTemp = new ArrayList();
        productsListTemp = findWithQuery(Product.class, "Select * from Product where product_online_id = ?", id);
        return productsListTemp.get(0);
    }

    public static Product getProductFromName(String name){
        List<Product> productsListTemp = new ArrayList();
        productsListTemp = findWithQuery(Product.class, "Select * from Product where product_name = ?", name);
        return productsListTemp.get(0);
    }

    public static RelatedListProduct getRelatedProductFromOnlineId(String id){
        List<RelatedListProduct> productsListTemp = new ArrayList();
        productsListTemp = findWithQuery(RelatedListProduct.class, "Select * from RELATED_LIST_PRODUCT where related_product_online_id = ?", id);
        return productsListTemp.get(0);
    }

    public static Boolean productExists(String productName){
        List<Product> productsListTemp = new ArrayList();
        productsListTemp = Product.listAll(Product.class);
        for (int i = 0; i < productsListTemp.size() ; i++) {
            if (productsListTemp.get(i).getProductName().toLowerCase().equals(productName.toLowerCase())){
                return true;
            }
        }
        return false;
    }


}
