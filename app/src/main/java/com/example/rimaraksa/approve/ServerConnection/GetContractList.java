package com.example.rimaraksa.approve.ServerConnection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.rimaraksa.approve.Activity.DisplayApprovedContractActivity;
import com.example.rimaraksa.approve.Activity.DisplayContractToBeApprovedActivity;
import com.example.rimaraksa.approve.Activity.DisplayRejectedContractActivity;
import com.example.rimaraksa.approve.Activity.DisplaySentContractActivity;
import com.example.rimaraksa.approve.Adapter.ContractAdapter;
import com.example.rimaraksa.approve.Util;
import com.example.rimaraksa.approve.Model.Contract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by rimaraksa on 15/6/15.
 */
public class GetContractList extends AsyncTask<String,Void,String> {
    private Context context;
    private Activity activity;
    private ArrayList<Contract> contracts = new ArrayList<Contract>();
    private ArrayList<Bitmap> profpics = new ArrayList<Bitmap>();
    private ArrayList<String> namesTarget = new ArrayList<String>();
    private ArrayList<String> usernamesTarget = new ArrayList<String>();
    private ArrayList<String> phonesTarget = new ArrayList<String>();
    private String account_id;
    private String role, target, contractStatus;

    private ListView lvContractList;

    private EditText tfSearchContract;
    private ContractAdapter adapter;



    public GetContractList(Context context, Activity activity, ListView lvContractList, EditText tfSearchContract) {
        this.context = context;
        this.activity = activity;
        this.lvContractList = lvContractList;
        this.tfSearchContract = tfSearchContract;
    }

    @Override
    protected void onPreExecute(){


    }

    @Override
    protected String doInBackground(String... arg0) {
        try{
            role = (String)arg0[0];
            target = (String)arg0[1];
            contractStatus = (String)arg0[2];

            account_id = Integer.toString(Util.account.getAccount_id());
            String link = Util.linkDisplayContractList;
            String data  = URLEncoder.encode("account_id", "UTF-8") + "=" + URLEncoder.encode(account_id, "UTF-8");
            data  += "&" + URLEncoder.encode("role", "UTF-8") + "=" + URLEncoder.encode(role, "UTF-8");
            data  += "&" + URLEncoder.encode("target", "UTF-8") + "=" + URLEncoder.encode(target, "UTF-8");
            data  += "&" + URLEncoder.encode("contractStatus", "UTF-8") + "=" + URLEncoder.encode(contractStatus, "UTF-8");


            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                sb.append(line);
//                break;
            }
            return sb.toString();
        }
        catch(Exception e){
            return new String("Exception on Display: " + e.getMessage());
        }

    }

    @Override
    protected void onPostExecute(String result){
        System.out.println("Display Result: " + result);
        try{
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                int contract_id = jsonData.getInt("contract_id");
                String contractKey = jsonData.getString("contractKey");
                int sender_id = jsonData.getInt("sender_id");
                int receiver_id = jsonData.getInt("receiver_id");
                String contractSubject = jsonData.getString("contractSubject");
                String contractBody = jsonData.getString("contractBody");
                String location = jsonData.getString("location");
                String contractStatus = jsonData.getString("contractStatus");
                String dateRequest = jsonData.getString("dateRequest");
                String dateAppOrReject = jsonData.getString("dateAppOrReject");
                String videoApproval = jsonData.getString("videoApproval");
                String reasonForRejection = jsonData.getString("reasonForRejection");
                Contract contract = new Contract(contract_id, contractKey, sender_id, receiver_id, contractSubject, contractBody, location, contractStatus, dateRequest, dateAppOrReject, videoApproval, reasonForRejection);
                contracts.add(0, contract);

                String encodedProfpicTarget = jsonData.getString("encodedProfpicTarget");
                Bitmap bitmap = Util.stringToBitmap(encodedProfpicTarget);
                profpics.add(0, bitmap);

                String nameTarget = jsonData.getString(target + "Name");
                namesTarget.add(0, nameTarget);

                String usernameTarget = jsonData.getString( target + "Username");
                usernamesTarget.add(0, usernameTarget);

                String phoneTarget = jsonData.getString(target + "Phone");
                phonesTarget.add(0, phoneTarget);
            }


            populateLVContract();
            registerClickCallBackContract();

        }
        catch (JSONException e){
            e.printStackTrace();
        }




    }

    private void populateLVContract() {
        //build adapter
        adapter = new ContractAdapter(context, activity, contracts, profpics, namesTarget, usernamesTarget, phonesTarget, role);
        //configure list view
        lvContractList.setAdapter(adapter);

        tfSearchContract.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = tfSearchContract.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }
        });


    }

    private void registerClickCallBackContract() {
        lvContractList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contract contract = contracts.get(position);
                Bitmap profpic = profpics.get(position);
                String targetName = namesTarget.get(position);
                String targetUsername = usernamesTarget.get(position);
                String targetPhone = phonesTarget.get(position);

                System.out.println("size: " + contracts.size() + " " + profpics.size() + " " + namesTarget.size() + " " + usernamesTarget.size() + " " + phonesTarget.size());
                Intent i;
                if (contract.getStatus().equals("pending")) {
                    if (role.equals("receiver")) {
                        i = new Intent(activity, DisplayContractToBeApprovedActivity.class);
                        i.putExtra("Contract", contract);
                        i.putExtra("SenderProfpic", profpic);
                        i.putExtra("SenderName", targetName);
                        i.putExtra("SenderUsername", targetUsername);
                        i.putExtra("SenderPhone", targetPhone);
                        activity.startActivity(i);

                    }
                    else {
                        i = new Intent(activity, DisplaySentContractActivity.class);
                        i.putExtra("Contract", contract);
                        i.putExtra("ReceiverName", targetName);
                        i.putExtra("ReceiverUsername", targetUsername);
                        i.putExtra("ReceiverPhone", targetPhone);
                        activity.startActivity(i);

                    }
                } else if (contract.getStatus().equals("approved")) {
                    i = new Intent(activity, DisplayApprovedContractActivity.class);
                    i.putExtra("Contract", contract);
                    i.putExtra("TargetProfpic", profpic);
                    i.putExtra("TargetName", targetName);
                    i.putExtra("TargetUsername", targetUsername);
                    i.putExtra("Role", role);
                    activity.startActivity(i);
                } else {
                    i = new Intent(activity, DisplayRejectedContractActivity.class);
                    i.putExtra("Contract", contract);
                    i.putExtra("TargetProfpic", profpic);
                    i.putExtra("TargetName", targetName);
                    i.putExtra("TargetUsername", targetUsername);
                    i.putExtra("Role", role);
                    activity.startActivity(i);
                }

            }
        });
    }
}
