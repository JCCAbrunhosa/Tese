package com.example.jcca.teseandroid.Misc;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.jcca.teseandroid.Gallery.galleryFeed;
import com.example.jcca.teseandroid.Gallery.guide_activity;
import com.example.jcca.teseandroid.Gallery.otherPhotosGallery;
import com.example.jcca.teseandroid.R;

public class initialScreen extends AppCompatActivity {


    private Button goToMA;
    private Button goToGA;
    private Button goToLE;
    private Button goToMapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        goToMA = findViewById(R.id.goToMA);
        goToGA = findViewById(R.id.goToGA);
        goToLE = findViewById(R.id.goToLE);
        goToMapa = findViewById(R.id.goToMapa);


        goToMA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent k = new Intent(initialScreen.this, galleryFeed.class);
                startActivity(k);
            }
        });

        goToGA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent k = new Intent(initialScreen.this, otherPhotosGallery.class);
                startActivity(k);
            }
        });

        goToLE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent k = new Intent(initialScreen.this, guide_activity.class);
                startActivity(k);
            }
        });

        goToMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent k = new Intent(initialScreen.this, map_activity.class);
                startActivity(k);
            }
        });

    }

}
