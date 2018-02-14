package com.appit.listit.Lists;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appit.listit.General.AppConstants;
import com.appit.listit.R;
import com.appit.listit.Utilities.ItemClickListener;

import java.util.ArrayList;

/**
 * Created by itay feldman on 13/02/2018.
 */

public class ListsAdapter extends RecyclerView.Adapter<ListsAdapter.ListsHolder> {

    java.util.List<List> listsList = new ArrayList<>();
    ItemClickListener clickListener;

    public ListsAdapter(java.util.List<List> listsList, ItemClickListener clickListener) {
        this.listsList = listsList;
        this.clickListener = clickListener;
    }


    @Override
    public ListsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ListsHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_view, parent, false));
    }

    @Override
    public void onBindViewHolder(ListsHolder holder, final int position) {
        final List list = getItem(position);
        holder.listName.setText(list.getListName());
        holder.listDate.setText("Created date: " + list.getListCreateDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(v, AppConstants.LIST_CLICKED, position);
            }
        });
    }

    public List getItem(int i){
        return listsList.get(i);
    }

    @Override
    public int getItemCount() {
        return listsList != null ? listsList.size() : 0;
    }

    public class ListsHolder extends RecyclerView.ViewHolder{

        public TextView listName;
        public TextView listDate;

        public ListsHolder(View itemView) {
            super(itemView);
            listName = ((TextView) itemView.findViewById(R.id.listName));
            listDate = ((TextView) itemView.findViewById(R.id.listDate));
        }
    }
}


