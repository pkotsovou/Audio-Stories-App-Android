package com.example.teliki_ergasia_2;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class AudioStoryActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    private TextToSpeech textToSpeech;
    private TextView storyTitle, storyAuthor, storyText;
    private ImageView storyImage;
    private Button speakerButton;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_audio_story);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        storyTitle = findViewById(R.id.audioStoryTitle);
        storyAuthor = findViewById(R.id.audioStoryAuthor);
        storyText = findViewById(R.id.audioStoryText);
        storyImage = findViewById(R.id.audioStoryImage);
        speakerButton = findViewById(R.id.speakerButton);

        // Λήψη δεδομένων από το Intent
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String author = intent.getStringExtra("author");
        String text = intent.getStringExtra("text");
        String image = intent.getStringExtra("image");

        // Ρύθμιση των δεδομένων στο UI
        storyTitle.setText(title);
        storyAuthor.setText(author);
        storyText.setText(text);
        // Φόρτωση της εικόνας από το drawable με βάση το όνομα που παρέχεται
        int imageResId = getResources().getIdentifier(image, "drawable", getPackageName());
        storyImage.setImageResource(imageResId);

        // Ρύθμιση Firebase αναφοράς
        databaseReference = FirebaseDatabase.getInstance().getReference("stories");

        // Ρύθμιση του TextToSpeech
        textToSpeech = new TextToSpeech(this, this);

        // Ρύθμιση του click listener για το κουμπί αναπαραγωγής
        speakerButton.setOnClickListener(v -> {
            // Λήψη του κειμένου της ιστορίας και αναπαραγωγή του μέσω TTS
            String storyTextToRead = storyText.getText().toString();
            textToSpeech.speak(storyTextToRead, TextToSpeech.QUEUE_FLUSH, null, null);
            // Ενημέρωση του totalPlays στο Firebase
            updateTotalPlays(title);
        });
    }


    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            // Ρύθμιση γλώσσας στο TTS
            int langResult = textToSpeech.setLanguage(Locale.US);
            if (langResult == TextToSpeech.LANG_MISSING_DATA |
                    langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "Language not supported", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "TextToSpeech initialization failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        // Σταμάτημα και αποδέσμευση πόρων του TTS κατά την καταστροφή της Activity
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    private void updateTotalPlays(String title) {
        // Έλεγχος αν ο τίτλος της ιστορίας είναι έγκυρο
        if (title == null || title.isEmpty()) {
            Toast.makeText(this, "Invalid story title!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Προσθήκη listener για ανάγνωση δεδομένων από το Firebase
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean storyFound = false;

                // Αναζήτηση ιστορίας με τον αντίστοιχο τίτλο
                for (DataSnapshot storySnapshot : snapshot.getChildren()) {
                    String storyTitle = storySnapshot.child("title").getValue(String.class);

                    if (storyTitle != null && storyTitle.equals(title)) {
                        storyFound = true;
                        // Ανάκτηση του αριθμού των τρεχόντων ακροάσεων
                        Integer currentPlays = storySnapshot.child("totalPlays").getValue(Integer.class);

                        if (currentPlays == null) {
                            currentPlays = 0; // Εξασφάλιση μη null τιμής
                        }

                        // Ενημέρωση του totalPlays με την νέα τιμή
                        int newTotalPlays = currentPlays + 1;
                        storySnapshot.getRef().child("totalPlays").setValue(newTotalPlays)
                                .addOnSuccessListener(aVoid ->             Log.d("AudioStoryActivity", "Plays updated successfully to: " + newTotalPlays))
                                .addOnFailureListener(e -> Toast.makeText(AudioStoryActivity.this, "Failed to update plays: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                        break;
                    }
                }

                if (!storyFound) {
                    // Ενημέρωση αν η ιστορία δεν βρέθηκε
                    Log.d("AudioStoryActivity", "Story not found in database: " + title);
                    Toast.makeText(AudioStoryActivity.this, "Story not found in database!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Χειρισμός αποτυχιών ανάγνωσης από το Firebase
                Toast.makeText(AudioStoryActivity.this, "Failed to fetch story: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}