package com.nguyenthanh.placearound.model;

import android.util.Log;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;

public class MapPlaces {

    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    private static final String API_KEY = "AIzaSyAPir-D44kZWvmsOJEkCK6jtQC4RyQiqDE";

    private static final String PLACES_SEARCH_URL =
            "https://maps.googleapis.com/maps/api/place/search/json?";

    private static final String PLACES_TEXT_SEARCH_URL =
            "https://maps.googleapis.com/maps/api/place/search/json?";

    private static final String PLACES_DETAILS_URL =
            "https://maps.googleapis.com/maps/api/place/details/json?";

    private double latiTude;

    private double longiTude;

    private double radiUs;

    public Places searCh(double latitude, double longitude, double radius, String types)
            throws Exception {

        this.latiTude = latitude;
        this.longiTude = longitude;
        this.radiUs = radius;
        try {
            HttpRequestFactory httpRequestFactory = createRequestFactory(HTTP_TRANSPORT);
            HttpRequest request = httpRequestFactory
                    .buildGetRequest(new GenericUrl(PLACES_SEARCH_URL));
            request.getUrl().put("key", API_KEY);
            request.getUrl().put("location", latitude + "," + longitude);
            request.getUrl().put("radius", radius); // in meters
            request.getUrl().put("sensor", "false");
            if (types != null) {
                request.getUrl().put("types", types);
            }
            Places list = request.execute().parseAs(Places.class);
            // Check log cat for places response status
            Log.d("Places Status", "" + list.status);
            return list;
        } catch (HttpResponseException e) {
            Log.e("Error:", e.getMessage());
            return null;
        }
    }

    public static HttpRequestFactory createRequestFactory(
            final HttpTransport transport) {
        return transport.createRequestFactory(new HttpRequestInitializer() {
            public void initialize(HttpRequest request) {
                HttpHeaders headers = new HttpHeaders();
                headers.setUserAgent("Places");
                request.setHeaders(headers);
                JsonObjectParser parser = new JsonObjectParser(new JacksonFactory());
                request.setParser(parser);
            }
        });
    }

    public PlaceDetails getPlaceDetails(String reference) throws Exception {
        try {
            HttpRequestFactory httpRequestFactory = createRequestFactory(HTTP_TRANSPORT);
            HttpRequest request = httpRequestFactory
                    .buildGetRequest(new GenericUrl(PLACES_DETAILS_URL));
            request.getUrl().put("key", API_KEY);
            request.getUrl().put("reference", reference);
            request.getUrl().put("sensor", "false");
            PlaceDetails place = request.execute().parseAs(PlaceDetails.class);
            return place;
        } catch (HttpResponseException e) {
            Log.e("Error...", e.getMessage());
            throw e;
        }
    }
}
