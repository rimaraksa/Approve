package com.example.rimaraksa.approve.DatabaseConnection;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.rimaraksa.approve.Activity.DisplayActivity;
import com.example.rimaraksa.approve.Global;
import com.example.rimaraksa.approve.Model.Account;
import com.example.rimaraksa.approve.Model.Contract;

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
    private Account account;
    private Contract contract;
    String reasonForRejection;


    public RejectContract(Context context, Account account, Contract contract) {
        this.context = context;
        this.account = account;
        this.contract = contract;
    }

    protected void onPreExecute(){

    }

    @Override
    protected String doInBackground(String... arg0) {
        try{
            reasonForRejection = (String)arg0[0];

            String link = Global.linkRejectContract;
            String data  = URLEncoder.encode("contractKey", "UTF-8") + "=" + URLEncoder.encode(contract.getContractKey(), "UTF-8");
            data  += "&" + URLEncoder.encode("reasonForRejection", "UTF-8") + "=" + URLEncoder.encode(reasonForRejection, "UTF-8");
            data  += "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(Global.getDateTime(), "UTF-8");


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
        System.out.println(result);
        try{
            JSONObject jsonData = new JSONObject(result);

            if(!jsonData.getBoolean("exists")){
                Toast pass = Toast.makeText(context, "Contract is not available!", Toast.LENGTH_SHORT);
                pass.show();
            }
            else{
                Toast pass = Toast.makeText(context, contract.getSubject() + " is successfully rejected!", Toast.LENGTH_SHORT);
                pass.show();
            }

            Intent i = new Intent(context, DisplayActivity.class);
            i.putExtra("Account", account);
            context.startActivity(i);
        }
        catch (JSONException e){
            e.printStackTrace();
        }



    }
}
