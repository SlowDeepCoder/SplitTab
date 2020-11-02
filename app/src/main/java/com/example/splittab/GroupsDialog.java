package com.example.splittab;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.splittab.Adapters.GroupAdapter;

public class GroupsDialog extends DialogFragment {
    private ListView listView;
    private GroupManager groupManager;
    public static GroupAdapter groupAdapter;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.groups_dialog_layout, null);
        builder.setView(view).setTitle("Groups");

        groupManager = GroupManager.getInstance();
        groupAdapter = new GroupAdapter(view.getContext(), R.layout.group_list_item, groupManager.getGroupArrayList());
        listView = (ListView)view.findViewById(R.id.groupsDialogListView);
        listView.setAdapter(groupAdapter);

        setOnClickListeners(view);
        return builder.create();
    }

    private void setOnClickListeners(View view) {
        view.findViewById(R.id.create_group_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewGroupDialog dialog = new NewGroupDialog();
                dialog.show(getActivity().getSupportFragmentManager(), "");
            }
        });

        view.findViewById(R.id.join_group_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ////
                ////   Not done yet
                ////
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String keyText = groupManager.getGroupArrayList().get(i).getKey();

                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Group key", keyText);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(getContext(), keyText, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
