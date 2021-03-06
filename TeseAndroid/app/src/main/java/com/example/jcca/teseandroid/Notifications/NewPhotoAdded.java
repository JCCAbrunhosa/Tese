package com.example.jcca.teseandroid.Notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.jcca.teseandroid.Gallery.photosToReview;
import com.example.jcca.teseandroid.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class NewPhotoAdded extends Service {


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        final DatabaseReference newPhotoAdded;
        newPhotoAdded = FirebaseDatabase.getInstance().getReference().child("toReview");
        final DatabaseReference whereToBuild = FirebaseDatabase.getInstance().getReference().child("Accounts");
        int counter=0;
        final SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final boolean check = sharedPreferences.getBoolean("notifications_new_message",false);
        final boolean checkVibration = sharedPreferences.getBoolean("notifications_new_message_vibrate", true);

        newPhotoAdded.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Random random = new Random();
                final int mNotificationId= 1;

                Log.d("BOOLEAN", String.valueOf(check));

                //Notificação para um investigador
                //Enviar notificação
                final NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(NewPhotoAdded.this)
                                .setSmallIcon(R.drawable.side_nav_bar)
                                .setContentTitle("Nova Capturas!")
                                .setContentText("Clique para ver as novas capturas.")
                                .setCategory(NotificationCompat.CATEGORY_MESSAGE);


                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    Notification noti = new Notification.Builder(NewPhotoAdded.this)
                            .setContentTitle("Nova Captura!")
                            .setContentText("Clique para ver as novas capturas.")
                            .setSmallIcon(R.drawable.side_nav_bar)
                            .build();
                }

                if(checkVibration){
                    mBuilder.setVibrate(new long[] { 1000, 1000});
                }


                mBuilder.setAutoCancel(true);
                //Notification Action
                Intent resultIntent = new Intent(NewPhotoAdded.this, photosToReview.class);
                Bundle toSend = new Bundle();
                toSend.putString("URL", dataSnapshot.child("url").getValue().toString());
                toSend.putString("Date", dataSnapshot.child("date").getValue().toString());
                toSend.putString("Species", dataSnapshot.child("species").getValue().toString());
                toSend.putString("Vulgar", dataSnapshot.child("vulgar").getValue().toString());
                toSend.putString("Lat", dataSnapshot.child("location").child("latitude").getValue().toString());
                toSend.putString("Long", dataSnapshot.child("location").child("longitude").getValue().toString());
                toSend.putString("photoName", dataSnapshot.getKey());
                toSend.putString("UID", dataSnapshot.child("uid").getValue().toString());
                resultIntent.putExtras(toSend);
                // Because clicking the notification opens a new ("special") activity, there's
                // no need to create an artificial back stack.
                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(
                                NewPhotoAdded.this,
                                mNotificationId,
                                resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );

                mBuilder.setContentIntent(resultPendingIntent);



                // Sets an ID for the notification

                // Gets an instance of the NotificationManager service
                final NotificationManager mNotifyMgr =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);



                //Notificação para um utilizador comum
                whereToBuild.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getRef()==null)
                            return ;
                        if(dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("isPro").getValue() != null)
                           if(check)
                            mNotifyMgr.notify(mNotificationId, mBuilder.build());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



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


        //The service will until until explicilty stopped
        return START_STICKY;
    }



}
