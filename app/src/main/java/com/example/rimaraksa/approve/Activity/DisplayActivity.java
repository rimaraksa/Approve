package com.example.rimaraksa.approve.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
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
import android.widget.TextView;

import com.example.rimaraksa.approve.Adapter.NavDrawerListAdapter;
import com.example.rimaraksa.approve.Fragment.ContractListFragment;
import com.example.rimaraksa.approve.Fragment.ProfileFragment;
import com.example.rimaraksa.approve.Global;
import com.example.rimaraksa.approve.Model.Contract;
import com.example.rimaraksa.approve.Model.NavDrawerItem;
import com.example.rimaraksa.approve.R;

import java.util.ArrayList;

public class DisplayActivity extends ActionBarActivity {

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
    int inbox, waitingInbox, approvedInbox, rejectedInbox;
    int outbox, waitingOutbox, approvedOutbox, rejectedOutbox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.ic_menu_white);
        getSupportActionBar().setTitle("");
//        mToolbar.setNavigationContentDescription("test");

        mTitle = mDrawerTitle = getTitle();

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList<NavDrawerItem>();
        unclickableNavDrawerItems = new ArrayList<Integer>();

//        Ids of each item in the drawer
        profile = 0;
        inbox = 1;
        waitingInbox = 2;
        approvedInbox = 3;
        rejectedInbox = 4;
        outbox = 5;
        waitingOutbox = 6;
        approvedOutbox = 7;
        rejectedOutbox = 8;

        navDrawerItems.add(new NavDrawerItem(-1)); //header

        // Inbox
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[inbox]));    //unclickable
        unclickableNavDrawerItems.add(navDrawerItems.size()-1);
        // Waiting Inbox
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[waitingInbox], navMenuIcons.getResourceId(waitingInbox, -1), true, (Global.waitingInboxCount + "")));
        // Approved Inbox
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[approvedInbox], navMenuIcons.getResourceId(approvedInbox, -1), true, (Global.approvedInboxCount + "")));
        approvedInbox = navDrawerItems.size()-1;
        // Rejected Inbox
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[rejectedInbox], navMenuIcons.getResourceId(rejectedInbox, -1), true, (Global.rejectedInboxCount + "")));
        rejectedInbox = navDrawerItems.size()-1;

        // Outbox
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[outbox]));    //unclickable
        unclickableNavDrawerItems.add(navDrawerItems.size()-1);
        // Waiting Outbox
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[waitingOutbox], navMenuIcons.getResourceId(waitingOutbox, -1), true, (Global.waitingOutboxCount + "")));
        // Approved Outbox
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[approvedOutbox], navMenuIcons.getResourceId(approvedOutbox, -1), true, (Global.approvedOutboxCount + "")));
        // Rejected Outbox
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[rejectedOutbox], navMenuIcons.getResourceId(rejectedOutbox, -1), true, (Global.rejectedOutboxCount + "")));


        // Recycle the typed array
        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());


        // Setting a customized navigation drawer list adapter
        Global.drawerAdapter = adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems)
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
                R.drawable.ic_menu_white, //nav menu toggle icon
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
            if(getIntent().getBooleanExtra("FromDisplayContractToBeApprovedActivity", false)){
                 displayView(waitingInbox);
            }
            else if(getIntent().getBooleanExtra("FromDisplaySentContractActivity", false)){
                displayView(waitingOutbox);

            }
            else if(getIntent().getBooleanExtra("FromDisplayRejectedContractActivityInbox", false)){
                displayView(rejectedInbox);
            }
            else if(getIntent().getBooleanExtra("FromDisplayRejectedContractActivityOutbox", false)){
                displayView(rejectedOutbox);
            }
            else if(getIntent().getBooleanExtra("FromDisplayApprovedContractActivityInbox", false)){
                displayView(approvedInbox);
            }
            else if(getIntent().getBooleanExtra("FromDisplayApprovedContractActivityOutbox", false)){
                displayView(approvedOutbox);
            }
            else{
                displayView(waitingInbox);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display, menu);
        return true;
    }

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
            case R.id.action_compose_contract:
                Intent i = new Intent(getApplicationContext(), CreateContractActivity.class);
                startActivity(i);
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
//        menu.findItem(R.id.action_compose_contract).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Displaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments

        Fragment fragment = null;
        if(position == profile){
            fragment = new ProfileFragment();

        }
        else {
            String type, status;

            ArrayList<Contract> contracts = new ArrayList<Contract>();
            if(position == waitingInbox || position == approvedInbox || position == rejectedInbox){
                type = "inbox";
                if(position == waitingInbox){
                    status = "waiting";
                }
                else if(position == approvedInbox){
                    status = "approved";
                }
                else{
                    status = "rejected";
                }

            }
            else{
                type = "outbox";
                if(position == waitingOutbox){
                    status = "waiting";
                }
                else if(position == approvedOutbox){
                    status = "approved";
                }
                else{
                    status = "rejected";
                }
            }

            Bundle bundle = new Bundle();
            bundle.putString("Type", type);
            bundle.putString("Status", status);
//            bundle.putString("AccountID", account_id);
            fragment = new ContractListFragment();
            fragment.setArguments(bundle);

        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
//            setTitle(navMenuTitles[position]);
            TextView tvToolbarTitle = (TextView) findViewById(R.id.TVToolbarTitle);
            tvToolbarTitle.setText(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
//            mDrawerLayout.setBackground(R.id.dr);
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
