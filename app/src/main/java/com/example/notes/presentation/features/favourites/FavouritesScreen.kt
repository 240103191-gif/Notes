package com.example.notes.presentation.features.favourites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.notes.R
import com.example.notes.domain.model.LoadState
import com.example.notes.domain.model.Note
import com.example.notes.presentation.components.CollectionTopBar
import com.example.notes.presentation.components.EmptyStateView
import com.example.notes.presentation.components.ErrorStateView
import com.example.notes.presentation.components.NoteItemCard
import com.example.notes.presentation.components.NoteShimmerGrid
import com.example.notes.presentation.theme.NotesTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavouritesScreen(
    viewModel: FavouritesViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    FavouritesContent(
        state = state,
        onBackClick = viewModel::onBackClick,
        onNoteClick = viewModel::onNoteClick,
        onRemoveFavouriteClick = viewModel::onRemoveFavouriteClick,
        onRetryClick = viewModel::onRetryClick,
    )
}

@Composable
private fun FavouritesContent(
    state: FavouritesState,
    onBackClick: () -> Unit,
    onNoteClick: (Long) -> Unit,
    onRemoveFavouriteClick: (Note) -> Unit,
    onRetryClick: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CollectionTopBar(
                title = stringResource(R.string.favourites_title),
                notesState = state.notesState,
                onBackClick = onBackClick,
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            when (val notesState = state.notesState) {
                is LoadState.Loading -> NoteShimmerGrid(
                    modifier = Modifier.padding(
                        start = 16.dp,
                        top = 4.dp,
                        end = 16.dp,
                    ),
                )
                is LoadState.Empty -> EmptyStateView(
                    message = stringResource(R.string.empty_favourites_message),
                )
                is LoadState.Error -> ErrorStateView(
                    message = stringResource(R.string.error_loading_notes),
                    onRetryClick = onRetryClick,
                )
                is LoadState.Success -> FavouritesItemsGrid(
                    notes = notesState.data,
                    onNoteClick = onNoteClick,
                    onRemoveFavouriteClick = onRemoveFavouriteClick,
                )
            }
        }
    }
}

@Composable
private fun FavouritesItemsGrid(
    notes: List<Note>,
    onNoteClick: (Long) -> Unit,
    onRemoveFavouriteClick: (Note) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 16.dp,
            top = 4.dp,
            end = 16.dp,
            bottom = 32.dp,
        ),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(
            items = notes,
            key = { it.id },
        ) { note ->
            NoteItemCard(
                note = note,
                onClick = { onNoteClick(note.id) },
                onFavouriteClick = { onRemoveFavouriteClick(note) },
            )
        }
    }
}

private val sampleFavouriteNotes = listOf(
    Note(
        id = 1L,
        title = "Text note 31/10",
        content = "Everything Claude code",
        createdAt = 1700000000000L,
        formattedDate = "18.11.2026, 16:40",
        isFavourite = true,
    ),
    Note(
        id = 3L,
        title = "Architecture sync",
        content = "Meeting summary: finalize dark and light palettes, 16dp spacing and Room entities.",
        createdAt = 1700000800000L,
        formattedDate = "17.11.2026, 20:05",
        isFavourite = true,
    ),
)

@Preview(showBackground = true)
@Composable
private fun FavouritesScreenPreview() {
    NotesTheme {
        FavouritesContent(
            state = FavouritesState(
                notesState = LoadState.Success(sampleFavouriteNotes),
            ),
            onBackClick = {},
            onNoteClick = {},
            onRemoveFavouriteClick = {},
            onRetryClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FavouritesEmptyScreenPreview() {
    NotesTheme {
        FavouritesContent(
            state = FavouritesState(
                notesState = LoadState.Empty,
            ),
            onBackClick = {},
            onNoteClick = {},
            onRemoveFavouriteClick = {},
            onRetryClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FavouritesShimmerScreenPreview() {
    NotesTheme {
        FavouritesContent(
            state = FavouritesState(
                notesState = LoadState.Loading,
            ),
            onBackClick = {},
            onNoteClick = {},
            onRemoveFavouriteClick = {},
            onRetryClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FavouritesErrorScreenPreview() {
    NotesTheme {
        FavouritesContent(
            state = FavouritesState(
                notesState = LoadState.Error(),
            ),
            onBackClick = {},
            onNoteClick = {},
            onRemoveFavouriteClick = {},
            onRetryClick = {},
        )
    }
}
