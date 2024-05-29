//package com.yogeshwar.indigotaxi;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//
//public class DriverlicenceUpload extends AppCompatActivity {
//
//    private static final int PROFILE_PHOTO_REQUEST_CODE = 1000;
//    private static final int DD_PHOTO_REQUEST_CODE = 1001;
//    private static final int PANCARD_PHOTO_REQUEST_CODE = 1002;
//    private static final int RC_PHOTO_REQUEST_CODE = 1003;
//    private static final int VICHELINSURENCE_PHOTO_REQUEST_CODE = 1004;
//    private static final int VECHILPERMIT_PHOTO_REQUEST_CODE = 1005;
//
//    private ImageView Upload1, Upload2, Upload3, Upload4, Upload5, Upload6;
//    private Button uploadB1, uploadB2, uploadB3, uploadB4, uploadB5, uploadB6;
//    private Button Upload_Documents;
//    private StorageReference storageReferenceForDriver;
//    private Toolbar toolbar;
//
//    private Uri profilePhotoUri, ddPhotoUri, pancardPhotoUri, rcPhotoUri, vichelinsurencePhotoUri, vechilpermitPhotoUri;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_driverlicence_upload);
//
//        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
//            toolbar.setTitle("Indigo Taxi");
//            toolbar.setSubtitle("Drivers Documents upload");
//        }
//
//        Upload1 = findViewById(R.id.profilephotoview);
//        Upload2 = findViewById(R.id.ddview);
//        Upload3 = findViewById(R.id.pancardview);
//        Upload4 = findViewById(R.id.RCsview);
//        Upload5 = findViewById(R.id.Vichelinsurenceview);
//        Upload6 = findViewById(R.id.vechilpermitview);
//
//        uploadB1 = findViewById(R.id.profilephoto);
//        uploadB2 = findViewById(R.id.dd);
//        uploadB3 = findViewById(R.id.pancard);
//        uploadB4 = findViewById(R.id.RC);
//        uploadB5 = findViewById(R.id.Vichelinsurence);
//        uploadB6 = findViewById(R.id.vechilpermit);
//
//        Upload_Documents = findViewById(R.id.Nextstep);
//
//        storageReferenceForDriver = FirebaseStorage.getInstance().getReference();
//
//        setOnClickListener(uploadB1, PROFILE_PHOTO_REQUEST_CODE);
//        setOnClickListener(uploadB2, DD_PHOTO_REQUEST_CODE);
//        setOnClickListener(uploadB3, PANCARD_PHOTO_REQUEST_CODE);
//        setOnClickListener(uploadB4, RC_PHOTO_REQUEST_CODE);
//        setOnClickListener(uploadB5, VICHELINSURENCE_PHOTO_REQUEST_CODE);
//        setOnClickListener(uploadB6, VECHILPERMIT_PHOTO_REQUEST_CODE);
//
//        Upload_Documents.setOnClickListener(v -> {
//            if (profilePhotoUri == null || ddPhotoUri == null || pancardPhotoUri == null || rcPhotoUri == null || vichelinsurencePhotoUri == null || vechilpermitPhotoUri == null) {
//                Toast.makeText(DriverlicenceUpload.this, "Please upload all documents before proceeding.", Toast.LENGTH_LONG).show();
//            } else {
//                Toast.makeText(DriverlicenceUpload.this, "All Documents Under Verification. If verfication done succesfully you get Verification Mark By us.", Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(DriverlicenceUpload.this, MainActivity9.class);
//                    startActivity(intent);
//            }
//        });
//    }
//
//    private void setOnClickListener(Button button, int requestCode) {
//        button.setOnClickListener(v -> {
//            Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            startActivityForResult(openGalleryIntent, requestCode);
//        });
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == RESULT_OK && data != null) {
//            Uri selectedImage = data.getData();
//            if (selectedImage != null) {
//                switch (requestCode) {
//                    case PROFILE_PHOTO_REQUEST_CODE:
//                        profilePhotoUri = selectedImage;
//                        Upload1.setImageURI(profilePhotoUri);
//                        uploadDocumentsToFirebase(profilePhotoUri, "Profile Photo");
//                        break;
//                    case DD_PHOTO_REQUEST_CODE:
//                        ddPhotoUri = selectedImage;
//                        Upload2.setImageURI(ddPhotoUri);
//                        uploadDocumentsToFirebase(ddPhotoUri, "Driving License");
//                        break;
//                    case PANCARD_PHOTO_REQUEST_CODE:
//                        pancardPhotoUri = selectedImage;
//                        Upload3.setImageURI(pancardPhotoUri);
//                        uploadDocumentsToFirebase(pancardPhotoUri, "PAN Card");
//                        break;
//                    case RC_PHOTO_REQUEST_CODE:
//                        rcPhotoUri = selectedImage;
//                        Upload4.setImageURI(rcPhotoUri);
//                        uploadDocumentsToFirebase(rcPhotoUri, "Registration Certificate");
//                        break;
//                    case VICHELINSURENCE_PHOTO_REQUEST_CODE:
//                        vichelinsurencePhotoUri = selectedImage;
//                        Upload5.setImageURI(vichelinsurencePhotoUri);
//                        uploadDocumentsToFirebase(vichelinsurencePhotoUri, "Vehicle Insurance");
//                        break;
//                    case VECHILPERMIT_PHOTO_REQUEST_CODE:
//                        vechilpermitPhotoUri = selectedImage;
//                        Upload6.setImageURI(vechilpermitPhotoUri);
//                        uploadDocumentsToFirebase(vechilpermitPhotoUri, "Vehicle Permit");
//                        break;
//                }
//            } else {
//                Toast.makeText(this, "Failed to get image URI", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    private void uploadDocumentsToFirebase(Uri imageUri, String documentType) {
//        StorageReference fileRef = storageReferenceForDriver.child("Drivers Documents").child(documentType);
//        fileRef.putFile(imageUri)
//                .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
//                    Toast.makeText(DriverlicenceUpload.this, "Uploaded: " + documentType, Toast.LENGTH_SHORT).show();
//                }).addOnFailureListener(e -> {
//                    Toast.makeText(DriverlicenceUpload.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
//                }))
//                .addOnFailureListener(e -> {
//                    Toast.makeText(DriverlicenceUpload.this, "Failed to upload " + documentType, Toast.LENGTH_SHORT).show();
//                });
//    }
//
//}

package com.yogeshwar.indigotaxi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DriverlicenceUpload extends AppCompatActivity {

    private static final int PROFILE_PHOTO_REQUEST_CODE = 1000;
    private static final int DD_PHOTO_REQUEST_CODE = 1001;
    private static final int PANCARD_PHOTO_REQUEST_CODE = 1002;
    private static final int RC_PHOTO_REQUEST_CODE = 1003;
    private static final int VEHICLE_INSURANCE_PHOTO_REQUEST_CODE = 1004;
    private static final int VEHICLE_PERMIT_PHOTO_REQUEST_CODE = 1005;

    private ImageView upload1, upload2, upload3, upload4, upload5, upload6;
    private Button uploadB1, uploadB2, uploadB3, uploadB4, uploadB5, uploadB6;
    private Button uploadDocumentsButton;
    private StorageReference storageReferenceForDriver;
    private Toolbar toolbar;

    private Uri profilePhotoUri, ddPhotoUri, pancardPhotoUri, rcPhotoUri, vehicleInsurancePhotoUri, vehiclePermitPhotoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driverlicence_upload);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setTitle("Indigo Taxi");
            toolbar.setSubtitle("Drivers Documents Upload");
        }

        upload1 = findViewById(R.id.profilephotoview);
        upload2 = findViewById(R.id.ddview);
        upload3 = findViewById(R.id.pancardview);
        upload4 = findViewById(R.id.RCsview);
        upload5 = findViewById(R.id.vehicleinsuranceview);
        upload6 = findViewById(R.id.vehiclepermitview);

        uploadB1 = findViewById(R.id.profilephoto);
        uploadB2 = findViewById(R.id.dd);
        uploadB3 = findViewById(R.id.pancard);
        uploadB4 = findViewById(R.id.RC);
        uploadB5 = findViewById(R.id.vehicleinsurance);
        uploadB6 = findViewById(R.id.vehiclepermit);

        uploadDocumentsButton = findViewById(R.id.Nextstep);

        storageReferenceForDriver = FirebaseStorage.getInstance().getReference();

        setOnClickListener(uploadB1, PROFILE_PHOTO_REQUEST_CODE);
        setOnClickListener(uploadB2, DD_PHOTO_REQUEST_CODE);
        setOnClickListener(uploadB3, PANCARD_PHOTO_REQUEST_CODE);
        setOnClickListener(uploadB4, RC_PHOTO_REQUEST_CODE);
        setOnClickListener(uploadB5, VEHICLE_INSURANCE_PHOTO_REQUEST_CODE);
        setOnClickListener(uploadB6, VEHICLE_PERMIT_PHOTO_REQUEST_CODE);

        uploadDocumentsButton.setOnClickListener(v -> {
            if (profilePhotoUri == null || ddPhotoUri == null || pancardPhotoUri == null || rcPhotoUri == null || vehicleInsurancePhotoUri == null || vehiclePermitPhotoUri == null) {
                Toast.makeText(DriverlicenceUpload.this, "Please upload all documents before proceeding.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(DriverlicenceUpload.this, "All Documents Under Verification. If verification is successful, you will receive a verification mark from us.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(DriverlicenceUpload.this, MainActivity9.class);
                startActivity(intent);
            }
        });
    }

    private void setOnClickListener(Button button, int requestCode) {
        button.setOnClickListener(v -> {
            Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(openGalleryIntent, requestCode);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            if (selectedImage != null) {
                switch (requestCode) {
                    case PROFILE_PHOTO_REQUEST_CODE:
                        profilePhotoUri = selectedImage;
                        upload1.setImageURI(profilePhotoUri);
                        uploadDocumentsToFirebase(profilePhotoUri, "Profile Photo");
                        break;
                    case DD_PHOTO_REQUEST_CODE:
                        ddPhotoUri = selectedImage;
                        upload2.setImageURI(ddPhotoUri);
                        uploadDocumentsToFirebase(ddPhotoUri, "Driving License");
                        break;
                    case PANCARD_PHOTO_REQUEST_CODE:
                        pancardPhotoUri = selectedImage;
                        upload3.setImageURI(pancardPhotoUri);
                        uploadDocumentsToFirebase(pancardPhotoUri, "PAN Card");
                        break;
                    case RC_PHOTO_REQUEST_CODE:
                        rcPhotoUri = selectedImage;
                        upload4.setImageURI(rcPhotoUri);
                        uploadDocumentsToFirebase(rcPhotoUri, "Registration Certificate");
                        break;
                    case VEHICLE_INSURANCE_PHOTO_REQUEST_CODE:
                        vehicleInsurancePhotoUri = selectedImage;
                        upload5.setImageURI(vehicleInsurancePhotoUri);
                        uploadDocumentsToFirebase(vehicleInsurancePhotoUri, "Vehicle Insurance");
                        break;
                    case VEHICLE_PERMIT_PHOTO_REQUEST_CODE:
                        vehiclePermitPhotoUri = selectedImage;
                        upload6.setImageURI(vehiclePermitPhotoUri);
                        uploadDocumentsToFirebase(vehiclePermitPhotoUri, "Vehicle Permit");
                        break;
                    default:
                        Toast.makeText(this, "Unknown request code: " + requestCode, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Failed to get image URI", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadDocumentsToFirebase(Uri imageUri, String documentType) {
        StorageReference fileRef = storageReferenceForDriver.child("Drivers Documents").child(documentType + "/" + imageUri.getLastPathSegment());
        fileRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    Toast.makeText(DriverlicenceUpload.this, "Uploaded: " + documentType, Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Toast.makeText(DriverlicenceUpload.this, "Failed to get download URL for " + documentType, Toast.LENGTH_SHORT).show();
                }))
                .addOnFailureListener(e -> {
                    Toast.makeText(DriverlicenceUpload.this, "Failed to upload " + documentType, Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
