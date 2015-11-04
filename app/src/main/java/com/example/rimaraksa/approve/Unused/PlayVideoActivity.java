package com.example.rimaraksa.approve.Unused;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.VideoView;

import com.example.rimaraksa.approve.Util;
import com.example.rimaraksa.approve.Model.Contract;
import com.example.rimaraksa.approve.R;


public class PlayVideoActivity extends ActionBarActivity {
    private Contract contract;
    private String filePath;

    ProgressDialog pDialog;
    VideoView vvApprovalVideo;

    String VideoURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        contract = (Contract) getIntent().getSerializableExtra("Contract");
        filePath = getIntent().getStringExtra("FilePath");
        vvApprovalVideo = (VideoView) findViewById(R.id.VVApprovalVideo);

        Util.previewVideo(vvApprovalVideo, filePath);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_play_video, menu);
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
}
