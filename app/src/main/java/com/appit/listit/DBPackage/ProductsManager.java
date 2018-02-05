package com.appit.listit.DBPackage;

import com.appit.listit.General.Category;
import com.appit.listit.ListItApplication;
import com.appit.listit.Products.Product;
import com.appit.listit.R;

import java.util.ArrayList;
import java.util.List;

import static com.orm.SugarRecord.findWithQuery;

/**
 * Created by איתי פלדמן on 28/01/2018.
 */

public class ProductsManager {

    private static List<Product> productsList = new ArrayList<>();

    public static List<Product> initiateProducts(){

        List<Product> productsList = new ArrayList<>();
        productsList = Product.listAll(Product.class);
        if (!productsList.isEmpty()){
            return productsList;
        }

        initMeatnFishProducts();
        initDairyProducts();
        initFruitsnVegsProducts();
        initBasicProducts();

        return productsList;
    }

    private static void initMeatnFishProducts(){
        List<Category> categoriesListTemp = new ArrayList();
        categoriesListTemp = findWithQuery(Category.class, "Select * from Category where category_name = ?", ListItApplication.getContext().getResources().getString(R.string.category_meatnfish));
        Category c = categoriesListTemp.get(0);
        for (String name : ListItApplication.getContext().getResources().getStringArray(R.array.meatnfishproducts_names)) {
            Product product = new Product(name, c.getCategoryOnlineId());
            product.save();
            product.setProductOnlineId(String.valueOf(product.getId()));
            productsList.add(product);
        }
    }

    private static void initDairyProducts(){
        List<Category> categoriesListTemp = new ArrayList();
        categoriesListTemp = findWithQuery(Category.class, "Select * from Category where category_name = ?", ListItApplication.getContext().getResources().getString(R.string.category_dairy));
        Category c = categoriesListTemp.get(0);
        for (String name : ListItApplication.getContext().getResources().getStringArray(R.array.dairyproducts_names)) {
            Product product = new Product(name, c.getCategoryOnlineId());
            product.save();
            product.setProductOnlineId(String.valueOf(product.getId()));
            productsList.add(product);
        }
    }

    private static void initFruitsnVegsProducts(){
        List<Category> categoriesListTemp = new ArrayList();
        categoriesListTemp = findWithQuery(Category.class, "Select * from Category where category_name = ?", ListItApplication.getContext().getResources().getString(R.string.category_fruitsnvegs));
        Category c = categoriesListTemp.get(0);
        for (String name : ListItApplication.getContext().getResources().getStringArray(R.array.fruitsnvegsproducts_names)) {
            Product product = new Product(name, c.getCategoryOnlineId());
            product.save();
            product.setProductOnlineId(String.valueOf(product.getId()));
            productsList.add(product);
        }
    }

    private static void initBasicProducts(){
        List<Category> categoriesListTemp = new ArrayList();
        categoriesListTemp = findWithQuery(Category.class, "Select * from Category where category_name = ?", ListItApplication.getContext().getResources().getString(R.string.category_basicproducts));
        Category c = categoriesListTemp.get(0);
        for (String name : ListItApplication.getContext().getResources().getStringArray(R.array.basicproducts_names)) {
            Product product = new Product(name, c.getCategoryOnlineId());
            product.save();
            product.setProductOnlineId(String.valueOf(product.getId()));
            productsList.add(product);
        }
    }

}
