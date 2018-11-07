package com.example.jcca.teseandroid.Misc;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.jcca.teseandroid.Gallery.galleryFeed;
import com.example.jcca.teseandroid.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class chooseLocation extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.greenTese)));

        getSupportActionBar().setTitle(R.string.chooseLocation);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
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

        // Add a marker and move the camera
        mMap.setMaxZoomPreference(21);
        mMap.setMinZoomPreference(10);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        LatLng sydney = new LatLng(Double.parseDouble("40.692034"), Double.parseDouble("-8.673691"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(final LatLng latLng) {

                Toast.makeText(
                        chooseLocation.this,
                        "Lat : " + latLng.latitude + " , "
                                + "Long : " + latLng.longitude,
                        Toast.LENGTH_LONG).show();


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(chooseLocation.this);
                LayoutInflater li = LayoutInflater.from(chooseLocation.this);
                View promptsView = li.inflate(R.layout.prompts, null);

                alertDialogBuilder
                        .setCancelable(false)
                        .setTitle("Confirmar Posição")
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        Intent intent = new Intent();
                                        Bundle bundle= new Bundle();
                                        bundle.putString("locationLat", String.valueOf(latLng.latitude));
                                        bundle.putString("locationLng", String.valueOf(latLng.longitude));
                                        intent.putExtras(bundle);

                                        setResult(Activity.RESULT_OK, intent);
                                        finish();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();


                alertDialog.show();

            }
        });




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == android.R.id.home){
            startActivity(new Intent(this, galleryFeed.class));
        }



        return super.onOptionsItemSelected(item);
    }
}
