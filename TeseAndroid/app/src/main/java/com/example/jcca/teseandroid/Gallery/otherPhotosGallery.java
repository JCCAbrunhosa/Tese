package com.example.jcca.teseandroid.Gallery;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jcca.teseandroid.Adapters.galleryFeedAdapter;
import com.example.jcca.teseandroid.DataObjects.ImageInfo;
import com.example.jcca.teseandroid.DataObjects.Position;
import com.example.jcca.teseandroid.Login_Registering.LoginActivity;
import com.example.jcca.teseandroid.Login_Registering.settingsActivity;
import com.example.jcca.teseandroid.Misc.cameraIntent;
import com.example.jcca.teseandroid.Misc.map_activity;
import com.example.jcca.teseandroid.Notifications.NewPhotoAdded;
import com.example.jcca.teseandroid.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class otherPhotosGallery extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    //Take a picture intent
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;

    String timeStamp;
    ImageInfo image;

    TextView noPhotos;

    //Firebase Storage Connection
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://catchabug-teste.appspot.com");

    DatabaseReference mDatabase;
    DatabaseReference toReview;
    DatabaseReference mReviewed;

    //RecyclerView
    private RecyclerView imageViewer;

    //Connection between RecyclerView and Firebase variables
    public List<ImageInfo> list = new ArrayList<>();

    public RecyclerView.Adapter adapter ;

    String path;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_photos_gallery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final SwipeRefreshLayout mySwipeRefreshLayout = findViewById(R.id.swiperefresh);
        imageViewer=(RecyclerView) findViewById(R.id.imageGallery1);
        imageViewer.setHasFixedSize(true);

        mDatabase =  FirebaseDatabase.getInstance().getReference();

        noPhotos=findViewById(R.id.noPhotos);

        getWindow().getDecorView().setBackgroundColor(Color.LTGRAY);

        progressBar=findViewById(R.id.progressBarOther);
        progressBar.setVisibility(View.INVISIBLE);


        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Accounts").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("isPro").getValue() == null){
                    navigationView.getMenu().findItem(R.id.nav_waitingPhotos).setVisible(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mReviewed= FirebaseDatabase.getInstance().getReferenceFromUrl("https://catchabug-teste.firebaseio.com/Species");
        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://catchabug-teste.firebaseio.com/Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        toReview = FirebaseDatabase.getInstance().getReference().child("toReview");

        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getApplicationContext());
        imageViewer.setLayoutManager(layoutManager);

        mReviewed.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    for(DataSnapshot captures: postSnapshot.getChildren()){
                        if(!captures.getKey().matches("description") && !captures.getKey().matches("ecology") && !captures.getKey().matches("vulgar")){
                            TextView name = findViewById(R.id.userName);
                            name.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

                            ImageInfo imageInfo=captures.getValue(ImageInfo.class);

                            list.add(imageInfo);
                        }

                    }

                }
                if(list.size()==0)
                    noPhotos.setVisibility(View.VISIBLE);
                else
                    noPhotos.setVisibility(View.GONE);
                adapter = new galleryFeedAdapter(otherPhotosGallery.this,list);
                imageViewer.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent e = new Intent(otherPhotosGallery.this, cameraIntent.class);
                startActivityForResult(e, REQUEST_TAKE_PHOTO);
            }
        });

        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        refreshList(mReviewed);
                        mySwipeRefreshLayout.setRefreshing(false);

                    }
                }
        );


    }

    public void refreshList(DatabaseReference mDatabase){

        Query orderByDate = mDatabase.orderByChild("date");

        orderByDate.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    for(DataSnapshot captures: postSnapshot.getChildren()){
                        if(!captures.getKey().matches("description") && !captures.getKey().matches("ecology") && !captures.getKey().matches("vulgar")){

                            ImageInfo imageInfo=captures.getValue(ImageInfo.class);

                            list.add(imageInfo);
                        }

                    }
                }
                if(list.size()==0)
                    noPhotos.setVisibility(View.VISIBLE);
                else
                    noPhotos.setVisibility(View.GONE);

                adapter = new galleryFeedAdapter(otherPhotosGallery.this, list);
                imageViewer.setAdapter(adapter);
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

        getMenuInflater().inflate(R.menu.other_photos_gallery, menu);
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
            goTo.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(goTo);
        } else if (id == R.id.nav_gallery) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
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
        }else if (id == R.id.nav_options){
            Intent goTo = new Intent(getApplicationContext(), settingsActivity.class);
            goTo.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(goTo);
        } else if (id == R.id.nav_signOut) {
            FirebaseAuth.getInstance().signOut();
            Intent goTo = new Intent(getApplicationContext(), LoginActivity.class);
            goTo.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            stopService(new Intent(getApplicationContext(), NewPhotoAdded.class));
            startActivity(goTo);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /////////////////////////////////////Photo Functions/////////////////////////////

    //After taking a photo, the upload occurs
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode!=RESULT_CANCELED) {
            path = data.getStringExtra("photoPath");
            timeStamp = data.getStringExtra("timeStamp");
            uploadPhoto(path);
            super.onBackPressed();
        }
    }


    private void uploadPhoto(String mCurrentPhotoPath) {

        //Upload to Data Storage
        Uri file = Uri.fromFile(new File(mCurrentPhotoPath));
        Log.d("Storage ", mCurrentPhotoPath.toString());
        StorageReference photosRef = storageRef.child("photos/" + FirebaseAuth.getInstance().getCurrentUser().getEmail() + "/" + file.getLastPathSegment());
        final StorageMetadata metadata = new StorageMetadata.Builder().setContentType("image/jpeg").build();
        UploadTask uploadTask = photosRef.putFile(file, metadata);
        //photoRef= mDatabase.getKey();

        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                progressBar.setVisibility(View.VISIBLE);
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                Log.d("Upload","Upload is " + progress + "% done");
                int currentProgress = (int) progress;
                progressBar.setProgress(currentProgress);

            }

        });


        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(otherPhotosGallery.this, "Upload failed. Try again!", Toast.LENGTH_SHORT).show();


            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL
                getLocation(taskSnapshot);
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(otherPhotosGallery.this, R.string.uploadDone, Toast.LENGTH_SHORT).show();
            }

        });


    }



    //This function will upload all data, including the position (inner class doesn't let values outside) - not the best way but it works
    private void getLocation(final UploadTask.TaskSnapshot taskSnapshot) {
        // Acquire a reference to the system Location Manager
        final LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                image = new ImageInfo(timeStamp, taskSnapshot.getDownloadUrl().toString(), FirebaseAuth.getInstance().getCurrentUser().getEmail(),new Position(location.getLatitude(), location.getLongitude()), "", "","", FirebaseAuth.getInstance().getCurrentUser().getUid());
                //mDatabase.child(timeStamp).setValue(image);
                Log.d("DATABASE:", mDatabase.getRef().toString());
                toReview.child(timeStamp).setValue(image);
                mDatabase.child("ToReview").child(timeStamp).setValue(image);
                //Immediately stops updates - get's position only once
                locationManager.removeUpdates(this);

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return ;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

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
