package ca.projectindigo.bigbrother;

// General UI and Location objects from Android OS
import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class GPS_ActivityTest extends ActionBarActivity{
    /* initialize the textview variables so we can actually see what's happening */
    protected TextView latitudeText;
    protected TextView longitudeText;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps__activity_test);
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
