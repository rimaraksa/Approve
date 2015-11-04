package com.example.rimaraksa.approve.ServerConnection;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.rimaraksa.approve.Util;
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
 * Created by rimaraksa on 17/9/15.
 */
public class UpdateAccount extends AsyncTask<String,Void,String> {
    private Context context;
    private Activity activity;
    private Account account;

    private String column, content;

    public UpdateAccount(Context context, Activity activity, Account account) {
        this.context = context;
        this.activity = activity;
        this.account = account;
    }

    protected void onPreExecute(){

    }

    @Override
    protected String doInBackground(String... arg0) {
        try{
            column = (String)arg0[0];
            content = (String)arg0[1];

            String link = Util.linkUpdateAccount;
            String data  = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(Util.account.getUsername(), "UTF-8");
            data  += "&" + URLEncoder.encode("column", "UTF-8") + "=" + URLEncoder.encode(column, "UTF-8");
            data  += "&" + URLEncoder.encode("content", "UTF-8") + "=" + URLEncoder.encode(content, "UTF-8");

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
            return new String("Exception on Update Account: " + e.getMessage());
        }

    }

    @Override
    protected void onPostExecute(String result) {
        System.out.println("UpdateAccount Result: " + result);
        try {
            JSONObject jsonData = new JSONObject(result);

            if (!jsonData.getBoolean("exists")) {

                Toast temp = Toast.makeText(context, "Account does not exist.", Toast.LENGTH_SHORT);
                temp.show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
