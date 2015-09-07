package com.example.rimaraksa.approve.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rimaraksa.approve.Model.Account;
import com.example.rimaraksa.approve.Model.Contract;
import com.example.rimaraksa.approve.R;
import com.example.rimaraksa.approve.DatabaseConnection.RejectContract;


public class RejectContractActivity extends ActionBarActivity {
    private Account account;
    private Contract contract;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reject_contract);

        account = (Account) getIntent().getSerializableExtra("Account");
        contract = (Contract) getIntent().getSerializableExtra("Contract");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reject_contract, menu);
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

    public void onRejectClick (View v) {
        if (v.getId() == R.id.BRejectRC) {
            EditText reasonForRejectionField = (EditText) findViewById(R.id.TFReasonForRejection);
            String reasonForRejection = reasonForRejectionField.getText().toString();

            if (reasonForRejectionField.equals("")) {
                //popup message
                Toast pass = Toast.makeText(RejectContractActivity.this, "Required fields have not been completed!", Toast.LENGTH_SHORT);
                pass.show();
            } else {
                new RejectContract(this, account, contract).execute(reasonForRejection);
            }

        }
    }

    public void onCancelClick (View v) {
        if (v.getId() == R.id.BCancelRC) {
            Intent i = new Intent(RejectContractActivity.this, DisplayActivity.class);
            i.putExtra("Account", account);
            startActivity(i);

        }
    }
}
