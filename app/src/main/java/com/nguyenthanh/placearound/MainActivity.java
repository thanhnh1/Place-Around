package com.nguyenthanh.placearound;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.nguyenthanh.placearound.model_places.Information;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 3/27/2017.
 */

@EActivity(R.layout.activity_main_choose_action)
public class MainActivity extends AppCompatActivity {

    private ActionBar actionBar;

    private ActionBarDrawerToggle mDrawerToggle;

    @ViewById(R.id.nav_view)
    NavigationView navigationView;

    @ViewById(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    private String mActivityTitle;

    @ViewById(R.id.btn1)
    Button button;

    @ViewById(R.id.list_view)
    ListView listView;

    CustomAdapterViewPayger adapter;
    ViewPager viewPager;
    Timer timer;

    @ViewById(R.id.ln_place)
    LinearLayout linearLayoutPlace;

    @ViewById(R.id.ln_weather)
    LinearLayout linearLayoutWeather;


    DatabaseSQLite db;
    CustomAdapter adapter_list;

    @AfterViews
    public void afterView() {

        checkGPS();
        checkWifi();

        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0277BD")));

        //create view pager
        viewPager = (ViewPager) findViewById(R.id.view_payger);
        adapter = new CustomAdapterViewPayger(this);
        viewPager.setAdapter(adapter);

        //set timer view pager
        timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimer(), 5000, 5000);

        //list view favorite
        db = new DatabaseSQLite(this);
        final List<Information> favPlaces = db.getAllPlace();
        adapter_list = new CustomAdapter(favPlaces, getApplicationContext());
        listView.setAdapter(adapter_list);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int posision, long arg3) {
                db.deletePlace(adapter_list.getItem(posision));
                adapter_list.notifyDataSetChanged();
                afterView();
                return false;
            }
        });


        linearLayoutPlace.setClickable(true);
        linearLayoutPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPlace = new Intent(getApplicationContext(), MapPlaceActivity_.class);
                startActivity(intentPlace);
            }
        });

        linearLayoutWeather.setClickable(true);
        linearLayoutWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentWeather = new Intent(getApplicationContext(), WeatherMapsActivity_.class);
                startActivity(intentWeather);
            }
        });

    }

    private boolean checkWifi() {
        //check WIFI activation
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected() == false) {
            showWIFIDisabledAlertToUser();
        }
        else {
//            Toast.makeText(this, "WIFI is Enabled in your devide", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void showWIFIDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("WIFI is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable WIFI",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        Settings.ACTION_WIFI_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private boolean checkGPS() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
//            Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
        }else{
            showGPSDisabledAlertToUser();
        }
        return false;
    }

    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public class MyTimer extends TimerTask {

        @Override
        public void run() {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() == 0) {
                        viewPager.setCurrentItem(1);
                    } else if (viewPager.getCurrentItem() == 1) {
                        viewPager.setCurrentItem(2);
                    } else
                        viewPager.setCurrentItem(0);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        afterView();
    }
}