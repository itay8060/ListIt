package com.appit.listit.FireBasePackage;

import com.appit.listit.Lists.List;
import com.appit.listit.Notes.Note;
import com.appit.listit.Products.Product;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by itay feldman on 09/01/2018.
 */

public class FireBaseDataManager {

    private static FirebaseDatabase database = FirebaseDatabase.getInstance();;
    private static DatabaseReference dataBaseLists;
    private static DatabaseReference dataBaseProducts;
    private static DatabaseReference dataBaseNotes;

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
        ProductFireBase fProduct = convertProductToFireBase(product, productId);
        dataBaseProducts.child(productId).setValue(fProduct);
    }

    private static ProductFireBase convertProductToFireBase(Product product, String id) {
        ProductFireBase fProduct = new ProductFireBase(id, product.getProductName(), product.productIsDone(), product.getQuantity(), product.getListId(), product.getCategorytId());
        return fProduct;
    }

    public static void addFireBaseNote(Note note, String productId){
        //database = FirebaseDatabase.getInstance();
        dataBaseNotes = database.getReference("notes").child(productId);
        String id = dataBaseNotes.push().getKey();
        note.setNoteOnlineId(id);
        NoteFireBase fNote = convertNoteToFireBase(note, id);
        dataBaseNotes.child(id).setValue(fNote);
    }

    private static NoteFireBase convertNoteToFireBase(Note note, String id) {
        NoteFireBase fnote = new NoteFireBase(id, note.getNoteTxt(), note.getUserName(), note.getProductId());
        return fnote;
    }

}
