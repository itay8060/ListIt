package com.appit.listit.Products;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appit.listit.DBPackage.ObjectsManager;
import com.appit.listit.DBPackage.RelatedListProduct;
import com.appit.listit.FireBasePackage.FireBaseDataManager;
import com.appit.listit.General.AppConstants;
import com.appit.listit.R;
import com.appit.listit.Utilities.ItemClickListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.orm.SugarRecord.findWithQuery;

/**
 * Created by itay feldman on 13/12/2017.
 */

public class EditProductActivity extends AppCompatActivity {

    @BindView(R.id.editactivity_label_textview)
    TextView editactivityLabelTextview;
    @BindView(R.id.editactivity_addpicture_layout)
    RelativeLayout editactivityAddpictureLayout;
    @BindView(R.id.editactivity_picture_imageview)
    ImageView editactivityPictureImageview;
    @BindView(R.id.editactivity_note_edittext)
    EditText editactivityNoteEdittext;
    @BindView(R.id.editactivity_note_listview)
    ListView editactivityNoteListview;
    @BindView(R.id.editactivity_categoryBtn)
    Button editactivityCategoryBtn;
    ListView dialoglist;
    @BindView(R.id.editactivity_closebtn)
    RelativeLayout editactivityClosebtn;
    private List<Note> notesList;
    RelatedListProduct product;
    ItemClickListener clickListener;
    private String productId;
    private Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.editproduct_activity);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        productId = intent.getStringExtra(AppConstants.PRODUCT_ID);

        /*if (product.getProductImage()!=null){
            editactivityPictureImageview.setImageBitmap(product.getProductImage());
        }*/
        activityInits();
    }

    private void activityInits() {
        firstInitiation();
        refreshNotes();
        createDialog();
        initClickListeners();
    }

    private void firstInitiation() {
        product = ObjectsManager.getRelatedProductFromOnlineId(productId);
        //editactivityPictureImageview.setImageBitmap(product.getProductImage());
        String categoryName = ObjectsManager.getCategoryNameById(product.getCategoryId());
        editactivityCategoryBtn.setText(getResources().getString(R.string.editproduct_category_label) + " " + categoryName);
    }

    private void initClickListeners() {
        editactivityNoteListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openEditNoteActivity(position);
            }
        });

        editactivityCategoryBtn.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.show();
            }
        });

        editactivityClosebtn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                closeEditProduct();
            }
        });
    }

    private void checkPermissions(int src) {

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(EditProductActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        switch (src) {
            case AppConstants.GALLERY:
                TedPermission.with(this)
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .check();
                break;
            case AppConstants.CAMERA:
                TedPermission.with(this)
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(Manifest.permission.CAMERA)
                        .check();
                break;
        }
    }

    private void runClickListeners() {
        clickListener = new ItemClickListener() {
            @Override
            public void onClick(View v, int src, int i) {
                Integer location;
                switch (src) {
                    case AppConstants.EDIT_NOTE_CONST:
                        openEditNoteActivity(i);
                        break;
                }
            }
        };
    }

    //region Notes

    private void openEditNoteActivity(int i) {
        Intent intent = new Intent(EditProductActivity.this, NoteEditActivity.class);
        intent.putExtra(AppConstants.NOTE_ID, notesList.get(i).getNoteOnlineId());
        startActivityForResult(intent, AppConstants.NOTE);
    }

    private void refreshNotes() {
        notesList = Note.findWithQuery(Note.class, "Select * from Note where product_id = ?", productId);
        editactivityNoteListview.setAdapter(new NoteAdapter(this.notesList, this, clickListener));
    }

    public void addNote(View v) {
        if (editactivityNoteEdittext.getText().toString().equals("")){
            Toast.makeText(this, getString(R.string.enter_note_toast), Toast.LENGTH_LONG).show();
        }
        else {
            List<com.appit.listit.Lists.List> listsListTemp = new ArrayList();
            listsListTemp = findWithQuery(com.appit.listit.Lists.List.class, "Select * from List where list_online_id = ?", product.getRelatedListId());
            com.appit.listit.Lists.List tempList = listsListTemp.get(0);
            Note note = new Note(editactivityNoteEdittext.getText().toString(), tempList.getUserId(), product.getRelatedProductOnlineId());
            note.save();
            FireBaseDataManager.addFireBaseNote(note, productId, product.getRelatedListId());
            editactivityNoteEdittext.setText("");
            refreshNotes();
        }
    }

    //endregion Notes

    public void closeEditProduct() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    //region Image funcs

    public void fromGallery(View v) {
        checkPermissions(AppConstants.GALLERY);
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, AppConstants.GALLERY);
    }

    public void fromCamera(View v) {
        checkPermissions(AppConstants.CAMERA);
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, AppConstants.CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent i) {
        super.onActivityResult(requestCode, resultCode, i);
        Bitmap bitmap = null;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case AppConstants.GALLERY:
                    Uri imageUri = i.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //ObjectsManager.getProductFromOnlineId(productId).setProductImage(bitmap);
                    editactivityPictureImageview.setImageBitmap(bitmap);
                    break;
                case AppConstants.CAMERA:
                    bitmap = (Bitmap) i.getExtras().get("data");
                    //ObjectsManager.getProductFromOnlineId(productId).setProductImage(bitmap);
                    product.save();
                    editactivityPictureImageview.setImageBitmap(bitmap);
                    break;
                case AppConstants.NOTE:
                    refreshNotes();
                    Log.e("f", "Refreshed");
                    Toast.makeText(this, "Note updated", Toast.LENGTH_LONG);
                    break;
            }
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("f", "Canceled");
            Toast.makeText(this, "No changes were made", Toast.LENGTH_LONG);
        }
        if (resultCode == AppConstants.RESULT_DELETED) {
            refreshNotes();
            Log.e("f", "Deleted");
            Toast.makeText(this, "Note deleted", Toast.LENGTH_LONG);
        }
    }
    //endregion Image funcs

    //region Dialog funcs

    protected void createDialog() {
        dialog = new Dialog(EditProductActivity.this);
        dialog.setContentView(R.layout.category_dialoglayout);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        //Prepare ListView in dialog
        dialoglist = (ListView) dialog.findViewById(R.id.dialoglist);
        ArrayList<String> listContent = ObjectsManager.getCategoriesAsStrings();
        ArrayAdapter<String> adapter
                = new ArrayAdapter<String>(this,
                R.layout.category_view, listContent);
        dialoglist.setAdapter(adapter);
        dialoglist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(EditProductActivity.this,
                        getResources().getString(R.string.editproduct_category_toast) + " " + parent.getItemAtPosition(position).toString(),
                        Toast.LENGTH_LONG).show();
                ObjectsManager.setNewCategoryToProduct(productId, parent.getItemAtPosition(position).toString());
                editactivityCategoryBtn.setText(getResources().getString(R.string.editproduct_category_label) + " " + parent.getItemAtPosition(position).toString());
                dialog.cancel();
            }
        });
        //return dialog;
    }

    //endregion Dialog funcs

}

