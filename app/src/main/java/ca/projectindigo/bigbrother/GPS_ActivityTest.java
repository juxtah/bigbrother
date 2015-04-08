package ca.projectindigo.bigbrother;

// General UI and Location objects from Android OS

import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.UUID;

//import com.google.android.gms.location.LocationListener;

public class GPS_ActivityTest extends ActionBarActivity{
    protected String deviceID;
    private Handler gpsRepeater;
    private LocationManager newLocationManager;

    private void setUniqueID(){
        TelephonyManager pid = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        String device, serial, id;
        device = "" + pid.getDeviceId();
        serial = "" + pid.getSimSerialNumber();
        id = "" + Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        UUID uniqueID = new UUID(id.hashCode(), ((long)device.hashCode() << 32) | serial.hashCode());
        this.deviceID = uniqueID.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps__activity_test);
        // also determine the unique device id as well
        setUniqueID();
        newLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        gpsRepeater = new Handler();
        updateAndSendLoc.run();
    }

    private Runnable updateAndSendLoc = new Runnable() {
        @Override
        public void run() {
            double [] timeLongLatArray = retrieveInformation();
            if (timeLongLatArray != null) writeValues(timeLongLatArray[0], timeLongLatArray[1], timeLongLatArray[2]);
            gpsRepeater.postDelayed(updateAndSendLoc, 120000);
        }
    };

    /**
     * push the values to server for storage into database
     */
    private void writeValues(double time, double longitude, double latitude){
        String t = Double.toString(time);
        String lon = Double.toString(longitude);
        String lat = Double.toString(latitude);
        AsyncHttpClient raw = new AsyncHttpClient();
        RequestParams data = new RequestParams();
        try{
            data.put("device_id", this.deviceID);
            data.put("time", t);
            data.put("longitude", lon);
            data.put("latitude", lat);
            raw.post("REDACTED", data, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
        }
        catch (Exception e) { System.out.println(e.getMessage()); }
    }

    /**
     * get the gps information and store in an array of 3 elements
     */
    private double [] retrieveInformation(){
        LocationListener newLocationListener = new DefaultLocationListener();

        double [] r = null;        // store null first in case nothing happens. We don't want to push bad values
        /* first attempt to lock onto GPS. If that fails, try Wi-Fi or cell towers */
        newLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 120000, 5, newLocationListener);
        if (newLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && DefaultLocationListener.latitude != 0 && DefaultLocationListener.longitude != 0){
            r = new double[3];
            r[0] = (System.currentTimeMillis() / 1000L);
            r[1] = DefaultLocationListener.longitude;
            r[2] = DefaultLocationListener.latitude;
        }
        else{
            /* try Wi-Fi */
            newLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 120000, 5, newLocationListener);
            if (newLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) && DefaultLocationListener.latitude != 0 && DefaultLocationListener.longitude != 0){
                r = new double[3];
                r[0] = (System.currentTimeMillis() / 1000L);
                r[1] = DefaultLocationListener.longitude;
                r[2] = DefaultLocationListener.latitude;
            }
        }
        // if r is still null, then GPS wasn't able to connect and therefore shouldn't push back any values
        return r;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gps__activity_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
