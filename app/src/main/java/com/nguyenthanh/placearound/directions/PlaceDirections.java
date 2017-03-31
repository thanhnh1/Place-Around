package com.nguyenthanh.placearound.directions;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.nguyenthanh.placearound.R;
import com.nguyenthanh.placearound.Utils;
import com.nguyenthanh.placearound.components.AlertDialogManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlaceDirections {

    private AlertDialogManager aLert = new AlertDialogManager();

    private String url;

    private Context conText;

    private ProgressDialog pDialog;

    private GoogleMap googleMap;

    private LatLng fromPositon;

    private LatLng toPosition;

    private byte typeWay;

    public PlaceDirections(Context context, GoogleMap googleMap, LatLng from, LatLng to,
                           byte typeWay) {
        this.conText = context;
        this.googleMap = googleMap;
        this.fromPositon = from;
        this.toPosition = to;
        this.typeWay = typeWay;

        pDialog = new ProgressDialog(context);
        url = getMapsApiDirectionsUrl();
        this.googleMap.clear();
        LoadDirections directions = new LoadDirections();
        directions.execute(url);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(from,
                14));
    }

    private String getMapsApiDirectionsUrl() {
        // add more mode here
        String waypoints = "origin=" + this.fromPositon.latitude + "," + this.fromPositon.longitude
                        + "&" + "destination=" + toPosition.latitude + "," + toPosition.longitude;
        String routerType;

        if (Utils.sKeyWay == Utils.WALK) {
            routerType = "mode=walking";
        } else if (Utils.sKeyWay == Utils.BICYCLE) {
            routerType = "mode=bicycling";
        } else {
            routerType = "mode=driving";
        }

        //routerType = "mode=driving";

        String sensor = "sensor=false";
        String params = waypoints + "&" + sensor + "&" + routerType;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + params;
        return url;
    }

    private void addMarkers() {
        if (googleMap != null) {
            googleMap.addMarker(new MarkerOptions()
                    .position(fromPositon)
                    .title("Me")
                    .snippet("Local of me")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_current)));
            googleMap.addMarker(new MarkerOptions()
                    .position(toPosition)
                    .title(Utils.sStrDestinaton)
                    .snippet(Utils.sStrSnippet)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_possion)));
        }
    }

    private class LoadDirections extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpUrlConnection http = new HttpUrlConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            addMarkers();
            new ParserTask().execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,
            String>>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                ParserJSON parser = new ParserJSON();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;
            // traversing through route
            for (int i = 0; i < routes.size(); i++) {
                points = new ArrayList<LatLng>();
                polyLineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = routes.get(i);
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }
                polyLineOptions.addAll(points);
                polyLineOptions.width(10);
                if (Utils.sKeyWay == Utils.WALK) {
                    polyLineOptions.color(Color.RED);
                }
                else if (Utils.sKeyWay == Utils.BICYCLE) {
                    polyLineOptions.color(Color.BLUE);
                }
                else {
                    polyLineOptions.color(Color.GREEN);
                }
            }
            if (polyLineOptions == null) {
                Toast toast = Toast.makeText(conText, "Dont have way for this type",
                        Toast.LENGTH_SHORT);
                toast.show();
            } else {
                googleMap.addPolyline(polyLineOptions);
            }
        }
    }
}
