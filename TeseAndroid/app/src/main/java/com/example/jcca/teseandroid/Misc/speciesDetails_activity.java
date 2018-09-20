package com.example.jcca.teseandroid.Misc;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
    TextView vulgar;

    RecyclerView sameSpecies;
    public RecyclerView.Adapter adapter;
    public List<ImageInfo> list = new ArrayList<>();
    public List<Position> positions = new ArrayList<>();
    DatabaseReference mDatabase;
    String[] urlImages;
    Handler handler = new Handler();
    int i=0;

    private DatabaseReference isPro;
    Context context;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_species_details_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context=this;


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);


        species = findViewById(R.id.speciesName);
        description= findViewById(R.id.speciesDescription);
        eco = findViewById(R.id.speciesEco);
        speciesImage = findViewById(R.id.speciesImage);
        vulgar = findViewById(R.id.vulgarName);

        final String spec = getIntent().getStringExtra("Species");
        //String ecol = getIntent().getStringExtra("Eco");
        String url = getIntent().getStringExtra("URL");
        //String vulgarName = getIntent().getStringExtra("Vulgar");


        //Image pops up when user clicks on it
        final ImagePopup imagePopup = new ImagePopup(this);
        imagePopup.initiatePopup(speciesImage.getDrawable());
        //Populates pop up with image from glide - need to change this to a local fetch
        imagePopup.initiatePopupWithGlide(url);
        imagePopup.setFullScreen(true);
        imagePopup.setImageOnClickClose(true);
        imagePopup.setHideCloseIcon(false);
        GlideApp.with(speciesDetails_activity.this).load(url).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(speciesImage);
        //handler.postDelayed(runnable, 4000);

        speciesImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imagePopup.viewPopup();
            }
        });


        setTitle(spec);
        toolbar.setTitleTextColor(Color.WHITE);


        species.setText(spec.toString());

        //vulgar.setText(vulgarName.toString());

        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://catchabug-teste.firebaseio.com/Species/" + spec);
        isPro=FirebaseDatabase.getInstance().getReference();

        //Photo
        sameSpecies = (RecyclerView) findViewById(R.id.sameSpeciesPhoto);
        sameSpecies.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(speciesDetails_activity.this,4);
        sameSpecies.setLayoutManager(layoutManager);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                urlImages= new String[(int)dataSnapshot.getChildrenCount()];
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if(!postSnapshot.getKey().matches("description") && !postSnapshot.getKey().toString().matches("vulgar") && !postSnapshot.getKey().toString().matches("ecology")){
                        description.setText(dataSnapshot.child("description").getValue().toString());
                        eco.setText(dataSnapshot.child("ecology").getValue().toString());
                        vulgar.setText(dataSnapshot.child("vulgar").getValue().toString());
                        urlImages[i++]= postSnapshot.child("url").getValue(String.class);

                        ImageInfo imageInfo = postSnapshot.getValue(ImageInfo.class);

                        list.add(imageInfo);
                    }

                }

                adapter = new RecyclerViewAdapter(speciesDetails_activity.this, list);
                sameSpecies.setAdapter(adapter);
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

        final FloatingActionButton editSpecies = (FloatingActionButton) findViewById(R.id.editSpecies);
        editSpecies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle sendSpecies = new Bundle();
                sendSpecies.putString("Species", spec.toString());
                Intent edit = new Intent(speciesDetails_activity.this, editSpeciesDetails.class);
                edit.putExtras(sendSpecies);
                startActivity(edit);
            }
        });

        isPro.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Accounts").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("isPro").getValue() == null){
                   editSpecies.setVisibility(View.GONE);
                }
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
        getMenuInflater().inflate(R.menu.species_details_activity, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home){
            super.onBackPressed();
            return true;
        }

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
            goTo.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(goTo);
        } else if (id == R.id.nav_gallery) {
            Intent goTo = new Intent(getApplicationContext(), otherPhotosGallery.class);
            goTo.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(goTo);
        } else if (id == R.id.nav_waitingPhotos) {
            Intent goTo = new Intent(getApplicationContext(), photosToReview.class);
            goTo.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(goTo);
        } else if (id == R.id.nav_guide) {
            Intent goTo = new Intent(getApplicationContext(), guide_activity.class);
            goTo.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(goTo);
        } else if (id == R.id.nav_map) {
            Intent goTo = new Intent(getApplicationContext(), map_activity.class);
            goTo.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(goTo);
        } else if (id == R.id.nav_signOut) {
            FirebaseAuth.getInstance().signOut();

            Intent goTo = new Intent(getApplicationContext(), LoginActivity.class);
            goTo.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(goTo);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }

    @Override public void onLowMemory() {
        super.onLowMemory();
        Glide.get(this).clearMemory();
    }
    @Override public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Glide.get(this).trimMemory(level);
    }

}
