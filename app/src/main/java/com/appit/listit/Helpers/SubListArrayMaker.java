package com.appit.listit.Helpers;

import com.appit.listit.General.Category;
import com.appit.listit.Lists.SubList;
import com.appit.listit.Products.Product;

import java.util.ArrayList;
import java.util.List;

import static com.orm.SugarRecord.findWithQuery;

/**
 * Created by איתי פלדמן on 03/01/2018.
 */

public class SubListArrayMaker {

    public static ArrayList createArray(List<Product> productsList, List<Category> categoriesList){
        List<SubList> subListList = new ArrayList<>();

        for (int i=0;i<categoriesList.size();i++){
            Category c = categoriesList.get(i);
            List<Product> tempProductsList = findWithQuery(Product.class, "Select * from Product where category_id = ?", String.valueOf(c.getId()));
            if (!tempProductsList.isEmpty()){
                SubList s = new SubList(c.getCategoryName(), c.getId());
                subListList.add(s);
            }
          /*  for (int k=0;k<productsList.size();k++){
                Product p = productsList.get(k);
                if (p.getCategorytId()==c.getId()){
                    if (!subListList.isEmpty()){
                        int count = 0;
                        for (int j=0;j<subListList.size();j++){
                            if (subListList.get(j).getSubListTitle()!=c.getCategoryName()){
                                count++;
                            }
                        }
                        if (count+1==subListList.size()){
                            SubList s = new SubList(c.getCategoryName(), c.getId());
                            subListList.add(s);
                        }
                    }
                    else{
                        SubList s = new SubList(c.getCategoryName(), c.getId());
                        subListList.add(s);
                    }

                }
            }*/
        }
        return (ArrayList) subListList;
    }

}
