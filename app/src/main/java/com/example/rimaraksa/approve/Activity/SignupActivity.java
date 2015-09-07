package com.example.rimaraksa.approve.Activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rimaraksa.approve.DatabaseConnection.Login;
import com.example.rimaraksa.approve.DatabaseConnection.UploadFileToServer;
import com.example.rimaraksa.approve.Global;
import com.example.rimaraksa.approve.Model.Account;
import com.example.rimaraksa.approve.R;
import com.example.rimaraksa.approve.DatabaseConnection.Signup;

import java.io.File;

/**
 * Created by rimaraksa on 25/5/15.
 */
public class SignupActivity extends Activity {

//    DatabaseHelper helper = new DatabaseHelper(this);

    String name, nric, phone, email, username, password, password2, profpic, signature;

//    Components on layout
    private EditText nameField, nricField, phoneField, emailField, usernameField, passwordField, password2Field;
    private Button signupButton;
    private TextView tvLoginLink;


    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        nameField = (EditText) findViewById(R.id.TFName);
        nricField = (EditText) findViewById(R.id.TFNRIC);
        phoneField = (EditText) findViewById(R.id.TFPhone);
        emailField = (EditText) findViewById(R.id.TFEmail);
        usernameField = (EditText) findViewById(R.id.TFUsernameSU);
        passwordField = (EditText) findViewById(R.id.TFPassword1);
        password2Field = (EditText) findViewById(R.id.TFPassword2);
        tvLoginLink = (TextView) findViewById(R.id.TVLoginLink);
        signupButton = (Button) findViewById(R.id.BSignup);

//        Signup the account
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameField.getText().toString();
                nric = nricField.getText().toString();
                phone = phoneField.getText().toString();
                email = emailField.getText().toString();
                username = usernameField.getText().toString();
                password = passwordField.getText().toString();
                password2 = password2Field.getText().toString();

                if(!password.equals(password2)){
                    //popup message
                    Toast pass = Toast.makeText(SignupActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT);
                    pass.show();
                }
                else if(name.equals("") || nric.equals("") || phone.equals("") || email.equals("") || username.equals("") || password.equals("")){
                    Toast pass = Toast.makeText(SignupActivity.this, "Required fields have not been completed!", Toast.LENGTH_SHORT);
                    pass.show();
                }
                else{

                    if (!isDeviceSupportCamera()) {
                        Toast.makeText(getApplicationContext(), "Sorry! Your device doesn't support camera", Toast.LENGTH_LONG).show();
                    }
                    else {
                        System.out.println("Capture Image");
                        captureImage();
                    }

                }
            }
        });
//        Listening to login link
        tvLoginLink.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                Switching to Signup screen
                Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });


    }

//    For capturing signature

    private void captureImage(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, Global.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == Global.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
                fileUri = data.getData();
                performCrop();

            }
            else if(requestCode == Global.IMAGE_CROP_REQUEST_CODE){
                launchSignupActivity();
            }
        }
        else if (resultCode == RESULT_CANCELED) {
            // User cancelled signature registration
            Toast.makeText(getApplicationContext(), "Signature registration is cancelled!", Toast.LENGTH_SHORT).show();
        }


    }

    private void launchSignupActivity(){
        String filePath = fileUri.getPath();
        if (filePath != null) {
            File file = new File(filePath);
            profpic = signature = file.getName();
            Account account = new Account(name, nric, phone, email, username, password, profpic, signature);
            new UploadFileToServer(this, account).execute(username, "signature", filePath);
//            new Signup(this).execute(name, nric, phone, email, username, password, filePath);
        }
        else{
            Toast pass = Toast.makeText(SignupActivity.this, "Self-picture for signature has not been completed!", Toast.LENGTH_SHORT);
            pass.show();
        }
    }

    private boolean isDeviceSupportCamera() {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // This device has a camera
            return true;
        } else {
            // This device does not have a camera
            return false;
        }
    }

    private void performCrop(){
//        Use try-catch in case it doesn't support crop operation
        try {
//            Call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
//            Indicate image type and Uri
            cropIntent.setDataAndType(fileUri, "image/*");
//            Set crop properties
            cropIntent.putExtra("crop", "true");
//            Indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
//            Indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
//            Retrieve data on return
            cropIntent.putExtra("return-data", true);
//            Get the uri
            fileUri = Global.getOutputMediaFileUri(Global.MEDIA_TYPE_SIGNATURE, username);
//            To store the image
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//            Start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, Global.IMAGE_CROP_REQUEST_CODE);

        }
        catch(ActivityNotFoundException anfe){
//            Display an error message
            Toast.makeText(getApplicationContext(), "Sorry, your device doesn't support the crop action!", Toast.LENGTH_SHORT).show();
        }
    }
//    public void onSignUpClick (View v){
//        if(v.getId() == R.id.BSignupSU){
//            EditText nameField = (EditText) findViewById(R.id.TFName);
//            EditText nricField = (EditText) findViewById(R.id.TFNRIC);
//            EditText phoneField = (EditText) findViewById(R.id.TFPhone);
//            EditText emailField = (EditText) findViewById(R.id.TFEmail);
//            EditText usernameField = (EditText) findViewById(R.id.TFUsernameSU);
//            EditText passwordField = (EditText) findViewById(R.id.TFPassword1);
//            EditText password2Field = (EditText) findViewById(R.id.TFPassword2);
//
//            String name = nameField.getText().toString();
//            String nric = nricField.getText().toString();
//            String phone = phoneField.getText().toString();
//            String email = emailField.getText().toString();
//            String username = usernameField.getText().toString();
//            String password = passwordField.getText().toString();
//            String password2 = password2Field.getText().toString();
//
//            if(!password.equals(password2)){
//                //popup message
//                Toast pass = Toast.makeText(SignupActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT);
//                pass.show();
//            }
//            else if(name.equals("") || nric.equals("") || phone.equals("") || email.equals("") || username.equals("") || password.equals("")){
//                Toast pass = Toast.makeText(SignupActivity.this, "Required fields have not been completed!", Toast.LENGTH_SHORT);
//                pass.show();
//            }
//            else{
//                //insert new account
////                Account account = new Account(name, nric, phone, email, username, password, null);
////                a.setName(name);
////                a.setNric(nric);
////                a.setPhone(phone);
////                a.setEmail(email);
////                a.setUsername(username);
////                a.setPassword(password);
//
////                helper.insertAccount(account);
//
//                new Signup(this).execute(name, nric, phone, email, username, password);
//
//
////                Intent i = new Intent(SignUpActivity.this, DisplayActivity.class);
////                i.putExtra("Account", account);
////                i.putExtra("Name", account.getName());
////                i.putExtra("Username", account.getUsername());
////                startActivity(i);
//            }
//
//        }
//    }
}
