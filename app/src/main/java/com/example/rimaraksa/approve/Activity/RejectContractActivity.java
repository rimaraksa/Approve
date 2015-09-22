package com.example.rimaraksa.approve.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rimaraksa.approve.Global;
import com.example.rimaraksa.approve.Model.Account;
import com.example.rimaraksa.approve.Model.Contract;
import com.example.rimaraksa.approve.R;
import com.example.rimaraksa.approve.DatabaseConnection.RejectContract;

import java.io.IOException;


public class RejectContractActivity extends ActionBarActivity {
    private Activity activity;
    private Toolbar mToolbar;

    private Contract contract;
    private String role, senderName, receiverName, box;
    private Bitmap senderProfpic;

    private ImageView ivProfileSender;
    private TextView tvSenderName, tvSenderUsername, tvReceiver, tvSubject, tvBody, tvRequestedDate, tvRejectedDate, tvLocation;
    private EditText tfReasonForRejection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reject_contract);

        activity = this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.ic_action_cancel);
        getSupportActionBar().setTitle("");

        TextView tvToolbarTitle = (TextView) findViewById(R.id.TVToolbarTitle);
        tvToolbarTitle.setText("Reject Contract");

        contract = (Contract) getIntent().getSerializableExtra("Contract");
        senderProfpic = (Bitmap) getIntent().getParcelableExtra("SenderProfpic");
        senderName = (String) getIntent().getExtras().getString("SenderName");

        contract = (Contract) getIntent().getSerializableExtra("Contract");

        ivProfileSender = (ImageView) findViewById(R.id.IVProfileSender);
        tvSenderName = (TextView) findViewById(R.id.TVSenderName);
        tvSenderUsername = (TextView) findViewById(R.id.TVSenderUsername);
        tvReceiver = (TextView) findViewById(R.id.TVReceiver);
        tvSubject = (TextView) findViewById(R.id.TVSubject);
        tvBody = (TextView) findViewById(R.id.TVBody);
        tvRequestedDate = (TextView) findViewById(R.id.TVRequestedDate);
        tvLocation = (TextView) findViewById(R.id.TVLocation);
        tfReasonForRejection = (EditText) findViewById(R.id.TFReasonForRejection);

        ivProfileSender.setImageBitmap(senderProfpic);
        tvSenderName.setText(senderName);
        tvSenderUsername.setText(contract.getSender());
        tvReceiver.setText(Global.account.getName() + " [" + Global.account.getUsername() + "]");
        tvSubject.setText("Rejecting: " + contract.getSubject());
        tvBody.setText(contract.getBody());
        tvRequestedDate.setText(Global.getTimeDetailToDisplayFromDateTime(contract.getDateRequest()));

        try {
            tvLocation.setText(Global.latLongToCity(RejectContractActivity.this, contract.getLocation()));
        }
        catch (IOException e) {
            e.printStackTrace();
            tvLocation.setText("Unknown");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reject_contract, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent i = new Intent(RejectContractActivity.this, DisplayContractToBeApprovedActivity.class);
                i.putExtra("Contract", contract);
                i.putExtra("SenderProfpic", senderProfpic);
                i.putExtra("SenderName", senderName);
                startActivity(i);
                return true;
            case R.id.action_send:
                rejectContract();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void rejectContract () {

        String reasonForRejection = tfReasonForRejection.getText().toString();

        if (reasonForRejection.equals("")) {
            //popup message
            Global.rejectError(activity, 0);
        } else {
            new RejectContract(this, activity, contract).execute(reasonForRejection);
        }

    }

}
