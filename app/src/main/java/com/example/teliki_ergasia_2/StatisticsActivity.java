package com.example.teliki_ergasia_2;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    private TextView story1Title, story1Plays;
    private TextView story2Title, story2Plays;
    private TextView story3Title, story3Plays;
    private TextView story4Title, story4Plays;
    private TextView story5Title, story5Plays;
    private LinearLayout linearLayoutFavorites;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_statistics);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Σύνδεση των TextView με τα αντίστοιχα Views για εμφάνιση τίτλων και play counts των ιστοριών
        story1Title = findViewById(R.id.story1Title);
        story1Plays = findViewById(R.id.story1Plays);
        story2Title = findViewById(R.id.story2Title);
        story2Plays = findViewById(R.id.story2Plays);
        story3Title = findViewById(R.id.story3Title);
        story3Plays = findViewById(R.id.story3Plays);
        story4Title = findViewById(R.id.story4Title);
        story4Plays = findViewById(R.id.story4Plays);
        story5Title = findViewById(R.id.story5Title);
        story5Plays = findViewById(R.id.story5Plays);


        // Σύνδεση με τη βάση δεδομένων Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("stories");

        // Σύνδεση με το LinearLayout για την εμφάνιση των αγαπημένων ιστοριών
        linearLayoutFavorites = findViewById(R.id.linearLayoutFavorites);

        // Κλήση της μεθόδου για να φορτώσουμε τα στατιστικά
        fetchStatistics();
        // Κλήση της μεθόδου για να φορτώσουμε τις κορυφαίες ιστορίες
        loadTopStories();

    }

    // Μέθοδος για την ανάκτηση και εμφάνιση των στατιστικών
    private void fetchStatistics() {
        // Listener για να παρακολουθεί τις αλλαγές στη βάση δεδομένων
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = 0;
                // Διατρέχουμε όλα τα δεδομένα που έχουμε από τη βάση
                for (DataSnapshot storySnapshot : snapshot.getChildren()) {
                    if (count >= 5) break;

                    // Ανάκτηση του τίτλου και των συνολικών plays για κάθε ιστορία
                    String title = storySnapshot.child("title").getValue(String.class);
                    int totalPlays = storySnapshot.child("totalPlays").getValue(Integer.class);

                    // Ενημέρωση των TextViews με τα δεδομένα της κάθε ιστορίας
                    switch (count) {
                        case 0:
                            story1Title.setText(title);
                            story1Plays.setText(String.valueOf(totalPlays));
                            break;
                        case 1:
                            story2Title.setText(title);
                            story2Plays.setText(String.valueOf(totalPlays));
                            break;
                        case 2:
                            story3Title.setText(title);
                            story3Plays.setText(String.valueOf(totalPlays));
                            break;
                        case 3:
                            story4Title.setText(title);
                            story4Plays.setText(String.valueOf(totalPlays));
                            break;
                        case 4:
                            story5Title.setText(title);
                            story5Plays.setText(String.valueOf(totalPlays));
                            break;
                    }
                    count++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Εμφάνιση μηνύματος λάθους αν αποτύχει η ανάκτηση των δεδομένων
                Toast.makeText(StatisticsActivity.this, "Failed to fetch data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Μέθοδος για την ανάκτηση και εμφάνιση των κορυφαίων ιστοριών με τα περισσότερα plays
    private void loadTopStories() {
        // Κλήση για ανάγνωση όλων των ιστοριών από τη βάση δεδομένων
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Story> allStories = new ArrayList<>();

                // Ανάκτηση όλων των ιστοριών από τη βάση δεδομένων
                for (DataSnapshot storySnapshot : snapshot.getChildren()) {
                    String title = storySnapshot.child("title").getValue(String.class);
                    Integer totalPlays = storySnapshot.child("totalPlays").getValue(Integer.class);

                    // Φιλτράρισμα ιστοριών με totalPlays > 0
                    if (title != null && totalPlays != null && totalPlays > 0) {
                        allStories.add(new Story(title, totalPlays));
                    }
                }

                // Ταξινόμηση σε φθίνουσα σειρά με βάση τα totalPlays
                Collections.sort(allStories, (story1, story2) -> story2.getTotalPlays() - story1.getTotalPlays());

                // Εμφάνιση δυναμικά των 3 κορυφαίων ιστοριών ή όσων υπάρχουν
                linearLayoutFavorites.removeAllViews();  // Αφαίρεση προηγούμενων Views
                int count = Math.min(3, allStories.size());

                for (int i = 0; i < count; i++) {
                    Story story = allStories.get(i);

                    // Δημιουργία TextView για την αρίθμηση
                    TextView numberTextView = new TextView(StatisticsActivity.this);
                    numberTextView.setText((i + 1) + ". ");
                    numberTextView.setTextSize(18);
                    numberTextView.setGravity(Gravity.START);
                    numberTextView.setPadding(16, 8, 16, 8);

                    // Δημιουργία TextView για τον τίτλο
                    TextView titleTextView = new TextView(StatisticsActivity.this);
                    titleTextView.setText(story.getTitle());
                    titleTextView.setTextSize(18);
                    titleTextView.setGravity(Gravity.START);
                    titleTextView.setPadding(16, 8, 16, 8);

                    // Προσθήκη του TextView με την αρίθμηση και του τίτλου στο LinearLayout
                    linearLayoutFavorites.addView(numberTextView);
                    linearLayoutFavorites.addView(titleTextView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Εμφάνιση μηνύματος λάθους αν αποτύχει η φόρτωση των κορυφαίων ιστοριών
                Toast.makeText(StatisticsActivity.this, "Failed to load statistics: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}