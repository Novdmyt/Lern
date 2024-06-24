package com.android.applearn.addcard;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.applearn.R;
import com.android.applearn.database.DataBase;

public class AddCard extends Fragment {

    private EditText tableNameEditText;
    private Button createTabButton;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_card, container, false);

        tableNameEditText = view.findViewById(R.id.textCard);
        createTabButton = view.findViewById(R.id.button);

        createTabButton.setOnClickListener(v ->{
            String tableName = tableNameEditText.getText().toString().trim();
            if(isValidTableName(tableName)){
                createTable(tableName);
            }else {
                Toast.makeText(getActivity(), "Invalid table name. Ensure it starts with a letter and contains only alphanumeric characters and spaces.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private boolean isValidTableName(String tableName) {
        return tableName.matches("[a-zA-Zа-яА-ЯäöüÄÖÜß][a-zA-Zа-яА-ЯäöüÄÖÜß0-9 ]*");
    }
    private void createTable(String tableName) {
        DataBase dataBase = new DataBase(getActivity());
        SQLiteDatabase db = dataBase.getWritableDatabase();
        if (db != null) {
            if (!dataBase.doesTableExist(db, tableName)) {
                dataBase.createTable(db, tableName);
                Toast.makeText(getActivity(), "Table '" + tableName + "' created successfully.", Toast.LENGTH_LONG).show();
                tableNameEditText.getText().clear(); // Clear the table name EditText
                // refreshTableNamesInFragments(); // Update table names in other fragments
            } else {
                Toast.makeText(getActivity(), "Table '" + tableName + "' already exists.", Toast.LENGTH_SHORT).show();
            }
            db.close();
        } else {
            Toast.makeText(getActivity(), "Failed to create table.", Toast.LENGTH_SHORT).show();
        }
    }

}