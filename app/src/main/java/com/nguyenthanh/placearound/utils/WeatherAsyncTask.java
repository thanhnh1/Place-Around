package com.nguyenthanh.placearound.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.nguyenthanh.placearound.MyInfoWindowAdapter;
import com.nguyenthanh.placearound.R;
import com.nguyenthanh.placearound.model_weather.OpenWeatherJSon;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeatherAsyncTask extends AsyncTask<Void, Void, OpenWeatherJSon> {

    private LinearLayout liNear;

    private ProgressDialog prgDialog;

    private Activity aActivity;

    private TypePrediction typePrediction;

    private String strQq;

    private int iCnt = 0;

    private double lngLatitude;

    private double lngLongitude;

    private NumberFormat nFormat = new DecimalFormat("#0.0");

    private Bitmap myBitmap = null;

    private Marker mMarker;

    private GoogleMap googleMap = null;

    private TextView tvTemp;

    private TextView tvAddress;

    private TextView tvWind;

    private TextView tvTempmaxmin;

    private TextView tvPressure;

    private TextView tvHumidity;

    private TextView tvSunrise;

    private TextView tvSunset;

    private TextView tvVisibility;

    private TextView tvDescription;

    private ImageView ivWeather;

    private String urlIcon;

    private LinearLayout llBackground;

    public WeatherAsyncTask(Activity activity, String q) {
        this.aActivity = activity;
        this.typePrediction = TypePrediction.ADDRESS_NAME;
        this.strQq = q;
        this.prgDialog = new ProgressDialog(activity);
        this.prgDialog.setTitle("Loading ... ");
        this.prgDialog.setMessage("Please wait...");
        this.prgDialog.setCancelable(true);
    }

    public WeatherAsyncTask(Activity activity, String q, int cnt) {
        this.aActivity = activity;
        this.typePrediction = TypePrediction.ADDRESS_NAME_DAILY;
        this.strQq = q;
        this.iCnt = cnt;
        this.prgDialog = new ProgressDialog(activity);
        this.prgDialog.setTitle("Loading ... ");
        this.prgDialog.setMessage("Please wait...");
        this.prgDialog.setCancelable(true);
    }

    public WeatherAsyncTask(Activity activity, double latitude, double longitude) {
        this.aActivity = activity;
        this.typePrediction = TypePrediction.LATITUDE_LONGITUDE;
        this.lngLatitude = latitude;
        this.lngLongitude = longitude;
        this.prgDialog = new ProgressDialog(activity);
        this.prgDialog.setTitle("Loading ...");
        this.prgDialog.setMessage("Please wait ...");
        this.prgDialog.setCancelable(true);
    }

    public WeatherAsyncTask(Activity activity, double latitude, double longitude, int cnt) {
        this.aActivity = activity;
        this.typePrediction = TypePrediction.LATITUDE_LONGITUDE_DAILY;
        this.lngLatitude = latitude;
        this.lngLongitude = longitude;
        this.iCnt = cnt;
        this.prgDialog = new ProgressDialog(activity);
        this.prgDialog.setTitle("Loading ...");
        this.prgDialog.setMessage("Please wait...");
        this.prgDialog.setCancelable(true);
    }

    public WeatherAsyncTask(Marker marker, GoogleMap map, Activity activity, double latitude,
                            double longitude) {
        this(activity, latitude, longitude);
        this.mMarker = marker;
        this.googleMap = map;
    }

    public WeatherAsyncTask(Marker marker, GoogleMap map, Activity activity, double latitude,
                            double longitude, int cnt) {
        this(activity, latitude, longitude, cnt);
        this.mMarker = marker;
        this.googleMap = map;
    }

    //Automatically call when the process is execute.
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.prgDialog.show();
    }

    @Override
    protected OpenWeatherJSon doInBackground(Void... params) {
        OpenWeatherJSon openWeatherJson = null;
        if (typePrediction == TypePrediction.LATITUDE_LONGITUDE) {
            openWeatherJson = OpenWeatherMapAPI.wtPrediction(lngLatitude, lngLongitude);
        } else {
            if (typePrediction == TypePrediction.ADDRESS_NAME) {
                openWeatherJson = OpenWeatherMapAPI.wtPrediction(strQq);
            } else if (typePrediction == TypePrediction.ADDRESS_NAME_DAILY) {
                openWeatherJson = OpenWeatherMapAPI.predictionDaily(strQq, iCnt);
            } else if (typePrediction == TypePrediction.LATITUDE_LONGITUDE_DAILY) {
                openWeatherJson = OpenWeatherMapAPI.predictionDaily(lngLatitude,
                        lngLongitude, iCnt);
            }
        }
        try {
            if (iCnt == 0) {
                String idIcon = openWeatherJson.getWeather().get(0).getIcon();
                String urlIcon = "http://openweathermap.org/img/w/" + idIcon + ".png";
                URL urlConnection = new URL(urlIcon);
                HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                myBitmap = BitmapFactory.decodeStream(input);
            } else {
                String idIcon = openWeatherJson.getList().get(iCnt - 1).getWeather()
                        .get(0).getIcon();
                String urlIcon = "http://openweathermap.org/img/w/" + idIcon + ".png";
                URL urlConnection = new URL(urlIcon);
                HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                myBitmap = BitmapFactory.decodeStream(input);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return openWeatherJson;
    }

    //Runs on the UI thread after publishProgress(Progress...) is invoked.
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    //Runs on the UI thread after doInBackground(Params...).
    @Override
    protected void onPostExecute(OpenWeatherJSon openWeatherJSon) {
        super.onPostExecute(openWeatherJSon);
        if (googleMap != null) {
            if (iCnt != 0) {
                googleMap.setInfoWindowAdapter(new MyInfoWindowAdapter(openWeatherJSon, myBitmap,
                        mMarker, this.aActivity, lngLatitude, lngLongitude, iCnt));
                mMarker.showInfoWindow();
                this.prgDialog.dismiss();
                return;
            } else {
                googleMap.setInfoWindowAdapter(new MyInfoWindowAdapter(openWeatherJSon, myBitmap,
                        mMarker, this.aActivity, lngLatitude, lngLongitude));
                mMarker.showInfoWindow();
                this.prgDialog.dismiss();
                return;
            }
        }

        inIt();
        if (iCnt == 0) {
            currentWeather(openWeatherJSon);
        } else if (iCnt != 0) {
            aDayWeather(openWeatherJSon);
        }
    }

    public void aDayWeather(OpenWeatherJSon openWeatherJSon) {
        String strTemperature = nFormat.format(openWeatherJSon.getList().get(iCnt - 1).getTemp()
                .getDay() - 273.15) + " °C ";
        String temMinMax = nFormat.format(openWeatherJSon.getList().get(iCnt - 1).getTemp()
                .getMax() - 273.15) + "°C \\" + nFormat.format(openWeatherJSon.getList()
                .get(iCnt - 1).getTemp().getMin() - 273.15) + "°C";
        String strWind = openWeatherJSon.getList().get(iCnt - 1).getSpeed() + " m/s";
        String strPressure = openWeatherJSon.getList().get(iCnt - 1).getPressure() + " hpa";
        String strHumidity = openWeatherJSon.getList().get(iCnt - 1).getHumidity() + " %";
        String strDescription = openWeatherJSon.getList().get(iCnt - 1).getWeather()
                .get(0).getDescription();
        tvDescription.setText(strDescription);
        tvTemp.setText(strTemperature);
        ivWeather.setImageBitmap(myBitmap);
        tvTempmaxmin.setText(temMinMax);
        tvWind.setText(strWind);
        tvHumidity.setText(strHumidity);
   //     tvPressure.setText(strPressure);
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this.aActivity, Locale.getDefault());
            if (typePrediction == TypePrediction.LATITUDE_LONGITUDE_DAILY) {
                addresses = geocoder.getFromLocation(lngLatitude, lngLongitude, 1);
            } else {
                addresses = geocoder.getFromLocationName(strQq, 1);
            }
            Address address = null;
            if (addresses.size() > 0) {
                address = addresses.get(0);
            }
            tvAddress.setText(strQq);
            if (address != null) {
                if (typePrediction == TypePrediction.LATITUDE_LONGITUDE_DAILY) {
                    tvAddress.setText(address.getAddressLine(0));
                } else {
                    tvAddress.setText(strQq);
                }
            }
        } catch (IOException e) {
        }

        if(Integer.parseInt(temMinMax.replaceAll("[\\D]", "")) <=10 ){
            llBackground.setBackgroundResource(R.drawable.ic_menu_atm_ldpi);
        }
        else
            llBackground.setBackgroundResource(R.drawable.ic_menu_atm_ldpi);
        this.prgDialog.dismiss();
    }

    public void currentWeather(OpenWeatherJSon openWeatherJSon) {
        String temperature = nFormat.format(openWeatherJSon.getMain()
                .getTemp() - 273.15) + " °C ";
        String temMinMax = nFormat.format(openWeatherJSon.getMain().getTemp_max() - 273.15) +
                "°C / " + nFormat.format(openWeatherJSon.getMain()
                .getTemp_min() - 273.15) + "°C";
        String wind = openWeatherJSon.getWind().getSpeed() + " m/s";
        String visibility = openWeatherJSon.getVisibility() + " km";
        String pressure = openWeatherJSon.getMain().getPressure() + " hpa";
        String humidity = openWeatherJSon.getMain().getHumidity() + " %";
        Date timeSunrise = new Date(openWeatherJSon.getSys().getSunrise() * 1000);
        String strSunrise = timeSunrise.getHours() + ":" + timeSunrise.getMinutes() + " AM";
        Date timeSunSet = new Date(openWeatherJSon.getSys().getSunset() * 1000);
        String strSunset = timeSunSet.getHours() + ":" + timeSunSet.getMinutes() + " PM";
        String strDescription = openWeatherJSon.getWeather().get(0).getDescription();
        tvDescription.setText(strDescription);
        tvTemp.setText(temperature);
        ivWeather.setImageBitmap(myBitmap);
        tvTempmaxmin.setText(temMinMax);
        tvWind.setText(wind);
        tvHumidity.setText(humidity);
 //       tvPressure.setText(pressure);
        tvSunrise.setText(strSunrise);
        tvSunset.setText(strSunset);
        tvVisibility.setText(visibility);
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this.aActivity, Locale.getDefault());
            if (typePrediction == TypePrediction.LATITUDE_LONGITUDE) {
                addresses = geocoder.getFromLocation(lngLatitude, lngLongitude, 1);
            } else {
                addresses = geocoder.getFromLocationName(strQq, 1);
            }
            Address address = null;
            if (addresses.size() > 0) {
                address = addresses.get(0);
            }
            tvAddress.setText(strQq);
            if (address != null) {
                if (typePrediction == TypePrediction.LATITUDE_LONGITUDE) {
                    tvAddress.setText(address.getAddressLine(0));
                } else {
                    tvAddress.setText(strQq);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if((Float.parseFloat(temperature.replaceAll("°C", ""))) <= 10 ){
            llBackground.setBackgroundResource(R.drawable.ic_menu_atm_ldpi);
        }
        else if((Float.parseFloat(temperature.replaceAll("°C", ""))) >= 32 ){
            llBackground.setBackgroundResource(R.drawable.ic_menu_atm_ldpi);
        }
        else{
            llBackground.setBackgroundResource(R.drawable.ic_menu_atm_ldpi);
        }
        this.prgDialog.dismiss();
    }


    public void inIt() {
        tvTemp = (TextView) aActivity.findViewById(
                R.id.activity_weather_tv_temp);
        tvAddress = (TextView) aActivity.findViewById(
                R.id.activity_weather_tv_address);
        ivWeather = (ImageView) aActivity.findViewById(
                R.id.activity_weather_current_location_iv_weather);
        tvTempmaxmin = (TextView) aActivity.findViewById(
                R.id.activity_weather_current_location_tv_temps);
        tvWind = (TextView) aActivity.findViewById(
                R.id.activity_weather_current_location_tv_wind);
//        tvPressure = (TextView) aActivity.findViewById(
//                R.id.activity_weather_current_location_tv_pressure);
        tvHumidity = (TextView) aActivity.findViewById(
                R.id.activity_weather_current_location_tv_humidity);
        tvSunrise = (TextView) aActivity.findViewById(
                R.id.activity_weather_current_location_tv_sunrise);
        tvSunset = (TextView) aActivity.findViewById(
                R.id.activity_weather_current_location_tv_sunset);
        tvVisibility = (TextView) aActivity.findViewById(
                R.id.activity_weather_current_location_tv_visibility);
        tvDescription = (TextView) aActivity.findViewById(
                R.id.activity_weather_current_location_tv_description);
        liNear = (LinearLayout) aActivity.findViewById(
                R.id.activity_weather_current_location_ll_info);
        llBackground = (LinearLayout)aActivity.findViewById(R.id.activity_weather_current_location_ll_info);
    }
}
