package com.example.jcca.teseandroid.Misc;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.example.jcca.teseandroid.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class showOnMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String lat;
    String lng;
    String species;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_on_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        lat = getIntent().getStringExtra("Lat");
        lng = getIntent().getStringExtra("Long");
        species=getIntent().getStringExtra("Species");
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
        mMap.addMarker(new MarkerOptions().position(sydney).title(species));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setMaxZoomPreference(20);
        mMap.setMinZoomPreference(10);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }
}
