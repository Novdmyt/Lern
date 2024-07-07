package com.android.applearn.util;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.applearn.R;
import com.android.applearn.database.DataBase;
import com.android.applearn.database.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> {

    private List<Word> words;
    private TextToSpeech tts;
    private Locale selectedLanguage;
    private Context context;
    private DataBase dbHelper;
    private String tableName;

    public WordAdapter(Context context, TextToSpeech tts, Locale selectedLanguage, String tableName) {
        this.words = new ArrayList<>();
        this.tts = tts;
        this.selectedLanguage = selectedLanguage;
        this.context = context;
        this.dbHelper = new DataBase(context);
        this.tableName = tableName;
    }

    public void setWords(List<Word> words) {
        this.words = words;
        notifyDataSetChanged();
    }

    public void filter(String query) {
        // Implement filtering logic here
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_word, parent, false);
        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        Word word = words.get(position);
        holder.wordTextView.setText(word.getWord());
        holder.translationTextView.setText(word.getTranslation());

        holder.itemView.setOnClickListener(v -> {
            if (tts != null) {
                tts.setLanguage(selectedLanguage);
                tts.speak(word.getWord(), TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });

        holder.editButton.setOnClickListener(v -> showEditDialog(word));
        holder.deleteButton.setOnClickListener(v -> showDeleteConfirmationDialog(word));
    }

    private void showEditDialog(Word word) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Word");

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_edit_word, null);
        EditText wordEditText = view.findViewById(R.id.editWord);
        EditText translationEditText = view.findViewById(R.id.editTranslation);

        wordEditText.setText(word.getWord());
        translationEditText.setText(word.getTranslation());

        builder.setView(view);
        builder.setPositiveButton("Save", (dialog, which) -> {
            String newWord = wordEditText.getText().toString().trim();
            String newTranslation = translationEditText.getText().toString().trim();
            if (!newWord.isEmpty() && !newTranslation.isEmpty()) {
                updateWord(word.getId(), newWord, newTranslation);
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void showDeleteConfirmationDialog(Word word) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Word");
        builder.setMessage("Are you sure you want to delete this word?");
        builder.setPositiveButton("Yes", (dialog, which) -> deleteWord(word.getId()));
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void updateWord(int id, String newWord, String newTranslation) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Word", newWord);
        values.put("Translate", newTranslation);
        db.update("[" + tableName + "]", values, "ID = ?", new String[]{String.valueOf(id)});
        refreshWords();
    }

    private void deleteWord(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("[" + tableName + "]", "ID = ?", new String[]{String.valueOf(id)});
        refreshWords();
    }

    private void refreshWords() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Word> words = dbHelper.getWordsFromTable(db, tableName);
        setWords(words);
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    static class WordViewHolder extends RecyclerView.ViewHolder {

        TextView wordTextView;
        TextView translationTextView;
        ImageButton editButton;
        ImageButton deleteButton;

        WordViewHolder(@NonNull View itemView) {
            super(itemView);
            wordTextView = itemView.findViewById(R.id.wordTextView);
            translationTextView = itemView.findViewById(R.id.translationTextView);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}