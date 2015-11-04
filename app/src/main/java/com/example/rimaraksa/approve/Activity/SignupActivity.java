package com.example.rimaraksa.approve.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rimaraksa.approve.Adapter.CountryCodeAdapter;
import com.example.rimaraksa.approve.ServerConnection.UploadFile;
import com.example.rimaraksa.approve.Util;
import com.example.rimaraksa.approve.Model.Account;
import com.example.rimaraksa.approve.Model.Country;
import com.example.rimaraksa.approve.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by rimaraksa on 25/5/15.
 */
public class SignupActivity extends ActionBarActivity  {

//    DatabaseHelper helper = new DatabaseHelper(this);

    private Activity activity;
    private Toolbar mToolbar;
    String name, nric, phone, username, password, confirmPassword, profpic, signature, filePath;

//    Components on layout
    private ImageView ivSignature;
    private EditText tfName, tfNric, tfPhone, tfUsername, tfPassword1, tfPassword2;
    private Button bCountryCode, bSignup;
//    private TextView tvLoginLink;

    private EditText tfSearchCountry;
    private ListView lvCountryCode;
    private CountryCodeAdapter adapter;


    private ArrayList<Country> countryList = new ArrayList<Country>();
    private AlertDialog adChangeCountryCode;

    private Uri fileUri;

    private boolean signatureRegistrationDone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        activity = this;

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

//        Setting own font for logo
        String fontPath = "fonts/Coquette Bold.ttf";
        TextView tvLogo = (TextView) findViewById(R.id.TVLogo);
        Typeface typeface = Typeface.createFromAsset(getAssets(), fontPath);
        tvLogo.setTypeface(typeface);

        ivSignature = (ImageView) findViewById(R.id.IVSignature);
        tfName = (EditText) findViewById(R.id.TFName);
        tfNric = (EditText) findViewById(R.id.TFNRIC);
        tfPhone = (EditText) findViewById(R.id.TFPhone);
        tfUsername = (EditText) findViewById(R.id.TFUsername);
        tfPassword1 = (EditText) findViewById(R.id.TFPassword1);
        tfPassword2 = (EditText) findViewById(R.id.TFPassword2);
//        tvLoginLink = (TextView) findViewById(R.id.TVLoginLink);

        bCountryCode = (Button) findViewById(R.id.BCountryCode);
        bSignup = (Button) findViewById(R.id.BSignup);

        ivSignature.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!isDeviceSupportCamera()) {
                    Util.cameraError(activity, 0);
                }
                else {
                    captureImage();
                }
            }
        });

//        Signup the account
        bSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = tfName.getText().toString();
                nric = tfNric.getText().toString();
                phone = tfPhone.getText().toString();
                username = tfUsername.getText().toString();
                password = tfPassword1.getText().toString();
                confirmPassword = tfPassword2.getText().toString();

                if (name.equals("") || nric.equals("") || phone.equals("") || username.equals("") || password.equals("") || confirmPassword.equals("")) {
                    Util.signupError(activity, 0);
                }
                else if (!password.equals(confirmPassword)) {
                    //popup message
                    Util.signupError(activity, 1);
                }
                else if(!Util.isPasswordValid(password)){
                    Util.signupError(activity, 2);
                }
                else if(!signatureRegistrationDone){
                    Util.signupError(activity, 3);
                }
                else {
                    String editedPhone = Util.getEditedPhoneNumber(bCountryCode.getText() + " " + phone);
                    phone = editedPhone;
                    launchSignupActivity();



//                    if (!isDeviceSupportCamera()) {
//                        Global.cameraError(activity, 0);
//                    } else {
//                        captureImage();
//                    }

                }
            }
        });

        bCountryCode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                popCountryCodeDialog();
            }
        });

////        Listening to login link
//        tvLoginLink.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
////                Switching to Signup screen
//                Intent i = new Intent(SignupActivity.this, LoginActivity.class);
//                startActivity(i);
//            }
//        });




    }



//    For capturing signature

    private void captureImage(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, Util.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == Util.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
                fileUri = data.getData();
                performCrop();

            }
            else if(requestCode == Util.IMAGE_CROP_REQUEST_CODE){
//                launchSignupActivity();

                Bundle extras = data.getExtras();
//                Get the cropped bitmap
                Bitmap bitmap = extras.getParcelable("data");
//                Display the returned cropped image
                ivSignature.setImageBitmap(bitmap);
                signatureRegistrationDone = true;
            }
        }
        else if (resultCode == RESULT_CANCELED) {
            // User cancelled signature registration
//            Toast.makeText(getApplicationContext(), "Signature registration is cancelled.", Toast.LENGTH_SHORT).show();
        }


    }

    private void launchSignupActivity(){
        String filePath = fileUri.getPath();
//        if (filePath != null) {
            File file = new File(filePath);
            profpic = signature = file.getName();
            Account account = new Account(name, nric, phone, username, password, profpic, signature);

            new UploadFile(this, activity, account).execute(username, "signature", filePath);



//            new Signup(this).execute(name, nric, phone, email, username, password, filePath);
//        }
//        else{
//            Global.signupError(activity, 2);
//            Toast pass = Toast.makeText(SignupActivity.this, "Self-picture for signature has not been completed!", Toast.LENGTH_SHORT);
//            pass.show();
//        }
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
            fileUri = Util.getOutputMediaFileUri(Util.MEDIA_TYPE_SIGNATURE, username);
//            To store the image
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//            Start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, Util.IMAGE_CROP_REQUEST_CODE);

        }
        catch(ActivityNotFoundException anfe){
//            Display an error message
            Toast.makeText(getApplicationContext(), "Sorry, your device doesn't support the crop action!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                                    // Navigate up to the closest parent
                            .startActivities();
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.

                    String activityToPass = "FromSignupActivity";
                    upIntent.putExtra(activityToPass, true);
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void popCountryCodeDialog(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        final View view = getLayoutInflater().inflate(R.layout.dialog_country_code, null);
        alertDialog.setView(view);


        lvCountryCode = (ListView) view.findViewById(R.id.LVCountryCode);

        populateLVCountryCode(view);
        registerClickCallBackCountryCode();



        // Showing Alert Message
        adChangeCountryCode = alertDialog.show();

    }

    private void populateLVCountryCode(View view) {
        //build adapter

        String[] countryNames = getResources().getStringArray(R.array.country_names);
        String[] countryCodes = getResources().getStringArray(R.array.country_codes);
        String[] countryPhoneCodes = getResources().getStringArray(R.array.country_phone_codes);

        for (int i = 0; i < countryCodes.length; i++)
        {
            Country country = new Country(countryCodes[i], countryPhoneCodes[i], countryNames[i]);
            // Binds all strings into an array
            countryList.add(country);
        }

//        CountryCodeAdapter adapter = new CountryCodeAdapter(getApplicationContext(), activity, countryNameList, countryCodeList, countryCodePhoneList);

        adapter = new CountryCodeAdapter(getApplicationContext(), activity, countryList);

        //configure list view
        lvCountryCode.setAdapter(adapter);

        tfSearchCountry = (EditText) view.findViewById(R.id.TFSearchCountry);

        tfSearchCountry.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = tfSearchCountry.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
            int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
            int arg3) {
                // TODO Auto-generated method stub
            }
        });

    }

    private void registerClickCallBackCountryCode() {
        lvCountryCode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adChangeCountryCode.dismiss();
                String countryCode = countryList.get(position).getCountryCode() + " " + countryList.get(position).getCountryPhoneCode();
                bCountryCode.setText(countryCode);
                countryList.clear();
            }

        });
    }
}
