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

        //This variable will always check the input on the Species field
        //If a new one is being inserted then a description will be needed
        checkInputFinished.run();
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

        //Checks if the species exist or not (handler on the bottom)
        name.addTextChangedListener(new TextWatcher() {
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
        if( descricao.getText().toString().matches("") && exists==false){
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
        if(exists==false){
            mDatabase.child(name.getText().toString()).child("ecology").setValue(ecologia.getText().toString());
            mDatabase.child(name.getText().toString()).child("description").setValue(descricao.getText().toString());
            mDatabase.child(name.getText().toString()).child("vulgar").setValue(vulgar.getText().toString());
        }


            mDatabase.child(species).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        if(!snapshot.getKey().toString().matches("description") && !snapshot.getKey().toString().matches("ecology") && !snapshot.getKey().toString().matches("vulgar")){
                            ImageInfo capture = snapshot.getValue(ImageInfo.class);
                            capture.setVulgar(vulgar.getText().toString());
                            capture.setSpecies(name.getText().toString());
                            mDatabase.child(name.getText().toString()).child(snapshot.getKey()).setValue(capture);

                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            genReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        for(DataSnapshot data: snapshot.getChildren()){
                            if(data.getKey().matches(species)){
                                //genReference.child("Users").child(snapshot.getKey()).child(name.getText().toString()).child("ecology").setValue(ecologia.getText().toString());
                                //genReference.child("Users").child(snapshot.getKey()).child(name.getText().toString()).child("description").setValue(descricao.getText().toString());
                                //genReference.child("Users").child(snapshot.getKey()).child(name.getText().toString()).child("vulgar").setValue(vulgar.getText().toString());
                                for(DataSnapshot images: data.getChildren()){
                                    if(!images.getKey().toString().matches("description") && !images.getKey().toString().matches("ecology") && !images.getKey().toString().matches("vulgar")) {
                                        ImageInfo capture = images.getValue(ImageInfo.class);
                                        capture.setVulgar(vulgar.getText().toString());
                                        capture.setSpecies(name.getText().toString());
                                        genReference.child("Users").child(snapshot.getKey()).child(name.getText().toString()).child(images.getKey()).setValue(capture);
                                        genReference.child("Users").child(snapshot.getKey()).child(species).removeValue();

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

            mDatabase.child(species).removeValue();

        Intent goTo = new Intent(editSpeciesDetails.this, galleryFeed.class);
        goTo.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(goTo);
    }


    //Runnable - waits 1 second and checks if the description/ecology/vulgar already exists
    //If the species exist then the user doesn't need to write those fields(and hides them)

    final long delay = 500; // 1 seconds after user stops typing
    long lastTextEdition = 0;
    Handler handler = new Handler();
    private Runnable checkInputFinished = new Runnable() {
        @Override
        public void run() {
            if (System.currentTimeMillis() > lastTextEdition + delay - 500) {
                if (!listOfSpecies.contains(name.getText().toString()) && name.getText().length()>0) {
                    descricao.setVisibility(View.VISIBLE);
                    ecologia.setVisibility(View.VISIBLE);
                    vulgar.setVisibility(View.VISIBLE);

                    layoutDesc.setHint(getResources().getString(R.string.descricao));
                    layoutEco.setHint(getResources().getString(R.string.ecologia));
                    layoutVulgar.setHint(getResources().getString(R.string.vulgar));
                    exists=false;
                } else{

                    layoutDesc.setHint(getResources().getString(R.string.descLayout));
                    layoutEco.setHint(getResources().getString(R.string.ecoLayout));
                    layoutVulgar.setHint(getResources().getString(R.string.vulgarLayout));
                    descricao.setVisibility(View.GONE);
                    ecologia.setVisibility(View.GONE);
                    vulgar.setVisibility(View.GONE);
                    exists=true;

                }


            }

        }

    };

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
