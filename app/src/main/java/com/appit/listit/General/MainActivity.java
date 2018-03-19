package com.appit.listit.General;

import android.content.ActivityNotFoundException;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appit.listit.DBPackage.ObjectsManager;
import com.appit.listit.DBPackage.ProductsManager;
import com.appit.listit.DBPackage.RelatedListProduct;
import com.appit.listit.FireBasePackage.FireBaseDataManager;
import com.appit.listit.ListItApplication;
import com.appit.listit.Lists.List;
import com.appit.listit.Lists.ListsActivity;
import com.appit.listit.Lists.SubList;
import com.appit.listit.Lists.SubListAdapter;
import com.appit.listit.LoginPackage.LoginActivity;
import com.appit.listit.Products.Category;
import com.appit.listit.Products.EditProductActivity;
import com.appit.listit.Products.Product;
import com.appit.listit.R;
import com.appit.listit.UIandViews.CustomAutoCompleteView;
import com.appit.listit.UIandViews.UIDialogsManager;
import com.appit.listit.Utilities.AutoCompleteAdapter;
import com.appit.listit.Utilities.CustomAutoCompleteTextChangedListener;
import com.appit.listit.Utilities.ItemClickListener;
import com.appit.listit.Utilities.SubListArrayMaker;
import com.appolica.flubber.Flubber;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.orm.SugarRecord.findWithQuery;

/**
 * Created by itay feldman on 03/12/2017.
 */

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.mainactivity_list_name)
    TextView mainactivityListName;
    @BindView(R.id.newProduct_editText)
    public CustomAutoCompleteView newProductEditText;
    @BindView(R.id.productsList)
    RecyclerView pRecyclerView;
    @BindView(R.id.clearCheckedBtn)
    Button clearCheckedBtn;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.mainactivity_share)
    TextView mainactivityShare;

    //region Variables initiation
    private List list = new List();
    private java.util.List<SubList> subListList = new ArrayList<>();
    private java.util.List<RelatedListProduct> productsList = new ArrayList<>();
    private java.util.List<Product> dbPoductsList = new ArrayList<>();
    private java.util.List<Category> categoriesList = new ArrayList<>();
    private java.util.List<List> listsList = new ArrayList<>();
    private ItemClickListener clickListener;
    private RelatedListProduct currentProduct;
    private Drawer navDrawer;
    private SubListAdapter adapter;
    private FirebaseAuth mAuth;
    public AutoCompleteAdapter autoCompleteAdapter;
    private DatabaseReference databaseProducts;
    public String chosenProductName;
    private int pressedTimes;
    UIDialogsManager dialogManager = new UIDialogsManager(this);
    public static Boolean choseFromSuggestedList = false;
    //endregion Variables initiation


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        activityInits();
        databaseProducts = FirebaseDatabase.getInstance().getReference("products").child(list.getListOnlineId());
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshProducts();
    }

    private void activityInits() {
        initPrefs();
        initData();
        InitToolBar();
        initClickListeners();
        InitNavDrawer();
        initAutoCompleteSearch();
        refreshProducts();
    }

    @Override
    public void onBackPressed() {
        if (navDrawer.isDrawerOpen()) {
            navDrawer.closeDrawer();
        }else{
            if (pressedTimes == 1) {
                pressedTimes = 0;
                finish();
            }else{
                if (pressedTimes == 0) {
                    displayToast(getString(R.string.pressagain_toast));
                    pressedTimes = 1;
                }
            }
        }
    }

    private void initPrefs() {
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
    }

    private void initLists() {
        /*SnapHelper snapHelper = new GravitySnapHelper(Gravity.TOP);
        pRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        snapHelper.attachToRecyclerView(pRecyclerView);*/
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        pRecyclerView.setLayoutManager(layoutManager);
        Flubber.with()
                .animation(Flubber.AnimationPreset.FADE_IN_DOWN)
                .repeatCount(1)
                .duration(1000)
                .createFor(pRecyclerView)
                .start();
        mainactivityListName.setText(list.getListName());
        subListList = SubListArrayMaker.createArray(productsList, categoriesList);
        adapter = new SubListAdapter(subListList, productsList, clickListener, this);
    }

    //region Products Funcs

    public void initAutoCompleteSearch() {

        newProductEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {

                RelativeLayout rl = (RelativeLayout) arg1;
                TextView tv = (TextView) rl.getChildAt(0);
                newProductEditText.setText(tv.getText().toString());
                chosenProductName = tv.getText().toString();
                choseFromSuggestedList = true;
            }
        });
        // add the listener so it will try to suggest while the user types
        newProductEditText.addTextChangedListener(new CustomAutoCompleteTextChangedListener(this));
        java.util.List<Product> ObjectProductData = new ArrayList<>();

        // set the custom ArrayAdapter
        autoCompleteAdapter = new AutoCompleteAdapter(this, R.layout.autocomplete_list_row, ObjectProductData);
        newProductEditText.setAdapter(autoCompleteAdapter);

    }


    public void initData() {
        //inits categories if they do not exist in DB
        categoriesList = ObjectsManager.getCategoryList();
        //inits DB products
        ProductsManager.initiateProducts();
        //gets the user's selected/last list
        list = ObjectsManager.getUserList();
        //list = FireBaseDataManager.getOnlineListData();
        //gets the products related to the list it pulled
        productsList = ObjectsManager.getRelatedProductsList(list.getListOnlineId());
        //productsListOnlinetest = FireBaseDataManager.getOnlineProductsList(list.getListOnlineId());
        //refreshProducts();
        initLists();
    }

    private void refreshProducts() {
        //categoriesList = Category.listAll(Category.class);
        //productsList = Product.listAll(Product.class);
        categoriesList = ObjectsManager.getCategoryList();
        list = ObjectsManager.getUserList();
        productsList = ObjectsManager.getRelatedProductsList(list.getListOnlineId());
        //productsList = FireBaseDataManager.getOnlineProductsList(list.getListOnlineId());
        subListList = SubListArrayMaker.createArray(productsList, categoriesList);
        pRecyclerView.setAdapter(new SubListAdapter(subListList, productsList, clickListener, this));
    }

    public void addProduct(View v) {
        if (newProductEditText.getText().toString().equals("")) {
            //if edtitext is empty
            displayToast(getString(R.string.enter_product_name_toast));
        } else {
            if (choseFromSuggestedList) {
                //if its chosen from the products DB suggested list - gets the product
                Product tProduct = ObjectsManager.getProductFromName(chosenProductName);
                //checks if product allready exists in user's list
                if (ObjectsManager.getRelatedProduct(list.getListOnlineId(), tProduct.getProductOnlineId()) != 0) {
                    //if yes - adds 1 to the existing product
                    RelatedListProduct trlProduct = RelatedListProduct.findById(RelatedListProduct.class, ObjectsManager.getRelatedProduct(list.getListOnlineId(), tProduct.getProductOnlineId()));
                    trlProduct.increaseQuantity();
                    displayToast(getString(R.string.adding_product_toexisting));
                    newProductEditText.setText("");
                    refreshProducts();
                } else {
                    //if no - creates the product
                    RelatedListProduct nrlp = new RelatedListProduct(list.getListOnlineId(), tProduct.getProductOnlineId()+list.getListOnlineId());
                    nrlp.save();
                    Log.e("Saved relatedproduct id", nrlp.getRelatedProductOnlineId());
                    choseFromSuggestedList = false;
                    newProductEditText.setText("");
                    refreshProducts();
                }

            } else {
                //if not chosen from the products DB list but typed in - checks if the typed in product exists in DB
                if (ObjectsManager.productExists(newProductEditText.getText().toString())) {
                    //if yes - gets the product
                    Product tProduct = ObjectsManager.getProductFromName(newProductEditText.getText().toString());
                    //checks if product allready exists in list
                    if (ObjectsManager.getRelatedProduct(list.getListOnlineId(), tProduct.getProductOnlineId()) != 0) {
                        //if yes - adds 1 to the existing product
                        RelatedListProduct trlProduct = RelatedListProduct.findById(RelatedListProduct.class, ObjectsManager.getRelatedProduct(list.getListOnlineId(), tProduct.getProductOnlineId()));
                        trlProduct.increaseQuantity();
                        displayToast(getString(R.string.adding_product_toexisting));
                        newProductEditText.setText("");
                        refreshProducts();
                    } else {
                        //if no - creates the product
                        RelatedListProduct nrlp = new RelatedListProduct(list.getListOnlineId(), tProduct.getProductOnlineId() + list.getListOnlineId());
                        nrlp.save();
                        newProductEditText.setText("");
                        refreshProducts();
                    }
                } else {
                    //if product was not chosen from DB suggested list and does not exist in DB - adds new product to DB for future use
                    Product product = new Product(newProductEditText.getText().toString(), ObjectsManager.getCategoryByName(ListItApplication.getContext().getResources().getString(R.string.category_others)).getCategoryOnlineId());
                    product.save();
                    FireBaseDataManager.addFireBaseProduct(product, list.getListOnlineId());
                    //adds product to user's list
                    RelatedListProduct rLProduct = new RelatedListProduct(list.getListOnlineId(), product.getProductOnlineId());
                    rLProduct.save();
                    newProductEditText.setText("");
                    refreshProducts();
                }
            }
        }
    }

    public void clearBtn(View v) {
        refreshProducts();
        //checked which products are checked and deletes them
        java.util.List<RelatedListProduct> productsListChecked = new ArrayList();
        for (int i = 0; i < productsList.size(); i++) {
            if (productsList.get(i).productIsDone()) {
                productsListChecked = findWithQuery(RelatedListProduct.class, "Select * from RELATED_LIST_PRODUCT where related_product_online_id = ?", productsList.get(i).getRelatedProductOnlineId());
                RelatedListProduct product = productsListChecked.get(0);
                productsList.remove(i);
                product.delete();
                i--;
            }
        }
        refreshProducts();
    }

    public void showEditProductActivity(String id) {
        Intent intent = new Intent(MainActivity.this, EditProductActivity.class);
        intent.putExtra(AppConstants.PRODUCT_ID, id);
        startActivityForResult(intent, AppConstants.EDIT_CATEGORY);
    }

    public void initClickListeners() {
        clickListener = new ItemClickListener() {
            @Override
            public void onClick(View v, int src, int i) {
                switch (src) {
                    case AppConstants.ADD_PRODUCT_QUANTITY:
                        currentProduct = ObjectsManager.getRelatedProductFromOnlineId(productsList.get(i).getRelatedProductOnlineId());
                        currentProduct.increaseQuantity();
                        refreshProducts();
                        break;
                    case AppConstants.SUB_PRODUCT_QUANTITY:
                        currentProduct = ObjectsManager.getRelatedProductFromOnlineId(productsList.get(i).getRelatedProductOnlineId());
                        currentProduct.decreaseQuantity();
                        refreshProducts();
                        break;
                    case AppConstants.EDIT_PRODUCT_CONST:
                        showEditProductActivity(productsList.get(i).getRelatedProductOnlineId());
                        break;
                    case AppConstants.CHECKBOX:
                        currentProduct = ObjectsManager.getRelatedProductFromOnlineId(productsList.get(i).getRelatedProductOnlineId());
                        currentProduct.productToggleChecked();
                        break;
                }
            }
        };
        mainactivityShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareList();
            }
        });
    }

    //endregion Products Funcs

    //region Nav Drawer Func
    private void InitNavDrawer() {

        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileDrawerItem().withName(mAuth.getCurrentUser().getDisplayName()).withIcon(GoogleMaterial.Icon.gmd_account_circle)
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

        newListAc.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                addList();
                return true;
            }
        });

        allListsAc.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                Intent i = new Intent(MainActivity.this, ListsActivity.class);
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

        navDrawer.setSelection(1);
    }

    private void addList() {
        final EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);

        dialogManager.showAlertDialogWithEditText("Enter list name", "Create", "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        ObjectsManager.addNewList(input.getText().toString());
                        dialog.dismiss();
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        }, input);
    }

    private void closeDrawer() {
        navDrawer.closeDrawer();
    }

    private void signOut() {
        mAuth.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
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


    //region Share list
    private void ShareList() {
        String sharedList = "";
        String stringList = "";
        String tempList = "";
        productsList = ObjectsManager.getRelatedProductsList(list.getListOnlineId());
        subListList = SubListArrayMaker.createArray(productsList, categoriesList);
        for (int i = 0; i < subListList.size(); i++) {
            stringList = stringList + subListList.get(i).getSubListTitle() + ":\n";
            for (int j = 0; j < productsList.size(); j++) {
                if (productsList.get(j).getCategoryId().equals(subListList.get(i).getCategoryId())) {
                    tempList = tempList + productsList.get(j).getProductName() + " - " + productsList.get(j).getQuantity() + "\n";
                }
            }
            stringList = stringList + tempList;
            tempList = "";
        }
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            sharedList = getString(R.string.share_list_msg1) + "\n\n";
            sharedList = sharedList + stringList;
            sharedList = sharedList + "\n" + getString(R.string.share_list_msg2);
            i.putExtra(Intent.EXTRA_TEXT, sharedList);
            startActivity(Intent.createChooser(i, getString(R.string.pick_share_option)));
        } catch (Exception e) {
            //e.toString();
        }
    }
    //endregion Share list

    private void displayToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}

