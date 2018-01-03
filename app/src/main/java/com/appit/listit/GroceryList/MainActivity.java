package com.appit.listit.GroceryList;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appit.listit.General.AppConstants;
import com.appit.listit.General.Category;
import com.appit.listit.Helpers.GravitySnapHelper;
import com.appit.listit.Helpers.SubListArrayMaker;
import com.appit.listit.Lists.List;
import com.appit.listit.Lists.SubList;
import com.appit.listit.Lists.SubListAdapter;
import com.appit.listit.Notes.Note;
import com.appit.listit.Products.EditProductActivity;
import com.appit.listit.Products.ItemClickListener;
import com.appit.listit.Products.Product;
import com.appit.listit.R;
import com.appit.listit.Users.User;
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

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.mainactivity_welcomelabel)
    TextView mainactivityWelcomelabel;
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
    private List list = new List();
    private java.util.List<SubList> subListList = new ArrayList<>();
    private java.util.List<Product> productsList = new ArrayList<>();
    private java.util.List<Category> categoriesList = new ArrayList<>();
    private ItemClickListener clickListener;
    private User currentUser;
    private Product currentProduct;
    private Drawer navDrawer;
    private SubListAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
        Intent i = getIntent();
        currentUser = User.findById(User.class, i.getLongExtra("UserId", 0));
        activityInits();
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
        SnapHelper snapHelper = new GravitySnapHelper(Gravity.TOP);
        pRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        snapHelper.attachToRecyclerView(pRecyclerView);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        pRecyclerView.setLayoutManager(layoutManager);
        categoriesList = Category.listAll(Category.class);
        productsList = Product.listAll(Product.class);
        subListList = SubListArrayMaker.createArray(productsList,categoriesList);
        adapter = new SubListAdapter(subListList, productsList, clickListener, this);
        pRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    public void addProduct(View v) {
        Product product = new Product(newProductEditText.getText().toString(), list.getId(), categoriesList.get(0).getId());
        product.save();
        newProductEditText.setText("");
        refreshProducts();
    }

    private void refreshProducts() {
        categoriesList = Category.listAll(Category.class);
        productsList = Product.listAll(Product.class);
        subListList = SubListArrayMaker.createArray(productsList,categoriesList);
        pRecyclerView.setAdapter(new SubListAdapter(subListList, productsList, clickListener, this));
    }

    public void clearBtn(View v) {
        for (int i = 0; i < productsList.size(); i++) {
            if (productsList.get(i).productIsDone()) {
                Product product = Product.findById(Product.class, productsList.get(i).getId());
                productsList.remove(i);
                product.delete();
                i--;
            }
        }
        refreshProducts();
        //saveProductsData();
    }

    public void initDemoData() {

        if (categoriesList.size()!= 4){
            Category.deleteAll(Category.class);
            categoriesList = new ArrayList<Category>();
            Category category1 = new Category("Others");
            category1.save();
            categoriesList.add(category1);
            Category category2 = new Category("Meat");
            category2.save();
            categoriesList.add(category2);
            Category category3 = new Category("Milk");
            category3.save();
            categoriesList.add(category3);
            Category category4 = new Category("Fruits");
            category4.save();
            categoriesList.add(category4);
        }
        if (list.getListName() == null) {
            List.deleteAll(List.class);
            list.setListName("My Frist GroceryListsList");
            list.setUserName(currentUser.getUserName());
            list.save();
        }
        if (productsList.size() == 0) {
            Product.deleteAll(Product.class);
            productsList = new ArrayList<Product>();
            Product product1 = new Product("Chicken breast", list.getId(), categoriesList.get(1).getId());
            productsList.add(product1);
            product1.save();
            Product product2 = new Product("Ice cream", list.getId(), categoriesList.get(2).getId());
            productsList.add(product2);
            product2.save();
            Product product3 = new Product("Oranges", list.getId(), categoriesList.get(3).getId());
            productsList.add(product3);
            product3.save();
            Note.deleteAll(Note.class);
            Note note1 = new Note("ERGENT!", currentUser.getUserName(), product2.getId());
            note1.save();
            Note note2 = new Note("We're all outt!", currentUser.getUserName(), product1.getId());
            note2.save();
            Note note3 = new Note("Anyone getting?!", currentUser.getUserName(), product1.getId());
            note3.save();
            refreshProducts();
        }
        refreshProducts();
    }

    public void initClickListeners() {
        clickListener = new ItemClickListener() {
            @Override
            public void onClick(View v, int src, int i) {
                switch (src) {
                    case AppConstants.ADD_PRODUCT_QUANTITY:
                        currentProduct = Product.findById(Product.class, productsList.get(i).getId());
                        currentProduct.increaseQuantity();
                        refreshProducts();
                        break;
                    case AppConstants.SUB_PRODUCT_QUANTITY:
                        currentProduct = Product.findById(Product.class, productsList.get(i).getId());
                        currentProduct.decreaseQuantity();
                        refreshProducts();
                        break;
                    case AppConstants.EDIT_PRODUCT_CONST:
                        showEditProductActivity(productsList.get(i).getId());
                        break;
                    case AppConstants.CHECKBOX:
                        currentProduct = Product.findById(Product.class, productsList.get(i).getId());
                        currentProduct.productToggleChecked();
                        refreshProducts();
                        break;
                }
            }
        };
    }

    public void showEditProductActivity(Long id) {
        Log.i("msg", "got inside");
        Intent intent = new Intent(MainActivity.this, EditProductActivity.class);
        intent.putExtra("productId", id);
        startActivity(intent);
    }

    //endregion Products Funcs

    private void InitToolBar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(list.getListName());
        }
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
    }

    //region Nav Drawer Func
    private void InitNavDrawer() {

        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileDrawerItem().withName("MyLists").withIcon(GoogleMaterial.Icon.gmd_account_circle)
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

        navDrawer = new DrawerBuilder()
                .withAccountHeader(headerResult)
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        itemListAc,
                        new DividerDrawerItem(),
                        settingsDrawer,
                        contactUsDrawer
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
}

