package com.example.jcca.teseandroid.Misc;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.example.jcca.teseandroid.Adapters.RecyclerViewAdapter;
import com.example.jcca.teseandroid.DataObjects.ImageInfo;
import com.example.jcca.teseandroid.Gallery.galleryFeed;
import com.example.jcca.teseandroid.Gallery.guide_activity;
import com.example.jcca.teseandroid.Gallery.otherPhotosGallery;
import com.example.jcca.teseandroid.Gallery.photosToReview;
import com.example.jcca.teseandroid.Glide_Module.GlideApp;
import com.example.jcca.teseandroid.Login_Registering.LoginActivity;
import com.example.jcca.teseandroid.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class photoDetails_activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView auth;
    private TextView ec;
    private TextView description;
    private TextView specie;
    private ImageView photo;
    private TextView vlgar;

    public DatabaseReference mDatabase;
    public DatabaseReference users;
    public DatabaseReference base;

    public List<ImageInfo> list= new ArrayList<>();

    RecyclerView similar;
    private RecyclerView.Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_details_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);





        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/



        auth = findViewById(R.id.photoAuthor);
        ec = findViewById(R.id.photoEco);
        description = findViewById(R.id.speciesName);
        specie = findViewById(R.id.photoSpecies);
        photo = findViewById(R.id.photoDasSpecies);
        vlgar = findViewById(R.id.vulgar);

        String author = getIntent().getStringExtra("Author");
        String eco= getIntent().getStringExtra("Eco");
        String desc = getIntent().getStringExtra("Desc");
        final String species  = getIntent().getStringExtra("Species");
        final String url= getIntent().getStringExtra("URL");
        final String lat = getIntent().getStringExtra("Lat");
        final String lng = getIntent().getStringExtra("Long");
        final String date = getIntent().getStringExtra("Date");
        final String uid = getIntent().getStringExtra("UID");
        final String vulgar = getIntent().getStringExtra("Vulgar");

        //Image pops up when user clicks on it
        final ImagePopup imagePopup = new ImagePopup(this);
        imagePopup.initiatePopup(photo.getDrawable());
        //Populates pop up with image from glide - need to change this to a local fetch
        imagePopup.initiatePopupWithGlide(url);
        imagePopup.setFullScreen(true);
        imagePopup.setImageOnClickClose(true);
        imagePopup.setHideCloseIcon(false);

        GlideApp.with(getApplicationContext()).load(url).into(photo);

        Log.d("Eco:", eco);

        auth.setText(author.toString());
        ec.setText(eco.toString());
        //description.setText(desc.toString());
        specie.setText(species.toString());
        vlgar.setText(vulgar.toString());


        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imagePopup.viewPopup();
            }
        });

        setTitle(species);

        if(specie.getText().toString().matches("")){
            description.setText("Sem informação!");
            ec.setText("Sem informação!");
            specie.setText("Sem informação!");
        }


        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://catchabug-teste.firebaseio.com/Species/" + species);
        users = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid());
        base = FirebaseDatabase.getInstance().getReference();




        //similar = (RecyclerView) findViewById(R.id.samePhotosGallery);
        //similar.setHasFixedSize(true);


        /*RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),3);
        similar.setLayoutManager(layoutManager);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    ImageInfo imageInfo = postSnapshot.getValue(ImageInfo.class);
                    list.add(imageInfo);
                }

                adapter = new RecyclerViewAdapter(getApplicationContext(), list);
                similar.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle pos = new Bundle();
                pos.putString("Lat", lat);
                pos.putString("Long", lng);
                Intent k = new Intent(photoDetails_activity.this, showOnMap.class);
                k.putExtras(pos);
                startActivity(k);

            }
        });


        final FloatingActionButton editDetails = (FloatingActionButton) findViewById(R.id.editDetails);
        editDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle edit = new Bundle();
                edit.putString("photoName", date);
                edit.putString("URL", url);
                edit.putString("Species", species);
                edit.putString("UID", uid);
                edit.putString("Vulgar", vulgar);
                edit.putString("Date", date);
                edit.putString("PreviousIntent", "photoDetails");
                Log.d("UUID: ", uid);
                Intent goTo = new Intent(photoDetails_activity.this, editDetails.class);
                goTo.putExtras(edit);
                startActivity(goTo);

            }
        });

        base.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Accounts").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue()==null)
                    editDetails.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.photo_details_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home)
            NavUtils.navigateUpFromSameTask(this);

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            // Handle the camera action
            Intent goTo = new Intent(getApplicationContext(), galleryFeed.class);
            startActivity(goTo);
        } else if (id == R.id.nav_gallery) {
            Intent goTo = new Intent(getApplicationContext(), otherPhotosGallery.class);
            startActivity(goTo);
        } else if (id == R.id.nav_waitingPhotos) {
            Intent goTo = new Intent(getApplicationContext(), photosToReview.class);
            startActivity(goTo);
        } else if (id == R.id.nav_guide) {
            Intent goTo = new Intent(getApplicationContext(), guide_activity.class);
            startActivity(goTo);
        } else if (id == R.id.nav_map) {
            Intent goTo = new Intent(getApplicationContext(), map_activity.class);
            startActivity(goTo);
        } else if (id == R.id.nav_signOut) {
            FirebaseAuth.getInstance().signOut();
            Intent goTo = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(goTo);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
