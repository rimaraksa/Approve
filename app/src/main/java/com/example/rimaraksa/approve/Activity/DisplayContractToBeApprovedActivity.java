package com.example.rimaraksa.approve.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rimaraksa.approve.ServerConnection.VerifySignature;
import com.example.rimaraksa.approve.Util;
import com.example.rimaraksa.approve.Model.Contract;
import com.example.rimaraksa.approve.R;

import java.io.IOException;


public class DisplayContractToBeApprovedActivity extends ActionBarActivity{

    private Activity activity;
    private Toolbar mToolbar;

    private Contract contract;
    private String senderName, senderUsername, senderPhone;
    private Bitmap senderProfpic;
    private Uri fileUri;

    private ImageView ivProfileSender;
    private TextView tvSenderName, tvSenderUsername, tvReceiver, tvSubject, tvBody, tvDate, tvLocation;
    private ImageButton ibReject, ibApprove;
    private TextView tvApprove, tvReject, tvContact;

    private Button bCancel, bContinue;
    private TextView tvTitle;
    private ImageButton ibCall, ibSMS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_contract_to_be_approved);

        activity = this;

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Pending Inbox");

        contract = (Contract) getIntent().getSerializableExtra("Contract");
        senderProfpic = (Bitmap) getIntent().getParcelableExtra("SenderProfpic");
        senderName = (String) getIntent().getExtras().getString("SenderName");
        senderUsername = (String) getIntent().getExtras().getString("SenderUsername");
        senderPhone = (String) getIntent().getExtras().getString("SenderPhone");

        ivProfileSender = (ImageView) findViewById(R.id.IVProfileSender);
        tvSenderName = (TextView) findViewById(R.id.TVSenderName);
        tvSenderUsername = (TextView) findViewById(R.id.TVSenderUsername);
        tvReceiver = (TextView) findViewById(R.id.TVReceiver);
        tvSubject = (TextView) findViewById(R.id.TVSubject);
        tvBody = (TextView) findViewById(R.id.TVBody);
        tvDate = (TextView) findViewById(R.id.TVRequestedDate);
        tvLocation = (TextView) findViewById(R.id.TVLocation);
        ibApprove = (ImageButton) findViewById(R.id.BApprove);
        ibReject = (ImageButton) findViewById(R.id.BReject);
        tvApprove = (TextView) findViewById(R.id.TVApprove);
        tvReject = (TextView) findViewById(R.id.TVReject);
        tvContact = (TextView) findViewById(R.id.TVContact);

        ivProfileSender.setImageBitmap(senderProfpic);
        tvSenderName.setText(senderName);
        tvSenderUsername.setText(senderUsername);
        tvReceiver.setText(Util.account.getName() + " [" + Util.account.getUsername() + "]");
        tvSubject.setText(contract.getSubject());
        tvBody.setText(contract.getBody());
        tvDate.setText(Util.getTimeDetailToDisplayFromDateTime(contract.getDateRequest()));
//        tvContact.setText("Contact " + senderName);



        try {
            tvLocation.setText(Util.latLongToCity(DisplayContractToBeApprovedActivity.this, contract.getLocation()));
        }
        catch (IOException e) {
            e.printStackTrace();
            tvLocation.setText("Unknown");
        }

        SpannableString content = new SpannableString("Contact " + senderName);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        tvContact.setText(content);


        tvContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popContactDialog();

            }
        });

        String fontPath = "fonts/Coquette Bold.ttf";
        TextView tvLogo = (TextView) findViewById(R.id.TVLogo);
        Typeface typeface = Typeface.createFromAsset(getAssets(), fontPath);
        tvLogo.setTypeface(typeface);

    }

    private void popContactDialog(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        final View view = getLayoutInflater().inflate(R.layout.dialog_contact, null);
        alertDialog.setView(view);

        tvTitle = (TextView) view.findViewById(R.id.TVTitle);
        ibCall = (ImageButton) view.findViewById(R.id.IBCall);
        ibSMS = (ImageButton) view.findViewById(R.id.IBSMS);
        bCancel = (Button) view.findViewById(R.id.BCancel);

        tvTitle.setText("Contact Correspondent");
        // Showing Alert Message
        final AlertDialog ad = alertDialog.show();

        bCancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                ad.dismiss();

            }
        });

        ibCall.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                ad.dismiss();
                call(senderPhone);
            }
        });

        ibSMS.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                ad.dismiss();
                sendSMS(senderPhone);


            }
        });
    }

    private void sendSMS(String phone) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("vnd.android-dir/mms-sms");
        intent.putExtra("address", phone);
        startActivity(intent);
    }


    private void call(String phone) {
        Intent i = new Intent(Intent.ACTION_CALL);
        String p = "tel:" + phone;
        i.setData(Uri.parse(p));
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_contract_to_be_approved, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                                    // Navigate up to the closest parent
                            .startActivities();
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    upIntent.putExtra("FromDisplayContractToBeApprovedActivity", true);
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
            case R.id.action_settings:
                return true;
            case R.id.action_compose_contract:
                Intent i = new Intent(getApplicationContext(), CreateContractActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
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


    public void onApprove(View v) {
        if(v.getId() == R.id.BApprove) {

            ibApprove.setVisibility(View.INVISIBLE);
            tvApprove.setTypeface(null, Typeface.BOLD);

            popVideoCaptureDialog();

//            AlertDialog.Builder alertDialog = new AlertDialog.Builder(DisplayContractToBeApprovedActivity.this);
//
//            // Setting Dialog Title
//            alertDialog.setTitle("Prepare Video...");
//
//            // Setting Dialog Message
//            alertDialog.setMessage("Make a valid confirmation video for approval (e.g. The Approver saying agree). Do you want to continue?");
//
//            // Setting Icon to Dialog
//            alertDialog.setIcon(R.drawable.ic_action);
//
//            // Setting Positive "Yes" Button
//            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//
//                    if (!isDeviceSupportCamera()) {
//                        Toast.makeText(getApplicationContext(), "Sorry! Your device doesn't support camera", Toast.LENGTH_LONG).show();
//                        // will close the app if the device does't have camera
//                        finish();
//                    } else {
//                        recordVideo();
//                    }
//
//                    ibApprove.setVisibility(View.VISIBLE);
//                    tvApprove.setTypeface(null, Typeface.NORMAL);
//                }
//            });
//
//            // Setting Negative "NO" Button
//            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    ibApprove.setVisibility(View.VISIBLE);
//                    tvApprove.setTypeface(null, Typeface.NORMAL);
//
//                }
//            });
//
//
//            // Showing Alert Message
//            alertDialog.show();

        }



    }

    private void popVideoCaptureDialog(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        final View view = getLayoutInflater().inflate(R.layout.dialog_video_capture, null);
        alertDialog.setView(view);
        alertDialog.setCancelable(false);

        TextView tvMessage = (TextView) view.findViewById(R.id.TVMessage);
        bCancel = (Button) view.findViewById(R.id.BCancel);
        bContinue = (Button) view.findViewById(R.id.BContinue);

        tvMessage.setText("Valid approval video must consist of your facial features. Press 'Continue' to start approval video capturing.");
        // Showing Alert Message
        final AlertDialog ad = alertDialog.show();

        bCancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                ibApprove.setVisibility(View.VISIBLE);
                tvApprove.setTypeface(null, Typeface.NORMAL);
                ad.dismiss();
            }
        });

        bContinue.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                ibApprove.setVisibility(View.VISIBLE);
                tvApprove.setTypeface(null, Typeface.NORMAL);
                ad.dismiss();
                if (!isDeviceSupportCamera()) {
                    Util.cameraError(activity, 0);
                } else {
                    recordVideo();
                }


            }
        });


    }



    public void onReject(View v){

        if(v.getId() == R.id.BReject) {

            ibReject.setVisibility(View.INVISIBLE);
            tvReject.setTypeface(null, Typeface.BOLD);

            popRejectDialog();

//            AlertDialog.Builder alertDialog = new AlertDialog.Builder(DisplayContractToBeApprovedActivity.this);
//
//            // Setting Dialog Title
//            alertDialog.setTitle("Confirm Reject...");
//
//            // Setting Dialog Message
//            alertDialog.setMessage("The approval process is configured to require user to specify a justification for rejection. Do you want to continue?");
//
//            // Setting Icon to Dialog
//            alertDialog.setIcon(R.drawable.delete);
//
//            // Setting Positive "Yes" Button
//            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//
//
//                    ibReject.setVisibility(View.VISIBLE);
//                    tvReject.setTypeface(null, Typeface.NORMAL);
//
//                    // Write your code here to invoke YES event
//                    Intent i = new Intent(DisplayContractToBeApprovedActivity.this, RejectContractActivity.class);
//                    i.putExtra("Contract", contract);
//                    i.putExtra("SenderProfpic", senderProfpic);
//                    i.putExtra("SenderName", senderName);
//                    startActivity(i);
//
//                }
//            });
//
//            // Setting Negative "NO" Button
//            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    // Write your code here to invoke NO event
//                    ibReject.setVisibility(View.VISIBLE);
//                    tvReject.setTypeface(null, Typeface.NORMAL);
//                }
//            });
//
//
//            // Showing Alert Message
//            alertDialog.show();
//
//
        }
    }

    private void popRejectDialog(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        final View view = getLayoutInflater().inflate(R.layout.dialog_reject, null);
        alertDialog.setView(view);
        alertDialog.setCancelable(false);

        TextView tvMessage = (TextView) view.findViewById(R.id.TVMessage);
        bCancel = (Button) view.findViewById(R.id.BCancel);
        bContinue = (Button) view.findViewById(R.id.BContinue);

        tvMessage.setText("The rejection process is configured to require user to specify a justification. Press 'Continue' to reject.");
        // Showing Alert Message
        final AlertDialog ad = alertDialog.show();

        bCancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                ibReject.setVisibility(View.VISIBLE);
                tvReject.setTypeface(null, Typeface.NORMAL);
                ad.dismiss();
            }
        });

        bContinue.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                ibReject.setVisibility(View.VISIBLE);
                tvReject.setTypeface(null, Typeface.NORMAL);
                ad.dismiss();
                Intent i = new Intent(DisplayContractToBeApprovedActivity.this, RejectContractActivity.class);
                i.putExtra("Contract", contract);
                i.putExtra("SenderProfpic", senderProfpic);
                i.putExtra("SenderName", senderName);
                i.putExtra("SenderUsername", senderUsername);
                startActivity(i);

            }
        });


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
        fileUri = Util.getOutputMediaFileUri(Util.MEDIA_TYPE_VIDEO, contract.getContractKey());

//        Set video quality
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
//        Set the image file name
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//        Start the video capture Intent
        startActivityForResult(intent, Util.CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
    }


    /**
     * Receiving activity result method will be called after closing the camera
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Util.CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
//                Video successfully recorded
                launchUploadActivity(false);
            }
            else if (resultCode == RESULT_CANCELED) {
//                User cancelled recording
                Toast.makeText(getApplicationContext(), "Video recording is cancelled.", Toast.LENGTH_SHORT).show();
            }
            else {
//                Failed to record video
                Toast.makeText(getApplicationContext(), "Sorry, failed to record video.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void launchUploadActivity(boolean isImage){
        String filePath = fileUri.getPath();
        if (filePath != null) {
            System.out.println("Filepath on DisplayContractToBeApproved: " + filePath);
            Bitmap videoFrame = Util.getVideoFrame(filePath);
            new VerifySignature(this, activity, contract, videoFrame).execute(filePath);
        }
        else {
            Toast.makeText(getApplicationContext(), "Sorry, file path is missing!", Toast.LENGTH_LONG).show();
        }
    }

//    @Override
//    public void onBackPressed() {
//    }





}
