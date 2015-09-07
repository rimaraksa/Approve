package com.example.rimaraksa.approve.Fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rimaraksa.approve.Activity.DisplayActivity;
import com.example.rimaraksa.approve.Activity.SignupActivity;
import com.example.rimaraksa.approve.DatabaseConnection.UploadFileToServer;
import com.example.rimaraksa.approve.Global;
import com.example.rimaraksa.approve.Model.Account;
import com.example.rimaraksa.approve.R;

import java.io.File;

public class ProfileFragment extends Fragment {

    private Account account = Global.account;
    private Uri fileUri;

    //        Components on layout
    private TextView tvName, tvNRIC, tvPhone, tvEmail, tvUsername, tvProfileLink;
    private Button editButton;
    private ImageView ivProfile, ivProfileExpanded;

    //    Hold a reference to the current animator,
//    so that it can be canceled mid-way.
    private Animator mCurrentAnimator;

    //    The system "short" animation time duration, in milliseconds. This
//    duration is ideal for subtle animations or animations that occur
//    very frequently.
    private int mShortAnimationDuration;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tvName = (TextView) view.findViewById(R.id.TVName);
        tvNRIC = (TextView) view.findViewById(R.id.TVNRIC);
        tvPhone = (TextView) view.findViewById(R.id.TVPhone);
        tvEmail = (TextView) view.findViewById(R.id.TVEmail);
        tvUsername = (TextView) view.findViewById(R.id.TVUsername);
        ivProfile = (ImageView) view.findViewById(R.id.IVProfile);
        ivProfileExpanded = (ImageView) view.findViewById(R.id.IVProfileExpanded);
        tvProfileLink = (TextView) view.findViewById(R.id.TVProfileLink);

        editButton = (Button) view.findViewById(R.id.BEdit);

        if(!account.getProfpic().equals("null")){
            ivProfile.setImageBitmap(Global.accountProfpicBitmap);
            ivProfileExpanded.setImageBitmap(Global.accountProfpicBitmap);
        }
        else{
            ivProfile.setImageResource(R.drawable.ic_profile);
        }

        tvName.setText(account.getName());
        tvNRIC.setText("NRIC: " + account.getNric());
        tvPhone.setText("Phone: " + account.getPhone());
        tvEmail.setText("Email: " + account.getEmail());
        tvUsername.setText("Username: " + account.getUsername());



        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivProfileExpanded.setVisibility(View.VISIBLE);
            }
        });

        ivProfileExpanded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivProfileExpanded.setVisibility(View.INVISIBLE);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//        Edit profile by editable views

            }
        });

        tvProfileLink.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
//                Upload a profile picture from camera/album
                if (!isDeviceSupportCamera()) {
                    Toast.makeText(getActivity(), "Sorry! Your device doesn't support camera", Toast.LENGTH_LONG).show();
                }
                else {
                    System.out.println("Capture Image");
                    captureImage();
                }
            }
        });

        return view;
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
     * Checking device has camera hardware or not
     */
    private boolean isDeviceSupportCamera() {
        if (getActivity().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // This device has a camera
            return true;
        } else {
            // This device does not have a camera
            return false;
        }
    }

    private void captureImage(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, Global.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == getActivity().RESULT_OK){
            if(requestCode == Global.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
                fileUri = data.getData();
                performCrop();
            }
            else if(requestCode == Global.IMAGE_CROP_REQUEST_CODE){
//            Upload the file to database
                launchUploadActivity(true);
//            Get the returned data
                Bundle extras = data.getExtras();
//            Get the cropped bitmap
                Bitmap bitmap = extras.getParcelable("data");

//            Set the profpic bitmap globally
                Global.accountProfpicBitmap = bitmap;
//            Set the profpic bitmap for drawer globally
                Global.drawerProfpic.setImageBitmap(bitmap);

//            Display the returned cropped image
                ivProfile.setImageBitmap(bitmap);
                ivProfileExpanded.setImageBitmap(bitmap);
            }
        }

    }

    private void launchUploadActivity(boolean isImage){
        String filePath = fileUri.getPath();
        if (filePath != null) {

            Global.setProfpicOnDisplay(filePath);

            new UploadFileToServer(getActivity()).execute(account.getUsername(), "picture", filePath);
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
            fileUri = Global.getOutputMediaFileUri(Global.MEDIA_TYPE_IMAGE, account.getUsername());
//            To store the image
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//            Start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, Global.IMAGE_CROP_REQUEST_CODE);

        }
        catch(ActivityNotFoundException anfe){
//            Display an error message
            Toast.makeText(getActivity(), "Sorry, your device doesn't support the crop action!", Toast.LENGTH_SHORT).show();
        }
    }

}
