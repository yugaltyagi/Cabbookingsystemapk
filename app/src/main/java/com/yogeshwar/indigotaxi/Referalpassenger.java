package com.yogeshwar.indigotaxi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Referalpassenger extends AppCompatActivity {

    private Button btn;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referalpassenger);

        Button btn = (Button)findViewById(R.id.Referalbtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Referalpassenger.this, passengerMap.class));        }
        });

    }
}