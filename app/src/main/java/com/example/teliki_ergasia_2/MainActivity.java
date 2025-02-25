package com.example.teliki_ergasia_2;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private Spinner languageSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Εφαρμογή αποθηκευμένης γλώσσας
        String savedLang = getSharedPreferences("AppSettings", MODE_PRIVATE).getString("Language", "en");
        // Έλεγχος αν η τρέχουσα γλώσσα είναι διαφορετική από την αποθηκευμένη
        if (!Locale.getDefault().getLanguage().equals(savedLang)) {
            setLocale(savedLang, false); // Ανανεώνει τη γλώσσα χωρίς να ανανεώσει τη σελίδα
        }

        setContentView(R.layout.activity_main);

        // Ρύθμιση Padding για το UI με το Edge-to-Edge
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Αρχικοποίηση Spinner για επιλογή γλώσσας
        languageSpinner = findViewById(R.id.language_spinner);
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLang = "en";
                switch (position) {
                    case 0:
                        selectedLang = "en"; // Αγγλικά
                        break;
                    case 1:
                        selectedLang = "el"; // Ελληνικά
                        break;
                    case 2:
                        selectedLang = "de"; // Γερμανικά
                        break;
                }
                // Αλλαγή γλώσσας μόνο αν είναι διαφορετική από την αποθηκευμένη
                if (!selectedLang.equals(savedLang)) {
                    setLocale(selectedLang, true); // Αλλαγή γλώσσας με ανανέωση
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Καμία ενέργεια αν δεν επιλέχθηκε γλώσσα
            }
        });
        // Ρύθμιση του Spinner στη σωστή γλώσσα
        setSpinnerSelection(languageSpinner, savedLang);

        //κουμπί για μετάβαση στη σελίδα στατιστικών
        Button statisticsButton = findViewById(R.id.button_statistics);
        statisticsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
            startActivity(intent);
        });
    }

    // Μέθοδος μετάβασης στις ιστορίες
    public void start(View view){
        Intent intent = new Intent(this, StoriesActivity.class);
        startActivity(intent);
    }

    // Μέθοδος αλλαγής γλώσσας
    private void setLocale(String langCode, boolean refresh) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);

        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());

        // Αποθήκευση της επιλεγμένης γλώσσας
        getSharedPreferences("AppSettings", MODE_PRIVATE).edit().putString("Language", langCode).apply();

        // Ανανέωση Activity μόνο αν είναι απαραίτητο
        if (refresh) {
            Intent refreshIntent = new Intent(this, MainActivity.class);
            startActivity(refreshIntent);
            finish();
        }
    }

    // Μέθοδος ρύθμισης του Spinner στην αποθηκευμένη γλώσσα
    private void setSpinnerSelection(Spinner spinner, String langCode) {
        int position = 0; // Default: Αγγλικά
        switch (langCode) {
            case "el":
                position = 1; // Ελληνικά
                break;
            case "de":
                position = 2; // Γερμανικά
                break;
        }
        spinner.setSelection(position);
    }



}