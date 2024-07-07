package com.android.applearn.util;

import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.applearn.R;
import com.android.applearn.database.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> {

    private List<Word> words;
    private TextToSpeech tts;
    private Locale selectedLanguage;

    public WordAdapter(TextToSpeech tts, Locale selectedLanguage) {
        this.words = new ArrayList<>();
        this.tts = tts;
        this.selectedLanguage = selectedLanguage;
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
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    static class WordViewHolder extends RecyclerView.ViewHolder {

        TextView wordTextView;
        TextView translationTextView;

        WordViewHolder(@NonNull View itemView) {
            super(itemView);
            wordTextView = itemView.findViewById(R.id.wordTextView);
            translationTextView = itemView.findViewById(R.id.translationTextView);
        }
    }
}
