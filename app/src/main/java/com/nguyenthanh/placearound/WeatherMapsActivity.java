package com.nguyenthanh.placearound;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nguyenthanh.placearound.model_weather.GPSTracker;
import com.nguyenthanh.placearound.utils.WeatherAsyncTask;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import static com.nguyenthanh.placearound.R.id.map;

@EActivity(R.layout.activity_maps)
public class WeatherMapsActivity extends FragmentActivity {

    private GoogleMap mMap;

    private ProgressDialog mProg;

    private final static String MYTAG = "MYTAG";

    @AfterViews
    public void afterViews() {
        mProg = new ProgressDialog(this);
        mProg.setTitle("Loading Map!");
        mProg.setMessage("Please! wait .....");
        mProg.setCancelable(true);
        mProg.show();
        setUpMapIfNeeded();
        addEvents();
        markerClick();
    }

    public void addEvents() {
        if (mMap == null) {
            return;
        }
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                moveShowWeather(latLng);
            }
        });
    }

    public void moveShowWeather(LatLng latLng) {
        if (latLng != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new
                    LatLng(latLng.latitude, latLng.longitude), 13));
        }
        CameraPosition cameraPosition = new CameraPosition.Builder().target(new
                LatLng(latLng.latitude, latLng.longitude)).zoom(80).bearing(90).tilt(40).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        MarkerOptions option = new MarkerOptions();
        option.position(new LatLng(latLng.latitude, latLng.longitude));
        option.title("My Location.").snippet("..");
        option.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        Marker maker = mMap.addMarker(option);
        WeatherAsyncTask task = new WeatherAsyncTask(maker, mMap, WeatherMapsActivity.this,
                latLng.latitude, latLng.longitude);
        task.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(map)).getMap();
            if (mMap != null) {
                mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        mProg.dismiss();
                    }
                });
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.getUiSettings().setZoomControlsEnabled(true);
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.
                        ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                } else {
                    Toast.makeText(WeatherMapsActivity.this, "You have to accept to " +
                            "enjoy all app's services!", Toast.LENGTH_LONG).show();
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                    }
                }
                getAddress();
            }
        }
    }

    public void getAddress() {
        LatLng lastLocation = null;
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, false);
        boolean enabled = locationManager.isProviderEnabled(bestProvider);
        if (!enabled) {
            Toast.makeText(this, "No location provider enabled!", Toast.LENGTH_LONG).show();
            Log.i(MYTAG, "No location provider enabled!");
            return;
        }
        try {
            GPSTracker globalpositonSystem;
            globalpositonSystem = new GPSTracker(WeatherMapsActivity.this);
            if (globalpositonSystem.canGetLocation()) {
                Log.d("Your Location", "latitude:" + globalpositonSystem.getLatitude() +
                        ", longitude: " + globalpositonSystem.getLongitude());
                double latiTude = globalpositonSystem.getLatitude();
                double longiTude = globalpositonSystem.getLongitude();
                lastLocation = new LatLng(latiTude, longiTude);
            } else {
                //  Can't get user's current location
                Toast.makeText(WeatherMapsActivity.this, "GPS Status! " +
                                "Couldn't get location information. Please enable GPS",
                        Toast.LENGTH_LONG).show();
            }
        } catch (SecurityException e) {
            Toast.makeText(this, "Show My Location Error: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
            Log.e(MYTAG, "Show My Location Error:" + e.getMessage());
            e.printStackTrace();
            return;
        }
        if (lastLocation != null) {
            haveLocation(lastLocation);
        }
    }

    public void haveLocation(LatLng lastLocation) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(lastLocation.latitude, lastLocation.longitude), 13));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lastLocation.latitude, lastLocation.longitude))
                .zoom(15)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(40)                   // Sets the tilt of the camera to 40 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        MarkerOptions option = new MarkerOptions();
        option.position(new LatLng(lastLocation.latitude, lastLocation.longitude));
        option.title("My Location.").snippet("...");
        option.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        Marker maker = mMap.addMarker(option);
        //Intent i = getIntent();

        WeatherAsyncTask task = new WeatherAsyncTask(maker, mMap, WeatherMapsActivity.this,
                lastLocation.latitude, lastLocation.longitude);
    }

    public void markerClick() {
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                marker.showInfoWindow();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
