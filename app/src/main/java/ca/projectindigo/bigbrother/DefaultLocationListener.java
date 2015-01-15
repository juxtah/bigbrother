package ca.projectindigo.bigbrother;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * The default listener for GPS data *
 * Created by ychen on 1/15/2015.
 */

public class DefaultLocationListener implements LocationListener {

    public static double latitude;
    public static double longitude;
    public static double relativeError;
    
    @Override
    public void onLocationChanged(Location currLoc){
        currLoc.getLatitude();
        currLoc.getLongitude();
        currLoc.getAccuracy();
        latitude=currLoc.getLatitude();
        longitude=currLoc.getLongitude();
        relativeError=currLoc.getAccuracy();
    }

    @Override
    public void onProviderDisabled(String provider){
        // do nothing for now
    }

    @Override
    public void onProviderEnabled(String provider){
        // do nothing for now
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras){

    }
}