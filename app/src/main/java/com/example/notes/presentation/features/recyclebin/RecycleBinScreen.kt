package com.example.notes.presentation.features.recyclebin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.notes.R
import com.example.notes.domain.model.LoadState
import com.example.notes.domain.model.Note
import com.example.notes.presentation.components.CollectionTopBar
import com.example.notes.presentation.components.EmptyStateView
import com.example.notes.presentation.components.ErrorStateView
import com.example.notes.presentation.components.NoteShimmerGrid
import com.example.notes.presentation.theme.NotesTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun RecycleBinScreen(
    viewModel: RecycleBinViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    RecycleBinContent(
        state = state,
        onBackClick = viewModel::onBackClick,
        onRestoreClick = viewModel::onRestoreClick,
        onDeletePermanentlyClick = viewModel::onDeletePermanentlyClick,
        onRetryClick = viewModel::onRetryClick,
    )
}

@Composable
private fun RecycleBinContent(
    state: RecycleBinState,
    onBackClick: () -> Unit,
    onRestoreClick: (Note) -> Unit,
    onDeletePermanentlyClick: (Note) -> Unit,
    onRetryClick: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CollectionTopBar(
                title = stringResource(R.string.recycle_bin_title),
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
                    message = stringResource(R.string.empty_recycle_bin_message),
                )
                is LoadState.Error -> ErrorStateView(
                    message = stringResource(R.string.error_loading_notes),
                    onRetryClick = onRetryClick,
                )
                is LoadState.Success -> RecycleBinItemsGrid(
                    notes = notesState.data,
                    onRestoreClick = onRestoreClick,
                    onDeletePermanentlyClick = onDeletePermanentlyClick,
                )
            }
        }
    }
}

@Composable
private fun RecycleBinItemsGrid(
    notes: List<Note>,
    onRestoreClick: (Note) -> Unit,
    onDeletePermanentlyClick: (Note) -> Unit,
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
            RecycleBinNoteCard(
                note = note,
                onRestoreClick = { onRestoreClick(note) },
                onDeletePermanentlyClick = { onDeletePermanentlyClick(note) },
            )
        }
    }
}

@Composable
private fun RecycleBinNoteCard(
    note: Note,
    onRestoreClick: () -> Unit,
    onDeletePermanentlyClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(16.dp)

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(168.dp)
                .clip(shape)
                .background(MaterialTheme.colorScheme.surface)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = shape,
                )
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = note.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(onClick = onRestoreClick),
                    painter = painterResource(R.drawable.ic_restore),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(onClick = onDeletePermanentlyClick),
                    painter = painterResource(R.drawable.ic_delete),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier.padding(horizontal = 4.dp),
            text = note.title.ifBlank { stringResource(R.string.untitled_note) },
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
            ),
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = note.formattedDate,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            textAlign = TextAlign.Center,
        )
    }
}

private val sampleDeletedNotes = listOf(
    Note(
        id = 10L,
        title = "Old grocery list",
        content = "Apples, oats, almond milk, cinnamon, honey.",
        createdAt = 1700000100000L,
        formattedDate = "14.11.2026, 18:10",
        isDeleted = true,
    ),
    Note(
        id = 11L,
        title = "Draft idea",
        content = "Initial sketch for weekend reading schedule and summary.",
        createdAt = 1700000200000L,
        formattedDate = "12.11.2026, 11:30",
        isDeleted = true,
    ),
)

@Preview(showBackground = true)
@Composable
private fun RecycleBinScreenPreview() {
    NotesTheme {
        RecycleBinContent(
            state = RecycleBinState(
                notesState = LoadState.Success(sampleDeletedNotes),
            ),
            onBackClick = {},
            onRestoreClick = {},
            onDeletePermanentlyClick = {},
            onRetryClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RecycleBinEmptyScreenPreview() {
    NotesTheme {
        RecycleBinContent(
            state = RecycleBinState(
                notesState = LoadState.Empty,
            ),
            onBackClick = {},
            onRestoreClick = {},
            onDeletePermanentlyClick = {},
            onRetryClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RecycleBinShimmerScreenPreview() {
    NotesTheme {
        RecycleBinContent(
            state = RecycleBinState(
                notesState = LoadState.Loading,
            ),
            onBackClick = {},
            onRestoreClick = {},
            onDeletePermanentlyClick = {},
            onRetryClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RecycleBinErrorScreenPreview() {
    NotesTheme {
        RecycleBinContent(
            state = RecycleBinState(
                notesState = LoadState.Error(),
            ),
            onBackClick = {},
            onRestoreClick = {},
            onDeletePermanentlyClick = {},
            onRetryClick = {},
        )
    }
}
