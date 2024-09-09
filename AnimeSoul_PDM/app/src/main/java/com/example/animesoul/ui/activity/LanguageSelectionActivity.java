package com.example.animesoul.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.animesoul.R;

public class LanguageSelectionActivity extends AppCompatActivity {

    private RadioButton defaultRadioButton, japaneseRadioButton;
    private TextView titleTextView;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_selection);

        sharedPreferences = getSharedPreferences("AnimeSoulPreferences", MODE_PRIVATE);

        titleTextView = findViewById(R.id.textView);
        defaultRadioButton = findViewById(R.id.radio_default);
        japaneseRadioButton = findViewById(R.id.radio_japanese);
        Button nextButton = findViewById(R.id.button_next);

        // Imposta il titolo iniziale
        updateTitle();

        // Listener per il cambiamento dei radiobutton
        RadioGroup radioGroup = findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> updateTitle());

        nextButton.setOnClickListener(v -> {
            saveLanguagePreference();
            navigateToMainActivity();
        });
    }

    private void updateTitle() {
        if (defaultRadioButton.isChecked()) {
            titleTextView.setText(getString(R.string.app_nameDef));
        } else if (japaneseRadioButton.isChecked()) {
            titleTextView.setText(getString(R.string.app_nameJap));
        }
    }

    private void saveLanguagePreference() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (defaultRadioButton.isChecked()) {
            editor.putString("title_language", "default");
        } else if (japaneseRadioButton.isChecked()) {
            editor.putString("title_language", "japanese");
        }
        editor.putBoolean("language_selected", true);  // Flag per indicare che la selezione è stata fatta
        editor.apply();
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(LanguageSelectionActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
