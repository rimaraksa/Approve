package com.example.rimaraksa.approve.Activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.rimaraksa.approve.Global;
import com.example.rimaraksa.approve.Model.Account;
import com.example.rimaraksa.approve.Model.Contract;
import com.example.rimaraksa.approve.R;

import java.io.IOException;


public class DisplaySentContractActivity extends ActionBarActivity {
    private Account account;
    private Contract contract;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_sent_contract);

        account = (Account) getIntent().getSerializableExtra("Account");
        contract = (Contract) getIntent().getSerializableExtra("Contract");

        TextView tvToWaiting = (TextView) findViewById(R.id.TVToWaiting);
        TextView tvSubjectWaiting = (TextView) findViewById(R.id.TVSubjectWaiting);
        TextView tvBodyWaiting = (TextView) findViewById(R.id.TVBodyWaiting);
        TextView tvDateWaiting = (TextView) findViewById(R.id.TVDateWaiting);
        TextView tvLocation = (TextView) findViewById(R.id.TVLocation);


        tvToWaiting.setText(contract.getReceiver());
        tvSubjectWaiting.setText(contract.getSubject());
        tvBodyWaiting.setText(contract.getBody());
        tvDateWaiting.setText(contract.getDateRequest());

        try {
            tvLocation.setText(Global.latLongToCity(DisplaySentContractActivity.this, contract.getLocation()));
        }
        catch (IOException e) {
            e.printStackTrace();
            tvLocation.setText("Unknown");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_sent_contract, menu);
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
