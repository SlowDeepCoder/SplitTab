package com.example.splittab.Dialogs;

import android.annotation.SuppressLint;
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

import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.fragment.app.DialogFragment;

import com.example.splittab.Adapters.GroupAdapter;
import com.example.splittab.Adapters.PaymentAdapter;
import com.example.splittab.AddPaymentFragment;
import com.example.splittab.FirebaseTemplates.Payment;
import com.example.splittab.GroupManager;
import com.example.splittab.R;

public class GroupListDialog extends DialogFragment {
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
        listView = (ListView) view.findViewById(R.id.groupsDialogListView);
        listView.setAdapter(groupAdapter);

        setOnClickListeners(view);
        return builder.create();
    }

    private void setOnClickListeners(View view) {
        view.findViewById(R.id.create_group_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateGroupDialog dialog = new CreateGroupDialog();
                dialog.show(getActivity().getSupportFragmentManager(), "Create Group");
            }
        });

        view.findViewById(R.id.join_group_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JoinGroupDialog dialog = new JoinGroupDialog();
                dialog.show(getActivity().getSupportFragmentManager(), "Join Group");
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                groupManager.setCurrentGroup(i);
                ActionMenuItemView item = (ActionMenuItemView)getActivity().findViewById(R.id.actionbarGroup);
                item.setTitle(groupManager.getCurrentGroup().getName());

                AddPaymentFragment.paymentAdapter = new PaymentAdapter(getContext(), R.layout.payment_list_item, groupManager.getCurrentGroup().getPaymentList());
                AddPaymentFragment.paymentListView.setAdapter(AddPaymentFragment.paymentAdapter);
                AddPaymentFragment.paymentAdapter.notifyDataSetChanged();

                dismiss();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String keyText = groupManager.getGroupArrayList().get(i).getKey();

                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Group key", keyText);
                clipboard.setPrimaryClip(clip);

                String toastText = keyText + " copied to clipboard";
                Toast.makeText(getContext(), toastText, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
}
