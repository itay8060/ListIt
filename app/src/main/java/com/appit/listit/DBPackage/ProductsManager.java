package com.appit.listit.DBPackage;

import android.content.res.Resources;

import com.appit.listit.Products.Category;
import com.appit.listit.ListItApplication;
import com.appit.listit.Products.Product;
import com.appit.listit.R;

import java.util.ArrayList;
import java.util.List;

import static com.orm.SugarRecord.findWithQuery;

/**
 * Created by itay feldman on 28/01/2018.
 */

public class ProductsManager {

    private static List<Product> productsList = new ArrayList<>();

    public static List<Product> initiateProducts(){

        List<Product> productsList = new ArrayList<>();
        productsList = Product.listAll(Product.class);
        if (!productsList.isEmpty()){
            return productsList;
        }
        Resources myRes = ListItApplication.getContext().getResources();
        initCategoryAndProduct(myRes.getString(R.string.category_meatnfish), myRes.getStringArray(R.array.meatnfishproducts_names));
        initCategoryAndProduct(myRes.getString(R.string.category_dairy), myRes.getStringArray(R.array.dairyproducts_names));
        initCategoryAndProduct(myRes.getString(R.string.category_fruitsnvegs), myRes.getStringArray(R.array.fruitsnvegsproducts_names));
        initCategoryAndProduct(myRes.getString(R.string.category_basicproducts), myRes.getStringArray(R.array.basicproducts_names));

        return productsList;
    }

    private static void initCategoryAndProduct(String catName , String[] products ){
        List<Category> categoriesListTemp = new ArrayList();
        categoriesListTemp = findWithQuery(Category.class, "Select * from Category where category_name = ?", catName);
        Category c = categoriesListTemp.get(0);
        for (String name : products) {
            Product product = new Product(name, c.getCategoryOnlineId());
            product.save();
            product.setProductOnlineId(String.valueOf(product.getId()));
            productsList.add(product);
        }
    }
}
