package com.nguyenthanh.placearound;

import android.support.v7.app.ActionBar;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import com.nguyenthanh.placearound.LoginApi.LoginActivity_;
import com.nguyenthanh.placearound.components.AddItemOverlay;
import com.nguyenthanh.placearound.components.AlertDialogManager;
import com.nguyenthanh.placearound.components.ConnectionDetector;
import com.nguyenthanh.placearound.components.SpinnerItem;
import com.nguyenthanh.placearound.components.TitleNavigationAdapter;
import com.nguyenthanh.placearound.directions.PlaceDirections;
import com.nguyenthanh.placearound.model_places.GPSTracker;
import com.nguyenthanh.placearound.model_places.MapPlaces;
import com.nguyenthanh.placearound.model_places.Place;
import com.nguyenthanh.placearound.model_places.Places;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@EActivity(R.layout.fragment_ways_map)
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        android.app.ActionBar.OnNavigationListener,
        LocationListener, GoogleMap.OnInfoWindowClickListener {

    public static final String KEY_REFERENCE = "reference";

    public static final String KEY_NAME = "name";

    private ActionBar actionBar;

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

    private List<Overlay> mapOverlays;

    private GeoPoint geoPoint;

    private MapController mapControl;

    private AddItemOverlay itemizedOverlay;

    private OverlayItem overlayItem;

    private GoogleMap mMap;

    private ArrayList<Marker> listMaker;

    private PlaceDirections direcTions;

    private AlertDialog mean;

    private static final int FAV_LIST_ACTIVITY_RESULT_CODE = 0;

    private static final int SECOND_ACTIVITY_RESULT_CODE = 0;

    private double lat;

    private double lon;

    private double lat1;

    private double lon1;



    @AfterViews
    public void afterViews() {
        // check internet
        initUi();
        detecTor = new ConnectionDetector(this.getApplicationContext());
        isInternet = detecTor.isConnectingToInternet();
        if (!isInternet) {
            // Internet Connection is not present
//            aLert.showAlertDialog(this, "Internet Connection Error",
//                    "Please connect to working Internet connection", false);
//            return;
//            // stop executing code by return
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("WIFI is disabled in your device. " +
                    "Would you like to enable it?")
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
        // check able of gps
        globalpositonSystem = new GPSTracker(this);
        if (globalpositonSystem.canGetLocation()) {
            Log.d("Your Location", "latitude:" + globalpositonSystem.getLatitude() +
                    ", longitude: " + globalpositonSystem.getLongitude());
            latiTude = globalpositonSystem.getLatitude();
            longiTude = globalpositonSystem.getLongitude();

            new LoadPlaces().execute();
        } else {
//            // Can't get user's current location
            aLert.showAlertDialog(this, "GPS Status",
                    "Couldn't get location information. Please enable GPS",
                    false);
//            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//            alertDialogBuilder.setMessage("GPS is disabled in your device. " +
//                    "Would you like to enable it?")
//                    .setCancelable(false)
//                    .setPositiveButton("Goto Settings Page To Enable GPS",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    Intent callGPSSettingIntent = new Intent(
//                                            android.provider.Settings
//                                                    .ACTION_LOCATION_SOURCE_SETTINGS);
//                                    startActivity(callGPSSettingIntent);
//                                }
//                            });
//            alertDialogBuilder.setNegativeButton("Cancel",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.cancel();
//                        }
//                    });
//            AlertDialog alert = alertDialogBuilder.create();
//            alert.show();
        }
        handleIntent(getIntent());
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0277BD")));
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

        mMap.setOnInfoWindowClickListener(this);
    }

    public void onInfoWindowClick(Marker marker) {

        aLert.showAlertDialog(this, "Information",
                marker.getTitle() + "\n" + marker.getSnippet(), false);
//        Intent intent = new Intent(this, PlaceDetailsActivity_.class);
//        intent.putExtra("address", marker.getSnippet());
//        intent.putExtra("title", marker.getTitle());
//        intent.putExtra("lat", marker.getPosition().latitude);
//        intent.putExtra("lon", marker.getPosition().longitude);
//        startActivityForResult(intent, FAV_LIST_ACTIVITY_RESULT_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check that it is the SecondActivity with an OK result
        if (requestCode == SECOND_ACTIVITY_RESULT_CODE) {
            if (resultCode == RESULT_OK) {
                makeDirection(data);
            }
        }
        if (requestCode == FAV_LIST_ACTIVITY_RESULT_CODE) {

            if (resultCode == RESULT_OK) {
                makeDirection(data);

            }
        }
    }

    public void makeDirection(Intent data) {

        lat1 = data.getDoubleExtra("lat", 10);
        lon1 = data.getDoubleExtra("lon", 10);
        String address = data.getStringExtra("address");

        // draw destination
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat1, lon1))
                .title(address)
                .snippet(address));


        Utils.sDestination = new LatLng(lat1, lon1);
        LatLng des = Utils.sDestination;
        byte way = 2;
        LatLng from = new LatLng(lat, lon);
        direcTions = new PlaceDirections(getApplicationContext()
                , mMap, from, des, way);

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
        @Override
        protected String doInBackground(String... args) {
            googlePlaces = new MapPlaces();
            try {
                String types = Utils.sKeyPlace;
                double radius = 3000;
                listPlace = googlePlaces.search(globalpositonSystem.getLatitude(),
                        globalpositonSystem.getLongitude(), radius, types);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId(); // Handle navigation view item clicks here.
        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_seting) {

        } else if (id == R.id.nav_about) {
            aboutVersion();
        } else if (id == R.id.nav_traffic) {
            mMap.setTrafficEnabled(true);
        } else if (id == R.id.nav_location) {
            getcurrentLocation();
        } else if (id == R.id.nav_login_api) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity_.class);
            startActivity(intent);
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
                    aLert.showAlertDialog(this, "Determine a type way", "Please choice a type way",
                            false);
                } else {
                    if (des == null) {
                        aLert.showAlertDialog(this, "Place empty", "Please choice destination place",
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


    private void aboutVersion() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Information...");
        alertDialog.setMessage("Version code: " + BuildConfig.VERSION_CODE + "\n"
                + "Version name: " + BuildConfig.VERSION_NAME + "\n" + "Build type: "
                + BuildConfig.BUILD_TYPE + "\n" + "Product flavor: " + BuildConfig.FLAVOR
                + "\n");
        alertDialog.setPositiveButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Toast.makeText(getApplicationContext(),
                                "You clicked on CANCEL", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
        alertDialog.show();
    }

}