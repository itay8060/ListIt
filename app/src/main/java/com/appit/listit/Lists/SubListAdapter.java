package com.appit.listit.Lists;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appit.listit.General.AppConstants;
import com.appit.listit.Products.ItemClickListener;
import com.appit.listit.Products.Product;
import com.appit.listit.R;

/**
 * Created by אitay feldman on 31/12/2017.
 */

public class SubListAdapter extends RecyclerView.Adapter<SubListAdapter.SubListHolder> {

    private java.util.List<SubList> subListsList;
    private java.util.List<Product> productsList;
    private ItemClickListener clickListener;
    private Context context;

    public SubListAdapter (java.util.List<SubList> subListsList, java.util.List<Product> productsList, ItemClickListener clickListener, Context context/*, OnStartDragListener mDragStartListener*/){
        this.subListsList = subListsList;
        this.productsList = productsList;
        this.clickListener = clickListener;
        this.context = context;
    }

    @Override
    public SubListHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        return new SubListHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sublist_view, parent, false));

    }


    @Override
    public void onBindViewHolder(SubListHolder holder, final int position) {

        final SubList subList = getItem(position);
        holder.subListTitle.setText(subList.getSubListTitle());

        LinearLayout list = (LinearLayout) holder.productsLayout.findViewById(R.id.subList_products_layout);
        list.removeAllViews();

        for (int i = 0; i < productsList.size(); i++)
        {
            if (productsList.get(i).getCategorytId().equals(String.valueOf(subListsList.get(position).getCategoryId()))) {
                final int k = i;

                View line = list.inflate(context, R.layout.product_view, null);

                TextView productName = ((TextView) line.findViewById(R.id.productName));
                CheckBox checkBox = ((CheckBox) line.findViewById(R.id.done));
                ImageButton subQunBtn = ((ImageButton) line.findViewById(R.id.subQuantityBtn));
                TextView productQuntity = ((TextView) line.findViewById(R.id.productQuntity));
                ImageButton addQuantityBtn = ((ImageButton) line.findViewById(R.id.addQuantityBtn));

                final Product product = getProductItem(i);
                checkBox.setChecked(product.productIsDone());
                productName.setText(product.getProductName());
                productQuntity.setText(String.valueOf(product.getQuantity()));

                addQuantityBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickListener.onClick(v, AppConstants.ADD_PRODUCT_QUANTITY, k);
                    }
                });
                subQunBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickListener.onClick(v, AppConstants.SUB_PRODUCT_QUANTITY, k);
                    }
                });

                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickListener.onClick(v, AppConstants.CHECKBOX, k);
                    }
                });

                productName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickListener.onClick(v, AppConstants.EDIT_PRODUCT_CONST, k);
                    }
                });

                list.addView(line);
            }else continue;
        }

    }

    public SubList getItem(int i){
        return subListsList.get(i);
    }

    public Product getProductItem(int i){
        return productsList.get(i);
    }

    @Override
    public int getItemCount() {
        return subListsList != null ? subListsList.size() : 0;
    }

    public void updateList(SubList subList) {
        insertItem(subList);
    }

    public void insertItem(SubList subList) {
        this.subListsList.add(subList);
        notifyItemInserted(getItemCount());
        notifyItemRangeInserted(getItemCount(), subListsList.size());

    }

    public void removerItem(int position) {
        subListsList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeRemoved(position, subListsList.size());
    }

    public java.util.List<SubList> getList(){
        return this.subListsList;
    }

    public class SubListHolder extends RecyclerView.ViewHolder{

        public TextView subListTitle;
        public LinearLayout productsLayout;

        public SubListHolder(View itemView) {
            super(itemView);
            subListTitle = ((TextView) itemView.findViewById(R.id.subList_textView));
            productsLayout = ((LinearLayout) itemView.findViewById(R.id.subList_products_layout));
        }
    }
}


