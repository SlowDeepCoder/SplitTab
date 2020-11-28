package com.example.splittab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.splittab.Adapters.PaymentAdapter;
import com.example.splittab.Dialogs.GroupListDialog;
import com.example.splittab.Dialogs.SelectParticipantsDialog;
import com.example.splittab.FirebaseTemplates.Group;
import com.example.splittab.FirebaseTemplates.Participant;
import com.example.splittab.FirebaseTemplates.Payment;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.splittab.ui.main.SectionsPagerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        setToolbar();

    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.toolbar);
        toolbar.setTitle(R.string.app_title);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.actionbarGroup:
                        GroupListDialog dialog = new GroupListDialog();
                        dialog.show(MainActivity.this.getSupportFragmentManager(), "");
                        return true;
                    case R.id.actionbarSettings:
                        Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivity(settingsIntent);
                        return true;
                    case R.id.actionbarSignOut:
                        FirebaseAuth.getInstance().signOut();
                        Intent LoginIntent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(LoginIntent);
                        finish();
                        return true;

                }
                return false;
            }
        });
    }



//        for (final Group group : groupManager.getGroupArrayList()) {
//            database.getReference("groups").child(group.getKey()).child("payments").addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.exists()) {
//                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                            for (Payment payment : group.getPaymentList()) {
//                                if (!snapshot.getValue(Payment.class).getKey().equals(payment.getKey())) {
//                                    group.addPayment(payment);
//
//                                    AddPaymentFragment.paymentAdapter = new PaymentAdapter(MainActivity.this, R.layout.payment_list_item, groupManager.getCurrentGroup().getPaymentList());
//                                    AddPaymentFragment.paymentListView.setAdapter(AddPaymentFragment.paymentAdapter);
//                                    AddPaymentFragment.paymentAdapter.notifyDataSetChanged();
//                                }
//                            }
//                        }
//                    }
//                    Log.d("onDataChange", "Data change detected in " + group.getKey() + " payments");
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//
//        }


    @SuppressLint("RestrictedApi")
        @Nullable
        @Override
        public View onCreateView (@NonNull String name, @NonNull Context
        context, @NonNull AttributeSet attrs){
            GroupManager groupManager = GroupManager.getInstance();
            if (groupManager.getCurrentGroup() != null) {
                ActionMenuItemView item = (ActionMenuItemView) findViewById(R.id.actionbarGroup);
                item.setTitle(groupManager.getCurrentGroup().getName());

            }

            return super.onCreateView(name, context, attrs);
        }

}