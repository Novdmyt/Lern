package com.android.applearn.startapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.applearn.R;
import com.android.applearn.addword.AddWordOnDataBase;
import com.android.applearn.setting.SettingApp;

public class Settings extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        if (savedInstanceState == null) {
            // Create an instance of the fragment
            SettingApp settingApp = new SettingApp();

            // Get the FragmentManager
            FragmentManager fragmentManager = getSupportFragmentManager();

            // Begin a fragment transaction
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            // Replace the content frame with the new fragment
            fragmentTransaction.replace(R.id.fragment_container, settingApp);

            // Commit the transaction
            fragmentTransaction.commit();
        }
    }
}