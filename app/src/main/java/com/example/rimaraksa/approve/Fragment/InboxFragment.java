package com.example.rimaraksa.approve.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.rimaraksa.approve.Activity.DisplayApprovedContractActivity;
import com.example.rimaraksa.approve.Activity.DisplayContractToBeApprovedActivity;
import com.example.rimaraksa.approve.Activity.DisplayRejectedContractActivity;
import com.example.rimaraksa.approve.Activity.DisplaySentContractActivity;
import com.example.rimaraksa.approve.Adapter.ContractAdapter;
import com.example.rimaraksa.approve.DatabaseConnection.Display;
import com.example.rimaraksa.approve.Global;
import com.example.rimaraksa.approve.Model.Account;
import com.example.rimaraksa.approve.Model.Contract;
import com.example.rimaraksa.approve.R;

import java.util.ArrayList;
import java.util.List;

public class InboxFragment extends Fragment {

    private Account account;
    private String status, account_id;
    private ArrayList<Contract> contracts;

    //    Components on layout
    ListView contractList;

    public InboxFragment(){
        account = Global.account;
        contracts = new ArrayList<Contract>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_inbox, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            status = getArguments().getString("Status");
            account_id = getArguments().getString("AccountID");
        }

        System.out.println(contracts);
        System.out.println(status);

//        Get components on layout to create list of contracts
        contractList = (ListView) view.findViewById(R.id.LVContract);

        System.out.println("INBOX DISPLAY FOR " + status);
        new Display(getActivity(), contractList).execute(account_id, "receiver", "sender", status);

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


}
