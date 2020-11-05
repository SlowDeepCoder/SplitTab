package com.example.splittab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.splittab.Adapters.ParticipantAdapter;
import com.example.splittab.Adapters.PaymentAdapter;

public class OverviewFragment extends Fragment {
    public static ParticipantAdapter participantAdapter;
    public static ListView participantListView;
    private GroupManager groupManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.overview_layout, container, false);
        participantListView = view.findViewById(R.id.participant_list_view);
        groupManager = GroupManager.getInstance();

        if (groupManager.getCurrentGroup() != null) {
            //Create adapter
            participantAdapter = new ParticipantAdapter(getContext(), R.layout.history_layout, groupManager.getCurrentGroup().getParticipantList());

            //Connect historyAdapter to ListView
            participantListView.setAdapter(participantAdapter);
        }
        return view;
    }
}