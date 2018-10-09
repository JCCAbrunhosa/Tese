package com.example.jcca.teseandroid.Gallery;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jcca.teseandroid.Adapters.CardViewAdapter;
import com.example.jcca.teseandroid.BuildConfig;
import com.example.jcca.teseandroid.DataObjects.ImageInfo;
import com.example.jcca.teseandroid.DataObjects.Position;
import com.example.jcca.teseandroid.Login_Registering.LoginActivity;
import com.example.jcca.teseandroid.Login_Registering.settingsActivity;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class guide_activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseReference mDatabase2;

    //Take a picture intent
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;

    //Firebase Storage Connection
    private String mCurrentPhotoPath;

    FirebaseStorage storage = FirebaseStorage.getInstance();

    StorageReference storageRef = storage.getReferenceFromUrl("gs://catchabug-teste.appspot.com");


    DatabaseReference mDatabase;
    DatabaseReference toReview;

    //Connection between RecyclerView and Firebase variables
    public List<ImageInfo> list = new ArrayList<>();

    public CardViewAdapter adapter;

    //RecyclerView
    private RecyclerView imageViewer;

    String timeStamp;

    ImageInfo image;

    ProgressBar progressBar;

    TextView noPhotos;

    SearchView searchView;
    ListAdapter listAdapter;

    boolean useName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeAPhotoIntent();
            }

        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getWindow().getDecorView().setBackgroundColor(Color.LTGRAY);

        mDatabase =  FirebaseDatabase.getInstance().getReference();

        mDatabase2 =  FirebaseDatabase.getInstance().getReference().child("Species");
        imageViewer = (RecyclerView)findViewById(R.id.speciesList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(guide_activity.this);
        imageViewer.setLayoutManager(layoutManager);
        imageViewer.setHasFixedSize(true);

        progressBar= findViewById(R.id.progressBarGuide);
        progressBar.setVisibility(View.INVISIBLE);

        final SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        useName = sharedPreferences.getBoolean("example_switch",false);

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


        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://catchabug-teste.firebaseio.com/Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        toReview = FirebaseDatabase.getInstance().getReference().child("toReview");

        mDatabase2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TextView name = findViewById(R.id.userName);
                name.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    for(DataSnapshot snapshot : postSnapshot.getChildren()){
                        if(!snapshot.getKey().toString().matches("description") && !snapshot.getKey().toString().matches("ecology") && !snapshot.getKey().toString().matches("vulgar")){
                            ImageInfo imageInfo = snapshot.getValue(ImageInfo.class);

                            list.add(imageInfo);
                        }

                        break; //this break is used for the cycle to add only one of each species
                    }
                }

                adapter = new CardViewAdapter(guide_activity.this, list);
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
        getMenuInflater().inflate(R.menu.guide_activity, menu);

        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!newText.matches("")){
                    adapter.setFilterOn(newText);
                }

                else
                    adapter.setFilterOff();
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
        if (id == R.id.action_settings) {
            return true;
        }

        if(id==R.id.action_search){}


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Intent goTo = new Intent(getApplicationContext(), galleryFeed.class);
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
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_map) {
            Intent goTo = new Intent(getApplicationContext(), map_activity.class);
            goTo.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(goTo);
        } else if (id == R.id.nav_signOut) {
            FirebaseAuth.getInstance().signOut();
            Intent goTo = new Intent(getApplicationContext(), LoginActivity.class);
            goTo.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            stopService(new Intent(getApplicationContext(), NewPhotoAdded.class));
            startActivity(goTo);
        }else if (id == R.id.nav_options){
            Intent goTo = new Intent(getApplicationContext(), settingsActivity.class);
            goTo.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(goTo);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /////////////////////////////////////Photo Functions/////////////////////////////
    private void takeAPhotoIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri uriSavedImage;
                if(Build.VERSION.SDK_INT >= 24) {
                    uriSavedImage = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider",new File(photoFile.getAbsolutePath()));
                }else{
                    uriSavedImage = Uri.fromFile(new File(photoFile.getAbsolutePath()));
                }
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }

    }

    //Done
    private File createImageFile() throws IOException {
        // Create an image file name
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //Saves on directory made for that day
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File dir = new File(storageDir, new SimpleDateFormat("yyyyMMdd").format(new Date()));
        if (!dir.exists())
            dir.mkdirs();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpeg",         /* suffix */
                dir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void uploadPhoto() {

        Uri file;

        //Upload to Data Storage
        if(Build.VERSION.SDK_INT >= 24) {
            file = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider",new File(mCurrentPhotoPath));
        }else{
            file = Uri.fromFile(new File(mCurrentPhotoPath));
        }

        StorageReference photosRef = storageRef.child("photos/" + FirebaseAuth.getInstance().getCurrentUser().getEmail() + "/" + file.getLastPathSegment());
        final StorageMetadata metadata = new StorageMetadata.Builder().setContentType("image/jpeg").build();
        UploadTask uploadTask = photosRef.putFile(file, metadata);


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
                Toast.makeText(guide_activity.this, "Upload failed. Try again!", Toast.LENGTH_SHORT).show();


            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //progressBar.setVisibility(View.INVISIBLE);
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL
                getLocation(taskSnapshot);
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(guide_activity.this, R.string.uploadDone, Toast.LENGTH_SHORT).show();


            }

        });




    }

    //After taking a photo, the upload occurs
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode!=RESULT_CANCELED) {
            uploadPhoto();
            super.onBackPressed();

        }else{

        }
    }

    //This function will upload all data, including the position (inner class doesn't let values outside) - not the best way but it works
    private void getLocation(final UploadTask.TaskSnapshot taskSnapshot) {
        // Acquire a reference to the system Location Manager
        final LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                if(useName){
                    image = new ImageInfo(timeStamp, taskSnapshot.getDownloadUrl().toString(), FirebaseAuth.getInstance().getCurrentUser().getEmail(),new Position(location.getLatitude(), location.getLongitude()), "", "","", FirebaseAuth.getInstance().getCurrentUser().getUid());

                }else{
                    image = new ImageInfo(timeStamp, taskSnapshot.getDownloadUrl().toString(), "",new Position(location.getLatitude(), location.getLongitude()), "", "","", FirebaseAuth.getInstance().getCurrentUser().getUid());
                }

                mDatabase.child("ToReview").child(timeStamp).setValue(image);
                toReview.child(timeStamp).setValue(image);
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
