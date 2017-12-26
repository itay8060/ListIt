package com.appit.listit.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.appit.listit.GroceryList.MainActivity;
import com.appit.listit.R;
import com.appit.listit.Users.User;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.signin_btn)
    Button signinBtn;
    @BindView(R.id.login_userName)
    EditText loginUserName;
    @BindView(R.id.login_userPassword)
    EditText loginUserPassword;
    private User user1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);
        runDemos();

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginVerifyUser();
                //Intent i = new Intent(LoginActivity.this, MainActivity.class);
                //startActivity(i);
            }
        });
    }

    private void runDemos() {
        user1 = new User("Itay1", "1@1.com", "111111");
        user1.save();
    }

    private void loginVerifyUser() {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            i.putExtra("UserId", user1.getId());
            startActivity(i);
    }
}


