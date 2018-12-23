package com.example.jcca.teseandroid.Misc;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jcca.teseandroid.DataObjects.ImageInfo;
import com.example.jcca.teseandroid.Gallery.galleryFeed;
import com.example.jcca.teseandroid.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class editSpeciesDetails extends AppCompatActivity {


    EditText descricao;
    EditText ecologia;
    EditText vulgar;
    AutoCompleteTextView name;


    TextInputLayout layoutEco;
    TextInputLayout layoutDesc;
    TextInputLayout layoutVulgar;

    String species;

    DatabaseReference mDatabase;
    DatabaseReference genReference;

    List<String> listOfSpecies = new ArrayList<>();
    boolean exists = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_species_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.greenTese)));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        species = getIntent().getStringExtra("Species");
        setTitle(species);
        toolbar.setTitleTextColor(Color.WHITE);

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Species");
        genReference=FirebaseDatabase.getInstance().getReference();

        descricao= findViewById(R.id.newDescription);
        ecologia=findViewById(R.id.newEcology);
        vulgar = findViewById(R.id.newVulgar);
        name=findViewById(R.id.newName);


        layoutDesc = (TextInputLayout) findViewById(R.id.layoutDescription);
        layoutEco= (TextInputLayout) findViewById(R.id.layoutEco);
        layoutVulgar= (TextInputLayout) findViewById(R.id.layoutVulgar);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot specie: dataSnapshot.getChildren()){
                    if(specie.getKey().matches(species)){
                        name.setText(specie.getKey());
                        vulgar.setText(specie.child("vulgar").getValue().toString());
                        ecologia.setText(specie.child("ecology").getValue().toString());
                        descricao.setText(specie.child("description").getValue().toString());

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //Gets the image clicked from Firebase
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    listOfSpecies.add(postSnapshot.getKey());

                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(editSpeciesDetails.this, android.R.layout.simple_dropdown_item_1line, listOfSpecies);
                if (arrayAdapter.getCount() == 0) {
                    return;
                } else
                    name.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        FloatingActionButton uploadDetails = (FloatingActionButton) findViewById(R.id.uploadDetails);
        uploadDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkIfFilled()==true){
                    Snackbar.make(view, "Yes", Snackbar.LENGTH_LONG)
                       .setAction("Action", null).show();
                    uploadNewData();
                }else{
                    Snackbar.make(view, "No", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
                }

                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    //    .setAction("Action", null).show();
            }
        });



        //Alert Boxes for data input

        //Description
        descricao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(editSpeciesDetails.this);
                LayoutInflater li = LayoutInflater.from(editSpeciesDetails.this);
                View promptsView = li.inflate(R.layout.prompts, null);

                alertDialogBuilder.setView(promptsView);
                alertDialogBuilder.setMessage(R.string.descDialog);

                final EditText userInput = promptsView
                        .findViewById(R.id.editTextDialogUserInput);
                if(descricao.getText()!=null)
                    userInput.setText(descricao.getText());
                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                        descricao.setText(userInput.getText());
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

        //Ecology
        ecologia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(editSpeciesDetails.this);
                LayoutInflater li = LayoutInflater.from(editSpeciesDetails.this);
                View promptsView = li.inflate(R.layout.prompts, null);

                alertDialogBuilder.setView(promptsView);
                alertDialogBuilder.setMessage(R.string.ecoDialog);

                final EditText userInput = promptsView
                        .findViewById(R.id.editTextDialogUserInput);
                if(ecologia.getText()!=null)
                    userInput.setText(ecologia.getText());
                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                        ecologia.setText(userInput.getText());
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

    //Options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_details, menu);
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

        return super.onOptionsItemSelected(item);
    }

    //Checks if the input text boxes are filled or not (description and ecology)
    private boolean checkIfFilled(){
        if( descricao.getText().toString().matches("") && ecologia.getText().toString().matches("") && vulgar.getText().toString().matches("") && name.getText().toString().matches("") && exists==false){
            Toast.makeText(this, "Incomplete information", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    //Uploads new data
    private void uploadNewData(){
        //If the name edit text is empty then the species name maintains

            //In this case then the species name changes, creating a new database entry for the new specie name and copy all the
            //nodes to it
            mDatabase.child(name.getText().toString()).child("ecology").setValue(ecologia.getText().toString());
            mDatabase.child(name.getText().toString()).child("description").setValue(descricao.getText().toString());
            mDatabase.child(name.getText().toString()).child("vulgar").setValue(vulgar.getText().toString());



            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String newSpecieVulgar = null;
                    String newSpecieEco=null;
                    for(DataSnapshot species: dataSnapshot.getChildren()){
                        if(species.getKey().matches(name.getText().toString())){
                            newSpecieEco=species.child("ecology").getValue().toString();
                            newSpecieVulgar=species.child("vulgar").getValue().toString();

                        }
                    }

                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        if(snapshot.getKey().matches(species)){
                            for(DataSnapshot details: snapshot.getChildren()){
                                if(!details.getKey().matches("description") && !details.getKey().matches("ecology") && !details.getKey().matches("vulgar")){
                                    ImageInfo capture = details.getValue(ImageInfo.class);
                                    capture.setVulgar(newSpecieVulgar);
                                    capture.setEco(newSpecieEco);
                                    capture.setSpecies(name.getText().toString());
                                    mDatabase.child(name.getText().toString()).child(details.getKey()).setValue(capture);

                                }
                            }

                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            genReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String newSpecieVulgar = null;
                    String newSpecieEco = null;
                    String newSpecieDesc=null;
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        if(snapshot.getKey().matches("Species")){
                            for (DataSnapshot species : snapshot.getChildren()) {
                                if(species.getKey().matches(name.getText().toString())){
                                    newSpecieEco=species.child("ecology").getValue().toString();
                                    newSpecieVulgar=species.child("vulgar").getValue().toString();
                                    newSpecieDesc=species.child("description").getValue().toString();
                                }
                            }


                        }
                        if(snapshot.getKey().matches("Users")) {
                            for (DataSnapshot users : snapshot.getChildren()) {
                                for(DataSnapshot specie: users.getChildren()){
                                    if (specie.getKey().matches(species)) {
                                        for (DataSnapshot images : specie.getChildren()) {
                                            if (!images.getKey().matches("description") && !images.getKey().matches("ecology") && !images.getKey().matches("vulgar")) {
                                                ImageInfo capture = images.getValue(ImageInfo.class);
                                                capture.setVulgar(newSpecieVulgar);
                                                capture.setEco(newSpecieEco);
                                                capture.setSpecies(name.getText().toString());
                                                genReference.child("Users").child(users.getKey()).child(name.getText().toString()).child(images.getKey()).setValue(capture);
                                                genReference.child("Users").child(users.getKey()).child(name.getText().toString()).child("description").setValue(newSpecieDesc);
                                                genReference.child("Users").child(users.getKey()).child(name.getText().toString()).child("vulgar").setValue(newSpecieVulgar);
                                                genReference.child("Users").child(users.getKey()).child(name.getText().toString()).child("ecology").setValue(newSpecieEco);
                                                if(!species.matches(name.getText().toString()))
                                                    genReference.child("Users").child(users.getKey()).child(species).removeValue();
                                            }
                                        }
                                    }
                                }

                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            if(!species.matches(name.getText().toString())){
                mDatabase.child(species).removeValue();
            }


        Intent goTo = new Intent(editSpeciesDetails.this, galleryFeed.class);
        goTo.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(goTo);
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
