package com.nguyenthanh.placearound.LoginApi;

/**
 * Created by Administrator on 3/16/2017.
 */

import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.nguyenthanh.placearound.LoginApi.object.CustomeAdapter;
import com.nguyenthanh.placearound.LoginApi.object.ObjectAccessToken;
import com.nguyenthanh.placearound.LoginApi.object.ObjectFullResponseData;
import com.nguyenthanh.placearound.LoginApi.services.DataService;
import com.nguyenthanh.placearound.LoginApi.services.ILoadDataSuccessListener;
import com.nguyenthanh.placearound.LoginApi.services.ILoginSuccessListener;
import com.nguyenthanh.placearound.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;

@Fullscreen
@EActivity(R.layout.activity_show_content_details)
public class ShowContentApiActivity extends AppCompatActivity {

    @ViewById(R.id.btn_showdetails)
    Button btnShow;

    @ViewById(R.id.lv_data)
    ListView lvData;

    @Bean
    protected DataService dataService;

    @Bean
    protected DataService dataServiceLoad;

    private CustomeAdapter customeAdapter;


    @AfterViews
    protected void initViews() {

        Bundle extras = getIntent().getExtras();
        final String username = extras.getString("user");
        final String password = extras.getString("pass");

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(username, password);
            }
        });
    }

    @Background
    protected void login(String user, String pass) {
        dataService.login("password", user, pass, new ILoginSuccessListener() {
            @Override
            public void onSuccess(ObjectAccessToken account) {
                dataServiceLoad.loadData(account.getAccessToken(), new ILoadDataSuccessListener() {

                    @Override
                    public void onSuccess(ObjectFullResponseData listData) {
                        uiThread(listData);
                    }
                });
            }
        });
    }

    @UiThread
    protected void uiThread(ObjectFullResponseData listData) {
        customeAdapter = new CustomeAdapter(listData.getData(), this);
        lvData.setAdapter(customeAdapter);
    }
}
