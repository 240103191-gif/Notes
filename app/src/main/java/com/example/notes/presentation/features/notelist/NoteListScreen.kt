package com.example.notes.presentation.features.notelist

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
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
import androidx.compose.ui.res.pluralStringResource
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
import com.example.notes.domain.model.Quote
import com.example.notes.presentation.components.ErrorStateView
import com.example.notes.presentation.components.NotesFab
import com.example.notes.presentation.components.shimmer
import com.example.notes.presentation.features.theme.ThemeChangeBottomSheet
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
        onRetryNotesClick = viewModel::onRetryNotesClick,
        onRetryQuoteClick = viewModel::onRetryQuoteClick,
        onThemeClick = viewModel::onThemeClick,
        onDismissThemeSheet = viewModel::onDismissThemeSheet,
    )
}

@Composable
private fun NoteListContent(
    state: NoteListState,
    onCreateClick: () -> Unit,
    onNoteClick: (Long) -> Unit,
    onRetryNotesClick: () -> Unit,
    onRetryQuoteClick: () -> Unit,
    onThemeClick: () -> Unit,
    onDismissThemeSheet: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            NoteListTopBar(
                notesState = state.notesState,
                onThemeClick = onThemeClick,
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
            when (val notesState = state.notesState) {
                is LoadState.Loading -> NoteListShimmerView()
                is LoadState.Error -> ErrorStateView(
                    message = stringResource(R.string.error_loading_notes),
                    onRetryClick = onRetryNotesClick,
                )
                is LoadState.Success -> NoteListItemsView(
                    quoteState = state.quoteState,
                    notes = notesState.data,
                    onNoteClick = onNoteClick,
                    onRetryQuoteClick = onRetryQuoteClick,
                )
            }
        }
    }

    if (state.isThemeSheetVisible) ThemeChangeBottomSheet(
        onDismiss = onDismissThemeSheet,
    )
}

@Composable
private fun NoteListTopBar(
    notesState: LoadState<List<Note>>,
    onThemeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val subtitle = when (notesState) {
        is LoadState.Loading -> stringResource(R.string.notes_loading)
        is LoadState.Success -> pluralStringResource(
            R.plurals.notes_count,
            notesState.data.size,
            notesState.data.size,
        )
        is LoadState.Error -> pluralStringResource(R.plurals.notes_count, 0, 0)
    }

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
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(R.string.all_notes_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(24.dp)
                    .clickable(onClick = onThemeClick),
                painter = painterResource(R.drawable.ic_palette),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun NoteListShimmerView(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 16.dp, top = 4.dp, end = 16.dp),
    ) {
        QuoteShimmerCard()

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            NoteShimmerItem(
                firstLineFraction = 0.8f,
                secondLineFraction = 0.6f,
                thirdLineFraction = 0.45f,
                titleFraction = 0.7f,
                modifier = Modifier.weight(1f),
            )
            NoteShimmerItem(
                firstLineFraction = 0.85f,
                secondLineFraction = 0.7f,
                thirdLineFraction = null,
                titleFraction = 0.65f,
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            NoteShimmerItem(
                firstLineFraction = 0.75f,
                secondLineFraction = 0.5f,
                thirdLineFraction = null,
                titleFraction = 0.72f,
                modifier = Modifier.weight(1f),
            )
            NoteShimmerItem(
                firstLineFraction = 0.82f,
                secondLineFraction = 0.58f,
                thirdLineFraction = null,
                titleFraction = 0.62f,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun NoteShimmerItem(
    firstLineFraction: Float,
    secondLineFraction: Float,
    thirdLineFraction: Float?,
    titleFraction: Float,
    modifier: Modifier = Modifier,
) {
    val cardShape = RoundedCornerShape(16.dp)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(168.dp)
                .clip(cardShape)
                .background(MaterialTheme.colorScheme.surface)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = cardShape,
                )
                .padding(14.dp),
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(firstLineFraction)
                    .height(14.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmer(),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(secondLineFraction)
                    .height(14.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmer(),
            )
            if (thirdLineFraction != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth(thirdLineFraction)
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmer(),
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Spacer(
            modifier = Modifier
                .fillMaxWidth(titleFraction)
                .height(14.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmer(),
        )
        Spacer(modifier = Modifier.height(6.dp))
        Spacer(
            modifier = Modifier
                .fillMaxWidth(0.55f)
                .height(11.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmer(),
        )
    }
}

@Composable
private fun QuoteShimmerCard(
    modifier: Modifier = Modifier,
) {
    val quoteShape = RoundedCornerShape(14.dp)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(quoteShape)
            .background(MaterialTheme.colorScheme.surface)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = quoteShape,
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(14.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmer(),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Spacer(
            modifier = Modifier
                .fillMaxWidth(0.65f)
                .height(14.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmer(),
        )
        Spacer(modifier = Modifier.height(10.dp))
        Spacer(
            modifier = Modifier
                .fillMaxWidth(0.35f)
                .height(11.dp)
                .align(Alignment.End)
                .clip(RoundedCornerShape(4.dp))
                .shimmer(),
        )
    }
}

@Composable
private fun NoteListEmptyView(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.empty_notes_message),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun NoteListItemsView(
    quoteState: LoadState<Quote>,
    notes: List<Note>,
    onNoteClick: (Long) -> Unit,
    onRetryQuoteClick: () -> Unit,
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
        item(span = { GridItemSpan(maxLineSpan) }) {
            when (quoteState) {
                is LoadState.Loading -> QuoteShimmerCard()
                is LoadState.Success -> QuoteCard(quote = quoteState.data)
                is LoadState.Error -> QuoteErrorCard(onRetryClick = onRetryQuoteClick)
            }
        }

        if (notes.isEmpty()) item(span = { GridItemSpan(maxLineSpan) }) {
            NoteListEmptyView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(360.dp),
            )
        } else items(
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
private fun QuoteErrorCard(
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cardShape = RoundedCornerShape(14.dp)
    val buttonShape = RoundedCornerShape(10.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(MaterialTheme.colorScheme.surface)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = cardShape,
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.error_loading_quote),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.width(12.dp))
        Box(
            modifier = Modifier
                .clip(buttonShape)
                .background(MaterialTheme.colorScheme.primary)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = buttonShape,
                )
                .clickable(onClick = onRetryClick)
                .padding(horizontal = 14.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.retry_button),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
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

private val sampleQuote = Quote(
    id = 1L,
    quote = "Simplicity is the ultimate sophistication.",
    author = "Leonardo da Vinci",
)

private val sampleNotes = listOf(
    Note(
        id = 1L,
        title = "Text note 31/10",
        content = "Everything Claude code",
        createdAt = 1700000000000L,
        formattedDate = "18.11.2026, 16:40",
    ),
    Note(
        id = 2L,
        title = "Template and plan",
        content = "Spec kit helps structure clean project specifications and plans.",
        createdAt = 1700000500000L,
        formattedDate = "18.11.2026, 14:15",
    ),
    Note(
        id = 3L,
        title = "Architecture sync",
        content = "Meeting summary: finalize dark and light palettes, 16dp spacing and Room entities.",
        createdAt = 1700000800000L,
        formattedDate = "17.11.2026, 20:05",
    ),
    Note(
        id = 4L,
        title = "Shopping list",
        content = "Milk, coffee beans, sourdough bread, dark chocolate, sparkling water.",
        createdAt = 1700001000000L,
        formattedDate = "15.11.2026, 09:20",
    ),
)

private val noteListStateStub = NoteListState(
    quoteState = LoadState.Success(sampleQuote),
    notesState = LoadState.Success(sampleNotes),
)

@Preview(showBackground = true)
@Composable
private fun NoteListScreenPreview() {
    NotesTheme {
        NoteListContent(
            state = noteListStateStub,
            onCreateClick = {},
            onNoteClick = {},
            onRetryNotesClick = {},
            onRetryQuoteClick = {},
            onThemeClick = {},
            onDismissThemeSheet = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NoteListQuoteErrorScreenPreview() {
    NotesTheme {
        NoteListContent(
            state = noteListStateStub.copy(quoteState = LoadState.Error()),
            onCreateClick = {},
            onNoteClick = {},
            onRetryNotesClick = {},
            onRetryQuoteClick = {},
            onThemeClick = {},
            onDismissThemeSheet = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NoteListEmptyScreenPreview() {
    NotesTheme {
        NoteListContent(
            state = NoteListState(
                quoteState = LoadState.Success(sampleQuote),
                notesState = LoadState.Success(emptyList()),
            ),
            onCreateClick = {},
            onNoteClick = {},
            onRetryNotesClick = {},
            onRetryQuoteClick = {},
            onThemeClick = {},
            onDismissThemeSheet = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NoteListShimmerScreenPreview() {
    NotesTheme {
        NoteListContent(
            state = NoteListState(
                quoteState = LoadState.Loading,
                notesState = LoadState.Loading,
            ),
            onCreateClick = {},
            onNoteClick = {},
            onRetryNotesClick = {},
            onRetryQuoteClick = {},
            onThemeClick = {},
            onDismissThemeSheet = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NoteListErrorScreenPreview() {
    NotesTheme {
        NoteListContent(
            state = NoteListState(
                quoteState = LoadState.Error(),
                notesState = LoadState.Error(),
            ),
            onCreateClick = {},
            onNoteClick = {},
            onRetryNotesClick = {},
            onRetryQuoteClick = {},
            onThemeClick = {},
            onDismissThemeSheet = {},
        )
    }
}
