package com.example.rimaraksa.approve.ServerConnection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.rimaraksa.approve.Activity.DisplayActivity;
import com.example.rimaraksa.approve.Util;
import com.example.rimaraksa.approve.Model.Contract;
import com.example.rimaraksa.approve.Model.NavDrawerItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by rimaraksa on 20/6/15.
 */
public class RejectContract extends AsyncTask<String,Void,String> {
    private Context context;
    private Activity activity;
    private Contract contract;
    String contract_id, reasonForRejection;


    public RejectContract(Context context, Activity activity, Contract contract) {
        this.context = context;
        this.activity = activity;
        this.contract = contract;
    }

    protected void onPreExecute(){

    }

    @Override
    protected String doInBackground(String... arg0) {
        try{
            reasonForRejection = (String)arg0[0];

            contract_id = Integer.toString(contract.getContract_id());
            String link = Util.linkRejectContract;
            String data  = URLEncoder.encode("contract_id", "UTF-8") + "=" + URLEncoder.encode(contract_id, "UTF-8");
            data  += "&" + URLEncoder.encode("reasonForRejection", "UTF-8") + "=" + URLEncoder.encode(reasonForRejection, "UTF-8");
            data  += "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(Util.getDateTime(), "UTF-8");


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
            return new String("Exception on Reject Contract: " + e.getMessage());
        }

    }

    @Override
    protected void onPostExecute(String result){
        System.out.println("RejectContract Result: " + result);
        try{
            JSONObject jsonData = new JSONObject(result);

            if(!jsonData.getBoolean("contract_exists")){
                Util.rejectError(activity, 1);
//                Toast pass = Toast.makeText(context, "Contract is not available!", Toast.LENGTH_SHORT);
//                pass.show();
            }
            else{
                Util.pendingInboxCount--;
                NavDrawerItem navDrawerItem = (NavDrawerItem) Util.drawerAdapter.getItem(Util.pendingInbox_id);
                String pendingInboxCount = Integer.toString(Util.pendingInboxCount);
                navDrawerItem.setCount(pendingInboxCount);

                Util.rejectedInboxCount++;
                navDrawerItem = (NavDrawerItem) Util.drawerAdapter.getItem(Util.rejectedInbox_id);
                String rejectedInboxCount = Integer.toString(Util.rejectedInboxCount);
                navDrawerItem.setCount(rejectedInboxCount);

                new EmailNotification(context, activity, contract).execute();

                Intent i = new Intent(context, DisplayActivity.class);
                i.putExtra("FromDisplayContractToBeApprovedActivity", true);
                context.startActivity(i);


            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }



    }
}
