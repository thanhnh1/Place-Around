package com.nguyenthanh.placearound;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.nguyenthanh.placearound.model_weather.OpenWeatherJSon;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 03/05/2016.
 */
public class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Activity conText;

    private double lngLatitude;

    private double lngLongitude;

    private int iCnt;

    private Marker mMaker = null;

    private OpenWeatherJSon openWeatherJson = null;

    private Bitmap myBitmap = null;

    private NumberFormat nbFormat = new DecimalFormat("#0.0");

 //   private ScrollView srDetail = null;

    private TextView tvTemperature;

    private TextView tvAddress;

    private ImageView imageWeather;

    private TextView tvTemmaxmin;

    private TextView tvWind;

  //  private TextView tvPressure;

    private TextView tvHumidty;

    private TextView tvSunrise;

    private TextView tvSunset;

    private TextView tvDescription;

    private TextView tvCnt;
    private LinearLayout llDetail;

    private static ArrayList<Marker> sMarkers = new ArrayList<>();

    public MyInfoWindowAdapter(Activity context) {
        this.conText = context;
    }

    public MyInfoWindowAdapter(OpenWeatherJSon openWeatherJSon, Bitmap myBitmap,
                               Marker maker, Activity context) {
        this(context);
        this.mMaker = maker;
        this.openWeatherJson = openWeatherJSon;
        this.myBitmap = myBitmap;
    }

    public MyInfoWindowAdapter(OpenWeatherJSon openWeatherJSon, Bitmap myBitmap, Marker maker,
                               Activity context, double latitude, double longitude) {
        this(openWeatherJSon, myBitmap, maker, context);
        this.lngLatitude = latitude;
        this.lngLongitude = longitude;
    }

    public MyInfoWindowAdapter(OpenWeatherJSon openWeatherJSon, Bitmap myBitmap, Marker maker,
                               Activity context, double latitude, double longitude, int cnt) {

        this(openWeatherJSon, myBitmap, maker, context);
        this.lngLatitude = latitude;
        this.lngLongitude = longitude;
        this.iCnt = cnt;
    }

    private void inIt(View v) {
        tvTemperature = (TextView) v.findViewById(
                R.id.activity_weather_tv_temp);
        tvAddress = (TextView) v.findViewById(
                R.id.activity_weather_tv_address);
        imageWeather = (ImageView) v.findViewById(
                R.id.activity_weather_current_location_iv_weather);
        tvTemmaxmin = (TextView) v.findViewById(
                R.id.activity_weather_current_location_tv_temps);
        tvWind = (TextView) v.findViewById(
                R.id.activity_weather_current_location_tv_wind);
//        tvPressure = (TextView) v.findViewById(
//                R.id.activity_weather_current_location_tv_pressure);
        tvHumidty = (TextView) v.findViewById(
                R.id.activity_weather_current_location_tv_humidity);
        tvSunrise = (TextView) v.findViewById(
                R.id.activity_weather_current_location_tv_sunrise);
        tvSunset = (TextView) v.findViewById(
                R.id.activity_weather_current_location_tv_sunset);
        tvDescription = (TextView) v.findViewById(
                R.id.activity_weather_current_location_tv_description);
        llDetail = (LinearLayout) v.findViewById(
                R.id.activity_weather_current_location_sv_srollview);
    }

    public void currentDay(View v) {
        double dbTemperature = openWeatherJson.getMain().getTemp() - 273.15;
        String maxTemp = nbFormat.format(openWeatherJson.getMain()
                .getTemp_max() - 273.15) + "°C";
        String minTemp = nbFormat.format(openWeatherJson.getMain()
                .getTemp_min() - 273.15) + "°C";
        String tempMaxMin = maxTemp + " " + minTemp;
        String wind = openWeatherJson.getWind().getSpeed() + " m/s";
        String pressure = openWeatherJson.getMain().getPressure() + " hpa";
        String humidity = openWeatherJson.getMain().getHumidity() + " %";
        Date timeSunrise = new Date(openWeatherJson.getSys().getSunrise() * 1000);
        String strSunrise = timeSunrise.getHours() + ":" + timeSunrise.getMinutes() + " AM";
        Date timeSunSet = new Date(openWeatherJson.getSys().getSunset() * 1000);
        String strSunset = timeSunSet.getHours() + ":" + timeSunSet.getMinutes();
        tvTemperature.setText(nbFormat.format(dbTemperature) + "°C");
        String strDescription = openWeatherJson.getWeather().get(0).getDescription();
        tvDescription.setText(strDescription);
        imageWeather.setImageBitmap(myBitmap);
        tvTemmaxmin.setText(tempMaxMin);
        tvWind.setText(wind);
   //     tvPressure.setText(pressure);
        tvHumidty.setText(humidity);
        tvSunrise.setText(strSunrise);
        tvSunset.setText(strSunset);
        v.findViewById(R.id.activity_weather_current_location_iv_wind);
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this.conText, Locale.getDefault());
            addresses = geocoder.getFromLocation(lngLatitude, lngLongitude, 1);
            Address address = null;
            if (addresses.size() > 0) {
                address = addresses.get(0);
            }
            if (address != null) {
                tvAddress.setText(address.getAddressLine(0));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(conText, WeatherMapsActivity.class);
        intent.putExtra("Temp", dbTemperature);
    }

    public void ifSetDay(View v) {
        double dbTemperature = openWeatherJson.getList().get(iCnt - 1)
                .getTemp().getDay() - 273.15;
        String maxtemp = nbFormat.format(openWeatherJson.getList().get(iCnt - 1)
                .getTemp().getMax() - 273.15) + "°C";
        String minTemp = nbFormat.format(openWeatherJson.getList().get(iCnt - 1)
                .getTemp().getMin() - 273.15) + "°C";
        String tempMaxMin = maxtemp + " /" + minTemp;
        String strWind = openWeatherJson.getList().get(iCnt - 1).getSpeed() + " m/s";
        String strPressure = openWeatherJson.getList().get(iCnt - 1).getPressure() + " hpa";
        String strHumidity = openWeatherJson.getList().get(iCnt - 1).getHumidity() + " %";
        String strDescription = openWeatherJson.getList().get(iCnt - 1)
                .getWeather().get(0).getDescription();
        tvDescription.setText(strDescription);
        tvTemperature.setText(nbFormat.format(dbTemperature) + "°C");
        imageWeather.setImageBitmap(myBitmap);
        tvTemmaxmin.setText(tempMaxMin);
        tvWind.setText(strWind);
  //      tvPressure.setText(strPressure);
        tvHumidty.setText(strHumidity);
        v.findViewById(R.id.activity_weather_current_location_iv_wind);
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this.conText, Locale.getDefault());
            addresses = geocoder.getFromLocation(lngLatitude, lngLongitude, 1);
            Address address = null;
            if (addresses.size() > 0) {
                address = addresses.get(0);
            }
            if (address != null) {
                tvAddress.setText(address.getAddressLine(0));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getInfoWindow(Marker marker) {
        View v = this.conText.getLayoutInflater().inflate(
                R.layout.activity_weather_current_location, null);
        inIt(v);
        if (sMarkers.contains(marker)) {
        } else {
            llDetail.setVisibility(View.GONE);
            sMarkers.add(marker);
        }
        if (iCnt == 0) {
            currentDay(v);
        } else {
            ifSetDay(v);
        }
        // v.setBackgroundResource(R.drawable.bg_weather_current_layout);

        v.setBackground(null);
        return v;
    }

    @Override
    public View getInfoContents(Marker marker) {
        // marker.getPosition();
        return null;
    }
}
