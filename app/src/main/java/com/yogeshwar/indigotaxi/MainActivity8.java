//package com.yogeshwar.indigotaxi;
//
//import static com.yogeshwar.indigotaxi.MainActivity9.SHARED_PREFS;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Patterns;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.activity.result.ActivityResult;
//import androidx.activity.result.ActivityResultCallback;
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContracts;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.gms.auth.api.signin.GoogleSignIn;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.common.api.ApiException;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//public class MainActivity8 extends AppCompatActivity {
//
//    private EditText loginEmail, loginPassword;
//    private TextView signupRedirectText;
//    private Button loginButton;
//    private FirebaseAuth auth;
//    private TextView forgotPassword;
//    private GoogleSignInOptions gOptions;
//    private FirebaseAuth.AuthStateListener firebaseAuthListener;
//    private GoogleSignInClient gClient;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main8);
//
//        loginEmail = findViewById(R.id.login_email);
//        loginPassword = findViewById(R.id.login_password);
//        loginButton = findViewById(R.id.login_button);
//        signupRedirectText = findViewById(R.id.signUpRedirectText);
//        forgotPassword = findViewById(R.id.forgot_password);
//
//
//        auth = FirebaseAuth.getInstance();
//        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                if (user!= null){
//                    Intent intent = new Intent(MainActivity8.this, MainActivity9.class);
//                    startActivity(intent);
//                    finish();
//                    return;
//                }
//            }
//        };
//
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final String email = loginEmail.getText().toString();
//               final String pass = loginPassword.getText().toString();
//
//
//                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//                    if (!pass.isEmpty()) {
//                        auth.signInWithEmailAndPassword(email, pass)
//                                .addOnCompleteListener(MainActivity8.this, new OnCompleteListener<AuthResult>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<AuthResult> task) {
//                                        if (task.isSuccessful()) {
//
//                                            String DriverID = auth.getCurrentUser().getUid();
//                                            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(DriverID);
//                                            current_user_db.setValue(true);
//                                        } else {
//
//                                            Toast.makeText(MainActivity8.this, "Login Failed", Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                });
//                    } else {
//                        loginPassword.setError("Empty fields are not allowed");
//                    }
//                } else if (email.isEmpty()) {
//                    loginEmail.setError("Empty fields are not allowed");
//                } else {
//                    loginEmail.setError("Please enter correct email");
//                }
//            }
//        });
//
//        signupRedirectText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity8.this, MainActivity7.class));
//            }
//        });
//
//        forgotPassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showForgotPasswordDialog();
//            }
//        });
//
//        // Inside onCreate
//        gOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
//        gClient = GoogleSignIn.getClient(this, gOptions);
//
//        GoogleSignInAccount gAccount = GoogleSignIn.getLastSignedInAccount(this);
//        if (gAccount != null) {
//            finish();
//            startActivity(new Intent(MainActivity8.this, MainActivity9.class));
//        }
//
//        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
//                new ActivityResultCallback<ActivityResult>() {
//                    @Override
//                    public void onActivityResult(ActivityResult result) {
//                        if (result.getResultCode() == Activity.RESULT_OK) {
//                            Intent data = result.getData();
//                            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//                            try {
//                                GoogleSignInAccount account = task.getResult(ApiException.class);
//                                firebaseAuthWithGoogle(account);
//                            } catch (ApiException e) {
//                                Toast.makeText(MainActivity8.this, "Google Sign-In failed", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//                });
//
//    }
//    private void checkBox() {
//        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
//        String check = sharedPreferences.getString("name", "" );
//        if (check.equals("true")){
//            Toast.makeText(MainActivity8.this, "Login Successful", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(MainActivity8.this, MainActivity9.class));
//            finish();
//        }
//    }
//
//    private void showForgotPasswordDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity8.this);
//        View dialogView = getLayoutInflater().inflate(R.layout.dialog_forgot, null);
//        EditText emailBox = dialogView.findViewById(R.id.emailBox);
//
//        builder.setView(dialogView);
//        AlertDialog dialog = builder.create();
//
//        dialogView.findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String userEmail = emailBox.getText().toString();
//
//                if (TextUtils.isEmpty(userEmail) || !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
//                    Toast.makeText(MainActivity8.this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                auth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(MainActivity8.this, "Check your email", Toast.LENGTH_SHORT).show();
//                            dialog.dismiss();
//                        } else {
//                            Toast.makeText(MainActivity8.this, "Unable to send, please try again", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//        });
//        dialogView.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//        if (dialog.getWindow() != null) {
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
//        }
//        dialog.show();
//    }
//
//    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
//        // Implement Firebase authentication using the GoogleSignInAccount object
//        // ...
//    }
//
//    private void signOut() {
//        gClient.signOut()
//                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        // ...
//                    }
//                });
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        auth.removeAuthStateListener(firebaseAuthListener);
//
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        auth.addAuthStateListener(firebaseAuthListener);
//    }
//}

package com.yogeshwar.indigotaxi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity8 extends AppCompatActivity {

    private EditText loginEmail, loginPassword;
    private TextView signupRedirectText, forgotPassword;
    private Button loginButton;
    private FirebaseAuth auth;
    private GoogleSignInOptions gOptions;
    private GoogleSignInClient gClient;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        try {
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            firebaseAuthWithGoogle(account);
                        } catch (ApiException e) {
                            Toast.makeText(MainActivity8.this, "Google Sign-In failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main8);

        initializeUI();
        configureGoogleSignIn();
        checkIfAlreadySignedIn();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToSignup();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showForgotPasswordDialog();
            }
        });
    }

    private void initializeUI() {
        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        signupRedirectText = findViewById(R.id.signUpRedirectText);
        forgotPassword = findViewById(R.id.forgot_password);
        auth = FirebaseAuth.getInstance();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    startActivity(new Intent(MainActivity8.this, MainActivity9.class));
                    finish();
                }
            }
        };
    }

    private void configureGoogleSignIn() {
        gOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        gClient = GoogleSignIn.getClient(this, gOptions);
    }

    private void checkIfAlreadySignedIn() {
        GoogleSignInAccount gAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (gAccount != null) {
            startActivity(new Intent(MainActivity8.this, MainActivity9.class));
            finish();
        }
    }

    private void loginUser() {
        String email = loginEmail.getText().toString().trim();
        String pass = loginPassword.getText().toString().trim();

        if (isValidInput(email, pass)) {
            auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(MainActivity8.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        saveUserToDatabase();
                        startActivity(new Intent(MainActivity8.this, MainActivity9.class));
                        finish();
                    } else {
                        Toast.makeText(MainActivity8.this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private boolean isValidInput(String email, String pass) {
        if (email.isEmpty()) {
            loginEmail.setError("Empty fields are not allowed");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginEmail.setError("Please enter a valid email");
            return false;
        }
        if (pass.isEmpty()) {
            loginPassword.setError("Empty fields are not allowed");
            return false;
        }
        return true;
    }

    private void saveUserToDatabase() {
        String driverID = auth.getCurrentUser().getUid();
        DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(driverID);
        currentUserDb.setValue(true);
    }

    private void redirectToSignup() {
        startActivity(new Intent(MainActivity8.this, MainActivity7.class));
    }

    private void showForgotPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity8.this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_forgot, null);
        EditText emailBox = dialogView.findViewById(R.id.emailBox);

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        dialogView.findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = emailBox.getText().toString();
                if (TextUtils.isEmpty(userEmail) || !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                    Toast.makeText(MainActivity8.this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
                } else {
                    sendPasswordResetEmail(userEmail, dialog);
                }
            }
        });
        dialogView.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        dialog.show();
    }

    private void sendPasswordResetEmail(String userEmail, AlertDialog dialog) {
        auth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity8.this, "Check your email", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(MainActivity8.this, "Unable to send, please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        // Implement Firebase authentication using the GoogleSignInAccount object
        // ...
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuthListener != null) {
            auth.removeAuthStateListener(firebaseAuthListener);
        }
    }
}
