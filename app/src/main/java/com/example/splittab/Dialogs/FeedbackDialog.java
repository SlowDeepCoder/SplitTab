package com.example.splittab.Dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.fragment.app.DialogFragment;

import com.example.splittab.Adapters.GroupAdapter;
import com.example.splittab.GroupManager;
import com.example.splittab.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FeedbackDialog extends DialogFragment {
    private EditText editTextFeedback;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.feedback_dialog_layout, null);

        builder.setView(view).setTitle(getResources().getString(R.string.feedback));

        editTextFeedback = (EditText)view.findViewById(R.id.editTextEnterFeedback);
        setOnClickListeners(view);
        return builder.create();
    }

    private void setOnClickListeners(View view) {
        view.findViewById(R.id.cancel_feedback_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        view.findViewById(R.id.leave_feedback_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase dataBase = FirebaseDatabase.getInstance();
                DatabaseReference ref = dataBase.getReference("feedback");
                String key = ref.push().getKey();
                String value = editTextFeedback.getText().toString().trim();

                ref.child(key).setValue(value);
                Toast.makeText(getContext(), getResources().getString(R.string.thank_you_feedback), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }
}
