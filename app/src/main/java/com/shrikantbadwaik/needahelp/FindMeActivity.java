package com.shrikantbadwaik.needahelp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FindMeActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_me);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        MyService gps= new MyService(this);
        //GPSTracker gps = new GPSTracker(this);

        if(gps.canGetLocation())
        //if(gps.getIsGPSTrackingEnabled())
        {
            //LatLng myLocation = new LatLng(gps.getLatitude(),gps.getLongitude());
            LatLng myLocation= new LatLng(18.4899141,73.8674509);
            mMap.addMarker(new MarkerOptions().position(myLocation).title("My Location"));//+gps.getLocality(this)+" "+gps.getPostalCode(this)+" "+gps.getCountryName(this)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
        }
        else {
            gps.showSettingsAlert();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            startActivity(new Intent(this,HomePageActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void refreshLocation(View view)
    {
        mMap=mapFragment.getMap();
        //MyService gps= new MyService(this);
        GPSTracker gps = new GPSTracker(this);
        //LatLng myLocation = new LatLng(gps.getLatitude(),gps.getLongitude());
        LatLng myLocation= new LatLng(18.4899141,73.8674509);
        mMap.addMarker(new MarkerOptions().position(myLocation).title("My Location"));//+ gps.getLocality(this) + " " + gps.getPostalCode(this) + " "+gps.getCountryName(this)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,15));
    }
}
