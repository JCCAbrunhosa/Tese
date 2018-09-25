package com.example.jcca.teseandroid.Misc;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.example.jcca.teseandroid.DataObjects.ImageInfo;
import com.example.jcca.teseandroid.Gallery.galleryFeed;
import com.example.jcca.teseandroid.Gallery.photosToReview;
import com.example.jcca.teseandroid.Glide_Module.GlideApp;
import com.example.jcca.teseandroid.R;
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

    Button submit;
    Button reject;

    List<String> listOfSpecies = new ArrayList<>();
    boolean exists = false;

    ImageView image;

    String key;
    String data;
    String species;
    String uid;
    String vulg;
    String date;

    TextInputLayout layoutEco;
    TextInputLayout layoutDesc;
    TextInputLayout layoutVulgar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);


        //Database references
        mDatabase = FirebaseDatabase.getInstance().getReference();
        toDatabase = FirebaseDatabase.getInstance().getReference().child("PhotosReviewed");

        //Get intents from previous activity
        final String url = getIntent().getStringExtra("URL");
        key = getIntent().getStringExtra("photoName");
        data = getIntent().getStringExtra("photoName");
        species = getIntent().getStringExtra("Species");
        uid = getIntent().getStringExtra("UID");
        date = getIntent().getStringExtra("Date");

        //If the species exist they are put on top of the activity
        if(species.matches(""))
            setTitle(R.string.newSight);
        else
            setTitle(species);
        toolbar.setTitleTextColor(0x00000000);

        //Fetch elements id
        submit = findViewById(R.id.submitData);
        reject = findViewById(R.id.rejectData);
        especie = findViewById(R.id.test);
        descricao = findViewById(R.id.descrição);
        ecologia = findViewById(R.id.ecologia);
        vulgar = findViewById(R.id.vulgar);
        submit = findViewById(R.id.submitData);
        image = findViewById(R.id.app_bar_image);

        layoutDesc = (TextInputLayout) findViewById(R.id.descricaoLayout);
        layoutEco= (TextInputLayout) findViewById(R.id.ecologiaLayout);
        layoutVulgar= (TextInputLayout) findViewById(R.id.vulgarLayout);

        //Alert Boxes for data input

        //Description
        descricao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(editDetails.this);
                LayoutInflater li = LayoutInflater.from(editDetails.this);
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
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(editDetails.this);
                LayoutInflater li = LayoutInflater.from(editDetails.this);
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


        //This variable will always check the input on the Species field
        //If a new one is being inserted then a description will be needed
        checkInputFinished.run();

        /*if(getIntent().getStringExtra("PreviousIntent").matches("onClickImage"))
            descricao.setVisibility(View.GONE);
        else
            descricao.setVisibility(View.VISIBLE);*/

        //Gets the image clicked from Firebase
        mDatabase.child("Species").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
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

        //Checks if the species exist or not (handler on the bottom)
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
        GlideApp.with(getApplicationContext()).load(url).into(image);


        //Passing values from new image notification to edit details activity
        //Submit the data to the Firebase database
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (species.matches("")) {
                        if (!especie.getText().toString().matches("")) {
                            mDatabase.child("toReview").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    ImageInfo newImage = dataSnapshot.child(key).getValue(ImageInfo.class);
                                    newImage.setVulgar(vulgar.getText().toString());
                                    newImage.setSpecies(especie.getText().toString());
                                    newImage.setEco(ecologia.getText().toString());


                                    toDatabase.child(key).setValue(newImage);
                                    mDatabase.child("Species").child(especie.getText().toString()).child(key).setValue(newImage);
                                    mDatabase.child("Users").child(uid).child(especie.getText().toString()).child(key).setValue(newImage);

                                    if(exists==false){
                                        mDatabase.child("Species").child(especie.getText().toString()).child("description").setValue(descricao.getText().toString());
                                        mDatabase.child("Species").child(especie.getText().toString()).child("vulgar").setValue(vulgar.getText().toString());
                                        mDatabase.child("Species").child(especie.getText().toString()).child("ecology").setValue(ecologia.getText().toString());
                                        mDatabase.child("Users").child(uid).child(especie.getText().toString()).child("description").setValue(descricao.getText().toString());
                                        mDatabase.child("Users").child(uid).child(especie.getText().toString()).child("vulgar").setValue(vulgar.getText().toString());
                                        mDatabase.child("Users").child(uid).child(especie.getText().toString()).child("ecology").setValue(ecologia.getText().toString());
                                    }

                                   // mDatabase.child("Users").child(uid).child(especie.getText().toString()).child(key).child("species").setValue(especie.getText().toString());
                                   // mDatabase.child("Users").child(uid).child(especie.getText().toString()).child(key).child("vulgar").setValue(vulgar.getText().toString());
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            mDatabase.child("toReview").child(key).getRef().removeValue();
                            mDatabase.child("Users").child(uid).child("ToReview").child(key).removeValue();

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
                        mDatabase.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                    ImageInfo image = dataSnapshot.child(uid).child(species).child(data).getValue(ImageInfo.class);

                                    mDatabase.child("Species").child(image.getSpecies()).child(data).removeValue();

                                    image.setEco(ecologia.getText().toString());
                                    image.setVulgar(vulgar.getText().toString());
                                    image.setSpecies(especie.getText().toString());

                                    mDatabase.child("Users").child(uid).child(especie.getText().toString()).child(key).setValue(image);
                                    mDatabase.child("Species").child(especie.getText().toString()).child(key).setValue(image);
                                    mDatabase.child("Species").child(species).child(key).removeValue();
                                    mDatabase.child("Users").child(uid).child(species).child(key).removeValue();

                                    if(!descricao.getText().toString().matches("")){
                                        mDatabase.child("Species").child(especie.getText().toString()).child("description").setValue(descricao.getText().toString());
                                        mDatabase.child("Species").child(especie.getText().toString()).child("vulgar").setValue(vulgar.getText().toString());
                                        mDatabase.child("Species").child(especie.getText().toString()).child("ecology").setValue(ecologia.getText().toString());
                                        mDatabase.child("Users").child(uid).child(especie.getText().toString()).child("description").setValue(descricao.getText().toString());
                                        mDatabase.child("Users").child(uid).child(especie.getText().toString()).child("vulgar").setValue(vulgar.getText().toString());
                                        mDatabase.child("Users").child(uid).child(especie.getText().toString()).child("ecology").setValue(ecologia.getText().toString());
                                    }


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        Toast.makeText(getApplicationContext(), "Informação editada!", Toast.LENGTH_SHORT).show();


                        Intent goBack = new Intent(getApplicationContext(), photosToReview.class);
                        startActivity(goBack);
                    }

            }

        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(editDetails.this)
                        .setTitle("Rejeitar Avistamento")
                        .setMessage("Confirme se deseja rejeitar o avistamento:")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                mDatabase.child("toReview").child(key).getRef().removeValue();
                                mDatabase.child("Users").child(especie.getText().toString()).child(uid).child(key).getRef().removeValue();
                                mDatabase.child("Species").child(species).child(key).getRef().removeValue();
                                mDatabase.child("PhotosReviewed").child(key).getRef().removeValue();
                                Intent goBack = new Intent(getApplicationContext(), photosToReview.class);
                                startActivity(goBack);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();

            }
        });

        //Image popup
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imagePopup.viewPopup();
            }
        });

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
                if (!listOfSpecies.contains(especie.getText().toString()) && especie.getText().length()>0) {
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


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_reject) {
                new AlertDialog.Builder(editDetails.this)
                        .setTitle("Rejeitar Avistamento")
                        .setMessage("Confirme se deseja rejeitar o avistamento:")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                mDatabase.child("toReview").child(key).getRef().removeValue();
                                mDatabase.child("Users").child(uid).child(key).getRef().removeValue();
                                mDatabase.child("Species").child(species).child(key).getRef().removeValue();
                                mDatabase.child("PhotosReviewed").child(key).getRef().removeValue();
                                Intent goBack = new Intent(getApplicationContext(), photosToReview.class);
                                startActivity(goBack);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }

}


