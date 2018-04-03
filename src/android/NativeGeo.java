package com.cordova.nativegeolocation;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.app.AlertDialog;
import android.content.DialogInterface;

import android.content.Intent;
import android.provider.Settings;

import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import android.support.annotation.NonNull;
import android.widget.Toast;
import android.os.Bundle;

/**
 * This class echoes a string called from JavaScript.
 */
public class NativeGeo extends CordovaPlugin implements ConnectionCallbacks,
        OnConnectionFailedListener {

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    CallbackContext mContext;

    // GPSTracker class
    // GPSTracker gps;

    private FusedLocationProviderClient mFusedLocationClient;

	// Google client to interact with Google API
	private GoogleApiClient mGoogleApiClient;

    /**
     * Represents a geographical location.
     */
    protected Location mLastLocation;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        this.mContext = callbackContext;
        if (action.equals("getCurrentLocation")) {
            // String message = args.getString(0);
            this.getCurrentLocation(callbackContext);
            return true;
        }
        return false;
    }


    private void getCurrentLocation(CallbackContext callbackContext) {
        
        Context context = cordova.getActivity().getApplicationContext();

        // First we need to check availability of play services
        if (checkPlayServices()) {
            // Building the GoogleApi client
            buildGoogleApiClient();
        } else{
            callbackContext.error("Can't get location - Failed at checking play services");
            // LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, cord);
        }
        

        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);
 
        if (mLastLocation != null) {
            String latitude = String.valueOf(mLastLocation.getLatitude());
            String longitude = String.valueOf(mLastLocation.getLongitude());

            JSONObject obj = new JSONObject();
            try {
                obj.put("latitude", latitude);
                obj.put("longitude", longitude);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                callbackContext.error("Can't get location - Failed at encoding json");
                mGoogleApiClient.disconnect();
            }

            callbackContext.success(obj);
            mGoogleApiClient.disconnect();
 
        } else {
            callbackContext.error("Can't get location - Failed since mLastLocation was null'");
            mGoogleApiClient.disconnect();
        }

        // mFusedLocationClient.getLastLocation()
        //         .addOnCompleteListener(cordova.getActivity(), new OnCompleteListener<Location>() {
        //             @Override
        //             public void onComplete(@NonNull Task<Location> task) {
        //                 if (task.isSuccessful() && task.getResult() != null) {
        //                     mLastLocation = task.getResult();
        //                     // mLastLocation.getLatitude() mLastLocation.getLongitude()
        //                     String latitude = String.valueOf(mLastLocation.getLatitude());
        //                     String longitude = String.valueOf(mLastLocation.getLongitude());

        //                     JSONObject obj = new JSONObject();
        //                         try {
        //                             obj.put("latitude", latitude);
        //                             obj.put("longitude", longitude);
        //                         } catch (JSONException e) {
        //                             // TODO Auto-generated catch block
        //                             e.printStackTrace();
        //                             callbackContext.error("Can't get location'");
        //                         }

        //                     callbackContext.success(obj);
                            
        //                 } else {
        //                     // Log.w(TAG, "getLastLocation:exception", task.getException());
        //                     callbackContext.error("Can't get location'");
        //                     // showSnackbar(getString(R.string.no_location_detected));
        //                 }
        //             }
        //         });

        // gps = new GPSTracker(context);

        // String provider = gps.getBestProviderForCall();
        // Location lastLocation = gps.getBestLocation(provider);
        // if (lastLocation == null) {
        //     callbackContext.error("Can't get location'");
        // } else {
        //        String latitude = String.valueOf(lastLocation.getLatitude());
        //        String longitude = String.valueOf(lastLocation.getLongitude());

        //        JSONObject obj = new JSONObject();
        //         try {
        //             obj.put("latitude", latitude);
        //             obj.put("longitude", longitude);
        //         } catch (JSONException e) {
        //             // TODO Auto-generated catch block
        //             e.printStackTrace();
        //             callbackContext.error("Can't get location'");
        //         }
        //        callbackContext.success(obj);
        // }

            
    }

    /**
     * Creating google api client object
     * */
    protected void buildGoogleApiClient() {
        Context context = cordova.getActivity().getApplicationContext();
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        // mGoogleApiClient.connect();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
            // Toast.makeText(context,
            //             "Google API Client connected.", Toast.LENGTH_LONG)
            //             .show();
        } else {
            // Toast.makeText(context,
            //             "Google API Client was null.", Toast.LENGTH_LONG)
            //             .show();
        }
    }

     /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices() {
        Context context = cordova.getActivity().getApplicationContext();
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, cordova.getActivity(),
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(context,
                        "Google Play Services not installed.", Toast.LENGTH_LONG)
                        .show();
            }
            return false;
        }
        return true;
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
        //         + result.getErrorCode());
    }
 
    @Override
    public void onConnected(Bundle arg0) {
 
        // Once connected with google api, get the location
        // displayLocation();
        getCurrentLocation(mContext);
    }
 
    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }
}
