package com.android.applearn.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DataBase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Database.db";

    public DataBase(Context context) {
        super(context, Objects.requireNonNull(context.getExternalFilesDir(null)).getAbsolutePath() + "/" + DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Використовується для створення базової структури бази даних, якщо необхідно
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Використовується для оновлення схеми бази даних, якщо необхідно
    }

    public void createTable(SQLiteDatabase db, String tableName) {
        String createTable = "CREATE TABLE [" + tableName + "] (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Word TEXT, " +
                "Translate TEXT)";
        db.execSQL(createTable);
    }

    public boolean doesTableExist(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", new String[]{tableName});
        boolean tableExists = cursor.getCount() > 0;
        cursor.close();
        return tableExists;
    }

    public List<String> getTableNames(SQLiteDatabase db) {
        List<String> tableNames = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name NOT IN ('android_metadata', 'sqlite_sequence')", null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                tableNames.add(cursor.getString(0));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return tableNames;
    }

    public void insertWord(SQLiteDatabase db, String tableName, String word, String translate) {
        ContentValues values = new ContentValues();
        values.put("Word", word);
        values.put("Translate", translate);
        db.insert("[" + tableName + "]", null, values);
    }

    public List<Word> getWordsFromTable(SQLiteDatabase db, String tableName) {
        List<Word> words = new ArrayList<>();
        // Заключаем имя таблицы в квадратные скобки
        String query = "SELECT Word, Translate FROM [" + tableName + "]";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                String word = cursor.getString(cursor.getColumnIndexOrThrow("Word"));
                String translation = cursor.getString(cursor.getColumnIndexOrThrow("Translate"));
                words.add(new Word(word, translation));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return words;
    }

    public List<Word> getAllWords(SQLiteDatabase db) {
        List<Word> words = new ArrayList<>();
        List<String> tableNames = getTableNames(db);
        for (String tableName : tableNames) {
            words.addAll(getWordsFromTable(db, tableName));
        }
        return words;
    }
}