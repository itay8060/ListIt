package com.appit.listit.Notes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.appit.listit.General.AppConstants;
import com.appit.listit.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoteEditActivity extends AppCompatActivity {

    @BindView(R.id.editnoteactivity_edittext)
    EditText editnoteactivityEdittext;
    @BindView(R.id.editnoteactivity_closebtn)
    Button editnoteactivityClosebtn;
    @BindView(R.id.editnoteactivity_confirmbtn)
    Button editnoteactivityConfirmbtn;
    @BindView(R.id.editnoteactivity_deletebtn)
    Button editnoteactivityDeletebtn;
    Note note;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.editnote_activity);
        Intent intent = getIntent();
        long id = intent.getLongExtra("myNote",0);
        note = Note.findById(Note.class, id);
        ButterKnife.bind(this);

        loadNoteData();
        setOnClickListeners();

    }

    private void loadNoteData(){
        if (note.getNoteTxt().length()>0) {
            editnoteactivityEdittext.setText(note.getNoteTxt());
        }else editnoteactivityEdittext.setText("");
    }

    private void setOnClickListeners() {
        editnoteactivityClosebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeNote();
            }
        });

        editnoteactivityConfirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptNote();
            }
        });

        editnoteactivityDeletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNote();
            }
        });
    }

    private void closeNote() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    private void acceptNote() {
        note.setNoteTxt(editnoteactivityEdittext.getText().toString());
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private void deleteNote() {
        note.delete();
        Intent returnIntent = new Intent();
        setResult(AppConstants.RESULT_DELETED, returnIntent);
        finish();
    }

    }
