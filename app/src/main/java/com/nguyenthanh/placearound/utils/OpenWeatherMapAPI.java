package com.nguyenthanh.placearound.utils;

import com.google.gson.Gson;
import com.nguyenthanh.placearound.model_weather.OpenWeatherJSon;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class OpenWeatherMapAPI {
    public static OpenWeatherJSon wtPrediction(String q) {
        try {
            String location = URLEncoder.encode(q, "UTF-8");
            URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + location +
                    "&appid=f7e91768c0424509258fd87835f099a8");
            InputStreamReader reader = new InputStreamReader(url.openStream(), "UTF-8");
            OpenWeatherJSon results = new Gson().fromJson(reader, OpenWeatherJSon.class);
            String idIcon = results.getWeather().get(0).getIcon().toString();
            String urlIcon = "http://openweathermap.org/img/w/" + idIcon + ".png";
            URL urlImage = new URL(urlIcon);
            return results;
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }
        return null;
    }

    public static OpenWeatherJSon predictionDaily(String q, int cnt) {
        try {
            String location = URLEncoder.encode(q, "UTF-8");
            URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=" +
                    location + "&cnt=" + cnt + "&appid=f7e91768c0424509258fd87835f099a8");
            InputStreamReader reader = new InputStreamReader(url.openStream(), "UTF-8");
            OpenWeatherJSon results = new Gson().fromJson(reader, OpenWeatherJSon.class);
            String idIcon = results.getList().get(cnt - 1).getWeather().get(0).getIcon().toString();
            // String idIcon = results.getWeather().get(0).getIcon().toString();
            String urlIcon = "http://openweathermap.org/img/w/" + idIcon + ".png";
            URL urlImage = new URL(urlIcon);
            return results;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }
        return null;
    }

    public static OpenWeatherJSon wtPrediction(double lat, double lon) {
        try {

            URL url = new URL("http://api.openweathermap.org/data/2.5/weather?lat=" + lat +
                    "&lon=" + lon + "&appid=f7e91768c0424509258fd87835f099a8");
            InputStreamReader reader = new InputStreamReader(url.openStream(), "UTF-8");
            OpenWeatherJSon results = new Gson().fromJson(reader, OpenWeatherJSon.class);
            String idIcon = results.getWeather().get(0).getIcon().toString();
            String urlIcon = "http://openweathermap.org/img/w/" + idIcon + ".png";
            URL urlImage = new URL(urlIcon);
            return results;
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }
        return null;
    }

    public static OpenWeatherJSon predictionDaily(double lat, double lon, int cnt) {
        try {

            URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?lat=" + lat +
                    "&lon=" + lon + "&cnt=" + cnt + "&appid=f7e91768c0424509258fd87835f099a8");
            InputStreamReader reader = new InputStreamReader(url.openStream(), "UTF-8");
            OpenWeatherJSon results = new Gson().fromJson(reader, OpenWeatherJSon.class);
            String idIcon = results.getList().get(cnt - 1).getWeather().get(0).getIcon().toString();
            String urlIcon = "http://openweathermap.org/img/w/" + idIcon + ".png";
            URL urlImage = new URL(urlIcon);
            return results;
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }
        return null;
    }
}
