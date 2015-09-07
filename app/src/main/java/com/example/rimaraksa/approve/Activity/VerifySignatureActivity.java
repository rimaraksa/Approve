package com.example.rimaraksa.approve.Activity;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.rimaraksa.approve.Global;
import com.example.rimaraksa.approve.Model.Account;
import com.example.rimaraksa.approve.Model.Contract;
import com.example.rimaraksa.approve.R;
import com.qualcomm.snapdragon.sdk.face.FaceData;
import com.qualcomm.snapdragon.sdk.face.FacialProcessing;

import java.io.IOException;
import java.security.Signature;
import java.util.Arrays;
import java.util.EnumSet;


public class VerifySignatureActivity extends ActionBarActivity {

    private Account account;
    private Contract contract;
    private ImageView ivSignature, ivVideoFrame;
    private String filePath;
    private Bitmap videoFrame;
    private Bitmap signature;

    private FacialProcessing mFacialProcessing;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_signature);

        account = Global.account;
        contract = (Contract) getIntent().getSerializableExtra("Contract");
        filePath = (String) getIntent().getExtras().getString("FilePath");
        ivVideoFrame = (ImageView) findViewById(R.id.IVVideoFrame);
        ivSignature = (ImageView) findViewById(R.id.IVSignature);

//        Extract video frame for signature verification - 4 Sept
        videoFrame = getVideoFrame(filePath);
        signature = Global.accountSignature;

        ivVideoFrame.setImageBitmap(videoFrame);
        ivSignature.setImageBitmap(signature);

        String signatureToBeVerified = "VERIFYING_" + signature;
        new VerifySignature(this, videoFrame).execute(signatureToBeVerified);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_verify_signature, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static Bitmap getVideoFrame(String filePath) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            return retriever.getFrameAtTime();
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        } catch (RuntimeException ex) {
            ex.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
            }
        }
        return null;
    }
}
