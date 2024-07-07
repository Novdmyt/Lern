package com.android.applearn.startapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.applearn.R;
import com.android.applearn.dictionary.DictionaryWord;
import com.android.applearn.learn.TestWord;

public class Dictionary extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dictionary);
        if(savedInstanceState == null){
            DictionaryWord dictionaryWord = new DictionaryWord();

            FragmentManager fragmentManager = getSupportFragmentManager();

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            // Replace the content frame with the new fragment
            fragmentTransaction.replace(R.id.fragment_container, dictionaryWord);

            // Commit the transaction
            fragmentTransaction.commit();
        }
    }
}