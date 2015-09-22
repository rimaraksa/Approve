package com.example.rimaraksa.approve.DatabaseConnection;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rimaraksa.approve.Global;
import com.example.rimaraksa.approve.Model.Contract;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by rimaraksa on 25/6/15.
 */
public class DownloadVideo extends AsyncTask<String,Void,String> {
    private Context context;
    private Contract contract;
    private ImageButton ibDownload;
    private TextView tvDownload;
    private String targetPath, fileName;
    private Uri fileUri;

    public DownloadVideo(Context context, Contract contract, ImageButton ibDownload, TextView tvDownload) {
        this.context = context;
        this.contract = contract;
        this.ibDownload = ibDownload;
        this.tvDownload = tvDownload;
    }

    protected void onPreExecute(){

    }

    @Override
    protected String doInBackground(String... arg0) {

        targetPath = "videos/";
        fileName = contract.getVideo();

        try{
            String link = Global.link + targetPath + fileName;


            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            //Determine how long it takes for the app to realize a connection problem
            conn.setReadTimeout(Global.TIMEOUT_CONNECTION);
            conn.setConnectTimeout(Global.TIMEOUT_SOCKET);

            //Create the file
            File file;
            file = Global.getOutputMediaFile(Global.MEDIA_TYPE_VIDEO, contract.getContractKey());

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




    @Override
    protected void onPostExecute(String result){
        System.out.println("DownloadFile Result: " + result);
        if(result.equals("success")){

            Intent intent = new Intent(Intent.ACTION_VIEW, fileUri);
            intent.setDataAndType(fileUri, "video/mp4");
            context.startActivity(intent);

        }
        else{
            Toast pass = Toast.makeText(context, "Failed downloading the file!", Toast.LENGTH_SHORT);
            pass.show();

        }
        ibDownload.setVisibility(View.VISIBLE);
        tvDownload.setTypeface(null, Typeface.NORMAL);


    }



}
