package com.nguyenthanh.placearound;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.nguyenthanh.placearound.components.AddItemOverlay;
import com.nguyenthanh.placearound.components.AlertDialogManager;
import com.nguyenthanh.placearound.components.ConnectionDetector;
import com.nguyenthanh.placearound.components.SpinnerItem;
import com.nguyenthanh.placearound.components.TitleNavigationAdapter;
import com.nguyenthanh.placearound.directions.PlaceDirections;
import com.nguyenthanh.placearound.model.GPSTracker;
import com.nguyenthanh.placearound.model.MapPlaces;
import com.nguyenthanh.placearound.model.Place;
import com.nguyenthanh.placearound.model.Places;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@EActivity(R.layout.fragment_ways_map)
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        ActionBar.OnNavigationListener,
        LocationListener {

    public static final String KEY_REFERENCE = "reference";

    public static final String KEY_NAME = "name";

    //ui
    private android.support.v7.app.ActionBar actionBar;

    private ActionBarDrawerToggle mDrawerToggle;

    @ViewById(R.id.nav_view)
    NavigationView navigationView;

    @ViewById(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    private String mActivityTitle;

    private ArrayList<SpinnerItem> navSpinner;

    private String[] vaLue;

    private String[] comPare;

    private TitleNavigationAdapter adapTer;

    // main variable 

    //MapView m;
    private boolean isInternet = false;

    private ConnectionDetector detecTor;

    private AlertDialogManager aLert = new AlertDialogManager();

    private GPSTracker globalpositonSystem;

    private MapPlaces googlePlaces;

    private String keyWorl;

    private Places listPlace;

    private double latiTude;

    private double longiTude;

    private double latTmp;

    private double lonTmp;

    private ProgressDialog pDialog;

    private ArrayList<HashMap<String, String>> placesListItems =
            new ArrayList<HashMap<String, String>>();

    // new
    private List<Overlay> mapOverlays;

    private GeoPoint geoPoint;

    private MapController mapControl;

    private AddItemOverlay itemizedOverlay;

    private OverlayItem overlayItem;

    private GoogleMap mMap;

    private ArrayList<Marker> listMaker;

    private PlaceDirections direcTions;

    private AlertDialog mean;

    /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ways_map);

        // init UI
        initUi();

        // check internet
        detecTor = new ConnectionDetector(this.getApplicationContext());
        isInternet = detecTor.isConnectingToInternet();
        if (!isInternet) {
            // Internet Connection is not present
            aLert.showAlertDialog(this, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }
        // check able of gps
        globalpositonSystem = new GPSTracker(this);
        if (globalpositonSystem.canGetLocation()) {
            Log.d("Your Location", "latitude:" + globalpositonSystem.getLatitude() +
                    ", longitude: " + globalpositonSystem.getLongitude());
            latiTude = globalpositonSystem.getLatitude();
            longiTude = globalpositonSystem.getLongitude();

            new LoadPlaces().execute();
        } else {
            // Can't get user's current location
            aLert.showAlertDialog(this, "GPS Status",
                    "Couldn't get location information. Please enable GPS",
                    false);
        }
        handleIntent(getIntent());
    } */

    @AfterViews
    public void afterViews() {
        initUi();

//        // check internet
//        detecTor = new ConnectionDetector(this.getApplicationContext());
//        isInternet = detecTor.isConnectingToInternet();
//        if (!isInternet) {
//            // Internet Connection is not present
//            aLert.showAlertDialog(this, "Internet Connection Error",
//                    "Please connect to working Internet connection", false);
//            // stop executing code by return
//            return;
//        }

        checkWifi();
        checkGPS();

        if (!(checkWifi() && checkGPS())) {
            loadMap();
            getcurrentLocation();
//            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(MainActivity.this);
//            // Setting Dialog Title
//            alertDialog2.setTitle("Information...");
//            // Setting Dialog Message
//            alertDialog2.setMessage("You can find ATM near me?");
//            alertDialog2.setPositiveButton("Yes",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            // Write your code here to execute after dialog
//                            globalpositonSystem = new GPSTracker(MainActivity.this);
//                            if (globalpositonSystem.canGetLocation()) {
//                                Log.d("Your Location", "latitude:" + globalpositonSystem.getLatitude() +
//                                        ", longitude: " + globalpositonSystem.getLongitude());
//                                latiTude = globalpositonSystem.getLatitude();
//                                longiTude = globalpositonSystem.getLongitude();
//
//                                new LoadPlaces().execute();
//                            }
//                        }
//                    });
//            alertDialog2.setNegativeButton("No",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.cancel();
//                        }
//                    });
//            // Showing Alert Dialog
//            alertDialog2.show();
        }

//        globalpositonSystem = new GPSTracker(this);
//        if (globalpositonSystem.canGetLocation()) {
//            Log.d("Your Location", "latitude:" + globalpositonSystem.getLatitude() +
//                    ", longitude: " + globalpositonSystem.getLongitude());
//            latiTude = globalpositonSystem.getLatitude();
//            longiTude = globalpositonSystem.getLongitude();
//
//            new LoadPlaces().execute();
//        }

        // check able of gps
//        globalpositonSystem = new GPSTracker(this);
//        if (globalpositonSystem.canGetLocation()) {
//            Log.d("Your Location", "latitude:" + globalpositonSystem.getLatitude() +
//                    ", longitude: " + globalpositonSystem.getLongitude());
//            latiTude = globalpositonSystem.getLatitude();
//            longiTude = globalpositonSystem.getLongitude();
//
//            new LoadPlaces().execute();
//        } else {
//            // Can't get user's current location
//            aLert.showAlertDialog(this, "GPS Status",
//                    "Couldn't get location information. Please enable GPS",
//                    false);
//        }
        handleIntent(getIntent());

    }

    private boolean checkWifi() {
        //check WIFI activation
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected() == false) {
            showWIFIDisabledAlertToUser();
        }
        else {
            Toast.makeText(this, "WIFI is Enabled in your devide", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
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

    private void showTraffic() {
        mMap.setTrafficEnabled(true);
    }

    private void aboutVersion() {
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(MainActivity.this);
        // Setting Dialog Title
        alertDialog2.setTitle("Information...");
        // Setting Dialog Message
        alertDialog2.setMessage("Version code: " + BuildConfig.VERSION_CODE + "\n"
                + "Version name: " + BuildConfig.VERSION_NAME + "\n" + "Build type: "
                + BuildConfig.BUILD_TYPE + "\n" + "Product flavor: " + BuildConfig.FLAVOR
                + "\n");
        alertDialog2.setPositiveButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();
                        Toast.makeText(getApplicationContext(),
                                "You clicked on CANCEL", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
        // Showing Alert Dialog
        alertDialog2.show();
    }
    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            mMap.clear();
            Utils.sKeyPlace = query;
            new LoadPlaces().execute();
        }
    }

    private void initUi() {

        mActivityTitle = getTitle().toString();
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        navigationView.setNavigationItemSelectedListener(this);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        /////////////////
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        actionBar = getSupportActionBar();

        //actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0277BD")));
        //actionBar.setDisplayShowTitleEnabled(false);
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        vaLue = getValue();
        comPare = getValue1();

        addBar();

        adapTer = new TitleNavigationAdapter(getApplicationContext(), navSpinner);
//        actionBar.setListNavigationCallbacks(adapTer, this);
//        RadioGroup rg = (RadioGroup) findViewById(R.id.radio_group_list_selector);
//        rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                switch (checkedId) {
//                    case R.id.cycle:
//                        // TODO Something
//                        Utils.sKeyWay = Utils.BICYCLE;
//                        break;
//                    case R.id.car:
//                        // TODO Something
//                        Utils.sKeyWay = Utils.OTO;
//                        break;
//                    case R.id.walk:
//                        // TODO Something
//                        Utils.sKeyWay = Utils.WALK;
//                        break;
//                }
//            }
//        });

        getSupportActionBar().setListNavigationCallbacks(adapTer,
                new android.support.v7.app.ActionBar.OnNavigationListener() {
                    @Override
                    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                        // Action to be taken after selecting a spinner item
                        // dua list marker = rong
                        if (itemPosition > 0) {
                            mMap.clear();
                            Utils.sKeyPlace = comPare[itemPosition];
                            new LoadPlaces().execute();
                            itemPosition = -1;
                        }
                        return true;
                    }
                });
        getSupportActionBar().setIcon(R.drawable.ic_launcher_place_around);
    }

    private String[] getValue() {
        return getResources().getStringArray(R.array.items);
    }

    private String[] getValue1() {
        return getResources().getStringArray(R.array.compare);
    }

    private void addBar() {
        // Spinner title navigation data
        navSpinner = new ArrayList<SpinnerItem>();
        navSpinner.add(new SpinnerItem(vaLue[0], R.drawable.ic_menu_airport));
        navSpinner.add(new SpinnerItem(vaLue[1], R.drawable.ic_menu_atm));
        navSpinner.add(new SpinnerItem(vaLue[2], R.drawable.ic_menu_bank));
        navSpinner.add(new SpinnerItem(vaLue[3], R.drawable.ic_menu_bar));
        navSpinner.add(new SpinnerItem(vaLue[4], R.drawable.ic_menu_church));
        navSpinner.add(new SpinnerItem(vaLue[5], R.drawable.ic_menu_coffee));
        navSpinner.add(new SpinnerItem(vaLue[6], R.drawable.ic_menu_hospital));
        navSpinner.add(new SpinnerItem(vaLue[7], R.drawable.ic_menu_hotel));
        navSpinner.add(new SpinnerItem(vaLue[8], R.drawable.ic_menu_library));
        navSpinner.add(new SpinnerItem(vaLue[9], R.drawable.ic_menu_museum));
        navSpinner.add(new SpinnerItem(vaLue[10], R.drawable.ic_menu_restaurant));
    }

    private void loadMap() {
        if (mMap != null) {
            return;
        }
        mMap = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();
        if (mMap == null) {
            return;
        }
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMyLocationEnabled(true);
        listMaker = new ArrayList<Marker>();
        mMap.setOnMarkerClickListener(new OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker arg0) {
                // TODO Auto-generated method stub
                Utils.sDestination = arg0.getPosition();
                Utils.sStrDestinaton = arg0.getTitle();
                Utils.sStrSnippet = arg0.getSnippet();
                return false;
            }
        });

        mMap.getUiSettings().setZoomControlsEnabled(true);
        //mMap.getUiSettings().setRotateGesturesEnabled(true);
        //mMap.getUiSettings().setScrollGesturesEnabled(true);
        //mMap.getUiSettings().setTiltGesturesEnabled(true);
        //mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
    }

    private void getcurrentLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();
        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);
        // Getting Current Location
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            onLocationChanged(location);
        }
        locationManager.requestLocationUpdates(provider, 20000, 0, this);
    }

    class LoadPlaces extends AsyncTask<String, String, String> {
        //Before starting background thread Show Progress Dialog
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage(Html.fromHtml("<b>Search</b><br/>Loading Places..."));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        //getting Places JSON
        protected String doInBackground(String... args) {
            googlePlaces = new MapPlaces();
            try {
                String types = Utils.sKeyPlace;
                double radius = 3000;
                listPlace = googlePlaces.searCh(globalpositonSystem.getLatitude(),
                        globalpositonSystem.getLongitude(), radius, types);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Get json response status
                    String status = listPlace.status;
                    // Check for all possible status
                    if (status.equals("OK")) {
                        // Successfully got places details
                        if (listPlace.results != null) {
                            // loop through each place
                            for (Place p : listPlace.results) {
                                HashMap<String, String> map = new HashMap<String, String>();
                                // Place reference won't display in listview - it will be hidden
                                // Place reference is used to get "place full details"
                                map.put(KEY_REFERENCE, p.reference);
                                // Place name
                                map.put(KEY_NAME, p.name);
                                // adding HashMap to ArrayList
                                placesListItems.add(map);
                            }
                            System.out.println("hfjkdhdk" + listPlace.results.size());
                        }
                        // add map, load map
                        loadMap();
                        getcurrentLocation();
                        // draw my position 
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(latiTude, longiTude))
                                .title("Me")
                                .snippet("Local of me")
                                .icon(BitmapDescriptorFactory
                                        .fromResource(R.drawable.ic_marker_current)));
                        // Drawable marker icon
                        if (listPlace.results != null) {
                            // loop through all the places
                            for (Place place : listPlace.results) {
                                latTmp = place.geometry.location.lat; // latitude
                                lonTmp = place.geometry.location.lng; // longitude
                                Marker marker = mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(latTmp, lonTmp))
                                        .title(place.name)
                                        .snippet(place.vicinity)
                                        .icon(BitmapDescriptorFactory
                                                .fromResource(R.drawable.ic_marker_possion)));
                                listMaker.add(marker);
                            }
                        }
                    } else if (status.equals("ZERO_RESULTS")) {
                        // Zero results found
                        aLert.showAlertDialog(MainActivity.this, "Near Places",
                                "Sorry no places found. Try to change the types of places",
                                false);
                    } else if (status.equals("UNKNOWN_ERROR")) {
                        aLert.showAlertDialog(MainActivity.this, "Places Error",
                                "Sorry unknown error occured.",
                                false);
                    } else if (status.equals("OVER_QUERY_LIMIT")) {
                        aLert.showAlertDialog(MainActivity.this, "Places Error",
                                "Sorry query limit to google places is reached",
                                false);
                    } else if (status.equals("REQUEST_DENIED")) {
                        aLert.showAlertDialog(MainActivity.this, "Places Error",
                                "Sorry error occured. Request is denied",
                                false);
                    } else if (status.equals("INVALID_REQUEST")) {
                        aLert.showAlertDialog(MainActivity.this, "Places Error",
                                "Sorry error occured. Invalid Request",
                                false);
                    } else {
                        aLert.showAlertDialog(MainActivity.this, "Places Error",
                                "Sorry error occured.",
                                false);
                    }
                }
            });
        }
    }

//    private void sendInfo(Marker marker) {
//        String strInfo = marker.getTitle() + "\n" + marker.getSnippet();
//        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//        shareIntent.putExtra(Intent.EXTRA_TEXT, strInfo);
//        shareIntent.setType("text/plain");
//        startActivity(Intent.createChooser(shareIntent, "Send via !"));
//    }

    @Override
    public boolean onNavigationItemSelected (MenuItem item) {

        int id = item.getItemId(); // Handle navigation view item clicks here.

        if (id == R.id.nav_home) {
            // sign in activity
        } else if (id == R.id.nav_seting) {
            // choose map style
        } else if (id == R.id.nav_about) {
            // view option activity
            aboutVersion();
        } else if (id == R.id.nav_traffic) {
            // list of fav place activity
            showTraffic();

        } else if (id == R.id.nav_location) {
            getcurrentLocation();
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_mnu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //On selecting action bar icons
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.direc: {
                // get type way
                byte way = Utils.sKeyWay;
                // get destination
                LatLng des = Utils.sDestination;
                // get ways by listview + show on map
                if (way < 0) {
                    aLert.showAlertDialog(this, "Determine a type way",
                            "Please choice a type way",
                            false);
                } else {
                    if (des == null) {
                        aLert.showAlertDialog(this, "Place empty",
                                "Please choice destination place",
                                false);
                    } else {
                        LatLng from = new LatLng(latiTude, longiTude);
                        direcTions = new PlaceDirections(getApplicationContext(),
                                mMap,
                                from,
                                des,
                                way);
                    }
                }
                return true;
            }
            case R.id.action_normal: {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            }
            case R.id.action_hybrid: {
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            }
            case R.id.action_satellite: {
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            }
            case R.id.action_terrain: {
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
        //return super.onOptionsItemSelected(item);
    }


    //Actionbar navigation item select listener
    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        // Action to be taken after selecting a spinner item
        // dua list markeer = rong
        if (itemPosition > 0) {
            mMap.clear();
            Utils.sKeyPlace = comPare[itemPosition];
            new LoadPlaces().execute();
            itemPosition = -1;
        }
        return true;

    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        double latitude = location.getLatitude();
        // Getting longitude of the current location
        double longitude = location.getLongitude();
        // Creating a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 3000, null);
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        loadMap();
    }
}
