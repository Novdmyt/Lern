package com.android.applearn.learn;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.applearn.R;
import com.android.applearn.database.DataBase;
import com.android.applearn.database.Word;
import com.android.applearn.setting.SettingApp;

import java.util.Collections;
import java.util.List;

public class TestWord extends Fragment {

    private Spinner tableSpinner;
    private EditText wordTextView;
    private EditText translateEditText;
    private Button checkButton;
    private Button helpButton;
    private ImageButton speakerButton;
    private Switch randomOrderSwitch;

    private DataBase dbHelper;
    private SQLiteDatabase db;
    private List<Word> words;
    private int currentIndex = 0;
    private boolean randomOrder = false;
    private TextToSpeech textToSpeech;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_word, container, false);

        tableSpinner = view.findViewById(R.id.spinner3);
        translateEditText = view.findViewById(R.id.editText);
        wordTextView  = view.findViewById(R.id.translateEditText);
        checkButton = view.findViewById(R.id.buttonCheck);
        helpButton = view.findViewById(R.id.buttonHelp);
        speakerButton = view.findViewById(R.id.imageSpeaker);
        randomOrderSwitch = view.findViewById(R.id.randomOrderSwitch);

        dbHelper = new DataBase(getActivity());
        db = dbHelper.getWritableDatabase();

        textToSpeech = new TextToSpeech(getActivity(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(SettingApp.getSelectedLanguage());
            } else {
                Log.d("TestWord", "TextToSpeech initialization failed.");
            }
        });


        loadTableNames();
        tableSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTable = (String) tableSpinner.getSelectedItem();
                if (selectedTable != null && !selectedTable.isEmpty()) {
                    loadWordsFromTable(selectedTable);
                } else {
                    Toast.makeText(getActivity(), "Please select a table", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        randomOrderSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            randomOrder = isChecked;
            if (words != null && !words.isEmpty()) {
                if (randomOrder) {
                    Collections.shuffle(words);
                } else {
                    Collections.sort(words, (w1, w2) -> w1.getWord().compareTo(w2.getWord())); // Sort by word
                }
                currentIndex = 0;
                showNextWord();
            }
        });

        speakerButton.setOnClickListener(v -> {
            Log.d("TestWord", "Speaker button clicked.");
            speakCurrentWord();
        });

        checkButton.setOnClickListener(v -> checkTranslation());
        helpButton.setOnClickListener(v -> showHelp());

        return view;
    }

    private void loadTableNames() {
        List<String> tableNames = dbHelper.getTableNames(db);
        tableNames.add(0, ""); // Add empty option at the beginning
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, tableNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tableSpinner.setAdapter(adapter);
    }

    private void loadWordsFromTable(String tableName) {
        words = dbHelper.getWordsFromTable(db, tableName);
        currentIndex = 0;
        if (randomOrder) {
            Collections.shuffle(words);
        }
        showNextWord();
    }

    private void showNextWord() {
        if (words != null && !words.isEmpty()) {
            wordTextView.setText("");
            translateEditText.setText(words.get(currentIndex).getTranslation());
        }
    }

    public void checkTranslation() {
        if (words != null && !words.isEmpty()) {
            String userWord = wordTextView.getText().toString().trim();
            String correctWord = words.get(currentIndex).getWord().trim();

            if (userWord.equalsIgnoreCase(correctWord)) {
                Toast.makeText(getActivity(), "Correct!", Toast.LENGTH_SHORT).show();
                currentIndex++;
                if (currentIndex >= words.size()) {
                    currentIndex = 0;
                    if (randomOrder) {
                        Collections.shuffle(words);
                    }
                    Toast.makeText(getActivity(), "All words checked! Starting over.", Toast.LENGTH_SHORT).show();
                }
                wordTextView.setTextColor(Color.BLACK); // Reset text color to black
                showNextWord();
            } else {
                wordTextView.setTextColor(Color.RED); // Set text color to red
                Toast.makeText(getActivity(), "Incorrect! Try again.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "No words loaded!", Toast.LENGTH_SHORT).show();
        }
    }

    public void showHelp() {
        if (words != null && !words.isEmpty()) {
            wordTextView.setText(words.get(currentIndex).getWord());
        }
    }

    private void speakCurrentWord() {
        if (words != null && !words.isEmpty()) {
            String word = words.get(currentIndex).getWord();
            Log.d("TestWord", "Speaking word: " + word);
            textToSpeech.setLanguage(SettingApp.getSelectedLanguage());
            textToSpeech.speak(word, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            Log.d("TestWord", "Words list is empty or null.");
        }
    }
}
