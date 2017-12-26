package com.appit.listit.Products;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.appit.listit.General.AppConstants;
import com.appit.listit.R;

import java.util.List;

public class ProductAdapter extends BaseAdapter{

    final List<Product> products;
    final Context context;
    private ItemClickListener clickListener;

    public ProductAdapter(List<Product> products, Context context, ItemClickListener clickListener){
        this.products = products;
        this.context = context;
        this.clickListener = clickListener;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Product getItem(int i) {
        return products.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View rView, ViewGroup viewGroup) {
        if(rView==null){
            rView= LayoutInflater.from(context).inflate(R.layout.product_view,viewGroup ,false);
        }

        final Product product = getItem(i);
        CheckBox checkBox =  ((CheckBox)rView.findViewById(R.id.done));
        TextView productName =   ((TextView)rView.findViewById(R.id.productName));
        ImageButton subQunBtn =  ((ImageButton)rView.findViewById(R.id.subQuantityBtn));
        TextView productQuntity = ((TextView)rView.findViewById(R.id.productQuntity));
        ImageButton addQuantityBtn = ((ImageButton)rView.findViewById(R.id.addQuantityBtn));
        Log.e("123", String.valueOf(product.productIsDone()));
        checkBox.setChecked(product.productIsDone());
        productName.setText(product.getProductName());

        addQuantityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(v, AppConstants.ADD_CONST, i);
            }
        });
        subQunBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(v, AppConstants.SUB_CONST, i);
            }
        });

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(v, AppConstants.CHECKBOX, i);
            }
        });

        rView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(v, AppConstants.EDIT_PRODUCT_CONST, i);
            }
        });

        productQuntity.setText(String.valueOf(product.getQuantity()));

        return rView;
    }

}