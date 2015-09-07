package com.example.rimaraksa.approve;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import com.example.rimaraksa.approve.Model.Account;
import com.example.rimaraksa.approve.Model.Contract;
import com.example.rimaraksa.approve.Model.NavDrawerItem;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by rimaraksa on 11/6/15.
 */
public class Global {
    public static final String link = "http://172.20.33.119/~rimaraksa/android_connect/";
    public static final String linkLogin = link + "login.php";
    public static final String linkSignup = link + "signup.php";
    public static final String linkCreateContract = link + "create_contract.php";
    public static final String linkDisplayContractList = link + "get_contract_list.php";
    public static final String linkUploadVideo = link + "upload_video.php";
    public static final String linkUploadImage = link + "upload_image.php";
    public static final String linkUploadFileToServer = link + "upload_file.php";
    public static final String linkRejectContract = link + "reject_contract.php";
    public static final String linkDownloadFile = link + "download_file.php";
    public static final String linkVerifySignature = link + "verify_signature.php";

    public static final String DIRECTORY_NAME = "Approve";

    public static final int MEDIA_TYPE_SIGNATURE = 0;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int IMAGE_CROP_REQUEST_CODE = 300;
    public static final int SPEECH_RECOGNITION_REQUEST_CODE = 400;

    public static final int TIMEOUT_CONNECTION = 5000;    //5sec
    public static final int TIMEOUT_SOCKET = 30000;   //30sec

    public static boolean firstTimeLogin = true;
    public static Account account = null;
    public static Bitmap accountProfpicBitmap = null;
    public static Bitmap accountSignature = null;
    public Contract contract = null;
    public static ArrayList<Contract> contracts = null;

    public  static ImageView drawerProfpic = null;

    public static void setContracts(ArrayList<Contract> contractList){
        contracts = contractList;
    }

    public ArrayList<Contract> getContracts(){
        return contracts;
    }

    public static String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static File getOutputMediaFile(int type, String key) {

        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),Global.DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(Global.DIRECTORY_NAME, "Oops! Failed to create! " + Global.DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;

        if (type == Global.MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + key + "_" + timeStamp + ".mp4");
        }
        else if(type == Global.MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + key + "_" + timeStamp + ".jpg");
        }
        else if(type == Global.MEDIA_TYPE_SIGNATURE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "SIG_" + key + "_" + timeStamp + ".jpg");
        }
        else{
            return null;
        }

        return mediaFile;
    }

    /**
     * Creating file uri to store image/video
     */
    public static Uri getOutputMediaFileUri(int type, String key) {
        return Uri.fromFile(Global.getOutputMediaFile(type, key));
    }

    public static void previewVideo(VideoView vidPreview, String filePath) {
        vidPreview.setVisibility(View.VISIBLE);
        vidPreview.setVideoPath(filePath);
        // start playing
        vidPreview.start();
    }

    public static String latLongToCity(Context context, String location) throws IOException {
        String city = null;
        String postalCode = null;
        String country = null;

        String[] stringArray = location.split(";");
        String latitude = stringArray[0];
        String longitude = stringArray[1];
        double lati = Double.parseDouble(latitude);
        double longi = Double.parseDouble(longitude);


        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(lati, longi, 1);
        try{
            city = addresses.get(0).getAdminArea();
        }
        catch(Exception e){
            e.printStackTrace();
        }

        try{
            postalCode = addresses.get(0).getPostalCode();
        }
        catch(Exception e){
            e.printStackTrace();
        }

        try{
            country = addresses.get(0).getCountryName();
        }
        catch(Exception e){
            e.printStackTrace();
        }

        String address = "" + ((city == null)?"":(city + ", ")) + ((country == null)?"":(country + " ")) + ((postalCode == null)?"":postalCode);

        if(address.equals("")){
            address = "Undetected location";
        }

        return address;
    }

    public static void setProfpicOnDisplay(String filePath){
        File file = new File(filePath);

//        Set the profpic of the account
        Global.account.setProfpic(file.getName());
    }

    public static File bitmapToFile(String name, Bitmap bitmap){
        File file = new File(name);

        return file;
    }

}
