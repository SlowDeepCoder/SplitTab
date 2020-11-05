package com.example.splittab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.splittab.Adapters.PaymentAdapter;

public class HistoryFragment extends Fragment {

    public static PaymentAdapter historyAdapter;
    public static ListView historyListView;
    private GroupManager groupManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_layout, container, false);
        historyListView = view.findViewById(R.id.history_list_view);

        groupManager = GroupManager.getInstance();

        if (groupManager.getCurrentGroup() != null) {
            //Create adapter
            historyAdapter = new PaymentAdapter(getContext(), R.layout.history_layout, groupManager.getCurrentGroup().getPaymentList());

            //Connect historyAdapter to ListView
            historyListView.setAdapter(historyAdapter);
        }
        return view;
    }
}