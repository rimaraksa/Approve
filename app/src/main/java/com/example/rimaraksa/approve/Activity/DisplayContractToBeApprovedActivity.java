package com.example.rimaraksa.approve.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rimaraksa.approve.Global;
import com.example.rimaraksa.approve.Model.Account;
import com.example.rimaraksa.approve.Model.Contract;
import com.example.rimaraksa.approve.R;
import com.example.rimaraksa.approve.DatabaseConnection.UploadFileToServer;

import java.io.IOException;


public class DisplayContractToBeApprovedActivity extends ActionBarActivity{
    private String username;
    private Account account;
    private Contract contract;
    private String account_id;
    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_contract_to_be_approved);

        account = Global.account;
        contract = (Contract) getIntent().getSerializableExtra("Contract");
        username = account.getUsername();
        account_id = account.getAccount_id() + "";

        TextView tvFromWaiting = (TextView) findViewById(R.id.TVFromWaiting);
        TextView tvSubjectWaiting = (TextView) findViewById(R.id.TVSubjectWaiting);
        TextView tvBodyWaiting = (TextView) findViewById(R.id.TVBodyWaiting);
        TextView tvDateWaiting = (TextView) findViewById(R.id.TVDateWaiting);
        TextView tvLocation = (TextView) findViewById(R.id.TVLocation);


        tvFromWaiting.setText(contract.getSender());
        tvSubjectWaiting.setText(contract.getSubject());
        tvBodyWaiting.setText(contract.getBody());
        tvDateWaiting.setText(contract.getDateRequest());

        try {
            tvLocation.setText(Global.latLongToCity(DisplayContractToBeApprovedActivity.this, contract.getLocation()));
        }
        catch (IOException e) {
            e.printStackTrace();
            tvLocation.setText("Unknown");
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_contract_to_be_approved, menu);
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

    public void onReject(View v){
        if(v.getId() == R.id.BReject){

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(DisplayContractToBeApprovedActivity.this);

            // Setting Dialog Title
            alertDialog.setTitle("Confirm Reject...");

            // Setting Dialog Message
            alertDialog.setMessage("Are you sure you want reject this contract?");

            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.delete);

            // Setting Positive "Yes" Button
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {

                    // Write your code here to invoke YES event
                    Intent i = new Intent(DisplayContractToBeApprovedActivity.this, RejectContractActivity.class);
                    i.putExtra("Account", account);
                    i.putExtra("Contract", contract);
                    startActivity(i);

                }
            });

            // Setting Negative "NO" Button
            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to invoke NO event
                }
            });

            // Showing Alert Message
            alertDialog.show();
        }
    }

    public void onApprove(View v){
        if(v.getId() == R.id.BApprove){

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(DisplayContractToBeApprovedActivity.this);

            // Setting Dialog Title
            alertDialog.setTitle("Prepare Video...");

            // Setting Dialog Message
            alertDialog.setMessage("Make a valid confirmation video for approval (e.g. The Approver saying agree). Do you want to continue?");

            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.ic_action);

            // Setting Positive "Yes" Button
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    if (!isDeviceSupportCamera()) {
                        Toast.makeText(getApplicationContext(), "Sorry! Your device doesn't support camera", Toast.LENGTH_LONG).show();
                        // will close the app if the device does't have camera
                        finish();
                    }
                    else{
                        recordVideo();
                    }
                }
            });

            // Setting Negative "NO" Button
            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            // Showing Alert Message
            alertDialog.show();


        }
    }

    /**
     * Checking device has camera hardware or not
     * */
    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // This device has a camera
            return true;
        } else {
            // This device does not have a camera
            return false;
        }
    }

    /**
     * Here we store the file url as it will be null after returning from camera app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    /**
     * Recording video
     */
    private void recordVideo(){
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        fileUri = Global.getOutputMediaFileUri(Global.MEDIA_TYPE_VIDEO, contract.getContractKey());

        // Set video quality
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

        // Start the video capture Intent
        startActivityForResult(intent, Global.CAMERA_CAPTURE_VIDEO_REQUEST_CODE);

    }

    /**
     * Receiving activity result method will be called after closing the camera
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        System.out.println("DONT FORGET TO DELETE THIS TEST");
//        resultCode = RESULT_CANCELED;
        if (requestCode == Global.CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
//                Video successfully recorded
                launchUploadActivity(false);
            }
            else if (resultCode == RESULT_CANCELED) {
                // User cancelled recording
                Toast.makeText(getApplicationContext(), "Video recording is cancelled!", Toast.LENGTH_SHORT).show();
            }
            else {
                // Failed to record video
                Toast.makeText(getApplicationContext(), "Sorry! Failed to record video!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void launchUploadActivity(boolean isImage){
        String filePath = fileUri.getPath();
        if (filePath != null) {
//            Requires key file, file type, the file
            Class intentTarget = DisplayActivity.class;
            System.out.println("UNCOMMENT BELOW");
//            new UploadFileToServer(this).execute(contract.getContractKey(), "video", filePath);





//            For test purpose only
            System.out.println("DELETE BELOW AFTER DONE TESTING");

            Intent i = new Intent(DisplayContractToBeApprovedActivity.this, VerifySignatureActivity.class);
            i.putExtra("Account", account);
            i.putExtra("Contract", contract);
            i.putExtra("FilePath", filePath);
            startActivity(i);



            System.out.println("UNCOMMENT BELOW");
////            Go back to profile page
//            Intent i = new Intent(DisplayContractToBeApprovedActivity.this, DisplayActivity.class);
//            i.putExtra("Account", account);
//            startActivity(i);
        }
        else {
            Toast.makeText(getApplicationContext(), "Sorry, file path is missing!", Toast.LENGTH_LONG).show();
        }
    }


}
