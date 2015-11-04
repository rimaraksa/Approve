package com.example.rimaraksa.approve.Activity;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rimaraksa.approve.ServerConnection.CreateContract;
import com.example.rimaraksa.approve.Util;
import com.example.rimaraksa.approve.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.UUID;


public class CreateContractActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private Activity activity;
    private Toolbar mToolbar;

    private static final String TAG = CreateContractActivity.class.getSimpleName();

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    //    Components on layout
    private EditText tfReceiverUsername, tfContractSubject, tfContractBody;
    private String receiverUsername, contractSubject, contractBody;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contract);

        activity = this;

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.ic_action_cancel);
        getSupportActionBar().setTitle("");

        TextView tvToolbarTitle = (TextView) findViewById(R.id.TVToolbarTitle);
        tvToolbarTitle.setText("New Contract");

        tfReceiverUsername = (EditText) findViewById(R.id.TFReceiverUsername);
        tfContractSubject = (EditText) findViewById(R.id.TFContractSubject);
        tfContractBody = (EditText) findViewById(R.id.TFContractBody);


        // We need to check availability of play services
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_contract, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent i = new Intent(CreateContractActivity.this, DisplayActivity.class);
                startActivity(i);
                return true;
            case R.id.action_send:
                sendContract();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void sendContract() {
        receiverUsername = tfReceiverUsername.getText().toString();
        contractSubject = tfContractSubject.getText().toString();
        contractBody = tfContractBody.getText().toString();

        String location = getLocation();

        if (receiverUsername.equals("") || contractSubject.equals("") || contractBody.equals("")) {
            Util.createContractError(activity, 0);
        }
        else if(receiverUsername.equals(Util.account.getUsername())){
            Util.createContractError(activity, 1);
        }
        else {
            String contractKey = UUID.randomUUID().toString();
            new CreateContract(this, activity).execute(contractKey, receiverUsername, contractSubject, contractBody, location, "pending", Util.getDateTime());

        }
    }

    private String getLocation(){
        String location = "0.0;0.0";

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();
            location = latitude + ";" + longitude;
        }

        return location;
    }


    /**
     * Creating google api client object
     * */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            else {
                Toast.makeText(this.getApplicationContext(),"This device is not supported.", Toast.LENGTH_LONG).show();
                this.finish();
            }
            return false;
        }
        return true;
    }


    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        checkPlayServices();
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        getLocation();

    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }


}
