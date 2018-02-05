package com.appit.listit.Products;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appit.listit.Utilities.ItemClickListener;
import com.appit.listit.R;

import java.util.List;

/**
 * Created by itay feldman on 13/12/2017.
 */

public class NoteAdapter extends BaseAdapter {

    List<Note> noteList;
    final Context context;
    private ItemClickListener clickListener;

    public NoteAdapter(List<Note> notesList, Context context, ItemClickListener clickListener){
        this.noteList = notesList;
        this.context = context;
        this.clickListener = clickListener;
    }

    @Override
    public int getCount() {
        return noteList.size();
    }

    @Override
    public Note getItem(int position) {
        return noteList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int i, View rView, ViewGroup parent) {
        if(rView==null){
            rView= LayoutInflater.from(context).inflate(R.layout.note_view, parent ,false);
        }
        final Note note = getItem(i);
        TextView noteTxt = ((TextView)rView.findViewById(R.id.note_TxtView));
        final RelativeLayout noteLayout = ((RelativeLayout)rView.findViewById(R.id.note_layout));

        noteTxt.setText(note.getNoteTxt());

        /*rView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(v, AppConstants.EDIT_NOTE_CONST, i);
            }
        });*/

        return rView;
    }
}
