package com.android.applearn.startapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.applearn.R;
import com.android.applearn.addword.AddWordOnDataBase;

public class AddWord extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_word);

        // Check if the savedInstanceState is null to avoid overlapping fragments
        if (savedInstanceState == null) {
            // Create an instance of the fragment
            AddWordOnDataBase addWordOnDataBaseFragment = new AddWordOnDataBase();

            // Get the FragmentManager
            FragmentManager fragmentManager = getSupportFragmentManager();

            // Begin a fragment transaction
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            // Replace the content frame with the new fragment
            fragmentTransaction.replace(R.id.fragment_container, addWordOnDataBaseFragment);

            // Commit the transaction
            fragmentTransaction.commit();
        }
    }
}
