package com.example.rimaraksa.approve.ServerConnection;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.Toast;

import com.example.rimaraksa.approve.Util;
import com.example.rimaraksa.approve.Model.Contract;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by rimaraksa on 6/9/15.
 */
public class VerifySignature extends AsyncTask<String,Void,String> {
    private Context context;
    private Activity activity;
    private Contract contract;
    private String filePath;
    private Bitmap bitmapSignature;

    private ProgressDialog progressDialog;

    public VerifySignature(Context context, Activity activity, Contract contract, Bitmap bitmapSignature) {
        this.context = context;
        this.activity = activity;
        this.contract = contract;
        this.bitmapSignature = bitmapSignature;
    }

    protected void onPreExecute(){
        super.onPreExecute();
        progressDialog = ProgressDialog.show(context, "Please Wait", "Verifying your signature...");

    }

    @Override
    @SuppressWarnings("deprecation")
    protected String doInBackground(String... arg0) {

        filePath = (String) arg0[0];

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(Util.linkVerifySignature);

        try {
            MultipartEntity multipartEntity = new MultipartEntity();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmapSignature.compress(Bitmap.CompressFormat.JPEG, 100, out);
            byte [] byteArray = out.toByteArray();
            String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

//            Parameters to pass
            multipartEntity.addPart("data", new StringBody(encodedImage));
            multipartEntity.addPart("username", new StringBody(Util.getLocalPartFromEmail(Util.account.getUsername())));

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
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        System.out.println("VerifySignature Result: " + result);

        try {
            JSONObject jsonData = new JSONObject(result);

            if (jsonData.getBoolean("error")) {
                Toast pass = Toast.makeText(context, "ERROR: " + jsonData.getString("message"), Toast.LENGTH_SHORT);
                pass.show();
            } else {
                if (jsonData.getBoolean("matched")) {
                    Toast pass = Toast.makeText(context, "Signature is matched!", Toast.LENGTH_SHORT);
                    pass.show();

                    System.out.println("Filepath on SMSOTP: " + filePath);
                    new SMSOTP(context, activity, contract).execute(filePath);

                    System.out.println("ENTER SMSOTP");
//                    new UploadFileToServer(context, activity).execute(contract.getContractKey(), "video", filePath);
                }
                else{
                    Toast pass = Toast.makeText(context, "Signature is not matched! Please record a steady video!", Toast.LENGTH_SHORT);
                    pass.show();

                }
            }
        }


        catch (JSONException e){
            e.printStackTrace();
        }

//        Intent i = new Intent(context, DisplayActivity.class);
//        context.startActivity(i);



    }






}
