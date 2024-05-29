package com.yogeshwar.indigotaxi;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class typeoftaxi extends AppCompatActivity {

    private ImageButton mBike, mMini, mRikshaw, mPortal;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_typeoftaxi);

        // Initialize ImageButtons
        mBike = findViewById(R.id.bike);
        mMini = findViewById(R.id.mini);
        mRikshaw = findViewById(R.id.rikshaw);
        mPortal = findViewById(R.id.portal);

        // Set click listeners for each ImageButton
        mBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(typeoftaxi.this, DriverlicenceUpload.class));
                Toast.makeText(typeoftaxi.this, "Login as a Motorbike Driver", Toast.LENGTH_SHORT).show();
            }
        });

        mMini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(typeoftaxi.this, DriverlicenceUpload.class));

            Toast.makeText(typeoftaxi.this, "Login as a Mini Driver", Toast.LENGTH_SHORT).show();

            // Start another activity for mMini ImageButton
            }
        });

        mRikshaw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(new Intent(typeoftaxi.this, DriverlicenceUpload.class));
                Toast.makeText(typeoftaxi.this, "Login as a Rikshaw Driver", Toast.LENGTH_SHORT).show();
                // Start another activity for mRikshaw ImageButton
            }
        });

        mPortal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start another activity for mPortal ImageButton
                startActivity(new Intent(typeoftaxi.this, DriverlicenceUpload.class));
                Toast.makeText(typeoftaxi.this, "Portal Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });

        // Set up the toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
