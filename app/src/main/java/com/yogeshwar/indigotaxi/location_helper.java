//package com.yogeshwar.indigotaxi;
//
//public class location_helper {
//
//    private double Longitude;
//    private double Latitude;
//
//
//    public location_helper(double longitude, double latitude) {
//        Longitude = longitude;
//        Latitude = latitude;
//    }
//
//
//    public double getLatitude() {
//        return Latitude;
//    }
//
//    public void setLatitude(double latitude) {
//        Latitude = latitude;
//    }
//
//    public double getLongitude() {
//        return Longitude;
//    }
//
//    public void setLongitude(double longitude) {
//        Longitude = longitude;
//    }
//}

package com.yogeshwar.indigotaxi;

public class location_helper {
    private double longitude;
    private double latitude;

    public location_helper() {
    }

    public location_helper(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
