package com.example.jcca.teseandroid.Gallery;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
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
import com.example.jcca.teseandroid.BuildConfig;
import com.example.jcca.teseandroid.DataObjects.ImageInfo;
import com.example.jcca.teseandroid.DataObjects.Position;
import com.example.jcca.teseandroid.Login_Registering.LoginActivity;
import com.example.jcca.teseandroid.Login_Registering.settingsActivity;
import com.example.jcca.teseandroid.Misc.aboutActivity;
import com.example.jcca.teseandroid.Misc.cameraIntent;
import com.example.jcca.teseandroid.Misc.chooseLocation;
import com.example.jcca.teseandroid.Misc.initialScreen;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class galleryFeed extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    //Take a picture intent
    static final int REQUEST_LOCATION=0;
    int ACTIVITY_DONE=1;

    //Connection between RecyclerView and Firebase variables
    public List<ImageInfo> list = new ArrayList<>();

    public RecyclerView.Adapter adapter;

    //Firebase Storage Connection

    FirebaseStorage storage = FirebaseStorage.getInstance();

    StorageReference storageRef = storage.getReferenceFromUrl("gs://catchabug-teste.appspot.com");

    DatabaseReference mDatabase;
    DatabaseReference toReview;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    //RecyclerView
    private RecyclerView imageViewer;

    ImageInfo image;

    ProgressBar progressBar;

    TextView noPhotos;

    String path;

    String timeStamp;

    boolean isCancelled;

    boolean fromMap=false;
    String locationFromMapLat;
    String locationFromMapLng;

    Uri photoUri;


    Handler handler = new Handler();
    boolean useName;

    boolean accessGranted=true;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getWindow().getDecorView().setBackgroundColor(Color.LTGRAY);
        getUserName();

        //Checks if the User wants to use the name or not for the photo taken
        final SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        useName = sharedPreferences.getBoolean("example_switch",false);

        //Creates Drawer Layout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //Creates NavigationView
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        noPhotos=findViewById(R.id.noPhotos);

        mDatabase =  FirebaseDatabase.getInstance().getReference();


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

        //Always check permissions when starting the App
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,"Permission not yet granted", Toast.LENGTH_LONG).show();
            //Request Permissions (Location)

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        }


        //Photo
        imageViewer = (RecyclerView) findViewById(R.id.imageGallery);
        imageViewer.setHasFixedSize(true);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        registerForContextMenu(imageViewer);
        if(FirebaseDatabase.getInstance().getReferenceFromUrl("https://catchabug-teste.firebaseio.com/Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid())!=null)
            mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://catchabug-teste.firebaseio.com/Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        toReview = FirebaseDatabase.getInstance().getReference().child("toReview");

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(galleryFeed.this);
        imageViewer.setLayoutManager(layoutManager);


        //Floating Action Button for taking a picture
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(galleryFeed.this,cameraIntent.class);
                startActivityForResult(intent, ACTIVITY_DONE );
            }

        });

        //Refresh by swipping down
        final SwipeRefreshLayout mySwipeRefreshLayout = findViewById(R.id.swiperefresh1);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        refreshList(mDatabase);
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
    }


    @Override
    public void onResume() {
        super.onResume();
        refreshList(mDatabase);

    }

    public void refreshList(DatabaseReference mDatabase){

        Query orderByDate = mDatabase.orderByChild("date");


        orderByDate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    for(DataSnapshot allImages: postSnapshot.getChildren()){
                        if(!allImages.getKey().matches("description") && !allImages.getKey().matches("ecology") && !allImages.getKey().matches("vulgar")){
                            //TextView name = findViewById(R.id.userName);
                            //name.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

                            ImageInfo imageInfo = allImages.getValue(ImageInfo.class);

                            list.add(imageInfo);
                        }

                    }

                }
                if(list.size()==0)
                    noPhotos.setVisibility(View.VISIBLE);
                else
                    noPhotos.setVisibility(View.GONE);

                Collections.reverse(list);
                adapter = new galleryFeedAdapter(galleryFeed.this, list);
                imageViewer.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //Kills the application on back pressed
    @Override
    public void onBackPressed() {
        Intent a = new Intent(this, initialScreen.class);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.gallery_feed, menu);
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
        }else if(id == R.id.action_upload){
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            verifyStoragePermissions(this);
            startActivityForResult(photoPickerIntent, 2);
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
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
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
            stopService(new Intent(galleryFeed.this, NewPhotoAdded.class));
            startActivity(goTo);
        }else if (id == R.id.nav_about) {
            Intent goTo = new Intent(getApplicationContext(), aboutActivity.class);
            goTo.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

    //After taking a photo, the upload occurs
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTIVITY_DONE && resultCode!=RESULT_CANCELED) {
            path=  data.getStringExtra("photoPath");
            timeStamp=data.getStringExtra("timeStamp");
            statusCheck();
            uploadPhoto(path, null);
        }else if(requestCode==2 && resultCode!=RESULT_CANCELED ){
            photoUri = data.getData();
            timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            Intent goToMap = new Intent(galleryFeed.this, chooseLocation.class);
            startActivityForResult(goToMap, 3);

        }else if(requestCode==3 && resultCode!=RESULT_CANCELED){
            fromMap=true;
            locationFromMapLat = data.getStringExtra("locationLat");
            locationFromMapLng = data.getStringExtra("locationLng");
            uploadPhoto(null, photoUri);
            //fromMap=false;
        }
    }


    private void uploadPhoto(String photoPath, Uri file) {
        //Upload to Data Storage

        if(file==null){
            if(Build.VERSION.SDK_INT >= 24) {
                file = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider",new File(photoPath));
            }else{
                file = Uri.fromFile(new File(photoPath));
            }
        }


        StorageReference photosRef = storageRef.child("photos/" + FirebaseAuth.getInstance().getCurrentUser().getEmail() + "/" + file.getLastPathSegment());
        final StorageMetadata metadata = new StorageMetadata.Builder().setContentType("image/jpeg").build();
        UploadTask uploadTask = photosRef.putFile(file, metadata);

        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                progressBar.setVisibility(View.VISIBLE);
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                Log.d("TotalByteCount",String.valueOf(taskSnapshot.getTotalByteCount()));
                if(Double.isNaN(progress)){
                    isCancelled=true;
                    progressBar.setVisibility(View.GONE);
                }


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
                Toast.makeText(galleryFeed.this, "Upload failed. Try again!", Toast.LENGTH_SHORT).show();


            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //progressBar.setVisibility(View.INVISIBLE);
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL

                if(!isCancelled){
                    Log.d("Task", String.valueOf(taskSnapshot.getTotalByteCount()));
                    getLocation(taskSnapshot);
                    progressBar.setVisibility(View.INVISIBLE);
                    if(accessGranted)
                        Toast.makeText(galleryFeed.this, R.string.uploadDone, Toast.LENGTH_SHORT).show();

                    else
                        Toast.makeText(galleryFeed.this, R.string.uploadFailed, Toast.LENGTH_SHORT).show();
                }
                isCancelled=false;

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
                if(fromMap==false){
                    image = new ImageInfo(timeStamp, taskSnapshot.getDownloadUrl().toString(), getUserName(),new Position(location.getLatitude(), location.getLongitude()), "", "","", FirebaseAuth.getInstance().getCurrentUser().getUid());

                }else if(fromMap==true){
                    double latitude = Double.parseDouble(locationFromMapLat);
                    double longitude = Double.parseDouble(locationFromMapLng);
                    image = new ImageInfo(timeStamp, taskSnapshot.getDownloadUrl().toString(), getUserName(),new Position(latitude, longitude), "", "","", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    fromMap=false;
                }
                else{
                    image = new ImageInfo(timeStamp, taskSnapshot.getDownloadUrl().toString(), "",new Position(location.getLatitude(), location.getLongitude()), "", "","", FirebaseAuth.getInstance().getCurrentUser().getUid());
                }

                mDatabase.child("ToReview").child(timeStamp).setValue(image);
                toReview.child(timeStamp).setValue(image);
                //Immediately stops updates - get's position only once
                locationManager.removeUpdates(this);
                accessGranted=true;

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

        adapter=null;
    }

    @Override public void onLowMemory() {
        super.onLowMemory();
        Glide.get(this).clearMemory();
    }
    @Override public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Glide.get(this).trimMemory(level);
    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.turnOnLocation)
                .setCancelable(false)
                .setPositiveButton(R.string.turnOnOptionsYes, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(R.string.turnOnOptionsNo, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        accessGranted=false;
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private String getUserName(){
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String getUserName;
        if(sharedPreferences.getBoolean("example_switch",true)){
             getUserName = sharedPreferences.getString("example_text", null);
        }else{
            getUserName= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        }

        return getUserName;
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

}
