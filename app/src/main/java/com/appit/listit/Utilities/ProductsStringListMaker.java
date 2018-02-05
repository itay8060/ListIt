package com.appit.listit.Utilities;

import com.appit.listit.ListItApplication;
import com.appit.listit.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by itay feldman on 30/01/2018.
 */

public class ProductsStringListMaker {

    public static ArrayList<String> getProductsStringList(){

        ArrayList<String> allProducts = new ArrayList<>();
        String [] productsString = ListItApplication.getContext().getResources().getStringArray(R.array.meatnfishproducts_names);
        java.util.List<String> productsStringList = Arrays.asList(productsString);
        allProducts.addAll(productsStringList);

        productsString = ListItApplication.getContext().getResources().getStringArray(R.array.dairyproducts_names);
        productsStringList = Arrays.asList(productsString);
        allProducts.addAll(productsStringList);

        productsString = ListItApplication.getContext().getResources().getStringArray(R.array.fruitsnvegsproducts_names);
        productsStringList = Arrays.asList(productsString);
        allProducts.addAll(productsStringList);

        productsString = ListItApplication.getContext().getResources().getStringArray(R.array.basicproducts_names);
        productsStringList = Arrays.asList(productsString);
        allProducts.addAll(productsStringList);

        return allProducts;
    }

}
