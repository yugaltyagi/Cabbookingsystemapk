package com.yogeshwar.indigotaxi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity6 extends AppCompatActivity {
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);
        button = (Button) findViewById(R.id.btn6);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) { openActivity6();}
            });
        }
        public void openActivity6(){
            Intent intent = new Intent(this, MainActivity7.class);
            startActivity(intent);
        }
}