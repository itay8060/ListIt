package com.appit.listit.FireBasePackage;

import android.util.Log;

import com.appit.listit.Lists.List;
import com.appit.listit.Products.Note;
import com.appit.listit.Products.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by itay feldman on 09/01/2018.
 */

public class FireBaseDataManager {

    private static FirebaseDatabase database = FirebaseDatabase.getInstance();;
    private static DatabaseReference dataBaseLists  = database.getReference("lists");
    private static DatabaseReference dataBaseProducts;
    private static DatabaseReference dataBaseNotes;
    private static java.util.List<List> listsList = new ArrayList<>();

    public interface OnGetDataListener {
        //make new interface for call back
        void onSuccess(DataSnapshot dataSnapshot);
        void onStart();
        void onFailure();
    }

    /*public static void addFireBaseUser(User user){
        database = FirebaseDatabase.getInstance();
        dataBaseUsers = database.getReference("users");
        String id = dataBaseUsers.push().getKey();
        UserFireBase fUser = converUserToFireBase(user, id);
        dataBaseUsers.child(id).setValue(fUser);
    }

    private static UserFireBase converUserToFireBase(User user, String id) {
        UserFireBase fUser = new UserFireBase(id, user.getUserName(), user.getUserEmail(), user.getUserPassword());
        return fUser;
    }*/

    // region user manager

    public static boolean checkIfUserLoggedIn(){
        if (getUser() != null){
            return true;
        }
        return false;
    }

    public static FirebaseUser getUser(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    // endregion user manager

    public static void addFireBaseList(List list){
        //database = FirebaseDatabase.getInstance();
        dataBaseLists = database.getReference("lists");
        String listId = dataBaseLists.push().getKey();
        list.setListOnlineId(listId);
        ListFireBase fList = convertListToFireBase(listId, list);
        dataBaseLists.child(listId).setValue(fList);
    }

    private static ListFireBase convertListToFireBase(String id, List list) {
        ListFireBase fList = new ListFireBase(id, list.getListName(), list.getUserId(), list.getListCreateDate());
        return fList;
    }

    public static void addFireBaseProduct(Product product, String listId){
        //database = FirebaseDatabase.getInstance();
        dataBaseProducts = database.getReference("products").child(listId);
        String productId = dataBaseProducts.push().getKey();
        product.setProductOnlineId(productId);

        //Waiting for FireBase -
        //ProductFireBase fProduct = convertProductToFireBase(product, productId);
        //dataBaseProducts.child(productId).setValue(fProduct);
    }

    private static ProductFireBase convertProductToFireBase(Product product, String id) {
        ProductFireBase fProduct = new ProductFireBase(id, product.getProductName(), product.getCategorytId());
        return fProduct;
    }

    public static void addFireBaseNote(Note note, String productId, String listId){
        //database = FirebaseDatabase.getInstance();
        dataBaseNotes = database.getReference("notes").child(listId).child(productId);
        String id = dataBaseNotes.push().getKey();
        note.setNoteOnlineId(id);
        NoteFireBase fNote = convertNoteToFireBase(note, id);
        dataBaseNotes.child(id).setValue(fNote);
    }

    private static NoteFireBase convertNoteToFireBase(Note note, String id) {
        NoteFireBase fnote = new NoteFireBase(id, note.getNoteTxt(), note.getUserName(), note.getProductId());
        return fnote;
    }

    //------------ getters ---------------

    public static List getOnlineListData(){
        dataBaseLists = database.getReference("lists");
        getOnlineListsListData(dataBaseLists);
        return listsList.get(0);
    }

    public static java.util.List getOnlineListsListData(DatabaseReference ref){
        readData(ref, new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                listsList.clear();
                for (DataSnapshot listSnapShot : dataSnapshot.getChildren()){
                    List list = listSnapShot.getValue(List.class);
                    listsList.add(list);
                    Log.e("Fetching List suceed", "insideeeeeeee");
                }
            }
            @Override
            public void onStart() {
                //whatever you need to do onStart
                Log.d("ONSTART", "Started");
            }

            @Override
            public void onFailure() {
                Log.e("Fetching List error", "onCancelled");
            }
        });
        return listsList;
    }

    public static java.util.List<Product> getOnlineProductsList(final String listId){
        //ObjectsManager.getDemoProducts(listId);
        dataBaseProducts = database.getReference();

        final java.util.List<Product> productsList = new ArrayList<>();
        readData(dataBaseProducts.child(listId), new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                productsList.clear();
                for(DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()){
                    //Loop 1 to go through all the child nodes of users
                    for(DataSnapshot productSnapShot : uniqueKeySnapshot.child("products").getChildren()){
                        //loop 2 to go through all the child nodes of books node
                        Product product = productSnapShot.getValue(Product.class);
                        productsList.add(product);
                    }
                }
            }
            @Override
            public void onStart() {
                //whatever you need to do onStart
                Log.d("ONSTART", "Started");
            }

            @Override
            public void onFailure() {
                Log.e("Fetching List error", "onCancelled");
            }
        });
        return productsList;
    }

    public static void readData(DatabaseReference ref, final OnGetDataListener listener) {
        listener.onStart();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailure();
            }
        });
    }

}
