package com.android.applearn.setting;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.applearn.R;
import com.android.applearn.database.Word;

import java.util.List;
import java.util.Locale;

public class SettingApp extends Fragment {

    private Spinner languageSpinner;
    private static TextToSpeech textToSpeech;
    private static List<Word> words;
    private static int currentIndex = 0;
    private SQLiteDatabase db;
    private static Locale selectedLanguage = Locale.ENGLISH;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_app, container, false);

        languageSpinner = view.findViewById(R.id.sprache);

        setupLanguageSpinner();

        textToSpeech = new TextToSpeech(getActivity(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(selectedLanguage);
            } else {
                Log.d("SettingApp", "TextToSpeech initialization failed.");
            }
        });

        return view;
    }

    private void setupLanguageSpinner() {
        // Додайте більше мов, якщо потрібно
        String[] languages = {"English", "German", "French", "Spanish", "Italian"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguageString = (String) languageSpinner.getSelectedItem();
                switch (selectedLanguageString) {
                    case "English":
                        selectedLanguage = Locale.ENGLISH;
                        break;
                    case "German":
                        selectedLanguage = Locale.GERMAN;
                        break;
                    case "French":
                        selectedLanguage = Locale.FRENCH;
                        break;
                    case "Spanish":
                        selectedLanguage = new Locale("es", "ES"); // Іспанська
                        break;
                    case "Italian":
                        selectedLanguage = Locale.ITALIAN;
                        break;
                }
                textToSpeech.setLanguage(selectedLanguage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }



    public static void speakWord(String word) {
        if (textToSpeech != null && word != null && !word.isEmpty()) {
            textToSpeech.speak(word, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            Log.d("SettingApp", "TextToSpeech is not initialized or word is empty.");
        }
    }

    public static Locale getSelectedLanguage() {
        return selectedLanguage;
    }

    @Override
    public void onDestroy() {
        if (db != null && db.isOpen()) {
            db.close();
        }
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}
