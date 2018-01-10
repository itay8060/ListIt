package com.appit.listit.GroceryList;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appit.listit.DBPackage.ObjectsManager;
import com.appit.listit.FireBasePackage.FireBaseDataManager;
import com.appit.listit.General.AppConstants;
import com.appit.listit.General.Category;
import com.appit.listit.Helpers.SubListArrayMaker;
import com.appit.listit.Lists.List;
import com.appit.listit.Lists.SubList;
import com.appit.listit.Lists.SubListAdapter;
import com.appit.listit.Products.EditProductActivity;
import com.appit.listit.Products.ItemClickListener;
import com.appit.listit.Products.Product;
import com.appit.listit.R;
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
    EditText newProductEditText;
    @BindView(R.id.productsList)
    RecyclerView pRecyclerView;
    @BindView(R.id.clearCheckedBtn)
    Button clearCheckedBtn;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    //region Variables initiation
    private List list = new List();
    private java.util.List<SubList> subListList = new ArrayList<>();
    private java.util.List<Product> productsList = new ArrayList<>();
    private java.util.List<Category> categoriesList = new ArrayList<>();
    private java.util.List<List> listsList = new ArrayList<>();
    private ItemClickListener clickListener;
    private Product currentProduct;
    private Drawer navDrawer;
    private SubListAdapter adapter;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseProducts;
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
        initDemoData();
        initLists();
        InitToolBar();
        initClickListeners();
        refreshProducts();
        InitNavDrawer();
    }

    private void initLists() {
        /*SnapHelper snapHelper = new GravitySnapHelper(Gravity.TOP);
        pRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        snapHelper.attachToRecyclerView(pRecyclerView);*/
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        pRecyclerView.setLayoutManager(layoutManager);
        Log.e("initlist user list id", list.getListOnlineId());
        mainactivityListName.setText(list.getListName());
        subListList = SubListArrayMaker.createArray(productsList,categoriesList);
        adapter = new SubListAdapter(subListList, productsList, clickListener, this);
    }

    //region Products Funcs

    public void addProduct(View v) {
        if(newProductEditText.getText().toString().equals("")) {
            Toast.makeText(this, "Enter product name", Toast.LENGTH_LONG).show();
        }else {Product product = new Product(newProductEditText.getText().toString(), list.getListOnlineId(), categoriesList.get(0).getId().toString());
        product.save();
        FireBaseDataManager.addFireBaseProduct(product, list.getListOnlineId());
        newProductEditText.setText("");
        refreshProducts();
        }
    }

    private void refreshProducts() {
        //categoriesList = Category.listAll(Category.class);
        //productsList = Product.listAll(Product.class);
        categoriesList = ObjectsManager.getCategoryList();
        productsList = ObjectsManager.getProductsList(list.getListOnlineId());
        subListList = SubListArrayMaker.createArray(productsList,categoriesList);
        pRecyclerView.setAdapter(new SubListAdapter(subListList, productsList, clickListener, this));
    }

    public void clearBtn(View v) {
        refreshProducts();
        java.util.List<Product> productsListTemp = new ArrayList();
        for (int i = 0; i < productsList.size(); i++) {
            if (productsList.get(i).productIsDone()) {
                productsListTemp = findWithQuery(Product.class, "Select * from Product where product_online_id = ?", productsList.get(i).getProductOnlineId());
                Product product = productsListTemp.get(0);
                productsList.remove(i);
                product.delete();
                i--;
            }
        }
        refreshProducts();
        //saveProductsData();
    }

    public void showEditProductActivity(String id) {
        Intent intent = new Intent(MainActivity.this, EditProductActivity.class);
        intent.putExtra("productId", id);
        startActivityForResult(intent, AppConstants.EDIT_CATEGORY);
    }

    public void initDemoData(){
        categoriesList = Category.listAll(Category.class);
        //Category.deleteAll(Category.class);
        initCategoryDemos();

        //List.deleteAll(List.class);
        initListsDemos();

        //Product.deleteAll(Product.class);
        initProductsDemos();

        //refreshProducts();
    }

    private void initProductsDemos() {
        productsList = ObjectsManager.getProductsList(list.getListOnlineId());
        Log.e("demo products list", productsList.toString());
    }

    private void initListsDemos() {
        list = ObjectsManager.getUserList();
    }

    private void initCategoryDemos() {
        categoriesList = ObjectsManager.getCategoryList();
        Log.e("demo category list", categoriesList.toString());
    }

    public void initClickListeners() {
        clickListener = new ItemClickListener() {
            @Override
            public void onClick(View v, int src, int i) {
                java.util.List<Product> productsListTemp = new ArrayList();
                switch (src) {
                    case AppConstants.ADD_PRODUCT_QUANTITY:
                        productsListTemp = findWithQuery(Product.class, "Select * from Product where product_online_id = ?", productsList.get(i).getProductOnlineId());
                        currentProduct = productsListTemp.get(0);
                        currentProduct.increaseQuantity();
                        refreshProducts();
                        break;
                    case AppConstants.SUB_PRODUCT_QUANTITY:
                        productsListTemp = findWithQuery(Product.class, "Select * from Product where product_online_id = ?", productsList.get(i).getProductOnlineId());
                        currentProduct = productsListTemp.get(0);
                        currentProduct.decreaseQuantity();
                        refreshProducts();
                        break;
                    case AppConstants.EDIT_PRODUCT_CONST:
                        showEditProductActivity(productsList.get(i).getProductOnlineId());
                        refreshProducts();
                        break;
                    case AppConstants.CHECKBOX:
                        productsListTemp = findWithQuery(Product.class, "Select * from Product where product_online_id = ?", productsList.get(i).getProductOnlineId());
                        currentProduct = productsListTemp.get(0);
                        currentProduct.productToggleChecked();
                        currentProduct.save();
                        break;
                }
            }
        };
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


        PrimaryDrawerItem itemListAc = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.drawer_item_list_ac)
                .withIcon(GoogleMaterial.Icon.gmd_home);

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
                        new DividerDrawerItem(),
                        settingsDrawer,
                        contactUsDrawer,
                        new DividerDrawerItem(),
                        logoutDrawer
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        closeDrawer();
                        return true;
                    }
                })
                .build();

        navDrawer.setSelection(1);
    }

    private void closeDrawer() {
        navDrawer.closeDrawer();
    }

    private void signOut(){
        mAuth.signOut();
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
            Toast.makeText(MainActivity.this, getString(R.string.no_email_clients), Toast.LENGTH_SHORT).show();
        }
    }
    //endregion Nav Drawer Func

    //region Alert Dialog Func
    public void CreateAlertDialog() {
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);

        alertbox.setTitle("Sign Out");
        alertbox.setMessage("Are you sure you want to sign out?");

        alertbox.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {

            // will automatically dismiss the dialog and will do nothing
                    }
                });


        alertbox.setPositiveButton("OK",
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

}

