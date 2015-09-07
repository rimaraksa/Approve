package com.example.rimaraksa.approve.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.rimaraksa.approve.Adapter.NavDrawerListAdapter;
import com.example.rimaraksa.approve.DatabaseConnection.DownloadFile;
import com.example.rimaraksa.approve.DatabaseConnection.UploadFileToServer;
import com.example.rimaraksa.approve.Fragment.CreateContractFragment;
import com.example.rimaraksa.approve.Fragment.InboxFragment;
import com.example.rimaraksa.approve.Fragment.OutboxFragment;
import com.example.rimaraksa.approve.Fragment.ProfileFragment;
import com.example.rimaraksa.approve.Global;
import com.example.rimaraksa.approve.Model.Account;
import com.example.rimaraksa.approve.Model.Contract;
import com.example.rimaraksa.approve.Model.NavDrawerItem;
import com.example.rimaraksa.approve.R;

import java.util.ArrayList;

public class DisplayActivity extends ActionBarActivity {
    private Account account;
    private String account_id;

    private Toolbar mToolbar;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    private ArrayList<Integer> unclickableNavDrawerItems;


//    Id of each view;
    int profile;
    int createContract;
    int waitingInbox, approvedInbox, rejectedInbox;
    int waitingOutbox, approvedOutbox, rejectedOutbox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_display);
        setContentView(R.layout.display_test);

        account = Global.account;
        account_id = account.getAccount_id() + "";

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        mTitle = mDrawerTitle = getTitle();

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList<NavDrawerItem>();
        unclickableNavDrawerItems = new ArrayList<Integer>();

        // adding nav drawer items to array
        // Profile
//        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1))); //header

//        navDrawerItems.add(new NavDrawerItem(-1)); //header

        if(!Global.account.getProfpic().equals("null")){
            navDrawerItems.add(new NavDrawerItem(-1)); //header
        }
        else{
            navDrawerItems.add(new NavDrawerItem(navMenuIcons.getResourceId(0, -1))); //header
        }

        // Create new contract
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        createContract = navDrawerItems.size()-1;

        // Inbox
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2]));    //unclickable
        unclickableNavDrawerItems.add(navDrawerItems.size()-1);
        // Waiting Inbox
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(2, -1), true, "22"));
        waitingInbox = navDrawerItems.size()-1;
        // Approved Inbox
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(3, -1), true, "22"));
        approvedInbox = navDrawerItems.size()-1;
        // Rejected Inbox
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(4, -1), true, "22"));
        rejectedInbox = navDrawerItems.size()-1;

        // Outbox
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6]));    //unclickable
        unclickableNavDrawerItems.add(navDrawerItems.size()-1);
        // Waiting Outbox
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons.getResourceId(5, -1), true, "22"));
        waitingOutbox = navDrawerItems.size()-1;
        // Approved Outbox
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[8], navMenuIcons.getResourceId(6, -1), true, "22"));
        approvedOutbox = navDrawerItems.size()-1;
        // Rejected Outbox
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[9], navMenuIcons.getResourceId(7, -1), true, "22"));
        rejectedOutbox = navDrawerItems.size()-1;


        // Recycle the typed array
        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());


        // Setting a customized navigation drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems)
        {
            public boolean areAllItemsEnabled()
            {
                return false;
            }
            public boolean isEnabled(int position)
            {
                if(unclickableNavDrawerItems.contains(position)){
                    return false;
                }
                return true;
            }
        };

        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ){
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }

    }

//    private void populateLVInbox() {
//        //create list of inbox
//        String [] inbox = {"Waiting", "Approved", "Rejected"};
//
//        //build adapter
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.contract_inbox_list, inbox);
//
//        //configure list view
//        ListView list = (ListView) findViewById(R.id.LVInbox);
//        list.setAdapter(adapter);
//
//    }
//
//    private void populateLVOutbox() {
//        //create list of outbox
//        String [] outbox = {"Waiting", "Approved", "Rejected"};
//
//        //build adapter
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.contract_outbox_list, outbox);
//
//        //configure list view
//        ListView list = (ListView) findViewById(R.id.LVOutbox);
//        list.setAdapter(adapter);
//    }
//
//    private void registerClickCallBackInbox() {
//        ListView list = (ListView) findViewById(R.id.LVInbox);
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                TextView textView = (TextView) view;
//
//                String inboxType = textView.getText().toString();
//                status = inboxType.toLowerCase();
//                new Display(DisplayActivity.this, account).execute(account_id, "receiver", "sender", status);
//
//            }
//        });
//    }
//
//    private void registerClickCallBackOutbox() {
//        ListView list = (ListView) findViewById(R.id.LVOutbox);
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                TextView textView = (TextView) view;
//
//                String inboxType = textView.getText().toString();
//                status = inboxType.toLowerCase();
//                new Display(DisplayActivity.this, account).execute(account_id, "sender", "receiver", status);
//
//            }
//        });
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    public void onCreateClick(View v){
//        if(v.getId() == R.id.BCreateContract){
//            Intent i = new Intent(DisplayActivity.this, CreateContractActivity.class);
//            i.putExtra("Account", account);
//            startActivity(i);
//        }
//    }

//    START TESTING

    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Displaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments


        Fragment fragment = null;
        if(position == profile || position == createContract){

            if(position == profile){
                fragment = new ProfileFragment();
            }
            else{
                fragment = new CreateContractFragment();
            }

        }
        else {
            String status;
            ArrayList<Contract> contracts = new ArrayList<Contract>();
            if(position == waitingInbox || position == approvedInbox || position == rejectedInbox){

                if(position == waitingInbox){
                    status = "waiting";
                }
                else if(position == approvedInbox){
                    status = "approved";
                }
                else{
                    status = "rejected";
                }

                Bundle bundle = new Bundle();
                bundle.putString("Status", status);
                bundle.putString("AccountID", account_id);
                fragment = new InboxFragment();
                fragment.setArguments(bundle);
            }
            else{
                if(position == waitingOutbox){
                    status = "waiting";
                }
                else if(position == approvedOutbox){
                    status = "approved";
                }
                else{
                    status = "rejected";
                }

                Bundle bundle = new Bundle();
                bundle.putString("Status", status);
                bundle.putString("AccountID", account_id);
                fragment = new OutboxFragment();
                fragment.setArguments(bundle);

            }
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        }
        else {
            // error in creating fragment
            Log.e("DisplayActivity", "Error in creating fragment");
        }

    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

}
