package com.example.rimaraksa.approve.DatabaseConnection;

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
import com.example.rimaraksa.approve.Model.Contract;
import com.example.rimaraksa.approve.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by rimaraksa on 25/6/15.
 */
public class DownloadFile extends AsyncTask<String,Void,String> {
    private Context context;
    private Account account;
    private Contract contract;
    private String fileType, mediaFile, targetPath, fileName;
    private int type;

    private Uri fileUri;



    public DownloadFile(Context context, Account account) {
        this.context = context;
        this.account = account;
    }

    public DownloadFile(Context context, Account account, Contract contract) {
        this.context = context;
        this.account = account;
        this.contract = contract;
    }

    protected void onPreExecute(){

    }

    @Override
    protected String doInBackground(String... arg0) {
        try{
            fileType = (String) arg0[0];

            if(fileType.equals("video")){
                targetPath = "videos/";
                fileName = contract.getVideo();
                type = 2;
            }
            else if(fileType.equals("picture")){
                targetPath = "pictures/";
                fileName = account.getProfpic();
                type = 1;
            }
            else{
                targetPath = "signatures/";
                fileName = account.getSignature();
                type = 0;
            }

            if(type == 2 || (type == 1 && !Global.account.getProfpic().equals("null")) || type == 0)
            {
                try{
                    String link = Global.link + targetPath + fileName;


                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    //Determine how long it takes for the app to realize a connection problem
                    conn.setReadTimeout(Global.TIMEOUT_CONNECTION);
                    conn.setConnectTimeout(Global.TIMEOUT_SOCKET);

                    //Create the file
                    File file;
                    if(type == 1 || type == 0){
                        file = Global.getOutputMediaFile(type, account.getUsername());
                    }
                    else{
                        file = Global.getOutputMediaFile(type, contract.getContractKey());
                    }

                    fileUri = fileUri.fromFile(file);

                    //Define InputStreams to read from the URLConnection.
                    InputStream is = conn.getInputStream();
                    BufferedInputStream inStream = new BufferedInputStream(is, 1024 * 5);
                    FileOutputStream outStream = new FileOutputStream(file);
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

                    return "success";
                }
                catch(Exception e){
                    return new String("Exception on Download File 1: " + e.getMessage());
                }
            }

        }
        catch(Exception e){
            return new String("Exception on Download File 2: " + e.getMessage());
        }
        return "no success";
    }

    @Override
    protected void onPostExecute(String result){
        System.out.println(result);

        if(result.equals("success")){
            if(type == 2){
                Intent i = new Intent(context, PlayVideoActivity.class);
                i.putExtra("Account", account);
                i.putExtra("FilePath", fileUri.getPath());
                context.startActivity(i);
            }
            else if(type == 1){
                Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath());
                Global.accountProfpicBitmap = bitmap;
                Intent i = new Intent(context, DisplayActivity.class);
                context.startActivity(i);
            }
            else{
                Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath());
                Global.accountSignature = bitmap;
            }

        }
        else{
            if(type == 2){
                Toast pass = Toast.makeText(context, "Failed downloading the file!", Toast.LENGTH_SHORT);
                pass.show();
            }
            else{
                Intent i = new Intent(context, DisplayActivity.class);
                context.startActivity(i);
            }
        }


    }



}
