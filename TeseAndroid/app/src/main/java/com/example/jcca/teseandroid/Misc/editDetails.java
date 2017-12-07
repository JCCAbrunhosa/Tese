package com.example.jcca.teseandroid.Misc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.example.jcca.teseandroid.DataObjects.ImageInfo;
import com.example.jcca.teseandroid.Gallery.galleryFeed;
import com.example.jcca.teseandroid.Glide_Module.GlideApp;
import com.example.jcca.teseandroid.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class editDetails extends AppCompatActivity {

    //Firebase Reference
    DatabaseReference mDatabase;
    DatabaseReference toDatabase;

    //UI
    EditText especie;
    EditText descricao;
    EditText ecologia;

    Button submit;

    ImageView image;

    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_details);

        mDatabase= FirebaseDatabase.getInstance().getReference();
        toDatabase = FirebaseDatabase.getInstance().getReference().child("PhotosReviewed");

        String url = getIntent().getStringExtra("URL");
        key = getIntent().getStringExtra("photoName");

        especie = findViewById(R.id.especie);
        descricao = findViewById(R.id.descrição);
        ecologia = findViewById(R.id.ecologia);
        submit = findViewById(R.id.submitData);
        image = findViewById(R.id.newImage);

        //Image pops up when user clicks on it
        final ImagePopup imagePopup = new ImagePopup(this);
        imagePopup.initiatePopup(image.getDrawable());
        //Populates pop up with image from glide - need to change this to a local fetch
        imagePopup.initiatePopupWithGlide(url);
        imagePopup.setFullScreen(true);
        imagePopup.setImageOnClickClose(true);
        imagePopup.setHideCloseIcon(true);

        //Populate Original ImageView with Glide
        GlideApp.with(getApplicationContext()).load(url).override(120,120).into(image);



        //Passing values from new image notification to edit details activity
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child("Species").child(especie.getText().toString()).child(getIntent().getStringExtra("photoName")).child("description").setValue(descricao.getText().toString());
                mDatabase.child("Species").child(especie.getText().toString()).child(getIntent().getStringExtra("photoName")).child("eco").setValue(ecologia.getText().toString());
                mDatabase.child("toReview").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        toDatabase.child(key).setValue(dataSnapshot.getValue());
                        toDatabase.child(key).child("eco").setValue(ecologia.getText().toString());
                        toDatabase.child(key).child("description").setValue(descricao.getText().toString());
                        toDatabase.child(key).child("species").setValue(especie.getText().toString());

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                mDatabase.child("toReview").child(key).getRef().removeValue();

                Toast.makeText(getApplicationContext(), "Informação editada!", Toast.LENGTH_SHORT).show();

                Intent goBack = new Intent(getApplicationContext(), galleryFeed.class);
                startActivity(goBack);
            }
        });


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imagePopup.viewPopup();
            }
        });



    }

}
