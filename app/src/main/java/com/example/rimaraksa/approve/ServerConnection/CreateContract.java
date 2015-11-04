package com.example.rimaraksa.approve.ServerConnection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.rimaraksa.approve.Activity.DisplayActivity;
import com.example.rimaraksa.approve.Util;
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
 * Created by rimaraksa on 12/6/15.
 */
public class CreateContract extends AsyncTask<String,Void,String> {
    private Context context;
    private Activity activity;
    private String contractKey, sender_id, receiverUsername, contractSubject, contractBody, location, contractStatus, dateRequest;

    public CreateContract(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    protected void onPreExecute(){

    }

    @Override
    protected String doInBackground(String... arg0) {
        try{
            contractKey = (String)arg0[0];
            receiverUsername = (String)arg0[1];
            contractSubject = (String)arg0[2];
            contractBody = (String)arg0[3];
            location = (String)arg0[4];
            contractStatus = (String)arg0[5];
            dateRequest = (String)arg0[6];

            sender_id = Integer.toString(Util.account.getAccount_id());

            String link = Util.linkCreateContract;
            String data  = URLEncoder.encode("contractKey", "UTF-8") + "=" + URLEncoder.encode(contractKey, "UTF-8");
            data  += "&" + URLEncoder.encode("sender_id", "UTF-8") + "=" + URLEncoder.encode(sender_id, "UTF-8");
            data  += "&" + URLEncoder.encode("receiverUsername", "UTF-8") + "=" + URLEncoder.encode(receiverUsername, "UTF-8");
            data  += "&" + URLEncoder.encode("contractSubject", "UTF-8") + "=" + URLEncoder.encode(contractSubject, "UTF-8");
            data  += "&" + URLEncoder.encode("contractBody", "UTF-8") + "=" + URLEncoder.encode(contractBody, "UTF-8");
            data  += "&" + URLEncoder.encode("location", "UTF-8") + "=" + URLEncoder.encode(location, "UTF-8");
            data += "&" + URLEncoder.encode("contractStatus", "UTF-8") + "=" + URLEncoder.encode(contractStatus, "UTF-8");
            data += "&" + URLEncoder.encode("dateRequest", "UTF-8") + "=" + URLEncoder.encode(dateRequest, "UTF-8");

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
                System.out.println("TEST line: " + line);
//                break;
            }
            return sb.toString();
        }
        catch(Exception e){
            return new String("Exception on Create Contract: " + e.getMessage());
        }

    }

    @Override
    protected void onPostExecute(String result){
        System.out.println("CreateContract Result: " + result);
        try{
            JSONObject jsonData = new JSONObject(result);

            if(!jsonData.getBoolean("receiver_exists")){
                Util.createContractError(activity, 2);
            }
            else{
                if(!jsonData.getBoolean("success")){
                    Toast temp = Toast.makeText(context, "Error during contract creation! Try again!", Toast.LENGTH_SHORT);
                    temp.show();
                    System.out.println("CreateContract contractKey: " + contractKey);
                }
                else{
                    Util.pendingOutboxCount++;
                    NavDrawerItem navDrawerItem = (NavDrawerItem) Util.drawerAdapter.getItem(Util.pendingOutbox_id);
                    String pendingOutboxCount = Integer.toString(Util.pendingOutboxCount);
                    navDrawerItem.setCount(pendingOutboxCount);
                    Intent i = new Intent(context, DisplayActivity.class);
                    context.startActivity(i);
                }
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }



    }
}
