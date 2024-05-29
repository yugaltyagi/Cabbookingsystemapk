//package com.yogeshwar.indigotaxi;
//
//import static com.google.firebase.auth.FirebaseAuth.getInstance;
//
//import android.Manifest;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import com.bumptech.glide.Glide;
//import com.firebase.geofire.GeoFire;
//import com.firebase.geofire.GeoLocation;
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationCallback;
//import com.google.android.gms.location.LocationListener;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationResult;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.ChildEventListener;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.List;
//import java.util.Map;
//
//public class MainActivity9 extends AppCompatActivity implements OnMapReadyCallback, LocationListener {
//    private GoogleMap myMap;
//    private FusedLocationProviderClient fusedLocationClient;
//    private LocationCallback locationCallback;
//    private FirebaseUser currentUser;
//    private FirebaseAuth mAuth;
//    private String userId;
//    private Toolbar toolbar;
//    private DatabaseReference AssainedCustomerRef;
//    private DatabaseReference assignedCustomerPickupRef;
//    private String customerID = "";
//    private Button mSettings;
//    private DatabaseReference UserId;
//    private DatabaseReference refAvailable, refWorking;
//    private String driverID;
//    private Location mLastLocation;
//    private Marker driverMarker;
//    private Marker passengerMarker;
//    private LinearLayout mCustomerInfo;
//    private ImageView mCustomerProfileimage;
//    private TextView mCustomername, mCustomercontact, mCustomerDestination;
//    public static final int MY_PERMISSION_REQUEST_CODE = 7000;
//    public static final String SHARED_PREFS = "sharedprefs";
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main9);
//
//        UserId = FirebaseDatabase.getInstance().getReference().child("driverID");
//        refAvailable = FirebaseDatabase.getInstance().getReference().child("Drivers Available");
//        refWorking = FirebaseDatabase.getInstance().getReference().child("Drivers Working");
//
//        mSettings = (Button) findViewById(R.id.Settings);
//
//        mAuth = getInstance();
//        driverID = mAuth.getCurrentUser().getUid();
//        currentUser = mAuth.getCurrentUser();
//
//        mCustomerInfo = (LinearLayout) findViewById(R.id.CutomerInfo);
//
//        mCustomerProfileimage = (ImageView) findViewById(R.id.Customerprofileimage);
//
//        mCustomername = (TextView) findViewById(R.id.customerName);
//
//        mCustomercontact = (TextView) findViewById(R.id.customerContact);
//
//        mCustomerDestination = (TextView) findViewById(R.id.customerDestination);
//
//        // Toolbar initialization
//        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }
//
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//        if (mapFragment != null) {
//            mapFragment.getMapAsync(this);
//        }
//
//        getAssignedCustomer();
//
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//
//        LocationRequest locationRequest = new LocationRequest();
//        locationRequest.setInterval(5000); // Update every 5 seconds
//        locationRequest.setFastestInterval(3000);
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//
//        // Create location callback
//        locationCallback = new LocationCallback() {
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                if (locationResult == null) {
//                    return;
//                }
//                for (Location location : locationResult.getLocations()) {
//                    updateMap(location);
//                }
//            }
//        };
//
//        // Check for location permission
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            startLocationUpdates(locationRequest);
//        } else {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_CODE);
//        }
//    }
//
//    private void startLocationUpdates(LocationRequest locationRequest) {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
//        } else {
//            // Handle the case where the permission is not granted
//            Log.e("Location", "Permission not granted");
//        }
//    }
//
//    private void updateMap(Location location) {
//        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
//        if (myMap != null) {
//            if (driverMarker != null) {
//                driverMarker.setPosition(currentLocation);
//            } else {
//                driverMarker = myMap.addMarker(new MarkerOptions().position(currentLocation).title("Your Location"));
//            }
//
//            myMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
//            myMap.animateCamera(CameraUpdateFactory.zoomTo(15));
//
//            String userId = getInstance().getCurrentUser().getUid();
//            DatabaseReference refAvailable = FirebaseDatabase.getInstance().getReference("Drivers Available");
//            GeoFire geoFire = new GeoFire(refAvailable);
//
//            // Update driver's location using GeoFire
//            geoFire.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()), (key, error) -> {
//                if (error != null) {
//                    Log.e("GeoFire", "Error updating location: " + error.getMessage());
//                } else {
//                    Log.d("GeoFire", "Location updated successfully");
//                }
//            });
//            mSettings.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    Intent intent = new Intent(MainActivity9.this, DriverSettingsActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the back stack
//                    startActivity(intent);
//                    finish();
//                }
//            });
//        }
//    }
//
//    @Override
//    public void onMapReady(@NonNull GoogleMap googleMap) {
//        myMap = googleMap;
//        LatLng latLng = new LatLng(0, 0);
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.title("My position");
//        markerOptions.position(latLng);
//        googleMap.addMarker(markerOptions);
//        googleMap.getUiSettings().setCompassEnabled(true);
//        googleMap.getUiSettings().setZoomControlsEnabled(true);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == MY_PERMISSION_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission granted
//                // Start location updates
//                LocationRequest locationRequest = new LocationRequest();
//                locationRequest.setInterval(5000); // Update every 5 seconds
//                locationRequest.setFastestInterval(3000);
//                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//                startLocationUpdates(locationRequest);
//            } else {
//                // Permission denied
//                Log.d("Location", "Permission denied");
//            }
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        // Stop location updates when the activity is destroyed
//        fusedLocationClient.removeLocationUpdates(locationCallback);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//
//        userId = getInstance().getCurrentUser().getUid();
//        DatabaseReference refAvailable = FirebaseDatabase.getInstance().getReference("Drivers Available");
//
//        GeoFire geoFire = new GeoFire(refAvailable);
//        geoFire.removeLocation(userId);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int itemId = item.getItemId();
//
//        if (itemId == R.id.referal) {
//            Toast.makeText(this, "Referal to other", Toast.LENGTH_SHORT).show();
//
//            Intent intent = new Intent(this, Referalpassenger.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the back stack
//            startActivity(intent);
//            return true;
//
//        } else if (itemId == R.id.logout) {
//            mAuth = FirebaseAuth.getInstance();
//
//            Toast.makeText(this, "Logout Successfully", Toast.LENGTH_SHORT).show();
//
//            Intent intent = new Intent(this, MainActivity4.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the back stack
//            startActivity(intent);
//            finish();
//
//            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString("name", "");
//            editor.apply();
//
//            return true;
//
//        } else if (itemId == R.id.profile) {
//            Intent intent = new Intent(this, passenger_profile.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the back stack
//            startActivity(intent);
//            return true;
//
//        } else if (itemId == android.R.id.home) {
//            onBackPressed();
//            return true;
//        } else {
//            Toast.makeText(this, "This file saved", Toast.LENGTH_SHORT).show();
//            return super.onOptionsItemSelected(item);
//        }
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//        if (getApplicationContext() != null) {
//            mLastLocation = location;
//            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//
//            if (driverMarker != null) {
//                driverMarker.setPosition(latLng);
//            } else {
//                driverMarker = myMap.addMarker(new MarkerOptions().position(latLng).title("My position"));
//            }
//
//            myMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//            myMap.animateCamera(CameraUpdateFactory.zoomTo(15));
//
//            String userId = getInstance().getCurrentUser().getUid();
//            DatabaseReference refAvailable = FirebaseDatabase.getInstance().getReference("Drivers Available");
//            GeoFire geoFireAvailable = new GeoFire(refAvailable);
//            geoFireAvailable.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()));
//            DatabaseReference refWorking = FirebaseDatabase.getInstance().getReference("Drivers Working");
//            GeoFire geoFireWorking = new GeoFire(refWorking);
//
//            if (customerID.equals("")) {
//                geoFireWorking.removeLocation(userId);
//                geoFireAvailable.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()));
//            } else {
//                geoFireAvailable.removeLocation(userId);
//                geoFireWorking.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()));
//            }
//        }
//    }
//
//    private void getAssignedCustomer() {
//        AssainedCustomerRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(driverID).child("CustomerRequests").child("CustomerRideID");
//        AssainedCustomerRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    customerID = dataSnapshot.getValue().toString();
//                    getAssignedCustomerPickupLocation();
//                    getAssignedCustomerDestination();
//                    getAssignedCustomerInfo();
//
//                }else {
//                    customerID = "";
//                    if (passengerMarker!=null){
//                        passengerMarker.remove();
//                    }
//                    if (assignedCustomerPickupRef!=null){
//                        assignedCustomerPickupRef.removeEventListener((ChildEventListener) assignedCustomerPickupRef);
//                    }
//                    mCustomerInfo.setVisibility(View.GONE);
//                    mCustomername.setText("");
//                    mCustomercontact.setText("");
//                    mCustomerDestination.setText("Destination:-");
//                    mCustomerProfileimage.setImageResource(R.drawable.profilesvg);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Handle onCancelled event
//            }
//        });
//    }
//
//    private void getAssignedCustomerDestination() {
//        AssainedCustomerRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(driverID).child("CustomerRequests").child("destination");
//        AssainedCustomerRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    String destination = dataSnapshot.getValue().toString();
//                    mCustomerDestination.setText("Destination: " + destination);
//                }
//                else{
//                    mCustomerDestination.setText("Destination: -- ");
//                  }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Handle onCancelled event
//            }
//        });
//    }
//    private void getAssignedCustomerInfo() {
//        mCustomerInfo.setVisibility(View.VISIBLE);
//        DatabaseReference mCustomerDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(customerID);
//        mCustomerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
//                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
//                    if (map != null) {
//                        if (map.get("name") != null) {
//
//                            mCustomername.setText( map.get("name").toString());
//                        }
//                        if (map.get("contact") != null) {
//
//                            mCustomercontact.setText( map.get("contact").toString());
//                        }
//                        if (map.get("profileImageUrl") != null) {
//                            Glide.with(getApplication()).load(map.get("profileImageUrl").toString()).into(mCustomerProfileimage);
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(MainActivity9.this, "Failed to get user information", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//
//
//    private void getAssignedCustomerPickupLocation() {
//        assignedCustomerPickupRef = FirebaseDatabase.getInstance().getReference().child("CustomerRequests").child(customerID).child("l");
//        assignedCustomerPickupRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    List<Object> Nmap = (List<Object>) dataSnapshot.getValue();
//
//                    double locationLat = 0;
//                    double locationLng = 0;
//                    if (Nmap.get(0) != null) {
//                        locationLat = Double.parseDouble(Nmap.get(0).toString());
//                    }
//                    if (Nmap.get(1) != null) {
//                        locationLng = Double.parseDouble(Nmap.get(1).toString());
//                    }
//                    LatLng driverLatLng = new LatLng(locationLat, locationLng);
//
//                    if (passengerMarker != null) {
//                        passengerMarker.setPosition(driverLatLng);
//                    } else {
//                        passengerMarker = myMap.addMarker(new MarkerOptions().position(driverLatLng).title("Pickup location"));
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Handle onCancelled event
//            }
//        });
//
//    }
//}

package com.yogeshwar.indigotaxi;

import static com.google.firebase.auth.FirebaseAuth.getInstance;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Map;

public class MainActivity9 extends AppCompatActivity implements OnMapReadyCallback, LocationListener {
    private GoogleMap myMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private String userId;
    private Toolbar toolbar;
    private DatabaseReference assignedCustomerRef;
    private DatabaseReference assignedCustomerPickupRef;
    private String customerID = "";
    private Button mSettings;
    private DatabaseReference refAvailable, refWorking;
    private String driverID;
    private Location mLastLocation;
    private Marker driverMarker;
    private Marker passengerMarker;
    private LinearLayout mCustomerInfo;
    private ImageView mCustomerProfileImage;
    private TextView mCustomerName, mCustomerContact, mCustomerDestination;
    public static final int MY_PERMISSION_REQUEST_CODE = 7000;
    public static final String SHARED_PREFS = "sharedprefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main9);

        initializeUI();
        initializeFirebase();
        setupToolbar();
        initializeMap();
        getAssignedCustomer();
        setupLocationUpdates();
        setupSettingsButton();
    }

    private void initializeUI() {
        mSettings = findViewById(R.id.Settings);
        mCustomerInfo = findViewById(R.id.CutomerInfo);
        mCustomerProfileImage = findViewById(R.id.Customerprofileimage);
        mCustomerName = findViewById(R.id.customerName);
        mCustomerContact = findViewById(R.id.customerContact);
        mCustomerDestination = findViewById(R.id.customerDestination);
    }

    private void initializeFirebase() {
        mAuth = getInstance();
        currentUser = mAuth.getCurrentUser();
        driverID = currentUser.getUid();
        refAvailable = FirebaseDatabase.getInstance().getReference().child("Drivers Available");
        refWorking = FirebaseDatabase.getInstance().getReference().child("Drivers Working");
        assignedCustomerRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(driverID).child("CustomerRequests");
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initializeMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void setupLocationUpdates() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        LocationRequest locationRequest = new LocationRequest()
                .setInterval(5000)  // Update every 5 seconds
                .setFastestInterval(3000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult != null) {
                    for (Location location : locationResult.getLocations()) {
                        updateMap(location);
                    }
                }
            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates(locationRequest);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_CODE);
        }
    }

    private void startLocationUpdates(LocationRequest locationRequest) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        } else {
            Log.e("Location", "Permission not granted");
        }
    }

    private void updateMap(Location location) {
        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        if (myMap != null) {
            if (driverMarker != null) {
                driverMarker.setPosition(currentLocation);
            } else {
                driverMarker = myMap.addMarker(new MarkerOptions().position(currentLocation).title("Your Location"));
            }

            myMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
            myMap.animateCamera(CameraUpdateFactory.zoomTo(15));

            String userId = getInstance().getCurrentUser().getUid();
            DatabaseReference refAvailable = FirebaseDatabase.getInstance().getReference("Drivers Available");
            GeoFire geoFire = new GeoFire(refAvailable);
            geoFire.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()), (key, error) -> {
                if (error != null) {
                    Log.e("GeoFire", "Error updating location: " + error.getMessage());
                } else {
                    Log.d("GeoFire", "Location updated successfully");
                }
            });
        }
    }

    private void setupSettingsButton() {
        mSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity9.this, DriverSettingsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);  // Clear the back stack
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;
        LatLng latLng = new LatLng(0, 0);
        MarkerOptions markerOptions = new MarkerOptions().title("My position").position(latLng);
        googleMap.addMarker(markerOptions);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LocationRequest locationRequest = new LocationRequest()
                        .setInterval(5000)  // Update every 5 seconds
                        .setFastestInterval(3000)
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                startLocationUpdates(locationRequest);
            } else {
                Log.d("Location", "Permission denied");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            GeoFire geoFire = new GeoFire(refAvailable);
            geoFire.removeLocation(userId);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.referal) {
            navigateTo(Referalpassenger.class);
            return true;
        } else if (itemId == R.id.logout) {
            logout();
            return true;
        } else if (itemId == R.id.profile) {
            navigateTo(passenger_profile.class);
            return true;
        } else if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            Toast.makeText(this, "This file saved", Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        }
    }

    private void navigateTo(Class<?> targetClass) {
        Intent intent = new Intent(this, targetClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  // Clear the back stack
        startActivity(intent);
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", "");
        editor.apply();
        Toast.makeText(this, "Logout Successfully", Toast.LENGTH_SHORT).show();
        navigateTo(MainActivity4.class);
        finish();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        if (driverMarker != null) {
            driverMarker.setPosition(latLng);
        } else {
            driverMarker = myMap.addMarker(new MarkerOptions().position(latLng).title("My position"));
        }

        myMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        myMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        String userId = getInstance().getCurrentUser().getUid();
        GeoFire geoFireAvailable = new GeoFire(refAvailable);
        GeoFire geoFireWorking = new GeoFire(refWorking);

        if (customerID.isEmpty()) {
            geoFireWorking.removeLocation(userId);
            geoFireAvailable.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()));
        } else {
            geoFireAvailable.removeLocation(userId);
            geoFireWorking.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()));
        }
    }

    private void getAssignedCustomer() {
        assignedCustomerRef.child("CustomerRideID").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    customerID = dataSnapshot.getValue(String.class);
                    getAssignedCustomerPickupLocation();
                    getAssignedCustomerDestination();
                    getAssignedCustomerInfo();
                } else {
                    clearAssignedCustomerInfo();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseError", "Error fetching assigned customer: " + error.getMessage());
            }
        });
    }

    private void clearAssignedCustomerInfo() {
        customerID = "";
        if (passengerMarker != null) {
            passengerMarker.remove();
        }
        if (assignedCustomerPickupRef != null) {
            assignedCustomerPickupRef.removeEventListener((ChildEventListener) assignedCustomerPickupRef);
        }
        mCustomerInfo.setVisibility(View.GONE);
        mCustomerName.setText("");
        mCustomerContact.setText("");
        mCustomerDestination.setText("Destination: --");
        mCustomerProfileImage.setImageResource(R.drawable.profilesvg);
    }

    private void getAssignedCustomerDestination() {
        assignedCustomerRef.child("destination").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String destination = dataSnapshot.getValue(String.class);
                    mCustomerDestination.setText("Destination: " + destination);
                } else {
                    mCustomerDestination.setText("Destination: --");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseError", "Error fetching customer destination: " + error.getMessage());
            }
        });
    }

    private void getAssignedCustomerInfo() {
        mCustomerInfo.setVisibility(View.VISIBLE);
        DatabaseReference mCustomerDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(customerID);
        mCustomerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map != null) {
                        mCustomerName.setText((String) map.get("name"));
                        mCustomerContact.setText((String) map.get("contact"));
                        String profileImageUrl = (String) map.get("profileImageUrl");
                        if (profileImageUrl != null) {
                            Glide.with(getApplication()).load(profileImageUrl).into(mCustomerProfileImage);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity9.this, "Failed to get user information", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAssignedCustomerPickupLocation() {
        assignedCustomerPickupRef = FirebaseDatabase.getInstance().getReference().child("CustomerRequests").child(customerID).child("l");
        assignedCustomerPickupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<Object> location = (List<Object>) dataSnapshot.getValue();
                    double locationLat = 0;
                    double locationLng = 0;
                    if (location.get(0) != null) {
                        locationLat = Double.parseDouble(location.get(0).toString());
                    }
                    if (location.get(1) != null) {
                        locationLng = Double.parseDouble(location.get(1).toString());
                    }
                    LatLng pickupLatLng = new LatLng(locationLat, locationLng);
                    if (passengerMarker != null) {
                        passengerMarker.setPosition(pickupLatLng);
                    } else {
                        passengerMarker = myMap.addMarker(new MarkerOptions().position(pickupLatLng).title("Pickup location"));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseError", "Error fetching pickup location: " + error.getMessage());
            }
        });
    }
}
