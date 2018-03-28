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

/**
 * This class echoes a string called from JavaScript.
 */
public class NativeGeo extends CordovaPlugin {

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;


    // GPSTracker class
   GPSTracker gps;

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

        gps = new GPSTracker(context);

            // check if GPS enabled
            gps.getLocation();
            if(gps.canGetLocation()){

               String latitude = String.valueOf(gps.getLatitude());
               String longitude = String.valueOf(gps.getLongitude());

               JSONObject obj = new JSONObject();
                try {
                    obj.put("latitude", latitude);
                    obj.put("longitude", longitude);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

               callbackContext.success(obj);
               if(gps.getLatitude() != 0.0 && gps.getLongitude() != 0.0){
                    gps.stopUsingGPS();
               }
               // \n is for new line
            //    Toast.makeText(context, "Your Location is - \nLat: "
            //       + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            }else{
               // can't get location
               // GPS or Network is not enabled
               // Ask user to enable GPS/network in settings
            //    this.showSettingsAlert();
               callbackContext.error("Can't get location'");
            }
    }
}
