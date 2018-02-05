package com.appit.listit.Utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.appit.listit.Products.Product;
import com.appit.listit.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itay feldman on 30/01/2018.
 */

public class AutoCompleteAdapter extends ArrayAdapter<Product> {

    Context mContext;
    int layoutResourceId;
    List<Product> productList = new ArrayList<>();

    public AutoCompleteAdapter (Context mContext, int layoutResourceId, List<Product> productsList){
        super(mContext, layoutResourceId, productsList);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.productList = productsList;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Product getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return productList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try{

        if (convertView==null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }
        final Product product = getItem(position);
        TextView textview = (TextView)convertView.findViewById(R.id.textViewItem);
        textview.setText(product.getProductName());


        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return convertView;
    }
}
