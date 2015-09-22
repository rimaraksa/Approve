package com.example.rimaraksa.approve.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rimaraksa.approve.Global;
import com.example.rimaraksa.approve.Model.Contract;
import com.example.rimaraksa.approve.Model.Country;
import com.example.rimaraksa.approve.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by rimaraksa on 19/9/15.
 */
public class CountryCodeAdapter extends BaseAdapter{
    private Context context;
    private Activity activity;
    private ArrayList<String> countryNameList;
    private ArrayList<String> countryCodeList;
    private ArrayList<String> countryCodePhoneList;
    private static LayoutInflater inflater;

    private ArrayList<Country> countryList = null;
    private ArrayList<Country> filteredCountryList;
//    public CountryCodeAdapter(Context context, Activity activity, ArrayList<String> countryNameList, ArrayList<String> countryCodeList, ArrayList<String> countryCodePhoneList) {
//        this.context = context;
//        this.activity = activity;
//        this.countryNameList = countryNameList;
//        this.countryCodeList = countryCodeList;
//        this.countryCodePhoneList = countryCodePhoneList;
//    }

    public CountryCodeAdapter(Context context, Activity activity, ArrayList<Country> countryList) {
        this.context = context;
        this.activity = activity;
        this.countryList = countryList;
        this.filteredCountryList = new ArrayList<Country>();
        this.filteredCountryList.addAll(countryList);
    }

    @Override
    public int getCount() {
        return countryList.size();
    }

    @Override
    public Object getItem(int location) {
        return countryList.get(location);
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
            convertView = inflater.inflate(R.layout.country_code_list_item, null);
        }

//        String c = countryCodeList.get(position) + " " + countryCodePhoneList.get(position) + ": " + countryNameList.get(position);

        String c = countryList.get(position).getCountryCode() + " " + countryList.get(position).getCountryPhoneCode() + ": " + countryList.get(position).getCountryName();

        TextView tvCountryCode = (TextView) convertView.findViewById(R.id.TVCountryCode);

        tvCountryCode.setText(c);

        return convertView;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        countryList.clear();
        if (charText.length() == 0) {
            countryList.addAll(filteredCountryList);
        }
        else
        {
            for (Country c : filteredCountryList)
            {
                if (c.getCountryName().toLowerCase(Locale.getDefault()).contains(charText) || c.getCountryCode().toLowerCase(Locale.getDefault()).contains(charText) || c.getCountryPhoneCode().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    countryList.add(c);
                }
            }
        }
        notifyDataSetChanged();
    }
}
