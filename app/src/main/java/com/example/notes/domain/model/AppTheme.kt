package com.example.notes.domain.model

enum class AppTheme(
    val key: String,
) {
    DARK("dark"),
    LIGHT("light");

    companion object {

        fun fromKey(key: String?): AppTheme =
            entries.find { it.key == key } ?: DARK
    }
}
