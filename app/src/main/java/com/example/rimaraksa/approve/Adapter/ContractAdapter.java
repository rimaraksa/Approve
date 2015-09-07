package com.example.rimaraksa.approve.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.rimaraksa.approve.Model.Contract;
import com.example.rimaraksa.approve.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rimaraksa on 5/6/15.
 */
public class ContractAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<Contract> contractList;
    private static LayoutInflater inflater;
    private String role;

    public ContractAdapter(Activity activity, ArrayList<Contract> contractList, String role) {
        this.activity = activity;
        this.contractList = contractList;
        this.role = role;
    }

    @Override
    public int getCount() {
        return contractList.size();
    }

    @Override
    public Object getItem(int location) {
        return contractList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.contract_list, null);

//        if (imageLoader == null)
//            imageLoader = AppController.getInstance().getImageLoader();
//        NetworkImageView thumbNail = (NetworkImageView) convertView
//                .findViewById(R.id.thumbnail);

        Contract c = contractList.get(position);
        if(role.equals("sender")){
            TextView receiver = (TextView) convertView.findViewById(R.id.TVReceiverOrSender);
            receiver.setText(c.getReceiver());
        }
        else{
            TextView sender = (TextView) convertView.findViewById(R.id.TVReceiverOrSender);
            sender.setText(c.getSender());

        }

        TextView subject = (TextView) convertView.findViewById(R.id.TVSubject);
        subject.setText(c.getSubject());

//        // thumbnail image
//        thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);

        return convertView;
    }
}
