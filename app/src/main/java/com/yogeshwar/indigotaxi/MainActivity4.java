package com.yogeshwar.indigotaxi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity4 extends AppCompatActivity {
    ImageButton myImageButton;
    ImageButton myImageButton2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        myImageButton = (ImageButton) findViewById(R.id.imageButton);
        myImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLoadNewActivity = new Intent(MainActivity4.this,MainActivity6.class);
                startActivity(intentLoadNewActivity);
            }
        });
        myImageButton2 = (ImageButton) findViewById(R.id.imageButton2);
        myImageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLoadNewActivity = new Intent(MainActivity4.this,MainActivity5.class);
                startActivity(intentLoadNewActivity);
            }
        });
    }
}