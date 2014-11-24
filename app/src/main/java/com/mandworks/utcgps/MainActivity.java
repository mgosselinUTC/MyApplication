package com.mandworks.utcgps;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class MainActivity extends Activity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        View.OnClickListener, LocationListener {

    private LocationRequest mLocationRequest;
    private boolean requesting = false;
    private LocationClient locationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //button listener
        findViewById(R.id.button).setOnClickListener(this);
    }

    private void startRequesting() {

        int boop = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (boop == ConnectionResult.SUCCESS) {
            Log.d("play services", "success");
            locationClient = new LocationClient(this, this, this);
            locationClient.connect();

        } else requesting = false;


    }


    public void onResume() {
        super.onResume();
        startRequesting();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void onPause() {
        super.onPause();
        stopRequesting();
    }

    @Override
    public void onConnected(Bundle bundle) {
        //Toast.makeText(this, "Successfully connected to google play services' location service", Toast.LENGTH_LONG).show();

        //once we connect, start requesting updates

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create();
        // Use high accuracy
        mLocationRequest.setPriority(
                LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Set the update interval to 5 seconds
        mLocationRequest.setInterval(1000);
        // Set the fastest update interval to 1 second
        mLocationRequest.setFastestInterval(1000);

        locationClient.requestLocationUpdates(mLocationRequest, this);

        requesting = true;
    }

    @Override
    public void onDisconnected() {
        Toast.makeText(this, "Disconnected from play services", Toast.LENGTH_LONG).show();
        requesting = false;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    @Override
    public void onClick(View v) {
        setOriginToCurent();
    }

    private void setOriginToCurent() {
        if(currentLocation != null) {
            zero = currentLocation;
            Log.d("Origin", format(currentLocation, " "));
        }
    }

    private String format(Location location, String separator) {

        double lat = location.getLatitude() * ACCURACY;
        lat = (int) lat;
        lat /= ACCURACY;
        double lon = location.getLongitude() * ACCURACY;
        lon = (int) lon;
        lon /= ACCURACY;

        return "" + Math.abs(lat) + " " + (lat > 0 ? "N" : "S") + separator
        + "" + Math.abs(lon) + " " + (lon > 0 ? "E" : "W");

    }

    private void stopRequesting() {
        locationClient.disconnect();
        requesting = false;
    }

    private long updates = 0;

    private Location zero = null;
    private Location currentLocation = null;

    private final static double ACCURACY = Math.pow(10  , 5);

    @Override
    public void onLocationChanged(Location location) {

        currentLocation = location;

        //place, coords
        //IT, 4482123 N
        //    687412 W

        //IT, 4482123 N
        //    6874118 W

        //IT, 4482123 N
        //    6874118 W

        //IT, 4482123 N
        //    6874118 W

        //IT, 4482123 N
        //    6874118 W

        //IT, 4482123 N
        //    6874118 W

        //IT, 4482123 N
        //    6874118 W

        //IT, 4482123 N
        //    6874118 W

        updates ++;
        TextView mainView = (TextView)findViewById(R.id.textView);
        TextView zeroView = (TextView)findViewById(R.id.zeroLocation);

        double lat = location.getLatitude() * ACCURACY;
        lat = (int) lat;
        lat /= ACCURACY;
        double lon = location.getLongitude() * ACCURACY;
        lon = (int) lon;
        lon /= ACCURACY;

        mainView.setText("Absolute Coordinates:\n");
        mainView.append(format(location, "\n"));
        if(zero != null) {
            zeroView.setText("\nDistance From Origin: ");
            zeroView.append("" + zero.distanceTo(location) + " meters");
        } else {
            zeroView.setText("Origin not set.");
        }


    }
}
