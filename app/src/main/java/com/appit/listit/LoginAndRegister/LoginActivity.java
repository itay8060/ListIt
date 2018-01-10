package com.appit.listit.LoginAndRegister;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appit.listit.DBPackage.ObjectsManager;
import com.appit.listit.General.Category;
import com.appit.listit.GroceryList.MainActivity;
import com.appit.listit.Lists.List;
import com.appit.listit.Notes.Note;
import com.appit.listit.Products.Product;
import com.appit.listit.R;
import com.appit.listit.Users.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by אitay feldman on 27/12/2017.
 */

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.signin_btn)
    Button signinBtn;
    @BindView(R.id.login_userEmail)
    EditText loginEmail;
    @BindView(R.id.login_userPassword)
    EditText loginUserPassword;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.register_Btn)
    TextView registerBtn;
    private FirebaseAuth mAuth;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        activityInits();
        User.deleteAll(User.class);
        List.deleteAll(List.class);
        Product.deleteAll(Product.class);
        Note.deleteAll(Note.class);
        Category.deleteAll(Category.class);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (ObjectsManager.checkIfUserLoggedIn()){
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
        }else{
            Toast.makeText(this, "Please login to use the app", Toast.LENGTH_SHORT).show();
        }
    }

    private void activityInits() {
        initDialog();
        initOnClickListeners();
        InitToolBar();
    }

    private void initOnClickListeners() {
        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginVerifyUser();
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
    }

    private void initDialog() {
        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("loading..");
        pDialog.setCancelable(false);
    }

    private void loginVerifyUser() {
        String email = loginEmail.getText().toString();
        String password = loginUserPassword.getText().toString();
        if (email.equals("")||password.equals("")){
            Toast.makeText(this, "One or more of the above are empty", Toast.LENGTH_LONG).show();
        }
        else{
            pDialog.show();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("msg", "signInWithEmail:success");
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                pDialog.cancel();
                                startActivity(intent);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("msg", "signInWithEmail:failure", task.getException());
                                pDialog.cancel();
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

    private void InitToolBar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
        }
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
    }
}


