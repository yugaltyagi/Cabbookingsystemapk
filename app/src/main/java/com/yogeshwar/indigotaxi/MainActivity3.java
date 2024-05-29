package com.yogeshwar.indigotaxi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity3 extends AppCompatActivity {
    ImageButton myImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        myImageButton = (ImageButton) findViewById(R.id.jump1);
        myImageButton.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLoadNewActivity = new Intent(MainActivity3.this, MainActivity4.class);
                startActivity(intentLoadNewActivity);
            }
        });
    }
}