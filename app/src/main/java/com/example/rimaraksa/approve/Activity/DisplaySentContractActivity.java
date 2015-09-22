package com.example.rimaraksa.approve.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
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

import com.example.rimaraksa.approve.Global;
import com.example.rimaraksa.approve.Model.Contract;
import com.example.rimaraksa.approve.R;

import java.io.IOException;


public class DisplaySentContractActivity extends ActionBarActivity {
    private Toolbar mToolbar;

    private Contract contract;
    private String receiverName, receiverPhone;
    private ImageView ivProfileSender;
    private TextView tvSenderName, tvSenderUsername, tvReceiver, tvSubject, tvBody, tvDate, tvLocation;

    private Button bCancel;
    private TextView tvTitle, tvContact;
    private ImageButton ibCall, ibSMS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_sent_contract);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Waiting Outbox");

        contract = (Contract) getIntent().getSerializableExtra("Contract");
        receiverName = (String) getIntent().getExtras().getString("ReceiverName");
        receiverPhone = (String) getIntent().getExtras().getString("ReceiverPhone");

        ivProfileSender = (ImageView) findViewById(R.id.IVProfileSender);
        tvSenderName = (TextView) findViewById(R.id.TVSenderName);
        tvSenderUsername = (TextView) findViewById(R.id.TVSenderUsername);
        tvReceiver = (TextView) findViewById(R.id.TVReceiver);
        tvSubject = (TextView) findViewById(R.id.TVSubject);
        tvBody = (TextView) findViewById(R.id.TVBody);
        tvDate = (TextView) findViewById(R.id.TVRequestedDate);
        tvLocation = (TextView) findViewById(R.id.TVLocation);
        tvContact = (TextView) findViewById(R.id.TVContact);


        ivProfileSender.setImageBitmap(Global.accountProfpicBitmap);
        tvSenderName.setText(Global.account.getName());
        tvSenderUsername.setText(Global.account.getUsername());
        tvReceiver.setText(receiverName + " [" + contract.getReceiver() + "]");
        tvSubject.setText(contract.getSubject());
        tvBody.setText(contract.getBody());
        tvDate.setText(Global.getTimeDetailToDisplayFromDateTime(contract.getDateRequest()));



        try {
            tvLocation.setText(Global.latLongToCity(DisplaySentContractActivity.this, contract.getLocation()));
        }
        catch (IOException e) {
            e.printStackTrace();
            tvLocation.setText("Unknown");
        }

        SpannableString content = new SpannableString("Contact " + receiverName);
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

        tvTitle.setText("Contact Receiver");
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
                call(receiverPhone);
            }
        });

        ibSMS.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                ad.dismiss();
                sendSMS(receiverPhone);


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
        getMenuInflater().inflate(R.menu.menu_display_sent_contract, menu);
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
                    upIntent.putExtra("FromDisplaySentContractActivity", true);
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
}
