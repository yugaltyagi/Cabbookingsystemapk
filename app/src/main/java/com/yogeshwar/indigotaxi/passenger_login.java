
package com.yogeshwar.indigotaxi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class passenger_login extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText signupEmail, signupPassword;
    private TextView loginRedirectText;
    private DatabaseReference CustomerdatabaseRef;
    private String onlineCustomerID;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_login);

        auth = FirebaseAuth.getInstance();
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        Button signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        loadingBar = new ProgressDialog(this);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = signupEmail.getText().toString().trim();
                String pass = signupPassword.getText().toString().trim();

                if (user.isEmpty()) {
                    signupEmail.setError("Email cannot be empty");
                    return;
                }
                if (pass.isEmpty()) {
                    signupPassword.setError("Password cannot be empty");
                    return;
                }

                loadingBar.setTitle("Passenger Registration");
                loadingBar.setMessage("Please wait, while we register your data...");
                loadingBar.show();

                auth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        loadingBar.dismiss();
                        if (task.isSuccessful()) {
                            onlineCustomerID = auth.getCurrentUser().getUid();
                            CustomerdatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(onlineCustomerID);
                            CustomerdatabaseRef.setValue(true);

                            Intent passengerIntent = new Intent(passenger_login.this, passengerMap.class);
                            startActivity(passengerIntent);
                            Toast.makeText(passenger_login.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(passenger_login.this, "SignUp Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(passenger_login.this, Passengerlogin_step2.class));
            }
        });
    }
}
