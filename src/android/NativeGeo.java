package com.cordova.nativegeolocation;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.widget.Toast;

/**
 * This class echoes a string called from JavaScript.
 */
public class NativeGeo extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("coolMethod")) {
            String message = args.getString(0);
            this.coolMethod(message, callbackContext);
            return true;
        }
        if (action.equals("getCurrentLocation")) {
            // String message = args.getString(0);
            this.getCurrentLocation("message", callbackContext);
            return true;
        }
        return false;
    }

    private void coolMethod(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

    private void getCurrentLocation(String message, CallbackContext callbackContext) {
        // if (message != null && message.length() > 0) {
        //     callbackContext.success(message);
        // } else {
        //     callbackContext.error("Expected one non-empty string argument.");
        // }
        Context context = cordova.getActivity().getApplicationContext();

        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);


        if (!enabled) {
                // Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                //         startActivity(intent);
                Toast.makeText(context,"Location services disabled",Toast.LENGTH_LONG).show();
                callbackContext.error("Location services disabled");
                return;
        }

        // mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

    }
}
