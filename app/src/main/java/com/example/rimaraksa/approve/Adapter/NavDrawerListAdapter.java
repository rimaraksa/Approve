package com.example.rimaraksa.approve.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rimaraksa.approve.Global;
import com.example.rimaraksa.approve.Model.NavDrawerItem;
import com.example.rimaraksa.approve.R;

import java.util.ArrayList;

/**
 * Created by rimaraksa on 9/7/15.
 */
public class NavDrawerListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<NavDrawerItem> navDrawerItems;

    public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems){
        this.context = context;
        this.navDrawerItems = navDrawerItems;
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
            context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if(navDrawerItems.get(position).isHeaderType()){
                convertView = mInflater.inflate(R.layout.drawer_list_header, null);

                TextView tvName = (TextView) convertView.findViewById(R.id.TVName);
                TextView tvUsername = (TextView) convertView.findViewById(R.id.TVUsername);
                tvName.setText(Global.account.getName());
                tvUsername.setText("(" + Global.account.getUsername() + ")");

                if(navDrawerItems.get(position).getIcon() == -1){
                    ImageView ivProfile = (ImageView) convertView.findViewById(R.id.IVProfile);
                    ivProfile.setImageBitmap(Global.accountProfpicBitmap);

                    Global.drawerProfpic = ivProfile;
                }
                else{
                    ImageView ivProfile = (ImageView) convertView.findViewById(R.id.IVProfile);
                    ivProfile.setImageResource(navDrawerItems.get(position).getIcon());
                }
            }
            else if(navDrawerItems.get(position).isSubheaderType()){
                convertView = mInflater.inflate(R.layout.drawer_list_subheader, null);

                TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
                txtTitle.setText(navDrawerItems.get(position).getTitle());
            }
            else{
                convertView = mInflater.inflate(R.layout.drawer_list_item, null);
                ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
                imgIcon.setImageResource(navDrawerItems.get(position).getIcon());
                TextView txtCount = (TextView) convertView.findViewById(R.id.counter);
                if(navDrawerItems.get(position).getCounterVisibility()){
                    txtCount.setText(navDrawerItems.get(position).getCount());
                }
                else{
                    // hide the counter view
                    txtCount.setVisibility(View.GONE);
                }

                TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
                txtTitle.setText(navDrawerItems.get(position).getTitle());
            }

        }

        return convertView;
    }
}