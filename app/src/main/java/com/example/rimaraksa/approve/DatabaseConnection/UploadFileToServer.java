package com.example.rimaraksa.approve.DatabaseConnection;

/**
 * Created by rimaraksa on 16/6/15.
 */


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;
//import org.apache.http.entity.mime.MultipartEntity;

import com.example.rimaraksa.approve.Global;
import com.example.rimaraksa.approve.Model.Account;

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
public class UploadFileToServer extends AsyncTask<String,Void,String> {
    private Context context;
    private Account account;

    private String key, fileType, filePath, targetPath;

    private File file;

    private ProgressDialog progressDialog;

    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;

    public UploadFileToServer(Context context) {
        this.context = context;
    }

//    Upload for the first time, upload signature
    public UploadFileToServer(Context context, Account account) {
        this.context = context;
        this.account = account;
    }

    protected void onPreExecute(){

        super.onPreExecute();
        progressDialog = ProgressDialog.show(context, "Please Wait", "Processing...");


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

        System.out.println("TYPE OF THE FILE TO UPLOAD = " + fileType);

//        Get the file
        file = new File(filePath);

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(Global.linkUploadFileToServer);

        try {
            MultipartEntity multipartEntity = new MultipartEntity();

//            Parameters to pass
            multipartEntity.addPart("data", new FileBody(file));
            multipartEntity.addPart("key", new StringBody(key));
            System.out.println("Key: " + key);
            multipartEntity.addPart("fileType", new StringBody(fileType));
            multipartEntity.addPart("date", new StringBody(Global.getDateTime()));
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
        System.out.println(result);

        try{
            JSONObject jsonData = new JSONObject(result);

            if(fileType.equals("signature")){
                new Signup(context).execute(account.getName(), account.getNric(), account.getPhone(), account.getEmail(), account.getUsername(), account.getPassword(), account.getProfpic(), account.getSignature());
            }
            else{
                if(jsonData.getBoolean("error")){
                    Toast pass = Toast.makeText(context, "ERROR: " + jsonData.getString("message"), Toast.LENGTH_SHORT);
                    pass.show();
                }
                else if(!jsonData.getBoolean("exists")){
                    Toast pass = Toast.makeText(context, "Contract/Account is no longer available!", Toast.LENGTH_SHORT);
                    pass.show();
                }
            }

        }
        catch (JSONException e){
            e.printStackTrace();
        }

        progressDialog.dismiss();

    }


}
