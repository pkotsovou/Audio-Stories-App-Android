package com.example.teliki_ergasia_2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;


// Το StoryAdapter είναι ένας προσαρμογέας για το RecyclerView, που διαχειρίζεται τη λίστα των ιστοριών
public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryViewHolder> {
    private Context context;
    private List<Story> storiesList;

    // Constructor του adapter
    public StoryAdapter(Context context, List<Story> storiesList) {
        this.context = context;
        this.storiesList = storiesList;
    }

    // Δημιουργία νέων ViewHolders όταν χρειάζεται
    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.story_item, parent, false);
        return new StoryViewHolder(view);
    }

    // Δέσιμο δεδομένων (binding) σε κάθε ViewHolder
    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {
        Story story = storiesList.get(position); // Παίρνει το αντικείμενο Story της τρέχουσας θέσης
        holder.storyTitle.setText(story.getTitle()); // Εμφανίζει τον τίτλο της ιστορίας
        holder.storyAuthor.setText("Author: " + story.getAuthor()); // Εμφανίζει τον συγγραφέα της ιστορίας

        // Ρύθμιση click listener για το στοιχείο
        View.OnClickListener clickListener = v -> {
            // Δημιουργία intent για να ανοίξει το AudioStoryActivity
            Intent intent = new Intent(context, AudioStoryActivity.class);
            intent.putExtra("title", story.getTitle());
            intent.putExtra("author", story.getAuthor());
            intent.putExtra("text", story.getText());
            intent.putExtra("image", story.getImage());
            context.startActivity(intent);
        };

        // Εφαρμογή του click listener στα στοιχεία του ViewHolder
        holder.storyTitle.setOnClickListener(clickListener);
        holder.storyAuthor.setOnClickListener(clickListener);
        holder.storyImage.setOnClickListener(clickListener);


        // Φόρτωση εικόνας από το drawable με βάση το όνομα που υπάρχει στη Firebase
        int imageId = context.getResources().getIdentifier(story.getImage(), "drawable", context.getPackageName());
        if (imageId != 0) {
            holder.storyImage.setImageResource(imageId); // Φόρτωση της εικόνας
        } else {
            // Εάν η εικόνα δεν βρεθεί, εμφανίζεται μια προεπιλεγμένη εικόνα
            holder.storyImage.setImageResource(R.drawable.audio_stories);
        }
    }

    // Επιστρέφει το μέγεθος της λίστας ιστοριών
    @Override
    public int getItemCount() {
        return storiesList.size();
    }

    // ViewHolder για τη διαχείριση των views κάθε στοιχείου
    public static class StoryViewHolder extends RecyclerView.ViewHolder {
        TextView storyTitle, storyAuthor;
        ImageView storyImage;

        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);
            storyTitle = itemView.findViewById(R.id.storyTitle);
            storyAuthor = itemView.findViewById(R.id.storyAuthor);
            storyImage = itemView.findViewById(R.id.storyImage);
        }
    }
}
