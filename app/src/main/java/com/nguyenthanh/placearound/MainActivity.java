package com.nguyenthanh.placearound;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

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
import com.nguyenthanh.placearound.directions.PlaceDirections;
import com.nguyenthanh.placearound.model.GPSTracker;
import com.nguyenthanh.placearound.model.GooglePlaces;
import com.nguyenthanh.placearound.model.Place;
import com.nguyenthanh.placearound.model.Places;
import com.nguyenthanh.placearound.view.AddItemizedOverlay;
import com.nguyenthanh.placearound.view.AlertDialogManager;
import com.nguyenthanh.placearound.view.ConnectionDetector;
import com.nguyenthanh.placearound.view.SpinnerItem;
import com.nguyenthanh.placearound.view.TitleNavigationAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends FragmentActivity implements ActionBar.OnNavigationListener,
        LocationListener {

    public static final String KEY_REFERENCE = "reference";

    public static final String KEY_NAME = "name";

    //ui
    private ActionBar actionBar;

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

    private GooglePlaces googlePlaces;

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

    private AddItemizedOverlay itemizedOverlay;

    private OverlayItem overlayItem;

    private GoogleMap mMap;

    private ArrayList<Marker> listMaker;

    private PlaceDirections direcTions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps);

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
        actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        vaLue = getValue();
        comPare = getValue1();
        addBar();
        adapTer = new TitleNavigationAdapter(getApplicationContext(), navSpinner);
        actionBar.setListNavigationCallbacks(adapTer, this);
        RadioGroup rg = (RadioGroup) findViewById(R.id.radio_group_list_selector);
        rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.xedap:
                        // TODO Something
                        Utils.sKeyWay = Utils.BICYCLE;
                        break;
                    case R.id.oto:
                        // TODO Something
                        Utils.sKeyWay = Utils.OTO;
                        break;
                    case R.id.dibo:
                        // TODO Something
                        Utils.sKeyWay = Utils.WALK;
                        break;
                }
            }
        });
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
        navSpinner.add(new SpinnerItem(vaLue[0], R.drawable.airport));
        navSpinner.add(new SpinnerItem(vaLue[1], R.drawable.atm));
        navSpinner.add(new SpinnerItem(vaLue[2], R.drawable.bank));
        navSpinner.add(new SpinnerItem(vaLue[3], R.drawable.bar));
        navSpinner.add(new SpinnerItem(vaLue[4], R.drawable.cafe));
        navSpinner.add(new SpinnerItem(vaLue[5], R.drawable.church));
        navSpinner.add(new SpinnerItem(vaLue[6], R.drawable.coffee));
        navSpinner.add(new SpinnerItem(vaLue[7], R.drawable.food));
        navSpinner.add(new SpinnerItem(vaLue[8], R.drawable.hospital));
        navSpinner.add(new SpinnerItem(vaLue[9], R.drawable.hotel));
        navSpinner.add(new SpinnerItem(vaLue[10], R.drawable.library));
        navSpinner.add(new SpinnerItem(vaLue[11], R.drawable.museum));
        navSpinner.add(new SpinnerItem(vaLue[12], R.drawable.pizza));
        navSpinner.add(new SpinnerItem(vaLue[13], R.drawable.police));
        navSpinner.add(new SpinnerItem(vaLue[14], R.drawable.restaurant));
        navSpinner.add(new SpinnerItem(vaLue[15], R.drawable.supermarket));
        navSpinner.add(new SpinnerItem(vaLue[16], R.drawable.theatre));
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
            googlePlaces = new GooglePlaces();
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
                                        .fromResource(R.drawable.direction_marker)));
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
                                                .fromResource(R.drawable.maps)));
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

    private void sendInfo(Marker marker) {
        String strInfo = marker.getTitle() + "\n" + marker.getSnippet();
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, strInfo);
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, "Send via !"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

     //On selecting action bar icons
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
            default:
                return super.onOptionsItemSelected(item);
        }
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
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        loadMap();
    }
}
