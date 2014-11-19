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

import java.util.Date;

public class MainActivity extends Activity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        View.OnClickListener, LocationListener {

    private LocationRequest mLocationRequest;
    private boolean requesting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //give that button a listener
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

    private LocationClient locationClient;

    public void onResume() {
        super.onResume();



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

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this, "Successfully connected to google play services' location service", Toast.LENGTH_LONG).show();

        //once we connect, start requesting updates

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create();
        // Use high accuracy
        mLocationRequest.setPriority(
                LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Set the update interval to 5 seconds
        mLocationRequest.setInterval(100);
        // Set the fastest update interval to 1 second
        mLocationRequest.setFastestInterval(100);

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
        Log.d("Events", "" + v.getId());
        if(requesting) stopRequesting();
        else startRequesting();
    }

    private void stopRequesting() {
        locationClient.disconnect();
    }

    private long updates = 0;

    private Location zero = null;

    @Override
    public void onLocationChanged(Location location) {
        if(zero == null) {
            zero = location;
        }
        updates ++;
        TextView view = (TextView)findViewById(R.id.textView);
        view.setText("" + Math.abs(location.getLatitude()) + " " + (location.getLatitude() > 0 ? "N" : "S") + "(" + (location.getLatitude() - zero.getLatitude()) + ")" + "\n");
        view.append("" + Math.abs(location.getLongitude()) + " " + (location.getLongitude() > 0 ? "E" : "W") + "(" + (location.getLongitude() - zero.getLongitude()) + ")" + "\n\n");
        view.append("Update " + updates);

    }
}
