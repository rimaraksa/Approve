package com.example.rimaraksa.approve.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.Toast;

import com.example.rimaraksa.approve.DatabaseConnection.Signup;
import com.example.rimaraksa.approve.Global;
import com.example.rimaraksa.approve.Model.Account;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by rimaraksa on 6/9/15.
 */
public class VerifySignature extends AsyncTask<String,Void,String> {
    private Context context;

    private String signature;
    private Bitmap bitmapSignature;

    private File file;

    private ProgressDialog progressDialog;

    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;

    public VerifySignature(Context context, Bitmap bitmapSignature) {
        this.context = context;
        this.bitmapSignature = bitmapSignature;
    }

    protected void onPreExecute(){

        super.onPreExecute();
        progressDialog = ProgressDialog.show(context, "Please Wait", "Processing...");


    }

    @Override
    @SuppressWarnings("deprecation")
    protected String doInBackground(String... arg0) {

        signature = (String) arg0[0];

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(Global.linkVerifySignature);

        try {
            MultipartEntity multipartEntity = new MultipartEntity();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmapSignature.compress(Bitmap.CompressFormat.JPEG, 100, out);
            byte [] byteArray = out.toByteArray();
            String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

//            Parameters to pass
            multipartEntity.addPart("data", new StringBody(encodedImage));
            multipartEntity.addPart("username", new StringBody(Global.account.getUsername()));

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

        try {
            JSONObject jsonData = new JSONObject(result);

            if (jsonData.getBoolean("error")) {
                Toast pass = Toast.makeText(context, "ERROR: " + jsonData.getString("message"), Toast.LENGTH_SHORT);
                pass.show();
            } else {
                if (jsonData.getBoolean("matched")) {
                    Toast pass = Toast.makeText(context, "Signature is matched!", Toast.LENGTH_SHORT);
                    pass.show();
                }
                else{
                    Toast pass = Toast.makeText(context, "Signature is not matched!", Toast.LENGTH_SHORT);
                    pass.show();


                }
            }
        }


        catch (JSONException e){
            e.printStackTrace();
        }

        Intent i = new Intent(context, DisplayActivity.class);
        context.startActivity(i);

        progressDialog.dismiss();

    }


}
