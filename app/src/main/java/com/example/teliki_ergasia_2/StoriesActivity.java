package com.example.teliki_ergasia_2;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StoriesActivity extends AppCompatActivity {

    // Δημιουργία RecyclerView και του adapter για την εμφάνιση των δεδομένων στην οθόνη
    private RecyclerView recyclerView;
    private StoryAdapter adapter;
    // Λίστα για την αποθήκευση των ιστοριών
    private List<Story> storiesList;
    // Αναφορά στη βάση δεδομένων Firebase
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stories);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true); // Δηλώνουμε ότι το μέγεθος του RecyclerView είναι σταθερό για καλύτερη απόδοση
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Ορίζουμε την διάταξη για την εμφάνιση των ιστοριών σε κάθετη λίστα

        // Δημιουργία της λίστας ιστοριών και του adapter για το RecyclerView
        storiesList = new ArrayList<>();
        adapter = new StoryAdapter(this, storiesList); // Adapter για την εμφάνιση των ιστοριών
        recyclerView.setAdapter(adapter); // Ρύθμιση του adapter στο RecyclerView

        // Σύνδεση με τη βάση δεδομένων Firebase, στην περιοχή "stories"
        databaseReference = FirebaseDatabase.getInstance().getReference("stories");

        // Φόρτωση δεδομένων από Firebase όταν αλλάξει κάτι
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Καθαρισμός της λίστας πριν την προσθήκη νέων δεδομένων
                storiesList.clear();
                // Διατρέχουμε όλα τα δεδομένα που έχουμε από τη βάση
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Story story = dataSnapshot.getValue(Story.class);
                    // Προσθήκη της ιστορίας στη λίστα
                    storiesList.add(story);
                }
                // Ενημέρωση του adapter για να ανανεώσει την εμφάνιση του RecyclerView
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Χειρισμός σφαλμάτων που μπορεί να προκύψουν κατά τη φόρτωση των δεδομένων
            }
        });
    }
}