package com.appit.listit.GroceryList;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appit.listit.General.AppConstants;
import com.appit.listit.Lists.List;
import com.appit.listit.Notes.Note;
import com.appit.listit.Products.EditProductActivity;
import com.appit.listit.Products.ItemClickListener;
import com.appit.listit.Products.Product;
import com.appit.listit.Products.ProductAdapter;
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
    @BindView(R.id.productList)
    ListView productListView;
    @BindView(R.id.clearCheckedBtn)
    Button clearCheckedBtn;
    public List list = new List();
    private ArrayList<Product> checkedArray = new ArrayList<Product>();
    private java.util.List<Product> productsList;
    ItemClickListener clickListener;
    private User currentUser;
    private Product currentProduct;
    private Drawer navDrawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
        Intent i = getIntent();
        currentUser = User.findById(User.class, i.getLongExtra("UserId",0));
        activityInits();
    }

    private void activityInits() {
        initProductList();
        initDemoData();
        initClickListeners();
        refreshProducts();
        InitNavDrawer();
    }

    //region Products Funcs
    private void initProductList() {
        productsList = Product.listAll(Product.class);
        final ProductAdapter adapter = new ProductAdapter(productsList, this, clickListener);
        productListView.setAdapter(adapter);
    }

    public void addProduct(View v) {
        Product product = new Product(newProductEditText.getText().toString(), list.getId());
        product.save();
        newProductEditText.setText("");
        refreshProducts();
    }

    private void saveProductsData() {
        final ProductAdapter adapter = new ProductAdapter(productsList, this, clickListener);
        productListView.setAdapter(adapter);
    }

    private void loadProductsData() {
        refreshProducts();
    }

    private void refreshProducts() {
        productsList = Product.listAll(Product.class);
        productListView.setAdapter(new ProductAdapter(productsList, this, clickListener));
    }

    public void clearBtn(View v) {
        for (int i = 0; i < productsList.size(); i++) {
            if (productsList.get(i).productIsDone()) {
                Product product = Product.findById(Product.class, productsList.get(i).getId());
                product.delete();
            }
        }
        refreshProducts();
        saveProductsData();
    }

        public void initDemoData(){
            if (productsList.size()==0) {
                Product.deleteAll(Product.class);
                Log.e("msg", currentUser.getUserName());
                list.setListName("My Frist GroceryListsList");
                list.setUserName(currentUser.getUserName());
                list.save();
                Log.e("msg", String.valueOf(list.getId()));
                Product product1 = new Product("Milk", list.getId());
                product1.save();
                Product product2 = new Product("Bread", list.getId());
                product2.save();
                Product product3 = new Product("Oranges", list.getId());
                product3.save();
                Note.deleteAll(Note.class);
                Note note1 = new Note("ERGENT!", currentUser.getUserName(), product2.getId());
                note1.save();
                Note note2 = new Note("We're all outt!", currentUser.getUserName(), product1.getId());
                note2.save();
                Note note3 = new Note("Anyone getting?!", currentUser.getUserName(), product1.getId());
                note3.save();
                refreshProducts();
            }else refreshProducts();
        }

        public void initClickListeners(){
            clickListener = new ItemClickListener() {
                @Override
                public void onClick(View v, int src, int i) {
                    Integer location;
                    switch (src) {
                        case AppConstants.ADD_CONST:
                            currentProduct = Product.findById(Product.class, productsList.get(i).getId());
                            currentProduct.increaseQuantity();
                            refreshProducts();
                            break;
                        case AppConstants.SUB_CONST:
                            currentProduct = Product.findById(Product.class, productsList.get(i).getId());
                            currentProduct.decreaseQuantity();
                            refreshProducts();
                            break;
                        case AppConstants.EDIT_PRODUCT_CONST:
                            showEditProductActivity(productsList.get(i).getId());
                            break;
                        case AppConstants.CHECKBOX:
                            location = (Integer)v.getTag();
                            currentProduct = Product.findById(Product.class, productsList.get(i).getId());
                            currentProduct.productToggleChecked();
                            refreshProducts();
                            break;
                    }
                }
            };
        }

        public void showEditProductActivity(Long id){
            Log.i("msg", "got inside");
            Intent intent = new Intent(MainActivity.this, EditProductActivity.class);
            intent.putExtra("productId", id);
            startActivity(intent);
          }

    //endregion Products Funcs

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

