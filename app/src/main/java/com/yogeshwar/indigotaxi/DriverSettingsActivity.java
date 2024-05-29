////package com.yogeshwar.indigotaxi;
////
////import android.app.Activity;
////import android.content.Intent;
////import android.graphics.Bitmap;
////import android.net.Uri;
////import android.os.Bundle;
////import android.provider.MediaStore;
////import android.widget.Button;
////import android.widget.EditText;
////import android.widget.ImageView;
////import androidx.appcompat.app.ActionBar;
////import androidx.appcompat.app.AppCompatActivity;
////import androidx.appcompat.widget.Toolbar;
////import com.bumptech.glide.Glide;
////import com.bumptech.glide.request.RequestOptions;
////import com.google.firebase.auth.FirebaseAuth;
////import com.google.firebase.database.DataSnapshot;
////import com.google.firebase.database.DatabaseError;
////import com.google.firebase.database.DatabaseReference;
////import com.google.firebase.database.FirebaseDatabase;
////import com.google.firebase.database.ValueEventListener;
////import com.google.firebase.storage.FirebaseStorage;
////import com.google.firebase.storage.StorageReference;
////import com.google.firebase.storage.UploadTask;
////import org.jetbrains.annotations.NotNull;
////import java.io.IOException;
////import java.util.HashMap;
////import java.util.Map;
////
/////**
//// * Activity that displays the settings to the Driver
//// */
////public class DriverSettingsActivity extends AppCompatActivity {
////
////    private EditText mNameField, mPhoneField, mCarField, mLicense, mService;
////    private ImageView mProfileImage;
////    private DatabaseReference mDriverDatabase;
////    private String userID;
////    private Uri resultUri;
////    private DriverObject mDriver = new DriverObject();
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_driver_settings);
////
////        // Initialize fields
////        mNameField = findViewById(R.id.Name);
////        mPhoneField = findViewById(R.id.phone);
////        mCarField = findViewById(R.id.car);
////        mLicense = findViewById(R.id.license);
////        mProfileImage = findViewById(R.id.profileImage);
////        mService = findViewById(R.id.service);
////        Button mConfirm = findViewById(R.id.confirm);
////
////        // Initialize Firebase Authentication and Database Reference
////        FirebaseAuth mAuth = FirebaseAuth.getInstance();
////        userID = mAuth.getCurrentUser().getUid();
////        mDriverDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(userID);
////
////        // Load user info
////        getUserInfo();
////
////        // Set up listeners
////        mProfileImage.setOnClickListener(v -> {
////            Intent intent = new Intent(Intent.ACTION_PICK);
////            intent.setType("image/*");
////            startActivityForResult(intent, 1);
////        });
////
////        mService.setOnClickListener(view -> {
////            Intent i = new Intent(DriverSettingsActivity.this, DriverChooseTypeActivity.class);
////            i.putExtra("service", mDriver.getService());
////            startActivityForResult(i, 2);
////        });
////
////        mConfirm.setOnClickListener(v -> saveUserInformation());
////        setupToolbar();
////    }
////
////    /**
////     * Sets up toolbar with custom text and a listener
////     * to go back to the previous activity
////     */
////    private void setupToolbar() {
////        Toolbar myToolbar = findViewById(R.id.toolbar);
////        setSupportActionBar(myToolbar);
////        ActionBar ab = getSupportActionBar();
////        if (ab != null) {
////            ab.setTitle(getString(R.string.settings));
////            ab.setDisplayHomeAsUpEnabled(true);
////        }
////        myToolbar.setTitleTextColor(getResources().getColor(R.color.white));
////        myToolbar.setNavigationOnClickListener(v -> finish());
////    }
////
////    /**
////     * Fetches current user's info and populates the design elements
////     */
////    private void getUserInfo(){
////        mDriverDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
////            @Override
////            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
////                if (!dataSnapshot.exists()) {
////                    return;
////                }
////                mDriver.parseData(dataSnapshot);
////                mNameField.setText(mDriver.getName());
////                mPhoneField.setText(mDriver.getPhone());
////                mCarField.setText(mDriver.getCar());
////                mLicense.setText(mDriver.getLicense());
////                mService.setText(Utils.getTypeById(DriverSettingsActivity.this, mDriver.getService()).getName());
////
////                if (!mDriver.getProfileImage().equals("default"))
////                    Glide.with(getApplication()).load(mDriver.getProfileImage()).apply(RequestOptions.circleCropTransform()).into(mProfileImage);
////            }
////
////            @Override
////            public void onCancelled(@NotNull DatabaseError databaseError) {
////                // Handle database error
////            }
////        });
////    }
////
////    /**
////     * Saves current user 's info to the database.
////     * If the resultUri is not null that means the profile image has been changed
////     * and we need to upload it to the storage system and update the database with the new url
////     */
////    private void saveUserInformation() {
////        String name = mNameField.getText().toString();
////        String phone = mPhoneField.getText().toString();
////        String car = mCarField.getText().toString();
////        String license = mLicense.getText().toString();
////        String service = mDriver.getService();
////
////        Map<String, Object> userInfo = new HashMap<>();
////        userInfo.put("name", name);
////        userInfo.put("phone", phone);
////        userInfo.put("car", car);
////        userInfo.put("license", license);
////        userInfo.put("service", service);
////        mDriverDatabase.updateChildren(userInfo);
////
////        if(resultUri != null) {
////            final StorageReference filePath = FirebaseStorage.getInstance().getReference().child("profile_images").child(userID);
////            UploadTask uploadTask = filePath.putFile(resultUri);
////            uploadTask.addOnFailureListener(e -> finish());
////            uploadTask.addOnSuccessListener(taskSnapshot -> filePath.getDownloadUrl().addOnSuccessListener(uri -> {
////                Map<String, Object> newImage = new HashMap<>();
////                newImage.put("profileImageUrl", uri.toString());
////                mDriverDatabase.updateChildren(newImage);
////                finish();
////            }).addOnFailureListener(exception -> finish()));
////        } else {
////            finish();
////        }
////    }
////
////    @Override
////    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
////        super.onActivityResult(requestCode, resultCode, data);
////        if(requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
////            resultUri = data.getData();
////            try {
////                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
////                Glide.with(getApplication()).load(bitmap).apply(RequestOptions.circleCropTransform()).into(mProfileImage);
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
////        }
////        if(requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
////            String result = data.getStringExtra("result");
////            mDriver.setService(result);
////            mService.setText(Utils.getTypeById(DriverSettingsActivity.this, result).getName());
////        }
////    }
////}
//
//package com.yogeshwar.indigotaxi;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//
//import com.bumptech.glide.Glide;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//public class DriverSettingsActivity extends AppCompatActivity {
//
//    private ImageView upload;
//    private Button cp;
//    private Button setting;
//    private Toolbar toolbar;
//    private String userID;
//
//    private EditText userName, contactNumber,  mCarField, mLicense, mService;;
//    private String mName;
//    private String mContact;
//    private ImageView mProfileImage;
//    private Uri resultUri;
//    private String mProfileUrl;
//    private FirebaseAuth mAuth;
//    private String mCar;
//    private DatabaseReference mDriverDatabase;
//    private StorageReference storageReference;
//
//    @SuppressLint("MissingInflatedId")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_driver_settings);
//
//        upload = findViewById(R.id.Upload);
//
//        setting = findViewById(R.id.setting);
//        userName = findViewById(R.id.user_name);
//        contactNumber = findViewById(R.id.Contactnumber);
//        toolbar = findViewById(R.id.toolbarsetting);
//        mProfileImage = findViewById(R.id.Upload);
//
//        mCarField = (EditText) findViewById(R.id.car);
//
//        mAuth = FirebaseAuth.getInstance();
//        userID = mAuth.getCurrentUser().getUid();
//        mDriverDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(userID);
//        storageReference = FirebaseStorage.getInstance().getReference().child("profile images").child(userID);
//
//        setSupportActionBar(toolbar);
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }
//
//        setting.setOnClickListener(v -> saveUserInformation());
//
//        mProfileImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType("image/*");
//                startActivityForResult(intent, 1);
//            }
//        });
//
//        cp.setOnClickListener(v -> {
//            Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            startActivityForResult(openGalleryIntent, 1000);
//        });
//
//        getUserInfo();
//    }
//
//    private void saveUserInformation() {
//        mName = userName.getText().toString();
//        mContact = contactNumber.getText().toString();
//        mCar = mCarField.getText().toString();
//
//        if (mName.isEmpty() || mContact.isEmpty()) {
//            Toast.makeText(DriverSettingsActivity.this, "Please enter both name and contact number", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        Map<String, Object> userInfo = new HashMap<>();
//        userInfo.put("name", mName);
//        userInfo.put("contact", mContact);
//        userInfo.put("car", mCar);
//        mDriverDatabase.updateChildren(userInfo);
//
//        if (resultUri != null) {
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
//                byte[] data = baos.toByteArray();
//
//                UploadTask uploadTask = storageReference.putBytes(data);
//                uploadTask.addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(DriverSettingsActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//                                Map<String, Object> newImage = new HashMap<>();
//                                newImage.put("profileImageUrl", uri.toString());
//                                mDriverDatabase.updateChildren(newImage);
//                                finish();
//                            }
//                        });
//                    }
//                });
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//            Toast.makeText(DriverSettingsActivity.this, "Settings saved", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(DriverSettingsActivity.this, passengerMap.class);
//            startActivity(intent);
//        }
//    }
//
//    private void getUserInfo() {
//        mDriverDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
//                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
//                    if (map != null) {
//                        if (map.get("name") != null) {
//                            mName = map.get("name").toString();
//                            userName.setText(mName);
//                        }
//                        if (map.get("contact") != null) {
//                            mContact = map.get("contact").toString();
//                            contactNumber.setText(mContact);
//                        }
//                        if (map.get("profileImageUrl") != null) {
//                            mProfileUrl = map.get("profileImageUrl").toString();
//                            Glide.with(getApplication()).load(mProfileUrl).into(mProfileImage);
//                        }
//                        if (map.get("car") != null) {
//                            mCar = map.get("car").toString();
//                            mCarField.setText(mCar);
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(DriverSettingsActivity.this, "Failed to get user information", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
//            resultUri = data.getData();
//            mProfileImage.setImageURI(resultUri);
//        }
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            onBackPressed();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//}

package com.yogeshwar.indigotaxi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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

public class DriverSettingsActivity extends AppCompatActivity {

    private EditText mNameField, mPhoneField, mCarField, mLicense, mService;
    private ImageView mProfileImage;
    private DatabaseReference mDriverDatabase;
    private String userID;
    private Uri resultUri;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_settings);

        initializeUI();
        setupToolbar();
        getUserInfo();

        mProfileImage.setOnClickListener(v -> pickImage());
        findViewById(R.id.confirm).setOnClickListener(v -> saveUserInformation());
    }

    private void initializeUI() {
        mNameField = findViewById(R.id.Name);
        mPhoneField = findViewById(R.id.phone);
        mCarField = findViewById(R.id.car);
        mLicense = findViewById(R.id.license);
        mService = findViewById(R.id.service);
        mProfileImage = findViewById(R.id.profileImage);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        mDriverDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(userID);
    }

    private void setupToolbar() {
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.indigo));
        }
        myToolbar.setNavigationOnClickListener(v -> finish());
    }

    private void getUserInfo() {
        mDriverDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map != null) {
                        mNameField.setText((String) map.get("name"));
                        mPhoneField.setText((String) map.get("contact"));
                        mCarField.setText((String) map.get("car"));
                        mLicense.setText((String) map.get("license"));
                        mService.setText((String) map.get("service"));
                        String profileImageUrl = (String) map.get("profileImageUrl");
                        if (profileImageUrl != null && !profileImageUrl.equals("default")) {
                            Glide.with(getApplication()).load(profileImageUrl).apply(RequestOptions.circleCropTransform()).into(mProfileImage);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DriverSettingsActivity.this, "Failed to get user information", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserInformation() {
        String name = mNameField.getText().toString();
        String contact = mPhoneField.getText().toString();
        String car = mCarField.getText().toString();
        String license = mLicense.getText().toString();
        String service = mService.getText().toString();

        if (name.isEmpty() || contact.isEmpty()) {
            Toast.makeText(this, "Please enter both name and contact number", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", name);
        userInfo.put("contact", contact);
        userInfo.put("car", car);
        userInfo.put("license", license);
        userInfo.put("service", service);
        mDriverDatabase.updateChildren(userInfo);

        if (resultUri != null) {
            uploadImageToFirebase();
        } else {
            Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void uploadImageToFirebase() {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();

            StorageReference filePath = FirebaseStorage.getInstance().getReference().child("profile_images").child(userID);
            UploadTask uploadTask = filePath.putBytes(data);
            uploadTask.addOnFailureListener(e -> Toast.makeText(DriverSettingsActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show())
                    .addOnSuccessListener(taskSnapshot -> filePath.getDownloadUrl().addOnSuccessListener(uri -> {
                        Map<String, Object> newImage = new HashMap<>();
                        newImage.put("profileImageUrl", uri.toString());
                        mDriverDatabase.updateChildren(newImage);
                        finish();
                    }));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
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
