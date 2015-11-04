package com.example.rimaraksa.approve.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rimaraksa.approve.ServerConnection.DownloadVideo;
import com.example.rimaraksa.approve.Util;
import com.example.rimaraksa.approve.Model.Contract;
import com.example.rimaraksa.approve.R;

import java.io.IOException;


public class DisplayApprovedContractActivity extends ActionBarActivity {
    private Toolbar mToolbar;

    private Contract contract;
    private String role, senderName, senderUsername, receiverName, receiverUsername, box;
    private Bitmap senderProfpic;

    private ImageView ivProfileSender;
    private TextView tvSenderName, tvSenderUsername, tvReceiver, tvSubject, tvBody, tvRequestedDate, tvApprovedDate, tvLocation;


    private ImageButton ibDownload;
    private TextView tvDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_approved_contract);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        contract = (Contract) getIntent().getSerializableExtra("Contract");
        role = getIntent().getStringExtra("Role");

        contract = (Contract) getIntent().getSerializableExtra("Contract");

        ivProfileSender = (ImageView) findViewById(R.id.IVProfileSender);
        tvSenderName = (TextView) findViewById(R.id.TVSenderName);
        tvSenderUsername = (TextView) findViewById(R.id.TVSenderUsername);
        tvReceiver = (TextView) findViewById(R.id.TVReceiver);
        tvSubject = (TextView) findViewById(R.id.TVSubject);
        tvBody = (TextView) findViewById(R.id.TVBody);
        tvRequestedDate = (TextView) findViewById(R.id.TVRequestedDate);
        tvApprovedDate = (TextView) findViewById(R.id.TVApprovedDate);
        tvLocation = (TextView) findViewById(R.id.TVLocation);
        ibDownload = (ImageButton) findViewById(R.id.BDownload);
        tvDownload = (TextView) findViewById(R.id.TVDownload);

        if(role.equals("sender")){
            senderName = Util.account.getName();
            senderUsername = Util.account.getUsername();
            senderProfpic = Util.accountProfpicBitmap;
            receiverName = (String) getIntent().getExtras().getString("TargetName");
            receiverUsername = (String) getIntent().getExtras().getString("TargetUsername");
            box = "Outbox";
        }
        else{
            senderName = (String) getIntent().getExtras().getString("TargetName");
            senderUsername = (String) getIntent().getExtras().getString("TargetUsername");
            senderProfpic = (Bitmap) getIntent().getParcelableExtra("TargetProfpic");
            receiverName = Util.account.getName();
            receiverUsername = Util.account.getUsername();
            box = "Inbox";
        }

        getSupportActionBar().setTitle("Approved " + box);

        ivProfileSender.setImageBitmap(senderProfpic);
        tvSenderName.setText(senderName);
        tvSenderUsername.setText(senderUsername);
        tvReceiver.setText(receiverName + " [" + receiverUsername + "]");
        tvSubject.setText(contract.getSubject() + " [Approved]");
        tvBody.setText(contract.getBody());
        tvRequestedDate.setText(Util.getTimeDetailToDisplayFromDateTime(contract.getDateRequest()));
        tvApprovedDate.setText(Util.getTimeDetailToDisplayFromDateTime(contract.getDateAppOrReject()));


        try {
            tvLocation.setText(Util.latLongToCity(DisplayApprovedContractActivity.this, contract.getLocation()));
        }
        catch (IOException e) {
            e.printStackTrace();
            tvLocation.setText("Unknown");
        }

        String fontPath = "fonts/Coquette Bold.ttf";
        TextView tvLogo = (TextView) findViewById(R.id.TVLogo);
        Typeface typeface = Typeface.createFromAsset(getAssets(), fontPath);
        tvLogo.setTypeface(typeface);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_approved_contract, menu);
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

                    String activityToPass = "FromDisplayApprovedContractActivity" + box;
                    upIntent.putExtra(activityToPass, true);
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

    public void onDownload (View v){
        if(v.getId() == R.id.BDownload) {
            ibDownload.setVisibility(View.INVISIBLE);
            tvDownload.setTypeface(null, Typeface.BOLD);

            new DownloadVideo(this, contract, ibDownload, tvDownload).execute("video");

        }
    }


}
