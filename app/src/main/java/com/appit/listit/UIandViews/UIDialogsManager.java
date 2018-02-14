package com.appit.listit.UIandViews;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by Asaf on 17/09/2017.
 */

public class UIDialogsManager {

    private Context context;

    private ProgressDialog progressDialog;


    public UIDialogsManager(Context context){
        this.context = context;
    }

    public void showProgressDialog(String message){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progressDialog.show();
    }

    public void hidePregressDialog(){
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void showToast(String message){
        Toast.makeText(context , message , Toast.LENGTH_LONG).show();

    }

    public void showAlertDialog(String message , String positive , String negative , DialogInterface.OnClickListener dialogClickListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setPositiveButton(positive, dialogClickListener)
                .setNegativeButton(negative, dialogClickListener).show();
    }

    public void showSingleBtnAlertDialog(String message , String positive , DialogInterface.OnClickListener dialogClickListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setPositiveButton(positive, dialogClickListener).show();

    }

    public void showAlertDialogWithEditText(String message , String positive , String negative , DialogInterface.OnClickListener dialogClickListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setPositiveButton(positive, dialogClickListener)
                .setNegativeButton(negative, dialogClickListener);

        final EditText input = new EditText(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);

        builder.show();
    }

}