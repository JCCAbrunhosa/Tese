package com.example.jcca.teseandroid.Misc;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.jcca.teseandroid.DataObjects.ImageInfo;
import com.example.jcca.teseandroid.Gallery.galleryFeed;
import com.example.jcca.teseandroid.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class map_activity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    DatabaseReference mDatabase;

    SearchView searchView;

    ArrayList<Marker> markers;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_activity);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.greenTese)));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        markers=new ArrayList<>();


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
        final Intent goTo = new Intent(map_activity.this, speciesDetails_activity.class);



        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LatLng pos=null;
                ImageInfo photo=null;
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    for(DataSnapshot snap: snapshot.getChildren()){
                        if(!snap.getKey().toString().matches("description") && !snap.getKey().toString().matches("vulgar")) {
                                photo = snap.getValue(ImageInfo.class);
                                pos = new LatLng(photo.getLocation().getLatitude(), photo.getLocation().getLongitude());

                        }
                    }
                        Marker marker = mMap.addMarker(new MarkerOptions().position(pos).title(photo.getSpecies()));
                        marker.setTag(photo);
                        marker.setIcon(BitmapDescriptorFactory.defaultMarker(new Random().nextInt(360)));
                        markers.add(marker);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // Add a marker in Sydney and move the camera
        mMap.setMaxZoomPreference(21);
        mMap.setMinZoomPreference(15);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Bundle toSend = new Bundle();
                ImageInfo photo = (ImageInfo) marker.getTag();
                toSend.putString("URL", photo.getUrl());
                toSend.putString("Species", photo.getSpecies());
                toSend.putString("Eco", photo.getEco());
                toSend.putString("Vulgar", photo.getVulgar());
                goTo.putExtras(toSend);
                startActivity(goTo);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.guide_activity, menu);

        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                Log.d("Markers", String.valueOf(markers.size()));

                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mMap.clear();
                        ImageInfo photo=null;
                        LatLng pos=null;
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Log.d("SnapshotToString", snapshot.getKey());
                            if(snapshot.getKey().toLowerCase().contains(newText.toLowerCase())){
                                Log.d("Contains",String.valueOf(snapshot.toString().toLowerCase().contains(newText.toLowerCase())));
                                for(DataSnapshot snap: snapshot.getChildren()){
                                    if(!snap.getKey().toString().matches("description") && !snap.getKey().toString().matches("vulgar")) {
                                        photo= snap.getValue(ImageInfo.class);
                                        pos = new LatLng(photo.getLocation().getLatitude(), photo.getLocation().getLongitude());

                                    }
                                }
                                Marker marker = mMap.addMarker(new MarkerOptions().position(pos).title(photo.getSpecies()));
                                marker.setTag(photo);
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }



        return super.onOptionsItemSelected(item);
    }
}
