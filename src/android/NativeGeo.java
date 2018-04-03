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
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * This class echoes a string called from JavaScript.
 */
public class NativeGeo extends CordovaPlugin {

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;


    // GPSTracker class
    // GPSTracker gps;

    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Represents a geographical location.
     */
    protected Location mLastLocation;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("getCurrentLocation")) {
            // String message = args.getString(0);
            this.getCurrentLocation(callbackContext);
            return true;
        }
        return false;
    }

    private void getCurrentLocation(CallbackContext callbackContext) {
        
        Context context = cordova.getActivity().getApplicationContext();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(context, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();
                            // mLastLocation.getLatitude() mLastLocation.getLongitude()
                            String latitude = String.valueOf(mLastLocation.getLatitude());
                            String longitude = String.valueOf(mLastLocation.getLongitude());

                            JSONObject obj = new JSONObject();
                                try {
                                    obj.put("latitude", latitude);
                                    obj.put("longitude", longitude);
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                    callbackContext.error("Can't get location'");
                                }

                            callbackContext.success(obj);
                            
                        } else {
                            Log.w(TAG, "getLastLocation:exception", task.getException());
                            callbackContext.error("Can't get location'");
                            // showSnackbar(getString(R.string.no_location_detected));
                        }
                    }
                });

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
}
