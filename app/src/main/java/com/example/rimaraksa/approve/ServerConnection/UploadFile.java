package com.example.rimaraksa.approve.ServerConnection;

/**
 * Created by rimaraksa on 16/6/15.
 */


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;
//import org.apache.http.entity.mime.MultipartEntity;

import com.example.rimaraksa.approve.Activity.DisplayActivity;
import com.example.rimaraksa.approve.Model.Contract;
import com.example.rimaraksa.approve.Util;
import com.example.rimaraksa.approve.Model.Account;
import com.example.rimaraksa.approve.Model.NavDrawerItem;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;


/**
 * Uploading the file to server
 * */
public class UploadFile extends AsyncTask<String,Void,String> {
    private Context context;
    private Activity activity;
    private Account account;
    private Contract contract;

    private String key, fileType, filePath, targetPath;

    private File file;

    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;

    public UploadFile(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

//    Upload for the first time, upload signature
    public UploadFile(Context context, Activity activity, Account account) {
        this.context = context;
        this.activity = activity;
        this.account = account;
    }

//    Upload for contract approval
    public UploadFile(Context context, Activity activity, Contract contract) {
        this.context = context;
        this.activity = activity;
        this.contract = contract;
    }

    protected void onPreExecute(){
        super.onPreExecute();

    }

    @Override
    @SuppressWarnings("deprecation")
    protected String doInBackground(String... arg0) {
        key = (String) arg0[0];
        fileType = (String) arg0[1];
        filePath = (String) arg0[2];

//        Determine file folder
        if(fileType.equals("video")){
            targetPath = "videos/";
        }
        else if(fileType.equals("picture")){
            targetPath = "pictures/";
        }
        else{
            targetPath = "signatures/";
        }

        System.out.println("Target Path: " + targetPath);

        System.out.println("TYPE OF THE FILE TO UPLOAD: " + fileType);

//        Get the file
        file = new File(filePath);

        System.out.println("Filepath on UploadFile: " + filePath);
        System.out.println("File on UploadFile: " + file);

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(Util.linkUploadFileToServer);

        try {
            MultipartEntity multipartEntity = new MultipartEntity();

//            Parameters to pass
            multipartEntity.addPart("data", new FileBody(file));
            multipartEntity.addPart("key", new StringBody(key));
            System.out.println("Key: " + key);
            multipartEntity.addPart("fileType", new StringBody(fileType));
            multipartEntity.addPart("date", new StringBody(Util.getDateTime()));
            multipartEntity.addPart("targetPath", new StringBody(targetPath));

            httppost.setEntity(multipartEntity);

            HttpResponse response = httpclient.execute(httppost);
            HttpEntity httpEntity = response.getEntity();
            String htmlResponse = "NO RESPONSE";

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // Server response
                htmlResponse = EntityUtils.toString(httpEntity);
            }
            else {
                htmlResponse = "Error occurred! Http Status Code: " + statusCode;
            }

            return htmlResponse;

        }
        catch (ClientProtocolException e) {
            return e.toString();
        }
        catch (IOException e) {
            return e.toString();
        }

    }

    @Override
    protected void onPostExecute(String result){
        System.out.println("UploadFileToServer Result: " + result);
        try {
            JSONObject jsonData = new JSONObject(result);

            if (!jsonData.getBoolean("error")) {
                if (fileType.equals("signature")) {
                    new Signup(context, activity).execute(account.getName(), account.getNric(), account.getPhone(), account.getUsername(), account.getPassword(), account.getProfpic(), account.getSignature());
                }
                else if(fileType.equals("video")){
//                    Success to approve a contract
                    new EmailNotification(context, activity, contract).execute();

                    Util.pendingInboxCount--;
                    NavDrawerItem navDrawerItem = (NavDrawerItem) Util.drawerAdapter.getItem(Util.pendingInbox_id);
                    String pendingInboxCount = Integer.toString(Util.pendingInboxCount);
                    navDrawerItem.setCount(pendingInboxCount);

                    Util.approvedInboxCount++;
                    navDrawerItem = (NavDrawerItem) Util.drawerAdapter.getItem(Util.approvedInbox_id);
                    String approvedInboxCount = Integer.toString(Util.approvedInboxCount);
                    navDrawerItem.setCount(approvedInboxCount);

                    Intent i = new Intent(context, DisplayActivity.class);
                    i.putExtra("FromDisplayContractToBeApprovedActivity", true);
                    context.startActivity(i);
                }
            }
            else {
                Toast pass = Toast.makeText(context, "ERROR: " + jsonData.getString("message"), Toast.LENGTH_SHORT);
                pass.show();
                if (!jsonData.getBoolean("exists")) {
                    pass = Toast.makeText(context, "Contract/Account is no longer available!", Toast.LENGTH_SHORT);
                    pass.show();
                }
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }



    }


}
