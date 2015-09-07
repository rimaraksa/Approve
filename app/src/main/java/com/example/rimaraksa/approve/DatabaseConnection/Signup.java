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
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by rimaraksa on 11/6/15.
 */
public class Signup extends AsyncTask<String,Void,String> {
    private Context context;
    int account_id;
    String name, nric, phone, email, username, password, profpic, signature, profpicSignature;


    public Signup(Context context) {
        this.context = context;
    }

    protected void onPreExecute(){

    }

    @Override
    protected String doInBackground(String... arg0) {
        try{
            name = (String)arg0[0];
            nric = (String)arg0[1];
            phone = (String)arg0[2];
            email = (String)arg0[3];
            username = (String)arg0[4];
            password = (String)arg0[5];
            profpic = (String)arg0[6];
            signature = (String)arg0[7];

            String link = Global.linkSignup;
            String data  = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
            data  += "&" + URLEncoder.encode("nric", "UTF-8") + "=" + URLEncoder.encode(nric, "UTF-8");
            data  += "&" + URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8");
            data  += "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
            data  += "&" + URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
            data += "&" + URLEncoder.encode("profpic", "UTF-8") + "=" + URLEncoder.encode(profpic, "UTF-8");
            data += "&" + URLEncoder.encode("signature", "UTF-8") + "=" + URLEncoder.encode(signature, "UTF-8");


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
            return new String("Exception on Signup: " + e.getMessage());
        }

    }

    @Override
    protected void onPostExecute(String result){
        System.out.println(result);
        try{
            JSONObject jsonData = new JSONObject(result);

            if(jsonData.getBoolean("exists")){
                Toast pass = Toast.makeText(context, "Username is already registered!", Toast.LENGTH_SHORT);
                pass.show();
            }
            else if(jsonData.getBoolean("signatureUploadFails")){
                Toast pass = Toast.makeText(context, "Failed contacting server, please try again later.", Toast.LENGTH_SHORT);
                pass.show();
            }
            else{
                new Login(context).execute(username, password);
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }



    }
}
