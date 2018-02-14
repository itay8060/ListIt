package com.appit.listit.LoginPackage;

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

import com.appit.listit.FireBasePackage.FireBaseDataManager;
import com.appit.listit.General.MainActivity;
import com.appit.listit.R;
import com.appit.listit.UIandViews.UIDialogsManager;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
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
    @BindView(R.id.fblogin_button)
    com.facebook.login.widget.LoginButton fbloginButton;
    private FirebaseAuth mAuth;
    private ProgressDialog pDialog;
    private UIDialogsManager mDialog = new UIDialogsManager(this);
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        activityInits();
        /*User.deleteAll(User.class);
        List.deleteAll(List.class);
        Product.deleteAll(Product.class);
        RelatedListProduct.deleteAll(RelatedListProduct.class);
        Note.deleteAll(Note.class);
        Log.e("Deleting tables", "Tables deleted");
        Category.deleteAll(Category.class); */
    }

    @Override
    public void onStart() {
        super.onStart();
        if (FireBaseDataManager.checkIfUserLoggedIn()) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        } else {
            Toast.makeText(this, getText(R.string.loginToUseApp), Toast.LENGTH_SHORT).show();
        }
    }

    private void activityInits() {
        InitFacebookLogin();
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

    //region Facebook login

    private void InitFacebookLogin() {
        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        fbloginButton.setReadPermissions("email", "public_profile");
        fbloginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("LoginActivity", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("LoginActivity", "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("LoginActivity", "facebook:onError", error);
            }
        });
    }

        private void handleFacebookAccessToken(AccessToken token) {
            Log.d("LoginActivity", "handleFacebookAccessToken:" + token);
            mDialog.showProgressDialog("Logging in...");
            AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("LoginActivity", "signInWithCredential:success");
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                mDialog.hidePregressDialog();
                                startActivity(intent);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("LoginActivity", "signInWithCredential:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            // Pass the activity result back to the Facebook SDK
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }

    //endregion Facebook login

    private void loginVerifyUser() {
        String email = loginEmail.getText().toString();
        String password = loginUserPassword.getText().toString();
        if (email.equals("") || password.equals("")) {
            Toast.makeText(this, "One or more of the above are empty", Toast.LENGTH_LONG).show();
        } else {
            mDialog.showProgressDialog("Logging in...");
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("msg", "signInWithEmail:success");
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                mDialog.hidePregressDialog();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("msg", "signInWithEmail:failure", task.getException());
                                mDialog.hidePregressDialog();
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


