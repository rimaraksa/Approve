package com.example.rimaraksa.approve.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rimaraksa.approve.Activity.DisplayActivity;
import com.example.rimaraksa.approve.Activity.LoginActivity;
import com.example.rimaraksa.approve.Adapter.CountryCodeAdapter;
import com.example.rimaraksa.approve.ServerConnection.UpdateAccount;
import com.example.rimaraksa.approve.ServerConnection.UploadFile;
import com.example.rimaraksa.approve.Util;
import com.example.rimaraksa.approve.Model.Account;
import com.example.rimaraksa.approve.Model.Country;
import com.example.rimaraksa.approve.R;

import java.util.ArrayList;
import java.util.Locale;

public class ProfileFragment extends Fragment {

    private DisplayActivity activity;
    private Account account = Util.account;
    private Uri fileUri;

    //        Components on layout
//    private TextView tvName, tvNRIC, tvPhone, tvUsername, tvProfileLink;
    private Button bSetProfilePicture;
    private ImageView ivProfile, ivProfileExpanded;

    private EditText tfName, tfNRIC, tfPhone, tfUsername, tfPassword;

    private ImageButton ibLogout;
    private TextView tvLogout;


//    For Change Phone Alert Dialog
    private AlertDialog adChangePassword, adInputPassword, adChangePhone, adChangeCountryCode;
    private ArrayList<Country> countryList = new ArrayList<Country>();
    private CountryCodeAdapter adapter;
    private ListView lvCountryCode;
    private Button bCountryCode;


    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        activity = (DisplayActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ivProfile = (ImageView) view.findViewById((R.id.IVProfile));
        tfName = (EditText) view.findViewById(R.id.TFName);
        tfUsername = (EditText) view.findViewById(R.id.TFUsername);
        tfPassword = (EditText) view.findViewById(R.id.TFPassword);
        tfNRIC = (EditText) view.findViewById(R.id.TFNRIC);
        tfPhone = (EditText) view.findViewById(R.id.TFPhone);
        bSetProfilePicture = (Button) view.findViewById(R.id.BSetProfilePicture);
        ibLogout = (ImageButton) view.findViewById(R.id.IBLogout);
        tvLogout = (TextView) view.findViewById(R.id.TVLogout);

        ivProfile.setImageBitmap(Util.accountProfpicBitmap);
        tfName.setText(account.getName());
        tfUsername.setText(account.getUsername());
        tfPassword.setText(account.getPassword());
        tfNRIC.setText(account.getNric());
        tfPhone.setText(account.getPhone());

        bSetProfilePicture.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
//                Upload a profile picture from camera/album
                bSetProfilePicture.setTypeface(null, Typeface.BOLD);
                if (!isDeviceSupportCamera()) {
                    Toast.makeText(getActivity(), "Sorry! Your device doesn't support camera", Toast.LENGTH_LONG).show();
                    bSetProfilePicture.setTypeface(null, Typeface.NORMAL);
                } else {

                    System.out.println("Capture Image");
                    captureImage();
                }

            }
        });

        tfName.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String newName = tfName.getText().toString();
                    if (!newName.equals(Util.account.getName())) {
                        Util.account.setName(newName);
                        Util.drawerName.setText(newName);


                        new UpdateAccount(getActivity().getApplicationContext(), getActivity(), account).execute("name", newName);
                    }
                }
            }
        });

        tfPassword.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                popChangePasswordDialog();

            }
        });

        tfPhone.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                popInputPasswordDialog();

            }
        });

        ibLogout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
//                Upload a profile picture from camera/album
                ibLogout.setVisibility(View.INVISIBLE);
                tvLogout.setTypeface(null, Typeface.BOLD);

                logout();

            }
        });


        tfNRIC.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                /* When focus is lost check that the text field
                * has valid values.
                */
                if (!hasFocus) {
                    String newNRIC = tfNRIC.getText().toString();
                    if (!newNRIC.equals(Util.account.getNric())) {
                        Util.account.setNric(newNRIC);
                        new UpdateAccount(getActivity().getApplicationContext(), getActivity(), account).execute("nric", newNRIC);
                    }
                }
            }
        });


        String fontPath = "fonts/Coquette Bold.ttf";
        TextView tvLogo = (TextView) view.findViewById(R.id.TVLogo);
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), fontPath);
        tvLogo.setTypeface(typeface);


        return view;
    }


    private void popChangePasswordDialog(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        final View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_change_password, null);
        alertDialog.setView(view);

        final Button bCancel = (Button) view.findViewById(R.id.BCancel);
        final Button bChange = (Button) view.findViewById(R.id.BChange);
        final EditText tfCurrentPassword = (EditText) view.findViewById(R.id.TFCurrentPassword);
        final EditText tfNewPassword1 = (EditText) view.findViewById(R.id.TFNewPassword1);
        final EditText tfNewPassword2 = (EditText) view.findViewById(R.id.TFNewPassword2);

        // Showing Alert Message
        adChangePassword = alertDialog.show();

        bCancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                adChangePassword.dismiss();
            }
        });

        bChange.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String currentPassword, newPassword1, newPassword2;

                currentPassword = tfCurrentPassword.getText().toString();
                newPassword1 = tfNewPassword1.getText().toString();
                newPassword2 = tfNewPassword2.getText().toString();

                if (currentPassword.equals("") || newPassword1.equals("") || newPassword2.equals("")) {
                    Util.changePasswordError(activity, 0);
                }
                else if (!currentPassword.equals(Util.account.getPassword())) {
                    Util.changePasswordError(activity, 1);
                }
                else if (!newPassword1.equals(newPassword2)) {
                    Util.changePasswordError(activity, 2);
                }
                else if(!Util.isPasswordValid(newPassword1)){
                    Util.changePasswordError(activity, 3);
                }
                else {
                    new UpdateAccount(getActivity().getApplicationContext(), getActivity(), account).execute("password", newPassword1);
                    tfPassword.setText(newPassword1);
                    adChangePassword.dismiss();
                }
            }
        });


    }

    private void popInputPasswordDialog(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        final View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_input_password, null);
        alertDialog.setView(view);

        final Button bCancel = (Button) view.findViewById(R.id.BCancel);
        final Button bConfirm = (Button) view.findViewById(R.id.BConfirm);
        final EditText tfPassword = (EditText) view.findViewById(R.id.TFPassword);

        // Showing Alert Message
        adInputPassword = alertDialog.show();

        bCancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                adInputPassword.dismiss();
            }
        });

        bConfirm.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String password;

                password = tfPassword.getText().toString();

                if(password.equals("")){
                    Util.inputPasswordError(activity, 0);
                }
                else if (!password.equals(Util.account.getPassword())) {
                    Util.inputPasswordError(activity, 1);

                }
                else {
                    adInputPassword.dismiss();
                    popChangePhoneDialog();
                }
            }
        });


    }

    private void popChangePhoneDialog(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        final View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_change_phone, null);
        alertDialog.setView(view);

        bCountryCode = (Button) view.findViewById(R.id.BCountryCode);
        final Button bCancel = (Button) view.findViewById(R.id.BCancel);
        final Button bChange = (Button) view.findViewById(R.id.BChange);
        final EditText tfPhoneDialog = (EditText) view.findViewById(R.id.TFPhone);

        // Showing Alert Message
        adChangePhone = alertDialog.show();

        bCountryCode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                popCountryCodeDialog();
            }
        });

        bCancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                adChangePhone.dismiss();
            }
        });

        bChange.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String phone;

                phone = tfPhoneDialog.getText().toString();

                if (phone.equals("")) {
                    Util.changePhoneError(activity, 0);
                }
                else {
                    String editedPhone = Util.getEditedPhoneNumber(bCountryCode.getText() + " " + phone);
                    new UpdateAccount(getActivity().getApplicationContext(), getActivity(), account).execute("phone", editedPhone);
                    tfPhone.setText(editedPhone);
                    adChangePhone.dismiss();
                }
            }
        });


    }

    private void popCountryCodeDialog(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        final View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_country_code, null);
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

        System.out.println("countrycodes length: " + countryCodes.length);

        for (int i = 0; i < countryCodes.length; i++)
        {

            Country country = new Country(countryCodes[i], countryPhoneCodes[i], countryNames[i]);
            // Binds all strings into an array
            countryList.add(country);
        }

        System.out.println("countrycodes, countryList: " + countryCodes.length + ", " + countryList.size());

        adapter = new CountryCodeAdapter(activity.getApplicationContext(), activity, countryList);

        //configure list view
        lvCountryCode.setAdapter(adapter);

        final EditText tfSearchCountry = (EditText) view.findViewById(R.id.TFSearchCountry);

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
                System.out.println("Size: " + countryList.size());
                String countryCode = countryList.get(position).getCountryCode() + " " + countryList.get(position).getCountryPhoneCode();
                System.out.println("countryCode: " + countryCode);
                bCountryCode.setText(countryCode);
                countryList.clear();
            }

        });
    }


    private void logout(){
        Intent i = new Intent(getActivity(), LoginActivity.class);
        startActivity(i);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * Checking device has a camera
     */
    private boolean isDeviceSupportCamera() {
        if (getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
//            Has a camera
            return true;
        } else {
//            Does not have a camera
            return false;
        }
    }

    private void onSetProfilePicture(View v){
        if(v.getId() == R.id.BSetProfilePicture) {
            bSetProfilePicture.setTypeface(null, Typeface.BOLD);
            if (!isDeviceSupportCamera()) {
                    Toast.makeText(getActivity(), "Sorry! Your device doesn't support camera", Toast.LENGTH_LONG).show();
                }
                else {
                    System.out.println("Capture Image");
                    captureImage();
                }
        }
        bSetProfilePicture.setTypeface(null, Typeface.NORMAL);
    }

    private void captureImage(){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, Util.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == Util.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
                fileUri = data.getData();
                performCrop();
            }
            else if(requestCode == Util.IMAGE_CROP_REQUEST_CODE){
//            Upload the file to database
                launchUploadActivity(true);
//            Get the returned data
                Bundle extras = data.getExtras();
//            Get the cropped bitmap
                Bitmap bitmap = extras.getParcelable("data");
//            Set the profpic bitmap globally
                Util.accountProfpicBitmap = bitmap;
//            Set the profpic bitmap for drawer globally
                Util.drawerProfpic.setImageBitmap(bitmap);
//            Display the returned cropped image
                ivProfile.setImageBitmap(bitmap);
//                ivProfileExpanded.setImageBitmap(bitmap);
            }
        }
        bSetProfilePicture.setTypeface(null, Typeface.NORMAL);
    }

    private void launchUploadActivity(boolean isImage){
        String filePath = fileUri.getPath();
        if (filePath != null) {

            Util.setProfpicOnDisplay(filePath);

            new UploadFile(getActivity().getApplicationContext(), getActivity()).execute(account.getUsername(), "picture", filePath);
        }
        else {
//            Display an error message
            Toast.makeText(getActivity(), "Sorry, file path is missing!", Toast.LENGTH_LONG).show();
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
            fileUri = Util.getOutputMediaFileUri(Util.MEDIA_TYPE_IMAGE, account.getUsername());
//            To store the image
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//            Start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, Util.IMAGE_CROP_REQUEST_CODE);

        }
        catch(ActivityNotFoundException anfe){
//            Display an error message
            Toast.makeText(getActivity(), "Sorry, your device doesn't support the crop action!", Toast.LENGTH_SHORT).show();
        }
    }

}
