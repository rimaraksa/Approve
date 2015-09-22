package com.example.rimaraksa.approve.DatabaseConnection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.example.rimaraksa.approve.Activity.DisplayActivity;
import com.example.rimaraksa.approve.Activity.LoginActivity;
import com.example.rimaraksa.approve.Global;
import com.example.rimaraksa.approve.Model.Account;
import com.example.rimaraksa.approve.R;

/**
 * Created by rimaraksa on 11/6/15.
 */
public class Login extends AsyncTask<String,Void,String> {

    private Context context;
    private Activity activity;
    private String username;
    private String password;


    private String targetPath, fileName;
    private int type;

    private Uri fileUri;


    public Login(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    protected void onPreExecute(){
    }

    @Override
    protected String doInBackground(String... arg0) {
        try{
            username = (String)arg0[0];
            password = (String)arg0[1];

            String link = Global.linkLogin;
            String data  = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

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
            return new String("Exception on Login 1: " + e.getMessage());
        }

    }

    @Override
    protected void onPostExecute(String result){
        System.out.println("Login Result: " + result);

        try{
            JSONObject jsonData = new JSONObject(result);

            if(jsonData.getInt("success") == 1){

                int account_id = jsonData.getInt("account_id");
                String name = jsonData.getString("name");
                String nric = jsonData.getString("nric");
                String phone = jsonData.getString("phone");
                String profpic = jsonData.getString("profpic");
                String signature = jsonData.getString("signature");


                Account account = new Account(name, nric, phone, username, password, profpic, signature);
                account.setAccount_id(account_id);

                Global.account = account;

                String encodedProfpic = jsonData.getString("encodedProfpic");
                Global.accountProfpicBitmap = Global.stringToBitmap(encodedProfpic);

                String encodedSignature = jsonData.getString("encodedSignature");
                Global.accountSignatureBitmap = Global.stringToBitmap(encodedSignature);

                Global.waitingInboxCount = jsonData.getInt("waitingInbox");
                Global.approvedInboxCount = jsonData.getInt("approvedInbox");
                Global.rejectedInboxCount = jsonData.getInt("rejectedInbox");

                Global.waitingOutboxCount = jsonData.getInt("waitingOutbox");
                Global.approvedOutboxCount = jsonData.getInt("approvedOutbox");
                Global.rejectedOutboxCount = jsonData.getInt("rejectedOutbox");

                Intent i = new Intent(context, DisplayActivity.class);
                context.startActivity(i);
            }
            else{
                Global.loginError(activity, 1);
//                Toast temp = Toast.makeText(context, "Incorrect username or password!", Toast.LENGTH_SHORT);
//                temp.show();
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        System.out.println("ASYNC FINISHED 1");


    }


}
