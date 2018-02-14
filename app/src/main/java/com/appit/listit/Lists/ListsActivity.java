package com.appit.listit.Lists;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import com.appit.listit.DBPackage.ObjectsManager;
import com.appit.listit.FireBasePackage.FireBaseDataManager;
import com.appit.listit.General.AppConstants;
import com.appit.listit.General.MainActivity;
import com.appit.listit.General.PrefsManager;
import com.appit.listit.LoginPackage.LoginActivity;
import com.appit.listit.R;
import com.appit.listit.UIandViews.UIDialogsManager;
import com.appit.listit.Utilities.ItemClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by itay feldman on 12/02/2018.
 */

public class ListsActivity extends AppCompatActivity {

    @BindView(R.id.ultimate_recycler_view)
    RecyclerView listsRecyclerView;
    java.util.List<List> listsList = new ArrayList<>();
    UIDialogsManager dialogManager = new UIDialogsManager(this);
    ListsAdapter listsAdapter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private int listPos;
    private FirebaseAuth mAuth;
    private Drawer navDrawer;
    private ItemClickListener clickListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lists_activity);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();

        /*List testList = new List(FireBaseDataManager.getUser().getUid(), "List 2");
        testList.save();
        FireBaseDataManager.addFireBaseList(testList);*/

        activityInits();
    }

    private void activityInits() {
        InitToolBar();
        InitNavDrawer();
        initClickListeners();
        initList();
    }

    private void initList() {
        listsList = ObjectsManager.getUserListsList();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        listsRecyclerView.setLayoutManager(layoutManager);
        listsAdapter = new ListsAdapter(listsList, clickListener);
        listsRecyclerView.setAdapter(listsAdapter);
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove swiped item from list and notify the RecyclerViewL
                listPos = viewHolder.getAdapterPosition();

                dialogManager.showAlertDialog("Are you sure you want to delete the list?", "Yes", "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                deleteList();
                                dialog.dismiss();
                                //refreshList();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                refreshList();
                                break;
                        }
                    }
                });
                //refreshList();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(listsRecyclerView);
    }

    private void initClickListeners() {
        clickListener = new ItemClickListener() {
            @Override
            public void onClick(View v, int src, int position) {
                switch (src) {
                    case AppConstants.LIST_CLICKED:
                        PrefsManager.setCurrentList(listsList.get(position).getListOnlineId());
                        Intent i = new Intent(ListsActivity.this, MainActivity.class);
                        startActivity(i);
                        break;
                }
            }
        };
    }

    private void deleteList() {
        List list = listsList.get(listPos);
        list.delete();
    }

    public void refreshList() {
        listsList = ObjectsManager.getUserListsList();
        listsAdapter = new ListsAdapter(listsList, clickListener);
        listsRecyclerView.setAdapter(listsAdapter);
    }

    //region Nav Drawer Func
    private void InitNavDrawer() {

        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileDrawerItem().withName(FireBaseDataManager.getUser().getDisplayName()).withIcon(GoogleMaterial.Icon.gmd_account_circle)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        PrimaryDrawerItem itemListAc = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.drawer_item_default_list)
                .withIcon(GoogleMaterial.Icon.gmd_home);


        PrimaryDrawerItem allListsAc = new PrimaryDrawerItem().withIdentifier(5).withName(R.string.drawer_item_allLists)
                .withIcon(GoogleMaterial.Icon.gmd_list);

        PrimaryDrawerItem newListAc = new PrimaryDrawerItem().withIdentifier(6).withName(R.string.drawer_item_newList)
                .withIcon(GoogleMaterial.Icon.gmd_add_circle);

        SecondaryDrawerItem settingsDrawer = new SecondaryDrawerItem().withIdentifier(2).withName(R.string.drawer_item_settings)
                .withIcon(GoogleMaterial.Icon.gmd_settings);

        /*settingsDrawer.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                Intent settingIntent = new Intent(MainActivity.this, SettingActivity.class);
                startActivityForResult(settingIntent, BackupConstants.IMPORT_FLAG_RESULT);
                navDrawer.setSelection(1);
                return true;
            }
        });*/

        itemListAc.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                Intent i = new Intent(ListsActivity.this, MainActivity.class);
                startActivity(i);
                finish();
                return true;
            }
        });

        SecondaryDrawerItem contactUsDrawer = new SecondaryDrawerItem().withIdentifier(3).withName(R.string.drawer_item_contact)
                .withIcon(GoogleMaterial.Icon.gmd_mail);

        contactUsDrawer.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                sendEmail();
                return true;
            }
        });

        SecondaryDrawerItem logoutDrawer = new SecondaryDrawerItem().withIdentifier(4).withName(R.string.drawer_item_signout)
                .withIcon(GoogleMaterial.Icon.gmd_backspace);

        logoutDrawer.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                CreateAlertDialog();

                return true;
            }
        });

        navDrawer = new DrawerBuilder()
                .withAccountHeader(headerResult)
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        itemListAc,
                        newListAc,
                        allListsAc,
                        new DividerDrawerItem(),
                        settingsDrawer,
                        contactUsDrawer,
                        new DividerDrawerItem()
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        closeDrawer();
                        return true;
                    }
                })
                .addStickyDrawerItems(logoutDrawer)
                .build();

        navDrawer.setSelection(5);
    }

    private void closeDrawer() {
        navDrawer.closeDrawer();
    }

    private void signOut() {
        mAuth.signOut();
        Intent intent = new Intent(ListsActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void sendEmail() {

        String[] TO = {"appitstudio@gmail.com"};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.contact_subject));

        try {
            startActivity(Intent.createChooser(emailIntent, getString(R.string.select_email_message)));
        } catch (ActivityNotFoundException ex) {
            displayToast(getString(R.string.no_email_clients));
        }
    }
    //endregion Nav Drawer Func

    //region Alert Dialog Func
    public void CreateAlertDialog() {
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);

        alertbox.setTitle(getString(R.string.signout_dialoge_title));
        alertbox.setMessage(getString(R.string.signout_dialoge_message));

        alertbox.setNegativeButton(getString(R.string.signout_dialoge_cancel),
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {

                        //dismiss the dialog and will do nothing

                    }
                });


        alertbox.setPositiveButton(getString(R.string.signout_dialoge_ok),
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {

                        signOut();
                    }
                });
        alertbox.show();
    }
    //endregion Alert Dialog Func

    private void InitToolBar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("ListIt");
        }
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
    }

    @Override
    public void onBackPressed() {
        if (navDrawer.isDrawerOpen()) {
            navDrawer.closeDrawer();
        }
        else{
            finish();
        }
    }

    private void displayToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
