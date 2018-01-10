package com.appit.listit.Helpers;

import com.appit.listit.General.Category;
import com.appit.listit.Lists.SubList;
import com.appit.listit.Products.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itay feldman on 03/01/2018.
 */

public class SubListArrayMaker {

    public static ArrayList createArray(List<Product> productsList, List<Category> categoriesList){
        List<SubList> subListList = new ArrayList<>();

        for (int i=0;i<categoriesList.size();i++){
            Category c = categoriesList.get(i);
            List<Product> tempProductsList = new ArrayList<>();
            for (int j = 0; j < productsList.size(); j++) {
                 if (productsList.get(j).getCategorytId().equals(c.getCategoryOnlineId())) {
                     tempProductsList.add(productsList.get(j));
                 }
            }
            if (!tempProductsList.isEmpty()){
                SubList s = new SubList(c.getCategoryName(), c.getCategoryOnlineId());
                subListList.add(s);
            }
        }
        return (ArrayList) subListList;
    }

}
