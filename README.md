# Notes

A simple Android app for writing and keeping notes. You can view all your notes in one place, add new ones, open a note to edit or delete it, see a random quote at the top of the home screen, and switch between dark and light themes. All notes and theme settings are saved locally on your phone.

## Group Members

- Kamila Bizhanova

## Main Features

- Show all notes in a 2-column grid
- Create a new note with a title and text
- Open a note on the detail screen to view, edit, or delete it
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
- `presentation` - Compose screens (`NoteListScreen`, `NoteCreateScreen`, `NoteDetailScreen`), theme bottom sheet (`ThemeChangeBottomSheet`), ViewModels, navigation, theme, and reusable UI components
