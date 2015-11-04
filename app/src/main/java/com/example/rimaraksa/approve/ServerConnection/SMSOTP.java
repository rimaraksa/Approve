package com.example.rimaraksa.approve.ServerConnection;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rimaraksa.approve.Util;
import com.example.rimaraksa.approve.Model.Contract;
import com.example.rimaraksa.approve.R;

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
public class SMSOTP extends AsyncTask<String,Void,String> {
    private Context context;
    private Activity activity;
    private Contract contract;
    private String filePath, otp;

    Button bCancel, bConfirm;
    EditText tfConfirmOTP;

    public SMSOTP(Context context, Activity activity, Contract contract) {
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
        try{
            filePath = (String)arg0[0];
            otp = Util.generateOTP();
            System.out.println("OTP: " + otp);
            String link = Util.linkSMSOTP;
            String data  = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(Util.account.getName(), "UTF-8");
            data += "&" + URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(Util.account.getPhone(), "UTF-8");
            data += "&" + URLEncoder.encode("otp", "UTF-8") + "=" + URLEncoder.encode(otp, "UTF-8");

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
            return new String("Exception on SMS OTP 1: " + e.getMessage());
        }

    }

    @Override
    protected void onPostExecute(String result){
        System.out.println("SMSOTP Result: " + result);

        try {
            JSONObject jsonData = new JSONObject(result);

            if (!jsonData.getBoolean("smsOTPSuccess")) {
                Toast pass = Toast.makeText(context, "ERROR: " + jsonData.getString("message"), Toast.LENGTH_SHORT);
                pass.show();
            }
            else {
                popOTPDialog();
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void popOTPDialog(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        final View view = activity.getLayoutInflater().inflate(R.layout.dialog_input_otp, null);
        alertDialog.setView(view);

        bCancel = (Button) view.findViewById(R.id.BCancel);
        bConfirm = (Button) view.findViewById(R.id.BConfirm);
        tfConfirmOTP = (EditText) view.findViewById(R.id.TFConfirmOTP);

        // Showing Alert Message
        final AlertDialog ad = alertDialog.show();

        bCancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
//                Upload a profile picture from camera/album
                ad.dismiss();
            }
        });

        bConfirm.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
//                Upload a profile picture from camera/album
                String confirmOTP;

                confirmOTP = tfConfirmOTP.getText().toString();

                if(confirmOTP.equals("")){
                    Util.inputOTPError(activity, 0);
                }
                else if (!confirmOTP.equals(otp)) {
                    Util.inputOTPError(activity, 1);

                }
                else {
                    ad.dismiss();
                    System.out.println("Filepath on SMSOTP: " + filePath);
                    new UploadFile(context, activity, contract).execute(contract.getContractKey(), "video", filePath);
                }
            }
        });
    }

}
