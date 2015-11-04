package com.example.rimaraksa.approve.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.example.rimaraksa.approve.ServerConnection.GetContractList;
import com.example.rimaraksa.approve.R;

public class ContractListFragment extends Fragment {

    private String type, status;

    //    Components on layout
    private ListView lvContract;
    private EditText tfSearchContract;

    public ContractListFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contract_list, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            type = getArguments().getString("Type");
            status = getArguments().getString("Status");
//            account_id = getArguments().getString("AccountID");
        }

//        Get components on layout to create list of contracts
        tfSearchContract = (EditText) view.findViewById(R.id.TFSearchContract);
        lvContract = (ListView) view.findViewById(R.id.LVContract);

        if(type.equals("inbox")){
            new GetContractList(getActivity().getApplicationContext(), getActivity(), lvContract, tfSearchContract).execute("receiver", "sender", status);
        }
        else{
            new GetContractList(getActivity().getApplicationContext(), getActivity(), lvContract, tfSearchContract).execute("sender", "receiver", status);
        }

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
