package com.example.rimaraksa.approve.Unused;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rimaraksa.approve.Activity.DisplayActivity;
import com.example.rimaraksa.approve.DatabaseConnection.CreateContract;
import com.example.rimaraksa.approve.Global;
import com.example.rimaraksa.approve.Model.Account;
import com.example.rimaraksa.approve.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.UUID;


public class CreateContractActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private String username;
    private Account account;

    private static final String TAG = CreateContractActivity.class.getSimpleName();

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;

    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters


//    DatabaseHelper helper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contract);

        account = (Account) getIntent().getSerializableExtra("Account");
        username = account.getUsername();

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

    public void onSendClick(View v){
        if(v.getId() == R.id.BSend){
            EditText receiverField = (EditText) findViewById(R.id.TFReceiver);
            EditText subjectField = (EditText) findViewById(R.id.TFSubject);
            EditText bodyField = (EditText) findViewById(R.id.TFContractBody);

            String receiver = receiverField.getText().toString();
            String subject = subjectField.getText().toString();
            String body = bodyField.getText().toString();

            String location = getLocation();

            if(receiver.equals("") || subject.equals("") || body.equals("")){
                //popup message
                Toast pass = Toast.makeText(CreateContractActivity.this, "Required fields have not been completed!", Toast.LENGTH_SHORT);
                pass.show();
            }
            else{
                //insert new contract
//                Contract c = new Contract();
//                c.setSender(username);
//                c.setReceiver(receiver);
//                c.setSubject(subject);
//                c.setBody(body);
////                c.setLocation();
//                c.setStatus("waiting");
//                c.setDateRequest(getDateTime());
//
//                int contractId = helper.insertContract(c);
//
//                int senderId = helper.searchId(username);
//                int receiverId = helper.searchId(receiver);
//
//
//                helper.insertAccountContract(senderId, receiverId, contractId);
//
//                Intent i = new Intent(CreateContractActivity.this, DisplayActivity.class);
//                i.putExtra("Name", helper.searchName(username));
//                i.putExtra("Username", username);
//                startActivity(i);

//                Get the location of where the contract is domiciled

//                gps = new GPSTracker(CreateContractActivity.this);
//
//
//                if(gps.canGetLocation()){
//
//                    double latitude = gps.getLatitude();
//                    double longitude = gps.getLongitude();
//
//                    // \n is for new line
//                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
//                    location = latitude + ";" + longitude;
//                }
//                else{
//                    // can't get location
//                    // GPS or Network is not enabled
//                    // Ask user to enable GPS/network in settings
//                    gps.showSettingsAlert();
//                }


                String contractKey = UUID.randomUUID().toString();
                new CreateContract(this, account).execute(contractKey, username, receiver, subject, body, location, "waiting", Global.getDateTime());

            }

        }


    }

    public void onCancelClick(View v){
        if(v.getId() == R.id.BCancel){
            Intent i = new Intent(CreateContractActivity.this, DisplayActivity.class);
//            i.putExtra("Name", helper.searchName(username));
//            i.putExtra("Username", username);
            i.putExtra("Account", account);
            startActivity(i);
        }
    }

    private String getLocation(){

        String location = "0.0;0.0";
        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

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
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
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
