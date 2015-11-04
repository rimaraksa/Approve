package com.example.rimaraksa.approve.ServerConnection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.rimaraksa.approve.Activity.DisplayActivity;
import com.example.rimaraksa.approve.Model.Contract;
import com.example.rimaraksa.approve.Model.NavDrawerItem;
import com.example.rimaraksa.approve.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by rimaraksa on 15/10/15.
 */
public class EmailNotification extends AsyncTask<String,Void,String> {
    private Context context;
    private Activity activity;
    private Contract contract;
    String contract_id;


    public EmailNotification(Context context, Activity activity, Contract contract) {
        this.context = context;
        this.activity = activity;
        this.contract = contract;
    }

    protected void onPreExecute(){

    }

    @Override
    protected String doInBackground(String... arg0) {
        try{
            contract_id = Integer.toString(contract.getContract_id());
            String link = Util.linkEmailNotification;
            String data  = URLEncoder.encode("contract_id", "UTF-8") + "=" + URLEncoder.encode(contract_id, "UTF-8");

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
            return new String("Exception on Email Notification: " + e.getMessage());
        }

    }

    @Override
    protected void onPostExecute(String result){
        System.out.println("EmailNotification Result: " + result);
        try{
            JSONObject jsonData = new JSONObject(result);

            if(!jsonData.getBoolean("email_succeeds")){
                Toast pass = Toast.makeText(context, "An error occurred while sending an email", Toast.LENGTH_SHORT);
                pass.show();
            }
            else{
                Toast pass = Toast.makeText(context, "Notification email is sent.", Toast.LENGTH_SHORT);
                pass.show();
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }



    }
}
