package com.nguyenthanh.placearound.LoginApi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.nguyenthanh.placearound.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Administrator on 3/15/2017.
 */
@Fullscreen
@EActivity(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity {

    @ViewById(R.id.btn_login)
    Button btnLogin;
    @ViewById(R.id.et_username)
    EditText etUsername;

    @ViewById(R.id.et_password)
    EditText etPassword;

    @AfterViews
    protected void initViews() {

        final String username = etUsername.getText().toString();
        final String password = etPassword.getText().toString();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ShowContentApiActivity.class);
                intent.putExtra("user", username);
                intent.putExtra("pass", password);
                startActivity(intent);
            }
        });
    }

}
