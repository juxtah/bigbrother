package ca.projectindigo.bigbrother;

// General UI and Location objects from Android OS
import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class GPS_ActivityTest extends ActionBarActivity{
    /* initialize the textview variables so we can actually see what's happening */
    protected TextView latitudeText;
    protected TextView longitudeText;
    protected String deviceID;
    private Handler gpsRepeater;

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
        gpsRepeater = new Handler();
        updateAndSendLoc.run();
    }

    private Runnable updateAndSendLoc = new Runnable() {
        @Override
        public void run() {
            double [] timeLongLatArray = retrieveInformation();
            if (timeLongLatArray != null) writeValues(timeLongLatArray[0], timeLongLatArray[1], timeLongLatArray[2]);
            gpsRepeater.postDelayed(updateAndSendLoc, 1000);
        }
    };

    /**
     * push the values to server for storage into database
     */
    private void writeValues(double time, double longitude, double latitude){
        String t = Double.toString(time);
        String lon = Double.toString(longitude);
        String lat = Double.toString(latitude);
        HttpClient dbConnection = new DefaultHttpClient();
        HttpPost target = new HttpPost("http://projectindigo.ca/bigbrother.php");
        try{
            List<NameValuePair> data = new ArrayList<>(4);     // there are 4 fields we want to send to the server
            data.add(new BasicNameValuePair("device_id", this.deviceID));
            data.add(new BasicNameValuePair("time", t));
            data.add(new BasicNameValuePair("long", lon));
            data.add(new BasicNameValuePair("lat", lat));
            target.setEntity(new UrlEncodedFormEntity(data));
            dbConnection.execute(target);
        }
        catch (Exception e) { System.out.println(e.getMessage()); }
    }

    /**
     * get the gps information and store in an array of 3 elements
     */
    private double [] retrieveInformation(){
        LocationManager newLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        LocationListener newLocationListener = new DefaultLocationListener();

        double [] r = null;        // store null first in case nothing happens. We don't want to push bad values
        /* first attempt to lock onto GPS. If that fails, try Wi-Fi or cell towers */
        newLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, newLocationListener);
        if (newLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && DefaultLocationListener.latitude != 0 && DefaultLocationListener.longitude != 0){
            r = new double[3];
            r[0] = Calendar.getInstance().get(Calendar.SECOND);
            r[1] = DefaultLocationListener.longitude;
            r[2] = DefaultLocationListener.latitude;
        }
        else{
            /* try Wi-Fi */
            newLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, newLocationListener);
            if (newLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) && DefaultLocationListener.latitude != 0 && DefaultLocationListener.longitude != 0){
                r = new double[3];
                r[0] = Calendar.getInstance().get(Calendar.SECOND);
                r[1] = DefaultLocationListener.longitude;
                r[2] = DefaultLocationListener.latitude;
            }
        }
        // if r is still null, then GPS wasn't able to connect and therefore shouldn't push back any values
        return r;
    }

    public void buttonOnClick(View v){
        latitudeText = (TextView) findViewById(R.id.lat_text);
        longitudeText = (TextView) findViewById(R.id.long_text);
        LocationManager newLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        LocationListener newLocationListener = new DefaultLocationListener();
        
        /* first attempt to lock onto GPS. If that fails, try Wi-Fi or cell towers */
        newLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, newLocationListener);
        if (newLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && DefaultLocationListener.latitude != 0 && DefaultLocationListener.longitude != 0){
            latitudeText.setText("Latitude = "+DefaultLocationListener.latitude+" with an error of "+DefaultLocationListener.relativeError);
            longitudeText.setText("Longitude = "+DefaultLocationListener.longitude+" with an error of "+DefaultLocationListener.relativeError);
        }
        else{
            /* try Wi-Fi */
            newLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, newLocationListener);
            if (newLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) && DefaultLocationListener.latitude != 0 && DefaultLocationListener.longitude != 0){
                latitudeText.setText("Latitude = "+DefaultLocationListener.latitude+" with an error of "+DefaultLocationListener.relativeError);
                longitudeText.setText("Longitude = "+DefaultLocationListener.longitude+" with an error of "+DefaultLocationListener.relativeError);
            }
            else{
                /* Location Services might not be enabled */
                Toast gpsError = Toast.makeText(getApplicationContext(), "Location Failed...Is Location Services Turned On?...", Toast.LENGTH_LONG);
                gpsError.show();
            }
        }
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
