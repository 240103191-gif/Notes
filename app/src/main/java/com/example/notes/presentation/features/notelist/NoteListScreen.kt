package com.example.notes.presentation.features.notelist

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.notes.R
import com.example.notes.domain.model.Note
import com.example.notes.domain.model.Quote
import com.example.notes.presentation.components.NotesFab
import com.example.notes.presentation.theme.NotesTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun NoteListScreen(
    viewModel: NoteListViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    NoteListContent(
        state = state,
        onCreateClick = viewModel::onCreateClick,
        onNoteClick = viewModel::onNoteClick,
    )
}

@Composable
private fun NoteListContent(
    state: NoteListState,
    onCreateClick: () -> Unit,
    onNoteClick: (Long) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            if (state.isLoading || state.notes.isNotEmpty()) NoteListTopBar(
                isLoading = state.isLoading,
                notesCount = state.notes.size,
            )
        },
        floatingActionButton = {
            NotesFab(
                onClick = onCreateClick,
                modifier = Modifier.padding(end = 16.dp, bottom = 24.dp),
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            if (!state.isLoading && state.notes.isNotEmpty()) NoteListItemsView(
                quote = state.quote,
                notes = state.notes,
                onNoteClick = onNoteClick,
            )
        }
    }
}

@Composable
private fun NoteListTopBar(
    isLoading: Boolean,
    notesCount: Int,
    modifier: Modifier = Modifier,
) {
    val subtitle = if (isLoading) stringResource(R.string.notes_loading)
    else pluralStringResource(R.plurals.notes_count, notesCount, notesCount)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(
                start = 16.dp,
                top = 24.dp,
                end = 16.dp,
                bottom = 16.dp,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.all_notes_title),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun NoteListItemsView(
    quote: Quote?,
    notes: List<Note>,
    onNoteClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 16.dp,
            top = 4.dp,
            end = 16.dp,
            bottom = 96.dp,
        ),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        if (quote != null) item(span = { GridItemSpan(maxLineSpan) }) {
            QuoteCard(quote = quote)
        }

        items(
            items = notes,
            key = { it.id },
        ) { note ->
            NoteItemCard(
                note = note,
                onClick = { onNoteClick(note.id) },
            )
        }
    }
}

@Composable
private fun QuoteCard(
    quote: Quote,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(14.dp)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = shape,
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
    ) {
        Text(
            text = stringResource(R.string.quote_text_format, quote.quote),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.quote_author_format, quote.author),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.End,
        )
    }
}

@Composable
private fun NoteItemCard(
    note: Note,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(16.dp)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
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
        ) {
            Text(
                text = note.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 7,
                overflow = TextOverflow.Ellipsis,
            )
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

private val noteListStateStub = NoteListState(
    isLoading = false,
    quote = Quote(
        id = 1L,
        quote = "Simplicity is the ultimate sophistication.",
        author = "Leonardo da Vinci",
    ),
    notes = listOf(
        Note(
            id = 1L,
            title = "First Note",
            content = "Sample note content for testing card layout",
            createdAt = 1700000000000L,
            formattedDate = "18.11.2026, 16:40",
        ),
        Note(
            id = 2L,
            title = "Shopping List",
            content = "Milk, coffee beans, sourdough bread, dark chocolate, sparkling water.",
            createdAt = 1700000500000L,
            formattedDate = "18.11.2026, 14:15",
        ),
        Note(
            id = 3L,
            title = "Architecture sync",
            content = "Meeting summary: finalize dark theme palette, 16dp horizontal spacing and Room entities.",
            createdAt = 1700000800000L,
            formattedDate = "17.11.2026, 20:05",
        ),
        Note(
            id = 4L,
            title = "Reading list",
            content = "Books to read: Clean Architecture, Designing Data-Intensive Applications.",
            createdAt = 1700001000000L,
            formattedDate = "15.11.2026, 09:20",
        ),
    ),
)

@Preview(showBackground = true)
@Composable
private fun NoteListScreenPreview() {
    NotesTheme {
        NoteListContent(
            state = noteListStateStub,
            onCreateClick = {},
            onNoteClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NoteListWithoutQuoteScreenPreview() {
    NotesTheme {
        NoteListContent(
            state = noteListStateStub.copy(quote = null),
            onCreateClick = {},
            onNoteClick = {},
        )
    }
}
