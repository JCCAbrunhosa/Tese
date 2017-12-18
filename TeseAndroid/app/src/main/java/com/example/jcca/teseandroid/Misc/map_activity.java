package com.example.jcca.teseandroid.Misc;

import android.media.Image;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.example.jcca.teseandroid.DataObjects.ImageInfo;
import com.example.jcca.teseandroid.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class map_activity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_activity);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Species");
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



        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    for(DataSnapshot snap: snapshot.getChildren()){
                        mDatabase.orderByChild("species");
                        ImageInfo photo = snap.getValue(ImageInfo.class);
                        LatLng sydney = new LatLng(photo.getLocation().getLatitude(), photo.getLocation().getLongitude());
                        mMap.addMarker(new MarkerOptions().position(sydney).title(photo.getSpecies()));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // Add a marker in Sydney and move the camera
        mMap.setMaxZoomPreference(20);
        mMap.setMinZoomPreference(10);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }
}
