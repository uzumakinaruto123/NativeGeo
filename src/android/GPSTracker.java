package com.cordova.nativegeolocation;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.location.Criteria;
import java.util.List;

public class GPSTracker extends Service implements LocationListener {

   private final Context mContext;

   // flag for GPS status
   boolean isGPSEnabled = false;

   // flag for network status
   boolean isNetworkEnabled = false;

   // flag for GPS status
   boolean canGetLocation = false;

   Location location; // location
   double latitude; // latitude
   double longitude; // longitude

   // The minimum distance to change Updates in meters
   private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

   // The minimum time between updates in milliseconds
   private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

   // Declaring a Location Manager
   protected LocationManager locationManager;

   public GPSTracker(Context context) {
      this.mContext = context;
      locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
      getLocation();
   }

   public Location getLocation() {
      try {
        //  locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

         // getting GPS status
         isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

         // getting network status
         isNetworkEnabled = locationManager
            .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

         if (!isGPSEnabled && !isNetworkEnabled) {
            // no network provider is enabled
         } else {
            this.canGetLocation = true;
            // First get location from Network Provider
            if (isNetworkEnabled) {
               locationManager.requestLocationUpdates(
                  LocationManager.NETWORK_PROVIDER,
                  MIN_TIME_BW_UPDATES,
                  MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						
               Log.d("Network", "Network");
               if (locationManager != null) {
                  location = locationManager
                     .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
							
                  if (location != null) {
                     latitude = location.getLatitude();
                     longitude = location.getLongitude();
                  }
               }
            }
				
            // if GPS Enabled get lat/long using GPS Services
            if (isGPSEnabled) {
               if (location == null) {
                  locationManager.requestLocationUpdates(
                     LocationManager.GPS_PROVIDER,
                     MIN_TIME_BW_UPDATES,
                     MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
							
                  Log.d("GPS Enabled", "GPS Enabled");
                  if (locationManager != null) {
                     location = locationManager
                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
								
                     if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                     }
                  }
               }
            }
         }

      } catch (Exception e) {
         e.printStackTrace();
      }

      return location;
   }

    public String getBestProviderForCall() {
        Criteria locationCriteria = new Criteria();
        locationCriteria.setAccuracy(Criteria.ACCURACY_FINE);
        locationCriteria.setSpeedRequired(false);
        locationCriteria.setAltitudeRequired(false);
        locationCriteria.setBearingRequired(false);
        locationCriteria.setCostAllowed(false);
        return locationManager.getBestProvider(locationCriteria, true);
    }

    public Location getBestLocation(String bestProvider) {
        List<String> providers = locationManager.getProviders(true);

        Location l = locationManager.getLastKnownLocation(bestProvider);
        Location bestLocation = l;

        if (bestLocation != null) {
        return bestLocation;
        }

        for (String provider : providers) {
        l = locationManager.getLastKnownLocation(provider);
        if (l == null) {
            continue;
        }
        if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
            bestLocation = l;
        }
        }
        return bestLocation;
    }

   /**
      * Stop using GPS listener
      * Calling this function will stop using GPS in your app
   * */
	
   public void stopUsingGPS(){
      if(locationManager != null){
         locationManager.removeUpdates(GPSTracker.this);
      }
   }

   /**
      * Function to get latitude
   * */
	
   public double getLatitude(){
      if(location != null){
         latitude = location.getLatitude();
      }

      // return latitude
      return latitude;
   }

   /**
      * Function to get longitude
   * */
	
   public double getLongitude(){
      if(location != null){
         longitude = location.getLongitude();
      }

      // return longitude
      return longitude;
   }

   /**
      * Function to check GPS/wifi enabled
      * @return boolean
   * */
	
   public boolean canGetLocation() {
      return this.canGetLocation;
   }

   /**
      * Function to show settings alert dialog
      * On pressing Settings button will lauch Settings Options
   * */
   

   @Override
   public void onLocationChanged(Location location) {
	   if(location != null){
		 latitude = location.getLatitude();
         longitude = location.getLongitude();
      }
   }

   @Override
   public void onProviderDisabled(String provider) {
   }

   @Override
   public void onProviderEnabled(String provider) {
       getLocation();
   }

   @Override
   public void onStatusChanged(String provider, int status, Bundle extras) {
   }

   @Override
   public IBinder onBind(Intent arg0) {
      return null;
   }
}