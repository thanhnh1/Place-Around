package com.nguyenthanh.placearound.LoginApi;

/**
 * Created by Administrator on 3/16/2017.
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.nguyenthanh.placearound.LoginApi.object.CustomeAdapter;
import com.nguyenthanh.placearound.LoginApi.object.ObjectFullResponseData;
import com.nguyenthanh.placearound.LoginApi.services.DataServiceBolts;
import com.nguyenthanh.placearound.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;

import bolts.Continuation;
import bolts.Task;

@Fullscreen
@EActivity(R.layout.activity_show_content_details)
public class ShowContentApiActivity extends AppCompatActivity {

    @ViewById(R.id.btn_showdetails)
    Button btnShow;

    @ViewById(R.id.lv_data)
    ListView lvData;

//    @Bean
//    protected DataService dataService;
//
//    @Bean
//    protected DataService dataServiceLoad;

    @Bean
    protected DataServiceBolts dataServiceBolts;

    private CustomeAdapter customeAdapter;


    @AfterViews
    protected void initViews() {

        Bundle extras = getIntent().getExtras();
        final String username = extras.getString("user");
        final String password = extras.getString("pass");

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataServiceBolts.loadData("password", username, password).continueWith(new Continuation<ObjectFullResponseData, Void>() {
                    @Override
                    public Void then(Task<ObjectFullResponseData> task) throws Exception {
                        customeAdapter = new CustomeAdapter(task.getResult().getData(), ShowContentApiActivity.this);
                        lvData.setAdapter(customeAdapter);
                        return null;
                    }
                });
            }
        });
    }
}
