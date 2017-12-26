package com.appit.listit.Products;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appit.listit.General.AppConstants;
import com.appit.listit.Notes.Note;
import com.appit.listit.Notes.NoteAdapter;
import com.appit.listit.Notes.NoteEditActivity;
import com.appit.listit.R;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.orm.SugarRecord.findWithQuery;

/**
 * Created by איתי פלדמן on 13/12/2017.
 */

public class EditProductActivity extends AppCompatActivity {

    @BindView(R.id.editactivity_addpid_textview)
    TextView editactivityAddpidTextview;
    @BindView(R.id.editactivity_closebtn)
    Button editactivityClosebtn;
    @BindView(R.id.editactivity_addpicture_layout)
    RelativeLayout editactivityAddpictureLayout;
    @BindView(R.id.editactivity_picture_imageview)
    ImageView editactivityPictureImageview;
    @BindView(R.id.editactivity_note_edittext)
    EditText editactivityNoteEdittext;
    @BindView(R.id.editactivity_note_listview)
    ListView editactivityNoteListview;
    private List<Note> notesList;
    private NoteAdapter adapter;
    Product product;
    ItemClickListener clickListener;
    private long id;
    private Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.editproduct_activity);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        id = intent.getLongExtra("productId", 0);
        notesList = findWithQuery(Note.class, "Select * from Note where product_id = ?", String.valueOf(id));
        adapter = new NoteAdapter(notesList, this, clickListener);
        editactivityNoteListview.setAdapter(adapter);
        product = Product.findById(Product.class, id);
        dialog = new Dialog(this);
        /*if (product.getProductImage()!=null){
            editactivityPictureImageview.setImageBitmap(product.getProductImage());
        }*/

        //runDemos();
        refreshNotes();
        //loadNotesData();
        //initClickListeners();

        editactivityNoteListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openEditNoteActivity(position);
            }
        });

        editactivityClosebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeEditProduct();
            }
        });

    }

    private void checkPermissions() {

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(EditProductActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(EditProductActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

        /*switch (src){
            case AppConstants.GALLERY:
                TedPermission.with(this)
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,)
                        .check();
                break;
            case AppConstants.CAMERA:
                TedPermission.with(this)
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(Manifest.permission.CAMERA)
                        .check();
                break;
        }*/
    }

    private void runClickListeners() {
        clickListener = new ItemClickListener() {
            @Override
            public void onClick(View v, int src, int i) {
                Integer location;
                switch (src){
                    case AppConstants.EDIT_NOTE_CONST:
                        openEditNoteActivity(i);
                    break;
                }
            }
        };
    }

    private void openEditNoteActivity(int i) {
        Intent intent = new Intent(EditProductActivity.this, NoteEditActivity.class);
        intent.putExtra("myNote", notesList.get(i).getId());
        startActivityForResult(intent,AppConstants.NOTE);
    }

    private void saveNotesData() {
        //need DB first
        editactivityNoteListview.setAdapter(new NoteAdapter(this.notesList, this, clickListener));
    }

    private void loadNotesData() {
        //need DB first

        editactivityNoteListview.setAdapter(new NoteAdapter(this.notesList, this, clickListener));
    }

    private void refreshNotes() {
        notesList = Note.findWithQuery(Note.class, "Select * from Note where product_id = ?", String.valueOf(id));
        editactivityNoteListview.setAdapter(new NoteAdapter(this.notesList, this, clickListener));
    }

    public void addNote(View v) {
        com.appit.listit.Lists.List tempList = com.appit.listit.Lists.List.findById(com.appit.listit.Lists.List.class, product.getListId());
        Note note = new Note(editactivityNoteEdittext.getText().toString(), tempList.getUserName(), product.getId());
        note.save();
        editactivityNoteEdittext.setText("");
        //saveNotesData();
        refreshNotes();
    }

    public void closeEditProduct(){
        finish();
    }

    public void fromGallery(View v){
        checkPermissions();
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, AppConstants.GALLERY);
    }

    public void fromCamera(View v){
        checkPermissions();
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, AppConstants.CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent i) {
        super.onActivityResult(requestCode, resultCode, i);
        if(resultCode==RESULT_OK){
            switch(requestCode){
                case AppConstants.GALLERY:
                    editactivityPictureImageview.setImageURI(i.getData());
                    break;
                case AppConstants.CAMERA:
                    Bitmap bm;
                    bm = (Bitmap)i.getExtras().get("data");
                    editactivityPictureImageview.setImageBitmap(bm);
                    break;
                case AppConstants.NOTE:
                    refreshNotes();
                    Log.e("f","Refreshed");
                    Toast.makeText(this, "Note updated", Toast.LENGTH_LONG);
                    break;
            }
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("f","Canceled");
            Toast.makeText(this, "No changes were made", Toast.LENGTH_LONG);
        }
        if (resultCode == AppConstants.RESULT_DELETED) {
            refreshNotes();
            Log.e("f","Deleted");
            Toast.makeText(this, "Note deleted", Toast.LENGTH_LONG);
        }
    }
}
