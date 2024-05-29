//package com.yogeshwar.indigotaxi;
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//public class MainActivity7 extends AppCompatActivity {
//
//    private FirebaseAuth auth;
//    private EditText signupEmail, signupPassword;
//    private Button signupButton;
//    private TextView loginRedirectText;
//
//    private DatabaseReference DriverDatabaseRef;
//    private String driverID;
//
//    private ProgressDialog loadingBar;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main7);
//
//        auth = FirebaseAuth.getInstance();
//
//        signupEmail = findViewById(R.id.signup_email);
//        signupPassword = findViewById(R.id.signup_password);
//        signupButton = findViewById(R.id.signup_button);
//        loginRedirectText = findViewById(R.id.loginRedirectText);
//
//        signupButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String user = signupEmail.getText().toString().trim();
//                String pass = signupPassword.getText().toString().trim();
//
//                if (user.isEmpty()){
//                    signupEmail.setError("Email cannot be empty");
//                }
//                if (pass.isEmpty()){
//                    signupPassword.setError("Password cannot be empty");
//                } else{
//                    loadingBar.setTitle("Driver Registration");
//                    loadingBar.setMessage("please wait, while we regestring you data..");
//                    loadingBar.show();
//
//                    auth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()) {
//
//                                driverID = auth.getCurrentUser().getUid();
//                                DriverDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(driverID);
//
//                                DriverDatabaseRef.setValue(true);
//                                Intent driverIntent = new Intent(MainActivity7.this, MainActivity8.class);
//                                startActivity(driverIntent);
//                                Toast.makeText(MainActivity7.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
//                                loadingBar.dismiss();
//
//                                String userId = auth.getCurrentUser().getUid();
//                                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(userId).child("name");
//                                current_user_db.setValue(signupEmail);
//                                startActivity(new Intent(MainActivity7.this, MainActivity8.class));
//                            } else {
//                                Toast.makeText(MainActivity7.this, "SignUp Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                                loadingBar.dismiss();
//                            }
//                        }
//                    });
//                }
//
//            }
//        });
//
//        loginRedirectText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MainActivity7.this, MainActivity8.class));
//            }
//        });
//
//    }
//}

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

public class MainActivity7 extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText signupEmail, signupPassword;
    private Button signupButton;
    private TextView loginRedirectText;

    private DatabaseReference DriverDatabaseRef;
    private String driverID;

    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7);

        auth = FirebaseAuth.getInstance();

        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        loadingBar = new ProgressDialog(this);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity7.this, MainActivity8.class));
            }
        });
    }

    private void registerUser() {
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

        loadingBar.setTitle("Driver Registration");
        loadingBar.setMessage("Please wait, while we register your data...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        auth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                loadingBar.dismiss();
                if (task.isSuccessful()) {
                    handleSuccessfulRegistration();
                } else {
                    Toast.makeText(MainActivity7.this, "SignUp Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void handleSuccessfulRegistration() {
        driverID = auth.getCurrentUser().getUid();
        DriverDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(driverID);
        DriverDatabaseRef.setValue(true);

        Toast.makeText(MainActivity7.this, "SignUp Successful", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(MainActivity7.this, typeoftaxi.class));
        finish();
    }
}
