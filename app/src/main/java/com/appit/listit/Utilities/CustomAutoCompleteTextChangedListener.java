package com.appit.listit.Utilities;

/**
 * Created by itay feldman on 31/01/2018.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import com.appit.listit.General.MainActivity;
import com.appit.listit.Products.Product;
import com.appit.listit.R;

import java.util.List;

import static com.orm.SugarRecord.findWithQuery;

public class CustomAutoCompleteTextChangedListener implements TextWatcher{

    public static final String TAG = "CustomAutoCompleteTextChangedListener.java";
    Context context;

    public CustomAutoCompleteTextChangedListener(Context context){
        this.context = context;
    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // TODO Auto-generated method stub

    }

    @SuppressLint("LongLogTag")
    @Override
    public void onTextChanged(CharSequence userInput, int start, int before, int count) {

        try{
            MainActivity.choseFromSuggestedList = false;
            // if you want to see in the logcat what the user types
            Log.e(TAG, "User input: " + userInput);

            MainActivity mainActivity = ((MainActivity) context);

            // update the adapater
            mainActivity.autoCompleteAdapter.notifyDataSetChanged();

            // get suggestions from the database
            List<Product> productsList = findWithQuery(Product.class, "Select * from Product where product_name like '%" + userInput.toString() + "%'");

            // update the adapter
            mainActivity.autoCompleteAdapter = new AutoCompleteAdapter(mainActivity, R.layout.autocomplete_list_row, productsList);

            mainActivity.newProductEditText.setAdapter(mainActivity.autoCompleteAdapter);





        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}