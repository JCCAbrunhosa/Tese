package com.example.jcca.teseandroid.Login_Registering;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jcca.teseandroid.Gallery.galleryFeed;
import com.example.jcca.teseandroid.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView1;
    private EditText mPasswordView2;
    private EditText mName;
    private View mProgressView;
    private View mLoginFormView;
    private CheckBox isPro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView1 = (EditText) findViewById(R.id.pwd1);
        mPasswordView2 = (EditText) findViewById(R.id.pwd2);

        mName = findViewById(R.id.name);
        isPro = findViewById(R.id.isPro);

        isPro = findViewById(R.id.isPro);

        Button mEmailSignInButton = (Button) findViewById(R.id.createAccount);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.createUserWithEmailAndPassword(mEmailView.getText().toString(), mPasswordView1.getText().toString())
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information

                                    FirebaseUser user = mAuth.getCurrentUser();

                                    if(isPro.isChecked()){
                                        sendVerificationEmail();
                                        mDatabase.child("Accounts").child(user.getUid()).child("isPro").setValue(true);
                                        mDatabase.child("Accounts").child(user.getUid()).child("userName").setValue(mName.getText().toString());
                                        Toast.makeText( RegisterActivity.this,"Email de Verificação Enviado",Toast.LENGTH_SHORT).show();
                                        FirebaseAuth.getInstance().signOut();
                                        Intent goTo = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(goTo);
                                    }else{
                                        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://catchabug-teste.firebaseio.com/Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        Intent goTo = new Intent(getApplicationContext(), galleryFeed.class);
                                        startActivity(goTo);
                                    }


                                } else {
                                    // If sign in fails, display a message to the user.
                                    try{
                                        throw task.getException();
                                    } catch (FirebaseAuthUserCollisionException e) {
                                        Toast.makeText( RegisterActivity.this,"Conta já existente!",Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        Toast.makeText( RegisterActivity.this,e.toString(),Toast.LENGTH_SHORT).show();

                                    }


                                    mPasswordView1.clearComposingText();
                                    mPasswordView2.clearComposingText();
                                }


                            }
                        });

            }
        });


        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }





    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    private void sendVerificationEmail()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // email sent
                            // after email is sent just logout the user and finish this activity
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                        }
                        else
                        {
                            // email not sent, so display message and restart the activity or do whatever you wish to do

                            //restart this activity
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());

                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }

}

