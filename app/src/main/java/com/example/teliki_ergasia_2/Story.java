package com.example.teliki_ergasia_2;

// Η κλάση Story αναπαριστά το μοντέλο δεδομένων για μια ιστορία
public class Story {
    // Ιδιότητες της ιστορίας
    private String author;
    private String image;
    private String text;
    private String title;
    private int totalPlays;


    // Empty constructor (απαραίτητο για το Firebase)
    // Η Firebase απαιτεί έναν no-argument constructor για την αυτόματη δημιουργία αντικειμένων
    public Story() {}

    // Constructor για τίτλο και totalPlays
    public Story(String title, int totalPlays) {
        this.title = title;
        this.totalPlays = totalPlays;
    }

    // Getters και Setters
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTotalPlays() {
        return totalPlays;
    }

    public void setTotalPlays(int totalPlays) {
        this.totalPlays = totalPlays;
    }
}
