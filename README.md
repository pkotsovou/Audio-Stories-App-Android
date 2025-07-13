# 📚 Unipi Audio Stories

A mobile Android app for children’s audio stories with multilingual support (English, Greek, German) and user listening statistics.  
Built with **Java**, **Firebase Realtime Database**, and **Android Studio**.

---

## ✨ **Features**

- 🎙️ **Listen to children’s audio stories** using Text-To-Speech (TTS)
- 🌐 **Switch app language** dynamically (EN | GR | DE)
- 📈 **Track listening statistics** (total plays per story & top 3 stories)
- 🗄️ **Data storage** with Firebase Realtime Database
- 📱 **Optimized for smartphones & tablets** (responsive design with `layout-sw600dp`)

---

## 🔗 **Firebase Database**

Stories and statistics are stored and retrieved from Firebase:  
[View Database](https://console.firebase.google.com/project/teliki-ergasia-2-64472/database/teliki-ergasia-2-64472-default-rtdb/data/~2F)

**Story schema:**
- `title` — story title
- `author` — story author
- `text` — story text
- `image` — image resource
- `totalPlays` — number of plays

---

## ⚙️ **Technologies**

- Java (Android)
- Firebase Realtime Database
- RecyclerView, Intent, Locale
- Text-To-Speech (TTS)
- Responsive layouts (smartphones & tablets)

---

## 📂 **Project Structure**

- `MainActivity` — welcome screen & language switch
- `StoriesActivity` — stories list
- `AudioStoryActivity` — listen to a story
- `StatisticsActivity` — view stats
- `StoryAdapter` — binds data to RecyclerView


