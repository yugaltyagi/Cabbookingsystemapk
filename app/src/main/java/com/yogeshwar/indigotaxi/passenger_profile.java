package com.yogeshwar.indigotaxi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class passenger_profile extends AppCompatActivity {

    private ImageView upload;
    private Button cp;
    private Button setting;
    private Toolbar toolbar;
    private String userID;
    private EditText userName, contactNumber;
    private String mName;
    private String mContact;
    private ImageView mProfileImage;
    private Uri resultUri;
    private String mProfileUrl;
    private FirebaseAuth mAuth;
    private DatabaseReference mCustomerDatabase;
    private StorageReference storageReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_profile);

        upload = findViewById(R.id.Upload);
        cp = findViewById(R.id.CP);
        setting = findViewById(R.id.setting);
        userName = findViewById(R.id.user_name);
        contactNumber = findViewById(R.id.Contactnumber);
        toolbar = findViewById(R.id.toolbarsetting);
        mProfileImage = findViewById(R.id.Upload);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        mCustomerDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(userID);
        storageReference = FirebaseStorage.getInstance().getReference().child("profile images").child(userID);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setting.setOnClickListener(v -> saveUserInformation());

        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        cp.setOnClickListener(v -> {
            Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(openGalleryIntent, 1000);
        });

        getUserInfo();
    }

    private void saveUserInformation() {
        mName = userName.getText().toString();
        mContact = contactNumber.getText().toString();

        if (mName.isEmpty() || mContact.isEmpty()) {
            Toast.makeText(passenger_profile.this, "Please enter both name and contact number", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", mName);
        userInfo.put("contact", mContact);
        mCustomerDatabase.updateChildren(userInfo);

        if (resultUri != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = storageReference.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(passenger_profile.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Map<String, Object> newImage = new HashMap<>();
                                newImage.put("profileImageUrl", uri.toString());
                                mCustomerDatabase.updateChildren(newImage);
                                finish();
                            }
                        });
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(passenger_profile.this, "Settings saved", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(passenger_profile.this, passengerMap.class);
            startActivity(intent);
        }
    }

    private void getUserInfo() {
        mCustomerDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map != null) {
                        if (map.get("name") != null) {
                            mName = map.get("name").toString();
                            userName.setText(mName);
                        }
                        if (map.get("contact") != null) {
                            mContact = map.get("contact").toString();
                            contactNumber.setText(mContact);
                        }
                        if (map.get("profileImageUrl") != null) {
                            mProfileUrl = map.get("profileImageUrl").toString();
                            Glide.with(getApplication()).load(mProfileUrl).into(mProfileImage);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(passenger_profile.this, "Failed to get user information", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            resultUri = data.getData();
            mProfileImage.setImageURI(resultUri);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
