package com.appit.listit.LoginAndRegister;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appit.listit.GroceryList.MainActivity;
import com.appit.listit.R;
import com.appit.listit.Users.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by itay feldman on 08/01/2018.
 */

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.userName)
    EditText userName;
    @BindView(R.id.userEmail)
    EditText userEmail;
    @BindView(R.id.password_1)
    EditText password1;
    @BindView(R.id.password_2)
    EditText password2;
    @BindView(R.id.cancel_Btn)
    TextView cancelBtn;
    @BindView(R.id.confirm_Btn)
    TextView confirmBtn;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private FirebaseAuth mAuth;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //supportRequestWindowFeature(Window.FEATURE_NO_TITLE); once made dialoge.
        setContentView(R.layout.register_activity);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        activityInits();
    }

    private void activityInits() {
        pDialog = new ProgressDialog(RegisterActivity.this);
        pDialog.setMessage("loading..");
        pDialog.setCancelable(false);
        initOnClickListeners();
        InitToolBar();
    }

    public void initOnClickListeners() {
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                //startActivity(i);
            }
        });
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerVerification();
                Toast.makeText(RegisterActivity.this, "user added", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void registerVerification() {
        if (password2.getText().toString().equals(password1.getText().toString())){
            pDialog.show();
            final String name = userName.getText().toString();
            final String email = userEmail.getText().toString();
            final String password = password1.getText().toString();
            mAuth.createUserWithEmailAndPassword(email ,password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                //User.deleteAll(User.class);
                                Log.d("msg", "createUserWithEmail:success");
                                /*User user = new User(name,email,password);
                                user.setUserOnlineId(mAuth.getCurrentUser().getUid());
                                user.save();*/
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name).build();
                                mAuth.getCurrentUser().updateProfile(profileUpdates);
                                //FireBaseDataManager.addFireBaseUser(user);
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                pDialog.cancel();
                                startActivity(intent);
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                User.deleteAll(User.class);
                                Log.w("msg", "createUserWithEmail:failure", task.getException());
                                pDialog.cancel();
                                Toast.makeText(RegisterActivity.this, "Registration failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });

        }
        else Log.e("msg", "Error");
    }

    private void InitToolBar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
        }
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
    }
}
