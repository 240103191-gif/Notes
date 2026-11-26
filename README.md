# Notes

A simple Android app for writing and keeping notes. You can view all your notes in one place, add new ones, open a note to edit or move it to the recycle bin, mark important notes as favourites, see a random quote at the top of the home screen, and switch between dark and light themes from the top menu. All notes and theme settings are saved locally on your phone.

## Group Members

- Kamila Bizhanova

## Main Features

- Show all active notes in a 2-column grid with a top menu (`Theme`, `Recycle bin`, `Favourites`)
- Create a new note with a title, text, and optional favourite flag
- Open a note on the detail screen to view, edit, toggle favourite status, or move it to the recycle bin
- Favourites screen for quick access to starred notes
- Recycle bin screen to restore deleted notes or delete them permanently
- Full local CRUD persistence with Room database
- Random quote card on the home screen loaded from a remote API with local retry support
- Dark and light themes with a modal bottom sheet and persistent selection in Preferences DataStore
- Shimmer loading, empty state, and error state with retry handling (`LoadState`)

## Tech Stack

- Kotlin
- Jetpack Compose (Material 3)
- ViewModel and StateFlow
- Jetpack Navigation
- Koin
- Room
- Preferences DataStore
- Retrofit and OkHttp
- Kotlinx Serialization

## Architecture

The project uses MVVM and Clean Architecture in a single `:app` module. The code is organized into four main packages:

- `domain` - models (`Note`, `Quote`, `AppTheme`, `Result`, `LoadState`) and repository interfaces
- `data` - local Room database for notes, Preferences DataStore for theme storage, and remote Retrofit API for quotes
- `di` - Koin setup for database, network, DataStore, repositories, and ViewModels
- `presentation` - Compose screens (`NoteListScreen`, `NoteCreateScreen`, `NoteDetailScreen`, `RecycleBinScreen`, `FavouritesScreen`), theme bottom sheet (`ThemeChangeBottomSheet`), ViewModels, navigation, theme, and reusable UI components
