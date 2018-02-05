package com.appit.listit.Utilities;

import com.appit.listit.DBPackage.RelatedListProduct;
import com.appit.listit.General.Category;
import com.appit.listit.Lists.SubList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

/**
 * Created by itay feldman on 03/01/2018.
 */

public class SubListArrayMaker {

    public static ArrayList createArray(List<RelatedListProduct> productsList, List<Category> categoriesList){
        List<SubList> subListList = new ArrayList<>();

        for (int i=0;i<categoriesList.size();i++){
            Category c = categoriesList.get(i);
            List<RelatedListProduct> tempProductsList = new ArrayList<>();
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

        Collections.sort(subListList, new Comparator<SubList>() {

            @Override
            public Comparator<SubList> reversed() {
                return null;
            }

            @Override
            public Comparator<SubList> thenComparing(Comparator<? super SubList> other) {
                return null;
            }

            @Override
            public <U> Comparator<SubList> thenComparing(Function<? super SubList, ? extends U> keyExtractor, Comparator<? super U> keyComparator) {
                return null;
            }

            @Override
            public <U extends Comparable<? super U>> Comparator<SubList> thenComparing(Function<? super SubList, ? extends U> keyExtractor) {
                return null;
            }

            @Override
            public Comparator<SubList> thenComparingInt(ToIntFunction<? super SubList> keyExtractor) {
                return null;
            }

            @Override
            public Comparator<SubList> thenComparingLong(ToLongFunction<? super SubList> keyExtractor) {
                return null;
            }

            @Override
            public Comparator<SubList> thenComparingDouble(ToDoubleFunction<? super SubList> keyExtractor) {
                return null;
            }

            public int compare(SubList s1, SubList s2) {
                return s1.getSubListTitle().compareTo(s2.getSubListTitle());
            }
        });

        return (ArrayList) subListList;
    }

}
