package com.nguyenthanh.placearound;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.nguyenthanh.placearound.directions.PlaceDirections;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Administrator on 3/22/2017.
 */
@EActivity(R.layout.activity_place_details)
public class PlaceDetailsActivity extends AppCompatActivity {

    @ViewById(R.id.lat1)
    TextView tv1;
    @ViewById(R.id.long1)
    TextView tv2;

    @ViewById(R.id.title1)
    TextView tv3;

    @ViewById(R.id.id1)
    TextView tv4;

    @ViewById(R.id.btn1)
    Button btn;

    private LatLng latLng = null;
    private PlaceDirections direcTions;
    private GoogleMap mMap;

    @AfterViews
    void afterview() {

        String s1 = getIntent().getStringExtra("address");
        String s2 = getIntent().getStringExtra("title");
        String s3 = getIntent().getStringExtra("lat");
        String s4 = getIntent().getStringExtra("lon");


        tv1.setText(s1);
        tv2.setText(s2);
        tv3.setText(s3);
        tv4.setText(s4);

    }

    @Click(R.id.btn1)
    void directionClicked() {

        if (latLng != null) {
            Intent intent = new Intent();
            //intent.putExtra("lat", latLng.latitude);
            //intent.putExtra("lon", latLng.longitude);
            intent.getStringExtra("lat");
            intent.getStringExtra("lon");
            setResult(RESULT_OK, intent);
            finish();
            onBackPressed();
        } else {
            Toast.makeText(this, "Please search for a place first"
                    , Toast.LENGTH_SHORT).show();
        }
    }
}
