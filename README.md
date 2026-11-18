# Notes

A simple Android app for writing and keeping notes. You can view all your notes in one place, add new ones, edit or delete them, and see a random quote at the top of the home screen. All notes are saved locally on your phone.

## Group Members

- Kamila Bizhanova

## Main Features

- Show all notes in a 2-column grid
- Create a new note with a title and text
- Open, edit, and delete notes
- Save notes locally on the device
- Random quote card on the home screen
- Dark theme
- Shimmer loading and empty screen when there are no notes

## Tech Stack

- Kotlin
- Jetpack Compose (Material 3)
- ViewModel and StateFlow
- Jetpack Navigation
- Koin
- Room
- Retrofit and OkHttp
- Kotlinx Serialization

## Architecture

The project uses MVVM in a single `:app` module. The code is organized into four main packages:

- `domain` - models (`Note`, `Quote`) and repository interfaces
- `data` - local Room database for notes and remote Retrofit API for quotes
- `di` - Koin setup for dependencies
- `presentation` - Compose screens, ViewModels, navigation, theme, and UI components
