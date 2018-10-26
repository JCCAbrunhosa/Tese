package com.example.jcca.teseandroid.Misc;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jcca.teseandroid.BuildConfig;
import com.example.jcca.teseandroid.DataObjects.ImageInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by JCCA on 29/03/18.
 */

public class cameraIntent extends AppCompatActivity {

    //Take a picture intent
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_LOCATION=0;
    static final int REQUEST_CAMERA=0;
    int permissionCheck;

    //Connection between RecyclerView and Firebase variables
    public List<ImageInfo> list = new ArrayList<>();

    public RecyclerView.Adapter adapter;

    //Firebase Storage Connection
    private String mCurrentPhotoPath;

    FirebaseStorage storage = FirebaseStorage.getInstance();

    StorageReference storageRef = storage.getReferenceFromUrl("gs://catchabug-teste.appspot.com");

    DatabaseReference mDatabase;
    DatabaseReference toReview;


    //RecyclerView
    private RecyclerView imageViewer;

    String timeStamp;

    ImageInfo image;

    ProgressBar progressBar;

    TextView noPhotos;

    int valueToReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        takeAPhotoIntent();

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
        Intent intent = new Intent();
        Bundle bundle= new Bundle();
        bundle.putString("photoPath", mCurrentPhotoPath);
        bundle.putString("timeStamp", timeStamp);
        intent.putExtras(bundle);

        setResult(Activity.RESULT_OK, intent);
        finish();

        return image;
    }

}
