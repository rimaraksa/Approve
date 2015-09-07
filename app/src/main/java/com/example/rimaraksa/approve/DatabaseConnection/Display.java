package com.example.rimaraksa.approve.DatabaseConnection;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.rimaraksa.approve.Activity.DisplayApprovedContractActivity;
import com.example.rimaraksa.approve.Activity.DisplayContractListActivity;
import com.example.rimaraksa.approve.Activity.DisplayContractToBeApprovedActivity;
import com.example.rimaraksa.approve.Activity.DisplayRejectedContractActivity;
import com.example.rimaraksa.approve.Activity.DisplaySentContractActivity;
import com.example.rimaraksa.approve.Adapter.ContractAdapter;
import com.example.rimaraksa.approve.Fragment.CreateContractFragment;
import com.example.rimaraksa.approve.Fragment.InboxFragment;
import com.example.rimaraksa.approve.Fragment.OutboxFragment;
import com.example.rimaraksa.approve.Global;
import com.example.rimaraksa.approve.Model.Account;
import com.example.rimaraksa.approve.Model.Contract;
import com.example.rimaraksa.approve.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by rimaraksa on 15/6/15.
 */
public class Display extends AsyncTask<String,Void,String> {
    private Activity activity;
    private ArrayList<Contract> contracts = new ArrayList<Contract>();
    String account_id;
    String role, target, status;

    ListView contractList;



    public Display(Activity activity, ListView contractList) {
        this.activity = activity;
        this.contractList = contractList;
    }

    @Override
    protected void onPreExecute(){


    }

    @Override
    protected String doInBackground(String... arg0) {
        try{
            account_id = (String)arg0[0];
            role = (String)arg0[1];
            target = (String)arg0[2];
            status = (String)arg0[3];

            System.out.println("DISPLAY: " + account_id + role + target + status);
            String link = Global.linkDisplayContractList;
            String data  = URLEncoder.encode("account_id", "UTF-8") + "=" + URLEncoder.encode(account_id, "UTF-8");
            data  += "&" + URLEncoder.encode("role", "UTF-8") + "=" + URLEncoder.encode(role, "UTF-8");
            data  += "&" + URLEncoder.encode("target", "UTF-8") + "=" + URLEncoder.encode(target, "UTF-8");
            data  += "&" + URLEncoder.encode("status", "UTF-8") + "=" + URLEncoder.encode(status, "UTF-8");


            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                sb.append(line);
//                break;
            }
            return sb.toString();
        }
        catch(Exception e){
            return new String("Exception on Display: " + e.getMessage());
        }

    }

    @Override
    protected void onPostExecute(String result){
        System.out.println(result);
        try{
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                String contractKey = jsonData.getString("contractKey");
                String sender = jsonData.getString("sender");
                String receiver = jsonData.getString("receiver");
                String subject = jsonData.getString("subject");
                String body = jsonData.getString("body");
                String location = jsonData.getString("location");
                String status = jsonData.getString("status");
                String dateRequest = jsonData.getString("dateRequest");
                String dateAppOrReject = jsonData.getString("dateAppOrReject");
                String video = jsonData.getString("video");
                String reasonForRejection = jsonData.getString("reasonForRejection");
                Contract contract = new Contract(contractKey, sender, receiver, subject, body, location, status, dateRequest, dateAppOrReject, video, reasonForRejection);
                contracts.add(contract);
            }


            populateLVContract();
            registerClickCallBackContract();

        }
        catch (JSONException e){
            e.printStackTrace();
        }




    }

    private void populateLVContract() {
        //build adapter
        ContractAdapter adapter = new ContractAdapter(activity, contracts, role);
        //configure list view
        contractList.setAdapter(adapter);


    }

    private void registerClickCallBackContract() {
        contractList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String msg = "You clicked inbox " + position;
//                Toast.makeText(DisplayContractListActivity.this, msg, Toast.LENGTH_LONG).show();

                Contract contract = contracts.get(position);
                Intent i = null;
                if(contract.getStatus().equals("waiting")){
                    if(role.equals("receiver")){
                        i = new Intent(activity, DisplayContractToBeApprovedActivity.class);
                        i.putExtra("Account", Global.account);
                        i.putExtra("Contract", contract);
                        activity.startActivity(i);

                    }
                    else{
                        i = new Intent(activity, DisplaySentContractActivity.class);
                        i.putExtra("Account", Global.account);
                        i.putExtra("Contract", contract);
                        activity.startActivity(i);

                    }
                }
                else if(contract.getStatus().equals("approved")){
                    i = new Intent(activity, DisplayApprovedContractActivity.class);
                    i.putExtra("Account", Global.account);
                    i.putExtra("Contract", contract);
                    i.putExtra("Role", role);
                    activity.startActivity(i);
                }
                else{
                    i = new Intent(activity, DisplayRejectedContractActivity.class);
                    i.putExtra("Account", Global.account);
                    i.putExtra("Contract", contract);
                    i.putExtra("Role", role);
                    activity.startActivity(i);
                }

            }
        });
    }
}
