package com.example.rimaraksa.approve.DatabaseConnection;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.rimaraksa.approve.Activity.DisplayActivity;
import com.example.rimaraksa.approve.Activity.PlayVideoActivity;
import com.example.rimaraksa.approve.Global;
import com.example.rimaraksa.approve.Model.Account;
import com.example.rimaraksa.approve.R;

/**
 * Created by rimaraksa on 11/6/15.
 */
public class Login extends AsyncTask<String,Void,String> {
    private Context context;
    private String username;
    private String password;


    private String targetPath, fileName;
    private int type;

    private Uri fileUri;


    public Login(Context context) {
        this.context = context;
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
        System.out.println(result);

        try{
            JSONObject jsonData = new JSONObject(result);

            if(jsonData.getInt("success") == 1){

                int account_id = jsonData.getInt("account_id");
                String name = jsonData.getString("name");
                String nric = jsonData.getString("nric");
                String phone = jsonData.getString("phone");
                String email = jsonData.getString("email");
                String profpic = jsonData.getString("profpic");
                String signature = jsonData.getString("signature");


                Account account = new Account(name, nric, phone, email, username, password, profpic, signature);
                account.setAccount_id(account_id);

                Global.account = account;

                new DownloadFile(context, account).execute("picture");
                new DownloadFile(context, account).execute("signature");
            }
            else{
                Toast temp = Toast.makeText(context, "Incorrect username or password!", Toast.LENGTH_SHORT);
                temp.show();
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        System.out.println("ASYNC FINISHED 1");


    }



    private String downloadProfpic(){
        try{
            targetPath = "pictures/";
            fileName = Global.account.getProfpic();
            type = 1;

            String link = Global.link + targetPath + fileName;

            System.out.println("LINK 1: " + link);

            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            //Determine how long it takes for the app to realize a connection problem
            conn.setReadTimeout(Global.TIMEOUT_CONNECTION);
            conn.setConnectTimeout(Global.TIMEOUT_SOCKET);

            //Create the file
            File file = Global.getOutputMediaFile(type, Global.account.getUsername());

            System.out.println("LINK 2: " + link);

            System.out.println("FILE: " + file);

            fileUri = fileUri.fromFile(file);

            System.out.println("LINK 3: " + link);
            System.out.println("fileUri: " + link);
            //Define InputStreams to read from the URLConnection.
            InputStream is = conn.getInputStream();
            System.out.println("LINK 4: " + link);
            BufferedInputStream inStream = new BufferedInputStream(is, 1024 * 5);
            System.out.println("LINK 5: " + link);
            FileOutputStream outStream = new FileOutputStream(file);
            System.out.println("LINK 6: " + link);
            byte[] buff = new byte[5 * 1024];

            //Read bytes (and store them) until there is nothing more to read(-1)
            int len;
            while ((len = inStream.read(buff)) != -1)
            {
                outStream.write(buff,0,len);
            }

            //Clean up
            outStream.flush();
            outStream.close();
            inStream.close();

        }
        catch(MalformedURLException e){
            return new String("Exception on Login 2: " + e.getMessage());
        }
        catch(IOException e){
            return new String("Exception on Login 3: " + e.getMessage());
        }
        return "success";
    }

    private void prepareAccount(){

        String result = downloadProfpic();

        System.out.println("RESULT: " + result);

        if(result.equals("success")){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
            Global.accountProfpicBitmap = bitmap;
        }
        else{
            Toast pass = Toast.makeText(context, "Failed downloading the file!", Toast.LENGTH_SHORT);
            pass.show();
        }


    }
}
