package com.example.rimaraksa.approve;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.rimaraksa.approve.Adapter.NavDrawerListAdapter;
import com.example.rimaraksa.approve.Model.Account;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by rimaraksa on 11/6/15.
 */
public class Util {
    public static final String DIRECTORY_NAME = "Approve";

//    public static final String link = "http://172.20.33.119/~rimaraksa/android_connect/";
    public static final String link = "http://approve.comuf.com/android_connect/";
//    public static final String link = "http://approve.grn.cc/android_connect/";

//    Account Management
    public static final String linkLogin = link + "login.php";
    public static final String linkSignup = link + "signup.php";
    public static final String linkUpdateAccount = link + "update_account.php";

//    Contract Management
    public static final String linkCreateContract = link + "create_contract.php";
    public static final String linkDisplayContractList = link + "get_contract_list.php";
    public static final String linkRejectContract = link + "reject_contract.php";
    public static final String linkVerifySignature = link + "verify_signature.php";
    public static final String linkSMSOTP = link + "sms_otp.php";
    public static final String linkEmailNotification = link + "email.php";

//    Others
    public static final String linkUploadFileToServer = link + "upload_file.php";

//    Variables ids
    public static final int MEDIA_TYPE_SIGNATURE = 0;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int IMAGE_CROP_REQUEST_CODE = 300;
    public static final int TIMEOUT_CONNECTION = 50000;    //5sec
    public static final int TIMEOUT_SOCKET = 30000;   //30sec

//    Global variables for an account
    public static Account account = null;

//    For drawer
    public static NavDrawerListAdapter drawerAdapter;
    public static Bitmap accountProfpicBitmap = null;
    public static Bitmap accountSignatureBitmap = null;
    public static ImageView drawerProfpic = null;
    public static TextView drawerName;

//    Counters for each contract category
    public static int pendingInboxCount = 0;
    public static int approvedInboxCount = 0;
    public static int rejectedInboxCount = 0;
    public static int pendingOutboxCount = 0;
    public static int approvedOutboxCount = 0;
    public static int rejectedOutboxCount = 0;

//    Ids of each item in the drawer
    public static int profile_id = 0;
    public static int inbox_id = 1;
    public static int pendingInbox_id = 2;
    public static int approvedInbox_id = 3;
    public static int rejectedInbox_id = 4;
    public static int outbox_id = 5;
    public static int pendingOutbox_id = 6;
    public static int approvedOutbox_id = 7;
    public static int rejectedOutbox_id = 8;


//    Time Management
    public static String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getYearMonthWeek() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ww", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getYearMonthDay() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getTimeDetailToDisplayFromDateTime(String dateTime) {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy, h:mm a");

        try{
            String reformattedDate = dateFormat.format(dateTimeFormat.parse(dateTime));
            return reformattedDate;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return dateTime;
    }

    public static String getDateFromDateTime(String dateTime){
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yyyy");

        try{
            String reformattedDate = dateFormat.format(dateTimeFormat.parse(dateTime));
            return reformattedDate;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return dateTime;
    }

    public static String getDayOfTheWeekFromDateTime(String dateTime){
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE h:mm a");

        try{
            String reformattedDate = dateFormat.format(dateTimeFormat.parse(dateTime));
            return reformattedDate;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return dateTime;
    }

    public static String getTimeFromDateTime(String dateTime){
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");

        try{
            String reformattedDate = dateFormat.format(dateTimeFormat.parse(dateTime));
            return reformattedDate;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return dateTime;
    }

    public static boolean isDateThisWeek(String dateTime){
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ww");

        try{
            String yearMonthWeekOfDateTime = dateFormat.format(dateTimeFormat.parse(dateTime));
            return yearMonthWeekOfDateTime.equals(Util.getYearMonthWeek());
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public static boolean isDateToday(String dateTime){
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try{
            String yearMonthDayOfDateTime = dateFormat.format(dateTimeFormat.parse(dateTime));
            return yearMonthDayOfDateTime.equals(Util.getYearMonthDay());
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return false;
    }


//    File Management
    public static File getOutputMediaFile(int type, String key) {

        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Util.DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(Util.DIRECTORY_NAME, "Oops! Failed to create! " + Util.DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;

        if (type == Util.MEDIA_TYPE_VIDEO) {
            System.out.println("mediatypevideo: " + key);
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + key + "_" + timeStamp + ".mp4");
        }
        else if(type == Util.MEDIA_TYPE_IMAGE){
            System.out.println("mediatypeimage: " + key);
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + getLocalPartFromEmail(key) + "_" + timeStamp + ".jpg");
        }
        else if(type == Util.MEDIA_TYPE_SIGNATURE){
            System.out.println("mediatypesignature: " + key);
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "SIG_" + getLocalPartFromEmail(key) + "_" + timeStamp + ".jpg");
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
        return Uri.fromFile(Util.getOutputMediaFile(type, key));
    }


    public static void previewVideo(VideoView vidPreview, String filePath) {
        vidPreview.setVisibility(View.VISIBLE);
        vidPreview.setVideoPath(filePath);
        // start playing
        vidPreview.start();
    }

    //    Location Management
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


    public static String latLongToCountry(Context context, String location) throws IOException {
        String country = null;

        String[] stringArray = location.split(";");
        String latitude = stringArray[0];
        String longitude = stringArray[1];
        double lati = Double.parseDouble(latitude);
        double longi = Double.parseDouble(longitude);


        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(lati, longi, 1);

        try{
            country = addresses.get(0).getCountryName();
        }
        catch(Exception e){
            e.printStackTrace();
        }

        String address = (country == null)?"":country;

        if(address.equals("")){
            address = "Undetected location";
        }

        return address;
    }

    public static void setProfpicOnDisplay(String filePath){
        File file = new File(filePath);

//        Set the profpic of the account
        Util.account.setProfpic(file.getName());
    }

    public static File bitmapToFile(String name, Bitmap bitmap){
        File file = new File(name);

        return file;
    }

    public static Bitmap stringToBitmap(String encodedString){
        try{
            byte [] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }
        catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    public static Bitmap getVideoFrame(String filePath) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            return retriever.getFrameAtTime();
        } catch (RuntimeException ex) {
            ex.printStackTrace();
        }
        finally {
            try {
                retriever.release();
            }
            catch (RuntimeException ignored) {
            }
        }
        return null;
    }

    public static void loginError(Activity activity, int errorId){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        final View view = activity.getLayoutInflater().inflate(R.layout.dialog_error, null);
        alertDialog.setView(view);

        TextView tvErrorTitle = (TextView) view.findViewById(R.id.TVErrorTitle);
        TextView tvError = (TextView) view.findViewById(R.id.TVError);
        Button bOk = (Button) view.findViewById(R.id.BOk);


        switch (errorId) {
            case 0:
                tvErrorTitle.setText("Login Error");
                tvError.setText("Please fill in all fields to login.");
                break;
            case 1:
                tvErrorTitle.setText("Oops! Login Failed");
                tvError.setText("Please check that your account information was entered correctly.");
                bOk.setText("Try Again");
                break;
            case 2:
                tvErrorTitle.setText("Connection Error");
                tvError.setText("While loggin you in, there was an error connecting to the server.");
                bOk.setText("Try Again");
                break;
            default:
        }

        final AlertDialog ad = alertDialog.show();

        bOk.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
//                Upload a profile picture from camera/album
                ad.dismiss();
            }
        });
    }

    public static void signupError(Activity activity, int errorId){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        final View view = activity.getLayoutInflater().inflate(R.layout.dialog_error, null);
        alertDialog.setView(view);

        TextView tvErrorTitle = (TextView) view.findViewById(R.id.TVErrorTitle);
        TextView tvError = (TextView) view.findViewById(R.id.TVError);
        Button bOk = (Button) view.findViewById(R.id.BOk);


        switch (errorId) {
            case 0:
                tvErrorTitle.setText("Signup Error");
                tvError.setText("Please fill in all fields to signup.");
                break;
            case 1:
                tvErrorTitle.setText("Oops! Signup Failed");
                tvError.setText("The new password and password confirmation do not match.");
                bOk.setText("Try Again");
                break;
            case 2:
                tvErrorTitle.setText("Oops! Signup Failed");
                tvError.setText("Your password must contain at least 8 characters and is a mix of letters and numbers.");
                bOk.setText("Try Again");
                break;
            case 3:
                tvErrorTitle.setText("Oops! Signup Failed");
                tvError.setText("Please take your portrait as your signature to signup.");
                break;
            case 4:
                tvErrorTitle.setText("Connection Error");
                tvError.setText("While signing you up, there was an error connecting to the server.");
                bOk.setText("Try Again");
                break;
            default:
        }

        final AlertDialog ad = alertDialog.show();

        bOk.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
//                Upload a profile picture from camera/album
                ad.dismiss();
            }
        });
    }

    public static void cameraError(Activity activity, int errorId){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        final View view = activity.getLayoutInflater().inflate(R.layout.dialog_error, null);
        alertDialog.setView(view);

        TextView tvErrorTitle = (TextView) view.findViewById(R.id.TVErrorTitle);
        TextView tvError = (TextView) view.findViewById(R.id.TVError);
        Button bOk = (Button) view.findViewById(R.id.BOk);


        switch (errorId) {
            case 0:
                tvErrorTitle.setText("Unsupported Device");
                tvError.setText("Your device doesn't support camera.");
                break;
            default:
        }

        final AlertDialog ad = alertDialog.show();

        bOk.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
//                Upload a profile picture from camera/album
                ad.dismiss();
            }
        });
    }

    public static void changePasswordError(Activity activity, int errorId){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        final View view = activity.getLayoutInflater().inflate(R.layout.dialog_change_password_error, null);
        alertDialog.setView(view);

        TextView tvError = (TextView) view.findViewById(R.id.TVError);
        Button bOk = (Button) view.findViewById(R.id.BOk);


        switch (errorId) {
            case 0:
                tvError.setText("Please fill in all fields to change your password.");
                break;
            case 1:
                tvError.setText("The password you've entered under 'Current Password' is incorrect.");
                break;
            case 2:
                tvError.setText("The new password and password confirmation do not match.");
                break;
            case 3:
                tvError.setText("Your password must contain at least 8 characters and is a mix of letters and numbers.");
                break;
            default:
                tvError.setText("To sign in into your Approve account securely, please type a good, solid password to yourself.");
        }

        final AlertDialog ad = alertDialog.show();

        bOk.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
//                Upload a profile picture from camera/album
                ad.dismiss();
            }
        });
    }

    public static void rejectError(Activity activity, int errorId){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        final View view = activity.getLayoutInflater().inflate(R.layout.dialog_reject_error, null);
        alertDialog.setView(view);

        TextView tvErrorTitle = (TextView) view.findViewById(R.id.TVErrorTitle);
        TextView tvError = (TextView) view.findViewById(R.id.TVError);
        Button bOk = (Button) view.findViewById(R.id.BOk);


        switch (errorId) {
            case 0:
                tvError.setText("Please fill in all fields to reject the contract.");
                break;
            case 1:
                tvErrorTitle.setText("Oops! Reject Contract Failed");
                tvError.setText("The contract is not available anymore.");
                break;
            default:
                tvErrorTitle.setText("Oops! Reject Contract Failed");
                tvError.setText("While rejecting the contract, there was an error connecting to the server.");
        }

        final AlertDialog ad = alertDialog.show();

        bOk.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
//                Upload a profile picture from camera/album
                ad.dismiss();
            }
        });
    }

    public static void inputOTPError(Activity activity, int errorId){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        final View view = activity.getLayoutInflater().inflate(R.layout.dialog_input_otp_error, null);
        alertDialog.setView(view);

        TextView tvErrorTitle = (TextView) view.findViewById(R.id.TVErrorTitle);
        TextView tvError = (TextView) view.findViewById(R.id.TVError);
        Button bOk = (Button) view.findViewById(R.id.BOk);


        switch (errorId) {
            case 0:
                tvError.setText("Please fill in all fields to approve the contract.");
                break;
            case 1:
                tvErrorTitle.setText("Oops! Approve Contract Failed");
                tvError.setText("Your password is incorrect.");
                break;
            default:
                tvErrorTitle.setText("Oops! Approve Contract Failed");
                tvError.setText("While approving the contract, there was an error connecting to the server.");
        }

        final AlertDialog ad = alertDialog.show();

        bOk.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
//                Upload a profile picture from camera/album
                ad.dismiss();
            }
        });
    }

    public static String generateOTP() {
        String chars = "0123456789";
        final int PW_LENGTH = 6;
        Random rnd = new SecureRandom();
        StringBuilder pass = new StringBuilder();

        for (int i = 0; i < PW_LENGTH; i++){
            pass.append(chars.charAt(rnd.nextInt(chars.length())));
        }

        return pass.toString();
    }

    public static void inputPasswordError(Activity activity, int errorId){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        final View view = activity.getLayoutInflater().inflate(R.layout.dialog_input_password_error, null);
        alertDialog.setView(view);

        TextView tvErrorTitle = (TextView) view.findViewById(R.id.TVErrorTitle);
        TextView tvError = (TextView) view.findViewById(R.id.TVError);
        Button bOk = (Button) view.findViewById(R.id.BOk);


        switch (errorId) {
            case 0:
                tvError.setText("Please fill in all fields to update your account.");
                break;
            case 1:
                tvErrorTitle.setText("Oops! Update Account Failed");
                tvError.setText("Your password is incorrect.");
                break;
            default:
                tvErrorTitle.setText("Oops! Update Account Failed");
                tvError.setText("While updating your credentials, there was an error connecting to the server.");
        }

        final AlertDialog ad = alertDialog.show();

        bOk.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
//                Upload a profile picture from camera/album
                ad.dismiss();
            }
        });
    }

    public static void changePhoneError(Activity activity, int errorId){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        final View view = activity.getLayoutInflater().inflate(R.layout.dialog_change_phone_error, null);
        alertDialog.setView(view);

        TextView tvErrorTitle = (TextView) view.findViewById(R.id.TVErrorTitle);
        TextView tvError = (TextView) view.findViewById(R.id.TVError);
        Button bOk = (Button) view.findViewById(R.id.BOk);


        switch (errorId) {
            case 0:
                tvError.setText("Please fill in all fields to change your mobile phone number.");
                break;
            default:
                tvErrorTitle.setText("Oops! Change Phone Failed");
                tvError.setText("While changing your phone number, there was an error connecting to the server.");
        }

        final AlertDialog ad = alertDialog.show();

        bOk.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
//                Upload a profile picture from camera/album
                ad.dismiss();
            }
        });
    }

    public static void createContractError(Activity activity, int errorId){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        final View view = activity.getLayoutInflater().inflate(R.layout.dialog_create_contract_error, null);
        alertDialog.setView(view);

        TextView tvError = (TextView) view.findViewById(R.id.TVError);
        Button bOk = (Button) view.findViewById(R.id.BOk);


        switch (errorId) {
            case 0:
                tvError.setText("Please fill in all fields to create a new contract.");
                break;
            case 1:
                tvError.setText("You cannot send a contract to yourself.");
                break;
            case 2:
                tvError.setText("The receiver does not exist, please make sure the receiver's username is correct.");
                break;
            default:
                tvError.setText("While sending the contract, there was an error connecting to the server.");
        }

        final AlertDialog ad = alertDialog.show();

        bOk.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
//                Upload a profile picture from camera/album
                ad.dismiss();
            }
        });
    }

    public static String getEditedPhoneNumber(String phone)
    {
        String editedPhoneNumber = "";
        for(int i = 0; i < phone.length(); i++){
            char c = phone.charAt(i);
            if((c >= '0' && c <= '9') || c == '+' || c == ' '){
                editedPhoneNumber += c;
            }
        }
        return editedPhoneNumber;

    }

    public static String getLocalPartFromEmail(String email){
        String localPart = "";
        if(email != null){
            for(int i = 0; i < email.length() && email.charAt(i) != '@'; i++){
                localPart += email.charAt(i);
            }
        }

        return localPart;
    }

    public static boolean isPasswordValid(String password){
        boolean hasDigit = false;
        boolean hasLetter = false;

        if(password.length() < 8){
            return false;
        }
        else{
            for(int i = 0; i < password.length(); i++){
                if(!hasDigit && Character.isDigit(password.charAt(i))){
                    hasDigit = true;
                }
                else if(!hasLetter && Character.isLetter(password.charAt(i))){
                    hasLetter = true;
                }
                if(hasDigit && hasLetter){
                    return true;
                }

            }
        }

        return false;
    }




}
