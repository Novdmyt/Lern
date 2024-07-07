package com.android.applearn.dictionary;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.applearn.R;
import com.android.applearn.database.DataBase;
import com.android.applearn.database.Word;
import com.android.applearn.util.WordAdapter;

import java.util.List;
import java.util.Locale;

public class DictionaryWord extends Fragment  {
    private Spinner languageSpinner;
    private Spinner tableSpinner;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private WordAdapter wordAdapter;
    private DataBase dbHelper;
    private SQLiteDatabase db;
    private TextToSpeech tts;
    private Locale selectedLanguage = Locale.GERMAN;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dictionary_word, container, false);


        tableSpinner = view.findViewById(R.id.spinnerCard);
        searchView = view.findViewById(R.id.searchView);
        recyclerView = view.findViewById(R.id.recyclerView);

        dbHelper = new DataBase(getActivity());



        loadTableNames();

        tableSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTable = (String) parent.getItemAtPosition(position);
                loadWordsFromTable(selectedTable);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                wordAdapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                wordAdapter.filter(newText);
                return false;
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        wordAdapter = new WordAdapter(tts, selectedLanguage);
        recyclerView.setAdapter(wordAdapter);

        return view;
    }






    private void loadTableNames() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<String> tableNames = dbHelper.getTableNames(db);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, tableNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tableSpinner.setAdapter(adapter);
    }

    private void loadWordsFromTable(String tableName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Word> words = dbHelper.getWordsFromTable(db, tableName);
        wordAdapter.setWords(words);
    }

    // Call this method whenever a new table is added to update the spinner
    public void refreshTableNames() {
        loadTableNames();
    }
}