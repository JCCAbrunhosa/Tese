package com.example.jcca.teseandroid.Misc;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;

public class editDetails extends AppCompatActivity {

    //Firebase Reference
    DatabaseReference mDatabase;
    DatabaseReference toDatabase;

    //UI
    AutoCompleteTextView especie;
    EditText descricao;
    EditText ecologia;
    EditText vulgar;

    FloatingActionButton submit;

    List<String> listOfSpecies = new ArrayList<>();
    int i = 0;

    ImageView image;

    String key;
    String data;
    String species;
    String uid;
    String vulg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_details);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        toDatabase = FirebaseDatabase.getInstance().getReference().child("PhotosReviewed");

        String url = getIntent().getStringExtra("URL");
        key = getIntent().getStringExtra("photoName");
        data = getIntent().getStringExtra("photoName");
        species = getIntent().getStringExtra("Species");
        uid = getIntent().getStringExtra("UID");

        submit = findViewById(R.id.submitData);

        especie = findViewById(R.id.test);
        descricao = findViewById(R.id.descrição);
        ecologia = findViewById(R.id.ecologia);
        vulgar = findViewById(R.id.vulgar);
        submit = findViewById(R.id.submitData);
        image = findViewById(R.id.newImage);

        //This variable will always check the input on the Species field
        //If a new one is being inserted then a description will be needed
        checkInputFinished.run();

        if(getIntent().getStringExtra("PreviousIntent").matches("onClickImage"))
            descricao.setVisibility(View.GONE);
        else
            descricao.setVisibility(View.VISIBLE);

        mDatabase.child("Species").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //Log.d("Species: ", postSnapshot.getKey());
                    listOfSpecies.add(postSnapshot.getKey());

                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(editDetails.this, android.R.layout.simple_dropdown_item_1line, listOfSpecies);
                if (arrayAdapter.getCount() == 0) {
                    return;
                } else
                    especie.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //Image pops up when user clicks on it
        final ImagePopup imagePopup = new ImagePopup(this);
        imagePopup.initiatePopup(image.getDrawable());
        //Populates pop up with image from glide - need to change this to a local fetch
        imagePopup.initiatePopupWithGlide(url);
        imagePopup.setFullScreen(true);
        imagePopup.setImageOnClickClose(true);
        imagePopup.setHideCloseIcon(false);

        especie.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                lastTextEdition=System.currentTimeMillis();
                handler.postDelayed(checkInputFinished,delay);
            }

        });

        //Populate Original ImageView with Glide
        GlideApp.with(getApplicationContext()).load(url).override(120, 120).into(image);


        //Passing values from new image notification to edit details activity
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    if (species.matches("")) {
                        if (!especie.getText().toString().matches("") && !ecologia.getText().toString().matches("")) {
                            mDatabase.child("toReview").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    toDatabase.child(key).setValue(dataSnapshot.getValue());
                                    toDatabase.child(key).child("eco").setValue(ecologia.getText().toString());
                                    toDatabase.child(key).child("description").setValue(descricao.getText().toString());
                                    toDatabase.child(key).child("species").setValue(especie.getText().toString());
                                    toDatabase.child(key).child("vulgar").setValue(vulgar.getText().toString());
                                    mDatabase.child("Species").child(especie.getText().toString()).child(key).setValue(dataSnapshot.getValue());
                                    mDatabase.child("Species").child(especie.getText().toString()).child(key).child("eco").setValue(ecologia.getText().toString());
                                    mDatabase.child("Species").child(especie.getText().toString()).child(key).child("description").setValue(descricao.getText().toString());
                                    mDatabase.child("Species").child(especie.getText().toString()).child(key).child("species").setValue(especie.getText().toString());
                                    mDatabase.child("Species").child(especie.getText().toString()).child(key).child("vulgar").setValue(vulgar.getText().toString());
                                    mDatabase.child("Users").child(uid).child(key).setValue(dataSnapshot.getValue());
                                    mDatabase.child("Users").child(uid).child(key).child("eco").setValue(ecologia.getText().toString());
                                    mDatabase.child("Users").child(uid).child(key).child("description").setValue(descricao.getText().toString());
                                    mDatabase.child("Users").child(uid).child(key).child("species").setValue(especie.getText().toString());
                                    mDatabase.child("Users").child(uid).child(key).child("vulgar").setValue(vulgar.getText().toString());
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

                            Toast.makeText(getApplicationContext(), "Informação adicionada!", Toast.LENGTH_SHORT).show();

                            Intent goBack = new Intent(getApplicationContext(), galleryFeed.class);
                            startActivity(goBack);

                            } else {
                                if (TextUtils.isEmpty(especie.getText()))
                                    especie.setError("Incompleto!");
                                if (TextUtils.isEmpty(ecologia.getText()))
                                    ecologia.setError("Incompleto!");
                                if (TextUtils.isEmpty(vulgar.getText()))
                                    vulgar.setError("Incompleto!");
                            }

                    } else {
                        mDatabase.child("Species").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ImageInfo image = dataSnapshot.child(uid).child(data).getValue(ImageInfo.class);
                                mDatabase.child("Species").child(especie.getText().toString()).child(key).setValue(image);
                                if(!ecologia.getText().toString().matches("")){
                                    mDatabase.child("Species").child(especie.getText().toString()).child(key).child("eco").setValue(ecologia.getText().toString());
                                    mDatabase.child("Users").child(uid).child(key).child("eco").setValue(ecologia.getText().toString());
                                }
                                if(!descricao.getText().toString().matches("")){
                                    mDatabase.child("Species").child(especie.getText().toString()).child(key).child("description").setValue(descricao.getText().toString());
                                    mDatabase.child("Users").child(uid).child(key).child("description").setValue(descricao.getText().toString());
                                }

                                if(!especie.getText().toString().matches("")){
                                    mDatabase.child("Species").child(especie.getText().toString()).child(key).child("species").setValue(especie.getText().toString());
                                    mDatabase.child("Users").child(uid).child(key).child("species").setValue(especie.getText().toString());

                                }
                                if(!especie.getText().toString().matches("")){
                                    mDatabase.child("Species").child(especie.getText().toString()).child(key).child("vulgar").setValue(vulgar.getText().toString());
                                    mDatabase.child("Users").child(uid).child(key).child("vulgar").setValue(vulgar.getText().toString());

                                }
                                Log.d("Remove:", mDatabase.child("Species").child(species).child(data).getRef().toString());
                                if (species.matches(especie.getText().toString()))
                                    return;
                                else
                                    mDatabase.child("Species").child(species).child(data).getRef().removeValue();

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        Toast.makeText(getApplicationContext(), "Informação editada!", Toast.LENGTH_SHORT).show();

                        Intent goBack = new Intent(getApplicationContext(), galleryFeed.class);
                        startActivity(goBack);
                    }

            }

        });


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imagePopup.viewPopup();
            }
        });

        FloatingActionButton deny = findViewById(R.id.denyPhoto);
        deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(editDetails.this)
                        .setTitle("Rejeitar Avistamento")
                        .setMessage("Confirme se deseja rejeitar o avistamento:")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                mDatabase.child("toReview").child(key).getRef().removeValue();
                                mDatabase.child("Users").child(uid).child(key).getRef().removeValue();
                                Intent goBack = new Intent(getApplicationContext(), galleryFeed.class);
                                startActivity(goBack);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();

            }
        });

    }

    final long delay = 500; // 1 seconds after user stops typing
    long lastTextEdition = 0;
    Handler handler = new Handler();

    private Runnable checkInputFinished = new Runnable() {
        @Override
        public void run() {
            if (System.currentTimeMillis() > lastTextEdition + delay - 500) {
                if (!listOfSpecies.contains(especie.getText().toString()) && especie.getText().length()>0) {
                    descricao.setVisibility(View.VISIBLE);
                } else{
                    descricao.setHint(null);
                    descricao.setVisibility(View.GONE);
                }


            }

        }

    };


}
