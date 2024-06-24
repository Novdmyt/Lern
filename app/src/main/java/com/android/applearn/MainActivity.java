package com.android.applearn;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.applearn.startapp.AddWord;
import com.android.applearn.startapp.CreateCard;
import com.android.applearn.startapp.Dictionary;
import com.android.applearn.startapp.LearnWord;
import com.android.applearn.startapp.Settings;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView newCard = findViewById(R.id.imageView4);
        ImageView addWord = findViewById(R.id.imageView5);
        ImageView playGame = findViewById(R.id.imageView6);
        ImageView dictionary = findViewById(R.id.imageView7);
        ImageView setting = findViewById(R.id.imageView);

        newCard.setOnClickListener(v -> openActivity(CreateCard.class));
        addWord.setOnClickListener(v -> openActivity(AddWord.class));
        playGame.setOnClickListener(v -> openActivity(LearnWord.class));
        dictionary.setOnClickListener(v -> openActivity(Dictionary.class));
        setting.setOnClickListener(v -> openActivity(Settings.class));
    }

    private void openActivity(Class<?> activitiClass) {
        Intent intent = new Intent(this, activitiClass);
        startActivity(intent);
    }

}