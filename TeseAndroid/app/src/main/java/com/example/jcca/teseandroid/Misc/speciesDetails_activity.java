package com.example.jcca.teseandroid.Misc;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
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
import com.example.jcca.teseandroid.DataObjects.Position;
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

public class speciesDetails_activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView species;
    TextView description;
    TextView eco;
    ImageView speciesImage;

    RecyclerView sameSpecies;
    public RecyclerView.Adapter adapter;
    public List<ImageInfo> list = new ArrayList<>();
    public List<Position> positions = new ArrayList<>();
    DatabaseReference mDatabase;
    String[] urlImages = new String[10];
    Handler handler = new Handler();
    int i=0;
    ImagePopup imagePopup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_species_details_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        species = findViewById(R.id.speciesName);
        description= findViewById(R.id.speciesDescription);
        eco = findViewById(R.id.speciesEco);
        speciesImage = findViewById(R.id.speciesImage);

        final String spec = getIntent().getStringExtra("Species");
        String desc = getIntent().getStringExtra("Desc");
        String ecol = getIntent().getStringExtra("Eco");
        String url = getIntent().getStringExtra("URL");
        handler.postDelayed(runnable, 4000);

        //Image pops up when user clicks on it
        imagePopup = new ImagePopup(this);
        imagePopup.initiatePopup(speciesImage.getDrawable());
        //Populates pop up with image from glide - need to change this to a local fetch
        imagePopup.initiatePopupWithGlide(url);
        imagePopup.setFullScreen(true);
        imagePopup.setImageOnClickClose(true);
        imagePopup.setHideCloseIcon(false);

        speciesImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imagePopup.viewPopup();
            }
        });


        setTitle(spec);

        GlideApp.with(getApplicationContext()).load(url).into(speciesImage);
        species.setText(spec.toString());

        eco.setText(ecol.toString());

        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://catchabug-teste.firebaseio.com/Species/" + spec);


        //Photo
        //sameSpecies = (RecyclerView) findViewById(R.id.sameSpeciesGallery);
//        sameSpecies.setHasFixedSize(true);

        //RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),3);
        //sameSpecies.setLayoutManager(layoutManager);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    description.setText(dataSnapshot.child("description").getValue().toString());
                    urlImages[i++]= postSnapshot.child("url").getValue(String.class);

                    //ImageInfo imageInfo = postSnapshot.getValue(ImageInfo.class);

                    //list.add(imageInfo);
                }

                //adapter = new RecyclerViewAdapter(getApplicationContext(), list);
                //sameSpecies.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.showOnMap);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle sendPositions = new Bundle();
                sendPositions.putString("Species", spec.toString());
                Intent k = new Intent(speciesDetails_activity.this, showSpeciesOnMap.class);
                k.putExtras(sendPositions);
                startActivity(k);
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
        getMenuInflater().inflate(R.menu.species_details_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

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

    //This runnable makes the images cycle
    Runnable runnable = new Runnable() {
        int i = 0;

        public void run() {
            GlideApp.with(getApplicationContext()).load(urlImages[i]).into(speciesImage);
            i++;
            //Each new rotating image can be popped up
            imagePopup.initiatePopup(speciesImage.getDrawable());
            imagePopup.setFullScreen(true);
            imagePopup.setImageOnClickClose(true);
            imagePopup.setHideCloseIcon(false);
            if (urlImages[i]==null) {
                i = 0;
            }
            handler.postDelayed(this, 4000);
        }
    };

}
