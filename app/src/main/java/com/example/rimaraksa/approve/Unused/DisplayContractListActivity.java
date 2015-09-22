package com.example.rimaraksa.approve.Unused;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.rimaraksa.approve.Adapter.ContractAdapter;
import com.example.rimaraksa.approve.Model.Account;
import com.example.rimaraksa.approve.Model.Contract;
import com.example.rimaraksa.approve.R;

import java.util.ArrayList;
import java.util.List;


public class DisplayContractListActivity extends ActionBarActivity {
//    private String username, role, status;
//    private Account account;
//    private ArrayList<Contract> contracts;
//    private String account_id;
//
////    DatabaseHelper helper = new DatabaseHelper(this);
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_display_contract_list);
//
//        account = (Account) getIntent().getSerializableExtra("Account");
//        contracts = (ArrayList<Contract>) getIntent().getSerializableExtra("Contracts");
//        username = account.getUsername();
//        role = getIntent().getStringExtra("Role");
//        account_id = account.getAccount_id() + "";
//
//        populateLVContract();
//        registerClickCallBackContract();
//    }
//
//    private void populateLVContract() {
//        //create list of contracts
////        if(role.equals("receiver")){
////            contracts = helper.getContractsByReceiver(helper.searchId(username), status);
////        else{
////            contracts = helper.getContractsBySender(helper.searchId(username), status);
////        }
//
//
//
//        //build adapter
//        ContractAdapter adapter = new ContractAdapter(this, contracts, role);
//        //configure list view
//
//        ListView list = (ListView) findViewById(R.id.LVContract);
//        list.setAdapter(adapter);
//
//    }
//
//    private void registerClickCallBackContract() {
//        ListView list = (ListView) findViewById(R.id.LVContract);
//
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                String msg = "You clicked inbox " + position;
////                Toast.makeText(DisplayContractListActivity.this, msg, Toast.LENGTH_LONG).show();
//
//                Contract contract = contracts.get(position);
//                Intent i = null;
//                if(contract.getStatus().equals("waiting")){
//                    if(role.equals("receiver")){
//                        i = new Intent(DisplayContractListActivity.this, DisplayContractToBeApprovedActivity.class);
//                        i.putExtra("Account", account);
//                        i.putExtra("Contract", contract);
//                        startActivity(i);
//
//                    }
//                    else{
//                        i = new Intent(DisplayContractListActivity.this, DisplaySentContractActivity.class);
//                        i.putExtra("Account", account);
//                        i.putExtra("Contract", contract);
//                        startActivity(i);
//
//                    }
//                }
//                else if(contract.getStatus().equals("approved")){
//                    i = new Intent(DisplayContractListActivity.this, DisplayApprovedContractActivity.class);
//                    i.putExtra("Account", account);
//                    i.putExtra("Contract", contract);
//                    i.putExtra("Role", role);
//                    startActivity(i);
//                }
//                else{
//                    i = new Intent(DisplayContractListActivity.this, DisplayRejectedContractActivity.class);
//                    i.putExtra("Account", account);
//                    i.putExtra("Contract", contract);
//                    i.putExtra("Role", role);
//                    startActivity(i);
//                }
//
//            }
//        });
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_display_contract_list, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
