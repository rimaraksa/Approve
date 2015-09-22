package com.example.rimaraksa.approve.DatabaseConnection;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.rimaraksa.approve.Global;
import com.example.rimaraksa.approve.Model.Account;
import com.example.rimaraksa.approve.Model.Contract;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by rimaraksa on 8/9/15.
 */
public class GetProfilePictureBitmap extends AsyncTask<String,Void,String> {
    private Context context;
    private Account account;
    private Contract contract;
    private String fileType, mediaFile, targetPath, fileName;
    private int type;

    private Uri fileUri;



    public GetProfilePictureBitmap() {
//        this.context = context;
    }

    protected void onPreExecute(){

    }

    @Override
    protected String doInBackground(String... arg0) {
        try{
            Bitmap bitmap = null;
            targetPath = (String) arg0[0];
            fileName = (String) arg0[1];

            String link = Global.link + targetPath + fileName;

            try{
                URL url = new URL(link);
                URLConnection conn = url.openConnection();
                System.out.println("buffString1 ");

                //Determine how long it takes for the app to realize a connection problem
                conn.setReadTimeout(Global.TIMEOUT_CONNECTION);
                conn.setConnectTimeout(Global.TIMEOUT_SOCKET);

                System.out.println("buffString2 ");

                //Define InputStreams to read from the URLConnection.
                System.out.println(conn);
                System.out.println(conn.getInputStream());
                InputStream is = conn.getInputStream();
                System.out.println("buffString3 ");
                BufferedInputStream inStream = new BufferedInputStream(is, 1024 * 5);
                System.out.println("buffString4 ");
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                System.out.println("buffString5 ");
                byte[] buff = new byte[5 * 1024];

                System.out.println("buffString6 ");

                int len;
                while ((len = inStream.read(buff)) != -1)
                {
                    outStream.write(buff,0,len);
                }

                String buffString = buff.toString();
                System.out.println("buffString: " + buffString);

                //Clean up
                outStream.flush();
                outStream.close();
                inStream.close();

                return buffString;
            }
            catch(Exception e){
                System.out.println("Exception on Global getFileStringFromServer: " + e.getMessage());
                return null;

            }

        }
        catch(Exception e){

            System.out.println("Exception on Get Profile Picture Bitmap 2: " + e.getMessage());
            return null;
        }
    }
}
