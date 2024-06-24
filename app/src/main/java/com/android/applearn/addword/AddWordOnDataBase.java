package com.android.applearn.addword;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.applearn.R;
import com.android.applearn.database.DataBase;
import com.android.applearn.language.Language;
import com.android.applearn.model.TableViewModel;

import java.util.List;

public class AddWordOnDataBase extends Fragment {

    private EditText wordEditText;
    private EditText translateEditText;
    private Spinner wordLanguageSpinner;
    private Spinner translateLanguageSpinner;
    private Spinner tableSpinner;
    private Language language;
    private DataBase dbHelper;
    private TableViewModel tableViewModel;
    private ArrayAdapter<String> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_word_data_base, container, false);
        wordEditText = view.findViewById(R.id.wordEditText);
        translateEditText = view.findViewById(R.id.translateEditText);
        wordLanguageSpinner = view.findViewById(R.id.wordLanguageSpinner);
        translateLanguageSpinner = view.findViewById(R.id.spinner2);
        tableSpinner = view.findViewById(R.id.spinner);
        Button addButton = view.findViewById(R.id.addData);

        language = new Language(getContext()); // Pass context to Language
        dbHelper = new DataBase(getContext());

        populateTableSpinner();

        setDrawableRightClickListener(wordEditText, wordLanguageSpinner, true);
        setDrawableRightClickListener(translateEditText, translateLanguageSpinner, false);

        addButton.setOnClickListener(v -> addWordToDatabase());

        tableViewModel = new ViewModelProvider(requireActivity()).get(TableViewModel.class);
        tableViewModel.getNewTableName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String tableName) {
                adapter.add(tableName);
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    private void populateTableSpinner() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<String> tables = dbHelper.getTableNames(db);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, tables);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tableSpinner.setAdapter(adapter);
    }

    private void addWordToDatabase() {
        String selectedTable = tableSpinner.getSelectedItem().toString();
        String word = wordEditText.getText().toString().trim();
        String translation = translateEditText.getText().toString().trim();

        if (!word.isEmpty() && !translation.isEmpty()) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            dbHelper.insertWord(db, selectedTable, word, translation);
            wordEditText.setText("");
            translateEditText.setText("");
            Toast.makeText(getContext(), "Words added successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Please enter both word and translation", Toast.LENGTH_SHORT).show();
        }
    }

    private void setDrawableRightClickListener(EditText editText, Spinner spinner, boolean isWordEditText) {
        Drawable drawable = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_more_vert);
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            editText.setCompoundDrawables(null, null, drawable, null);
        }

        editText.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP && event.getRawX() >= (editText.getRight() - drawable.getBounds().width())) {
                if (isWordEditText) {
                    language.showWordLanguageMenu(v, spinner, editText);
                } else {
                    language.showTranslationLanguageMenu(v, spinner, editText);
                }
                return true;
            }
            return false;
        });
    }

}