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

        String provider = gps.getBestProviderForCall();
        Location lastLocation = gps.getBestLocation(provider);
        if (lastLocation == null) {

            // new no location
            callbackContext.error("Can't get location'");
            
        } else {

            // new location success
            // check if GPS enabled
            // gps.getLocation();
            // if(gps.canGetLocation()){

               String latitude = String.valueOf(lastLocation.getLatitude());
               String longitude = String.valueOf(lastLocation.getLongitude());

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
            //    if(lastLocation.getLatitude() != 0.0 && lastLocation.getLongitude() != 0.0){
            //         gps.stopUsingGPS();
            //    }
            // }else{
            //    callbackContext.error("Can't get location'");
            // }
        }

            
    }
}
