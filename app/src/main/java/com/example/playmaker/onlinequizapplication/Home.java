package com.example.playmaker.onlinequizapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.playmaker.onlinequizapplication.Common.Common;


import java.util.Random;

public class Home extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    BroadcastReceiver mRegisterationBroadcastReceiver;

    /**not understood**/
    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegisterationBroadcastReceiver);
        super.onPause();
    }

    /**not understood**/
    @Override
    protected void onPostResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegisterationBroadcastReceiver,
                new IntentFilter("registrationCompleted"));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegisterationBroadcastReceiver,
                new IntentFilter(Common.STR_PUSH));
        super.onPostResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        registrationNotification();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment selectFragment = null;

                switch (item.getItemId()) {

                    case R.id.action_category:
                        selectFragment = CategoryFragment.newInstance();
                        break;

                    case R.id.action_ranking:
                        selectFragment = RakingFragment.newInstance();
                        break;
                }

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectFragment);
                transaction.commit();
                return true;
            }
        });
        setDefaultFragment();
    }

    /**not understood**/
    private void registrationNotification() {
        mRegisterationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Common.STR_PUSH)) {
                    String message = intent.getStringExtra("message");
                    showNotification("IQ test", message);
                }
            }
        };
    }

    private void showNotification(String title, String message) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getBaseContext(), 0, intent, PendingIntent
                .FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext(), "notification");
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launch)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(contentIntent)
                .setVibrate(null); // Passing null here silently fails;

        NotificationManager notificationManager = (NotificationManager) getBaseContext().getSystemService(Context.
                NOTIFICATION_SERVICE);
        notificationManager.notify(new Random().nextInt(), builder.build());

    }

    private void setDefaultFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, CategoryFragment.newInstance());
        transaction.commit();
    }
}
