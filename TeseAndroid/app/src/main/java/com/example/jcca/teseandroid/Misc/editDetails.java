package com.example.jcca.teseandroid.Misc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.jcca.teseandroid.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class editDetails extends AppCompatActivity {

    //Firebase Reference
    DatabaseReference mDatabase;

    //UI
    EditText especie;
    EditText descricao;
    EditText ecologia;

    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_details);

        mDatabase= FirebaseDatabase.getInstance().getReference();


        especie = findViewById(R.id.especie);
        descricao = findViewById(R.id.descrição);
        ecologia = findViewById(R.id.ecologia);
        submit = findViewById(R.id.submitData);

        //Passing values from new image notification to edit details activity
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child("Species").child(especie.getText().toString()).child(getIntent().getStringExtra("photoName")).child("description").setValue(descricao.getText().toString());
                mDatabase.child("Species").child(especie.getText().toString()).child(getIntent().getStringExtra("photoName")).child("eco").setValue(ecologia.getText().toString());

                //mDatabase.child("NewPhotos").child(getIntent().getStringExtra("photoName")).removeValue();
            }
        });



    }
}
