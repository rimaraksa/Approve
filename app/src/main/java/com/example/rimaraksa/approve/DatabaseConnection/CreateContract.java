package com.example.rimaraksa.approve.DatabaseConnection;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.rimaraksa.approve.Activity.DisplayActivity;
import com.example.rimaraksa.approve.Global;
import com.example.rimaraksa.approve.Model.Account;

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
    private Account account;
    private String contractKey, sender, receiver, subject, body, location, status, dateRequest;

    public CreateContract(Context context, Account account) {
        this.context = context;
        this.account = account;
    }

    protected void onPreExecute(){

    }

    @Override
    protected String doInBackground(String... arg0) {
        try{
            contractKey = (String)arg0[0];
            sender = (String)arg0[1];
            receiver = (String)arg0[2];
            subject = (String)arg0[3];
            body = (String)arg0[4];
            location = (String)arg0[5];
            status = (String)arg0[6];
            dateRequest = (String)arg0[7];

            System.out.println(contractKey + " + " + sender + " + " + receiver + " + " + subject + " + " + body + " + " + location + " + " + status + " + " + dateRequest);

            String link = Global.linkCreateContract;
            String data  = URLEncoder.encode("contractKey", "UTF-8") + "=" + URLEncoder.encode(contractKey, "UTF-8");
            data  += "&" + URLEncoder.encode("sender", "UTF-8") + "=" + URLEncoder.encode(sender, "UTF-8");
            data  += "&" + URLEncoder.encode("receiver", "UTF-8") + "=" + URLEncoder.encode(receiver, "UTF-8");
            data  += "&" + URLEncoder.encode("subject", "UTF-8") + "=" + URLEncoder.encode(subject, "UTF-8");
            data  += "&" + URLEncoder.encode("body", "UTF-8") + "=" + URLEncoder.encode(body, "UTF-8");
            data  += "&" + URLEncoder.encode("location", "UTF-8") + "=" + URLEncoder.encode(location, "UTF-8");
            data += "&" + URLEncoder.encode("status", "UTF-8") + "=" + URLEncoder.encode(status, "UTF-8");
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
        System.out.println("result: " + result);
        try{
            JSONObject jsonData = new JSONObject(result);

            if(!jsonData.getBoolean("receiver_exists")){


                Toast temp = Toast.makeText(context, "Receiver's username does not exist!", Toast.LENGTH_SHORT);
                temp.show();
            }
            else{
                if(!jsonData.getBoolean("contractKey_available")){
                    Toast temp = Toast.makeText(context, "Error during contract creation! Try again!", Toast.LENGTH_SHORT);
                    temp.show();
                    System.out.println(contractKey);
                }
                else{
                    Intent i = new Intent(context, DisplayActivity.class);

                    i.putExtra("Account", account);

                    context.startActivity(i);
                }
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }



    }
}
