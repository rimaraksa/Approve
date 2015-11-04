package com.example.rimaraksa.approve.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rimaraksa.approve.Util;
import com.example.rimaraksa.approve.Model.Contract;
import com.example.rimaraksa.approve.R;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by rimaraksa on 5/6/15.
 */
public class ContractAdapter extends BaseAdapter {
    private Context context;
    private Activity activity;
    private ArrayList<Contract> contractList = null;
    private ArrayList<Contract> filteredContractList;
    private ArrayList<Bitmap> profpicList = null;
    private ArrayList<Bitmap> filteredProfpicList;
    private ArrayList<String> nameList = null;
    private ArrayList<String> filteredNameList;
    private ArrayList<String> usernameList = null;
    private ArrayList<String> filteredUsernameList;
    private ArrayList<String> phoneList = null;
    private ArrayList<String> filteredPhoneList;

    private static LayoutInflater inflater;
    private String role;

    public ContractAdapter(Context context, Activity activity, ArrayList<Contract> contractList, ArrayList<Bitmap> profpicList, ArrayList<String> nameList, ArrayList<String> usernameList, ArrayList<String> phoneList, String role) {
        this.context = context;
        this.activity = activity;

        this.contractList = contractList;
        this.filteredContractList = new ArrayList<Contract>();
        this.filteredContractList.addAll(contractList);

        this.profpicList = profpicList;
        this.filteredProfpicList = new ArrayList<Bitmap>();
        this.filteredProfpicList.addAll(profpicList);

        this.nameList = nameList;
        this.filteredNameList = new ArrayList<String>();
        this.filteredNameList.addAll(nameList);

        this.usernameList = usernameList;
        this.filteredUsernameList = new ArrayList<String>();
        this.filteredUsernameList.addAll(usernameList);

        this.phoneList = phoneList;
        this.filteredPhoneList = new ArrayList<String>();
        this.filteredPhoneList.addAll(phoneList);

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
        if (inflater == null){
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null){
            convertView = inflater.inflate(R.layout.contract_list_item_card, null);
        }

        Contract c = contractList.get(position);
        Bitmap b = profpicList.get(position);
        String n = nameList.get(position);
        String u = usernameList.get(position);

        ImageView ivProfileSenderOrReceiver = (ImageView) convertView.findViewById(R.id.IVProfileSenderOrReceiver);
        TextView receiverOrSender = (TextView) convertView.findViewById(R.id.TVReceiverOrSender);
        TextView tvDate = (TextView) convertView.findViewById(R.id.TVDate);
        TextView subject = (TextView) convertView.findViewById(R.id.TVSubject);
        TextView tvDescription = (TextView) convertView.findViewById(R.id.TVDescription);


        ivProfileSenderOrReceiver.setImageBitmap(b);
        receiverOrSender.setText(n + " [" + u + "]");

//        if(role.equals("sender")){
//
//            receiverOrSender.setText(n + " [" + c.getReceiver() + "]");
//        }
//        else{
//            receiverOrSender.setText(n + " [" + usernameList.get(position) + "]");
//
//        }

        String time;

        if(c.getStatus().equals("pending")){

            time = c.getDateRequest();
        }
        else{
            time = c.getDateAppOrReject();

        }

        if(Util.isDateToday(time)){
            time = Util.getTimeFromDateTime(time);
        }
        else if(Util.isDateThisWeek(time)){
            time = Util.getDayOfTheWeekFromDateTime(time);
        }
        else{
            time = Util.getDateFromDateTime(time);
        }

        tvDate.setText(time);
        subject.setText(c.getSubject());
        tvDescription.setText(c.getBody());

        return convertView;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());

        contractList.clear();
        nameList.clear();
        usernameList.clear();
        profpicList.clear();
        phoneList.clear();

        if (charText.length() == 0) {
            contractList.addAll(filteredContractList);
            nameList.addAll(filteredNameList);
            usernameList.addAll(filteredUsernameList);
            profpicList.addAll(filteredProfpicList);
            phoneList.addAll(filteredPhoneList);
        }
        else
        {
            for (int i = 0; i < filteredContractList.size(); i++)
            {
                if (filteredNameList.get(i).toLowerCase(Locale.getDefault()).contains(charText) ||
                        filteredUsernameList.get(i).toLowerCase(Locale.getDefault()).contains(charText) ||
                        filteredContractList.get(i).getSubject().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    contractList.add(filteredContractList.get(i));
                    nameList.add(filteredNameList.get(i));
                    usernameList.add(filteredUsernameList.get(i));
                    profpicList.add(filteredProfpicList.get(i));
                    phoneList.add(filteredPhoneList.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }
}
