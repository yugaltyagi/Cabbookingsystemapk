//package com.yogeshwar.indigotaxi;
//
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
//import androidx.appcompat.widget.SearchView;
//import androidx.appcompat.widget.Toolbar;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import com.bumptech.glide.Glide;
//import com.firebase.geofire.GeoFire;
//import com.firebase.geofire.GeoLocation;
//import com.firebase.geofire.GeoQuery;
//import com.firebase.geofire.GeoQueryEventListener;
//import com.google.android.gms.common.api.Status;
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationCallback;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationResult;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.android.libraries.places.api.Places;
//import com.google.android.libraries.places.api.model.Place;
//import com.google.android.libraries.places.api.model.RectangularBounds;
//import com.google.android.libraries.places.api.model.TypeFilter;
//import com.google.android.libraries.places.api.net.PlacesClient;
//import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
//import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class passengerMap extends AppCompatActivity implements OnMapReadyCallback {
//
//    private GoogleMap myMap;
//    private Toolbar toolbar;
//    private Marker mDriverMarker;
//    private FusedLocationProviderClient fusedLocationClient;
//    private LocationCallback locationCallback;
//    private Button mRequest;
//    private Location mLastLocation;
//    private DatabaseReference DriverLocationRef, DriverAvailableRef;
//    private DatabaseReference driverRef;
//    private DatabaseReference CustomerDatabaseRef;
//    private String destination;
//    private LatLng pickupLocation;
//    private ValueEventListener DriverLocationRefListener;
//    private int radius = 1;
//    private Boolean driverFound = false;
//    private String driverFoundID;
//    private SearchView searchView;
//    private Boolean requestType = false;
//    private String customerID;
//    private Marker PickUpMarker;
//    private LinearLayout mdriverInfo;
//    private ImageView mdriverProfileimage;
//    private TextView mdrivername, mdrivercontact, mdrivercar;
//    public static final String SHARED_PREFS = "sharedprefs";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_passenger_map);
//        customerID = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        DriverAvailableRef = FirebaseDatabase.getInstance().getReference().child("Drivers Available");
//        DriverLocationRef = FirebaseDatabase.getInstance().getReference().child("Drivers Working");
//        CustomerDatabaseRef = FirebaseDatabase.getInstance().getReference().child("CustomerRequests");
//        mRequest = findViewById(R.id.request);
//
//        mdriverInfo = (LinearLayout) findViewById(R.id.driverInfo);
//
//        mdriverProfileimage = (ImageView) findViewById(R.id.driverprofileimage);
//
//        mdrivername = (TextView) findViewById(R.id.driverName);
//
//        mdrivercontact = (TextView) findViewById(R.id.driverContact);
//
//        mdrivercar = (TextView) findViewById(R.id.driverCar);
//
//        if (!Places.isInitialized()) {
//            Places.initialize(getApplicationContext(), getString(R.string.AIzaSyDGtilP2KlOOMX532k4DVCJTgxJq5ie_uI));
//            PlacesClient placesClient = Places.createClient(this);
//        }
//
//
////        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
////                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
////        autocompleteFragment.setTypesFilter(TypeFilter.ESTABLISHMENT);
////        autocompleteFragment.setLocationBias(RectangularBounds.newInstance(
////                new LatLng(-33.880490, 151.184363);
////        new LatLng(-33.858754, 151.229596)));
////        autocompleteFragment.setCountries("IN");
////        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
////
////        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
////
////            @Override
////            public void onPlaceSelected(@NonNull Place place) {
////                destination = place.getName().toString();
////                // TODO: Get info about the selected place.
////            }
////
////
////            @Override
////            public void onError(@NonNull Status status) {
////
////            }
////        });
//
//        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
//                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
//
//        if (autocompleteFragment != null) {
//            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
//            autocompleteFragment.setTypeFilter(TypeFilter.ESTABLISHMENT);
//            autocompleteFragment.setLocationBias(RectangularBounds.newInstance(
//                    new LatLng(-33.880490, 151.184363),
//                    new LatLng(-33.858754, 151.229596)));
//
//            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//                @Override
//                public void onPlaceSelected(@NonNull Place place) {
//                    destination = place.getName();
//                    Log.i("PlacesAPI", "Place selected: " + place.getName());
//                }
//
//                @Override
//                public void onError(@NonNull Status status) {
//                    Log.e("PlacesAPI", "An error occurred: " + status);
//                }
//            });
//        }
//
//
//
//        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }
//
//        toolbar.setTitle("Indigo Taxi");
//
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.Passengermap);
//        if (mapFragment != null) {
//            mapFragment.getMapAsync(this);
//        }
//
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//
//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            LocationRequest locationRequest = new LocationRequest();
//            locationRequest.setInterval(5000);
//            locationRequest.setFastestInterval(3000);
//            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//
//            locationCallback = new LocationCallback() {
//                @Override
//                public void onLocationResult(@NonNull LocationResult locationResult) {
//                    if (locationResult != null) {
//                        for (Location location : locationResult.getLocations()) {
//                            updateMap(location);
//                        }
//                    }
//                }
//            };
//
//            startLocationUpdates(locationRequest);
//        } else {
//            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//        }
//
//
//        setButtonClickListener();
//    }
//
//    private void startLocationUpdates(LocationRequest locationRequest) {
//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
//        } else {
//            Log.e("Location", "Permission not granted");
//        }
//    }
//    private void updateMap(Location location) {
//        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
//
//        location_helper helper = new location_helper(
//                location.getLongitude(),
//                location.getLatitude()
//        );
//        FirebaseDatabase.getInstance().getReference("current_location")
//                .setValue(helper)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(passengerMap.this, "Location saved", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(passengerMap.this, "Failed to save location", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//        if (myMap != null) {
//            myMap.clear();
//            myMap.addMarker(new MarkerOptions().position(currentLocation).title("Live Location").icon(BitmapDescriptorFactory.fromResource(R.mipmap.location_pin_foreground)));
//            myMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
//            myMap.getUiSettings().setZoomControlsEnabled(true);
//            myMap.setPadding(0, 300, 0, 0);
//            myMap.getUiSettings().setMyLocationButtonEnabled(true);
//            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//            myMap.setMyLocationEnabled(true);
//            myMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//                @Override
//                public void onMapClick(LatLng latLng) {
//                    // Handle map click event
//                }
//            });
//            mLastLocation = location;
//        }
//    }
//    private void setButtonClickListener() {
//        mRequest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (requestType) {
//                    requestType = false;
//
//                    if (DriverLocationRefListener != null) {
//                        DriverLocationRef.removeEventListener(DriverLocationRefListener);
//                    }
//
//                    if (driverFoundID != null) {
//                        driverRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(driverFoundID).child("CustomerRequests");
//                        driverRef.removeValue();
//                        driverFoundID = null;
//                    }
//                    driverFound = false;
//                    radius = 1;
//
//                    String customerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                    GeoFire geoFire = new GeoFire(CustomerDatabaseRef);
//                    geoFire.removeLocation(customerId);
//
//                    if (PickUpMarker != null) {
//                        PickUpMarker.remove();
//                    }
//                    mRequest.setText("Call a cab");
//                    mdriverInfo.setVisibility(View.GONE);
//                    mdrivername.setText("");
//                    mdrivercontact.setText("");
//                    mdrivercar.setText("");
//                    mdriverProfileimage.setImageResource(R.drawable.profilesvg);
//                } else {
//                    requestType = true;
//                    String customerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                    GeoFire geoFire = new GeoFire(CustomerDatabaseRef);
//                    geoFire.setLocation(customerId, new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
//                    pickupLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
//
//                    PickUpMarker = myMap.addMarker(new MarkerOptions().position(pickupLocation).title("Pickup Here"));
//
//                    mRequest.setText("Getting your driver...");
//
//                    getClosestDriver();
//                }
//            }
//        });
//    }
//
//
//
//    private void getClosestDriver() {
//        DatabaseReference DriverAvailableRef = FirebaseDatabase.getInstance().getReference().child("Drivers Available");
//        GeoFire geoFire = new GeoFire(DriverAvailableRef);
//        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(pickupLocation.latitude, pickupLocation.longitude), radius);
//        geoQuery.removeAllListeners();
//        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
//            @Override
//            public void onKeyEntered(String key, GeoLocation location) {
//                if (!driverFound && requestType) {
//                    driverFound = true;
//                    driverFoundID = key;
//
//                    DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(driverFoundID).child("CustomerRequests");
//                    FirebaseAuth.getInstance().getCurrentUser().getUid();
//                    HashMap drivermap = new HashMap();
////                    HashMap<String, Object> drivermap = new HashMap<>();
//                    drivermap.put("CustomerRideID", customerID);
//                    drivermap.put("destination", destination);
//                    driverRef.updateChildren(drivermap);
//
//                    getDriverLocation();
//                    getDriverInfo();
//                    mRequest.setText("Looking for Driver's Location");
//
//                }
//            }
//
//            @Override
//            public void onKeyExited(String key) {
//
//            }
//
//            @Override
//            public void onKeyMoved(String key, GeoLocation location) {
//
//            }
//
//            @Override
//            public void onGeoQueryReady() {
//                if (!driverFound) {
//                    radius++;
//                    getClosestDriver();
//                }
//            }
//
//            @Override
//            public void onGeoQueryError(DatabaseError error) {
//
//            }
//        });
//    }
//
//    private void getDriverInfo() {
//        mdriverInfo .setVisibility(View.VISIBLE);
//        DatabaseReference mCustomerDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(driverFoundID);
//        mCustomerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
//                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
//                    if (map != null) {
//                        if (map.get("name") != null) {
//
//                            mdrivername .setText( map.get("name").toString());
//                        }
//                        if (map.get("contact") != null) {
//
//                            mdrivercontact .setText( map.get("contact").toString());
//                        }  if (map.get("car") != null) {
//
//                            mdrivercar .setText( map.get("car").toString());
//                        }
//                        if (map.get("profileImageUrl") != null) {
//                            Glide.with(getApplication()).load(map.get("profileImageUrl").toString()).into(mdriverProfileimage);
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(passengerMap.this, "Failed to get user information", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void getDriverLocation() {
////        DriverLocationRef.child(driverFoundID).child("l");
//        DatabaseReference driverLocationRef = FirebaseDatabase.getInstance().getReference().child("Drivers Available").child(driverFoundID).child("l");
//        DriverLocationRefListener = driverLocationRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists() && requestType) {
//                    List<Object> Mmap = (List<Object>) dataSnapshot.getValue();
//                    double locationlat = 0;
//                    double locationlng = 0;
//                    mRequest.setText("Driver Found");
//                    if (Mmap.get(0) != null) {
//                        locationlat = Double.parseDouble(Mmap.get(0).toString());
//                        if (Mmap.get(1) != null) {
//                            locationlng = Double.parseDouble(Mmap.get(1).toString());
//                        }
//                        LatLng driverLatLng = new LatLng(locationlat, locationlng);
//                        if (mDriverMarker != null) {
//                            mDriverMarker.remove();
//                        }
//                        mDriverMarker = myMap.addMarker(new MarkerOptions().position(driverLatLng).title("Your driver").icon(BitmapDescriptorFactory.fromResource(R.mipmap.navigationn_foreground)));
//                        Location loc1 = new Location("");
//                        loc1.setLatitude(pickupLocation.latitude);
//                        loc1.setLongitude(pickupLocation.longitude);
//
//                        Location loc2 = new Location("");
//                        loc2.setLatitude(driverLatLng.latitude);
//                        loc2.setLongitude(driverLatLng.longitude);
//
//                        float distance = loc1.distanceTo(loc2);
//
//                        mRequest.setText("Driver found: " + distance);
//
//                        mDriverMarker = myMap.addMarker(new MarkerOptions().position(driverLatLng).title("Your Driver"));
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//
//    @Override
//    public void onMapReady(@NonNull GoogleMap googleMap) {
//        myMap = googleMap;
//
//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            myMap.setMyLocationEnabled(true);
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 1) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                LocationRequest locationRequest = new LocationRequest();
//                locationRequest.setInterval(5000);
//                locationRequest.setFastestInterval(3000);
//                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//                startLocationUpdates(locationRequest);
//            } else {
//                Log.d("Location", "Permission denied");
//            }
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (locationCallback != null) {
//            fusedLocationClient.removeLocationUpdates(locationCallback);
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
//            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//            DatabaseReference DriverAvailableRef = FirebaseDatabase.getInstance().getReference("drivers Available");
//            GeoFire geoFire = new GeoFire(CustomerDatabaseRef);
//            geoFire.removeLocation(userId);
//        }
//    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }
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
//
//            Intent intent = new Intent(this, passenger_profile.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the back stack
//            startActivity(intent);
//            return true;
//
//
//
//        } else if (itemId == android.R.id.home) {
//            onBackPressed();
//            return true;
//        } else {
//            Toast.makeText(this, "This file saved", Toast.LENGTH_SHORT).show();
//            return super.onOptionsItemSelected(item);
//
//        }
//    }
//
//}
//

package com.yogeshwar.indigotaxi;

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
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class passengerMap extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap myMap;
    private Toolbar toolbar;
    private Marker mDriverMarker;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Button mRequest;
    private Location mLastLocation;
    private DatabaseReference DriverLocationRef, DriverAvailableRef;
    private DatabaseReference CustomerDatabaseRef;
    private String destination;
    private LatLng pickupLocation;
    private DatabaseReference driverRef;
    private ValueEventListener DriverLocationRefListener;
    private int radius = 1;
    private boolean driverFound = false;
    private String driverFoundID;
    private boolean requestType = false;
    private String customerID;
    private Marker PickUpMarker;
    private LinearLayout mdriverInfo;
    private ImageView mdriverProfileimage;
    private TextView mdrivername, mdrivercontact, mdrivercar;
    public static final String SHARED_PREFS = "sharedprefs";
    public static final String PREF_IS_LOGGED_IN = "is_logged_in";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_map);

        initializeUI();
        initializeMap();
        initializePlacesAPI();
        setButtonClickListener();
    }

    private void initializeUI() {
        customerID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DriverAvailableRef = FirebaseDatabase.getInstance().getReference().child("Drivers Available");
        DriverLocationRef = FirebaseDatabase.getInstance().getReference().child("Drivers Working");
        CustomerDatabaseRef = FirebaseDatabase.getInstance().getReference().child("CustomerRequests");

        mRequest = findViewById(R.id.request);
        mdriverInfo = findViewById(R.id.driverInfo);
        mdriverProfileimage = findViewById(R.id.driverprofileimage);
        mdrivername = findViewById(R.id.driverName);
        mdrivercontact = findViewById(R.id.driverContact);
        mdrivercar = findViewById(R.id.driverCar);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setTitle("Indigo Taxi");
    }

    private void initializeMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.Passengermap);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationRequest locationRequest = LocationRequest.create()
                    .setInterval(5000)
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

            startLocationUpdates(locationRequest);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    private void initializePlacesAPI() {
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.AIzaSyDGtilP2KlOOMX532k4DVCJTgxJq5ie_uI));
        }

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        if (autocompleteFragment != null) {
            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
            autocompleteFragment.setTypeFilter(TypeFilter.ESTABLISHMENT);
            autocompleteFragment.setLocationBias(RectangularBounds.newInstance(
                    new LatLng(-33.880490, 151.184363),
                    new LatLng(-33.858754, 151.229596)));

            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(@NonNull Place place) {
                    destination = place.getName();
                    Log.i("PlacesAPI", "Place selected: " + place.getName());
                }

                @Override
                public void onError(@NonNull Status status) {
                    Log.e("PlacesAPI", "An error occurred: " + status);
                }
            });
        }
    }

    private void startLocationUpdates(LocationRequest locationRequest) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        } else {
            Log.e("Location", "Permission not granted");
        }
    }

    private void updateMap(Location location) {
        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        saveLocationToDatabase(location);
        if (myMap != null) {
            myMap.clear();
            myMap.addMarker(new MarkerOptions().position(currentLocation).title("Live Location").icon(BitmapDescriptorFactory.fromResource(R.mipmap.location_pin_foreground)));
            myMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
            myMap.getUiSettings().setZoomControlsEnabled(true);
            myMap.setPadding(0, 300, 0, 0);
            myMap.getUiSettings().setMyLocationButtonEnabled(true);
            enableMyLocation();
            mLastLocation = location;
        }
    }

    private void saveLocationToDatabase(Location location) {
        location_helper helper = new location_helper(
                location.getLongitude(),
                location.getLatitude()
        );
        FirebaseDatabase.getInstance().getReference("current_location")
                .setValue(helper)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(passengerMap.this, "Location saved", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(passengerMap.this, "Failed to save location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        myMap.setMyLocationEnabled(true);
    }

    private void setButtonClickListener() {
        mRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRequestButton();
            }
        });
    }

    private void handleRequestButton() {
        if (requestType) {
            cancelRequest();
        } else {
            createRequest();
        }
    }

    private void cancelRequest() {
        requestType = false;
        removeDriverLocationListener();
        removeCustomerRequest();

        if (PickUpMarker != null) {
            PickUpMarker.remove();
        }

        mRequest.setText("Call a cab");
        resetDriverInfo();
    }

    private void removeDriverLocationListener() {
        if (DriverLocationRefListener != null) {
            DriverLocationRef.removeEventListener(DriverLocationRefListener);
        }

        if (driverFoundID != null) {
            driverRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(driverFoundID).child("CustomerRequests");
            driverRef.removeValue();
            driverFoundID = null;
        }
        driverFound = false;
        radius = 1;
    }

    private void removeCustomerRequest() {
        String customerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        GeoFire geoFire = new GeoFire(CustomerDatabaseRef);
        geoFire.removeLocation(customerId);
    }

    private void resetDriverInfo() {
        mdriverInfo.setVisibility(View.GONE);
        mdrivername.setText("");
        mdrivercontact.setText("");
        mdrivercar.setText("");
        mdriverProfileimage.setImageResource(R.drawable.profilesvg);
    }

    private void createRequest() {
        requestType = true;
        String customerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        GeoFire geoFire = new GeoFire(CustomerDatabaseRef);
        geoFire.setLocation(customerId, new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
        pickupLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

        PickUpMarker = myMap.addMarker(new MarkerOptions().position(pickupLocation).title("Pickup Here"));

        mRequest.setText("Getting your driver...");

        getClosestDriver();
    }

    private void getClosestDriver() {
        GeoFire geoFire = new GeoFire(DriverAvailableRef);
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(pickupLocation.latitude, pickupLocation.longitude), radius);
        geoQuery.removeAllListeners();
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if (!driverFound && requestType) {
                    driverFound = true;
                    driverFoundID = key;

                    DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(driverFoundID).child("CustomerRequests");
                    HashMap<String, Object> drivermap = new HashMap<>();
                    drivermap.put("CustomerRideID", customerID);
                    drivermap.put("destination", destination);
                    driverRef.updateChildren(drivermap);

                    getDriverLocation();
                    getDriverInfo();
                    mRequest.setText("Looking for Driver's Location");
                }
            }

            @Override
            public void onKeyExited(String key) {
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
            }

            @Override
            public void onGeoQueryReady() {
                if (!driverFound) {
                    radius++;
                    getClosestDriver();
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                Log.e("GeoQuery", "Error: " + error.getMessage());
            }
        });
    }

    private void getDriverInfo() {
        mdriverInfo.setVisibility(View.VISIBLE);
        DatabaseReference driverDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(driverFoundID);
        driverDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map != null) {
                        updateDriverInfo(map);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(passengerMap.this, "Failed to get driver information", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDriverInfo(Map<String, Object> map) {
        if (map.get("name") != null) {
            mdrivername.setText(map.get("name").toString());
        }
        if (map.get("contact") != null) {
            mdrivercontact.setText(map.get("contact").toString());
        }
        if (map.get("car") != null) {
            mdrivercar.setText(map.get("car").toString());
        }
        if (map.get("profileImageUrl") != null) {
            Glide.with(getApplication()).load(map.get("profileImageUrl").toString()).into(mdriverProfileimage);
        }
    }

    private void getDriverLocation() {
        DatabaseReference driverLocationRef = FirebaseDatabase.getInstance().getReference().child("Drivers Available").child(driverFoundID).child("l");
        DriverLocationRefListener = driverLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && requestType) {
                    List<Object> Mmap = (List<Object>) dataSnapshot.getValue();
                    if (Mmap != null) {
                        updateDriverMarker(Mmap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DriverLocation", "Error: " + error.getMessage());
            }
        });
    }

    private void updateDriverMarker(List<Object> Mmap) {
        double locationlat = 0;
        double locationlng = 0;
        mRequest.setText("Driver Found");

        if (Mmap.get(0) != null) {
            locationlat = Double.parseDouble(Mmap.get(0).toString());
        }
        if (Mmap.get(1) != null) {
            locationlng = Double.parseDouble(Mmap.get(1).toString());
        }
        LatLng driverLatLng = new LatLng(locationlat, locationlng);

        if (mDriverMarker != null) {
            mDriverMarker.remove();
        }
        mDriverMarker = myMap.addMarker(new MarkerOptions().position(driverLatLng).title("Your driver").icon(BitmapDescriptorFactory.fromResource(R.mipmap.navigationn_foreground)));

        float distance = calculateDistance(pickupLocation, driverLatLng);
        mRequest.setText("Driver found: " + distance);
    }

    private float calculateDistance(LatLng start, LatLng end) {
        Location loc1 = new Location("");
        loc1.setLatitude(start.latitude);
        loc1.setLongitude(start.longitude);

        Location loc2 = new Location("");
        loc2.setLatitude(end.latitude);
        loc2.setLongitude(end.longitude);

        return loc1.distanceTo(loc2);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            myMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LocationRequest locationRequest = LocationRequest.create()
                        .setInterval(5000)
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
        if (locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference driverAvailableRef = FirebaseDatabase.getInstance().getReference("drivers Available");
            GeoFire geoFire = new GeoFire(CustomerDatabaseRef);
            geoFire.removeLocation(userId);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.referal) {
            navigateToReferral();
            return true;
        } else if (itemId == R.id.logout) {
            logout();
            return true;
        } else if (itemId == R.id.profile) {
            navigateToProfile();
            return true;
        } else if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            Toast.makeText(this, "This file saved", Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        }
    }

    private void navigateToReferral() {
        Toast.makeText(this, "Referal to other", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Referalpassenger.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void logout() {
        Toast.makeText(this, "Logout Successfully", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, MainActivity4.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREF_IS_LOGGED_IN, false);
        editor.apply();
    }

    private void navigateToProfile() {
        Intent intent = new Intent(this, passenger_profile.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
