package com.example.notes.presentation.features.notelist

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.notes.R
import com.example.notes.domain.model.LoadState
import com.example.notes.domain.model.Note
import com.example.notes.domain.model.Quote
import com.example.notes.presentation.components.EmptyStateView
import com.example.notes.presentation.components.ErrorStateView
import com.example.notes.presentation.components.NoteItemCard
import com.example.notes.presentation.components.NoteShimmerGrid
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
        onToggleFavouriteClick = viewModel::onToggleFavouriteClick,
        onRetryNotesClick = viewModel::onRetryNotesClick,
        onRetryQuoteClick = viewModel::onRetryQuoteClick,
        onMoreMenuClick = viewModel::onMoreMenuClick,
        onDismissMoreMenu = viewModel::onDismissMoreMenu,
        onThemeClick = viewModel::onThemeClick,
        onRecycleBinClick = viewModel::onRecycleBinClick,
        onFavouritesClick = viewModel::onFavouritesClick,
        onDismissThemeSheet = viewModel::onDismissThemeSheet,
    )
}

@Composable
private fun NoteListContent(
    state: NoteListState,
    onCreateClick: () -> Unit,
    onNoteClick: (Long) -> Unit,
    onToggleFavouriteClick: (Note) -> Unit,
    onRetryNotesClick: () -> Unit,
    onRetryQuoteClick: () -> Unit,
    onMoreMenuClick: () -> Unit,
    onDismissMoreMenu: () -> Unit,
    onThemeClick: () -> Unit,
    onRecycleBinClick: () -> Unit,
    onFavouritesClick: () -> Unit,
    onDismissThemeSheet: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            NoteListTopBar(
                notesState = state.notesState,
                isMoreMenuExpanded = state.isMoreMenuExpanded,
                onMoreMenuClick = onMoreMenuClick,
                onDismissMoreMenu = onDismissMoreMenu,
                onThemeClick = onThemeClick,
                onRecycleBinClick = onRecycleBinClick,
                onFavouritesClick = onFavouritesClick,
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
                is LoadState.Empty -> NoteListItemsView(
                    quoteState = state.quoteState,
                    notes = emptyList(),
                    onNoteClick = onNoteClick,
                    onToggleFavouriteClick = onToggleFavouriteClick,
                    onRetryQuoteClick = onRetryQuoteClick,
                )
                is LoadState.Error -> ErrorStateView(
                    message = stringResource(R.string.error_loading_notes),
                    onRetryClick = onRetryNotesClick,
                )
                is LoadState.Success -> NoteListItemsView(
                    quoteState = state.quoteState,
                    notes = notesState.data,
                    onNoteClick = onNoteClick,
                    onToggleFavouriteClick = onToggleFavouriteClick,
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
    isMoreMenuExpanded: Boolean,
    onMoreMenuClick: () -> Unit,
    onDismissMoreMenu: () -> Unit,
    onThemeClick: () -> Unit,
    onRecycleBinClick: () -> Unit,
    onFavouritesClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val subtitle = when (notesState) {
        is LoadState.Loading -> stringResource(R.string.notes_loading)
        is LoadState.Empty -> pluralStringResource(R.plurals.notes_count, 0, 0)
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
            Box(
                modifier = Modifier.align(Alignment.CenterEnd),
            ) {
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(onClick = onMoreMenuClick),
                    painter = painterResource(R.drawable.ic_more_vert),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                )
                DropdownMenu(
                    expanded = isMoreMenuExpanded,
                    onDismissRequest = onDismissMoreMenu,
                    shape = RoundedCornerShape(14.dp),
                    containerColor = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                    ),
                ) {
                    NoteListMenuItem(
                        iconRes = R.drawable.ic_palette,
                        text = stringResource(R.string.menu_theme),
                        onClick = onThemeClick,
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 16.dp),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant,
                    )
                    NoteListMenuItem(
                        iconRes = R.drawable.ic_delete,
                        text = stringResource(R.string.recycle_bin_title),
                        onClick = onRecycleBinClick,
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 16.dp),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant,
                    )
                    NoteListMenuItem(
                        iconRes = R.drawable.ic_star,
                        text = stringResource(R.string.favourites_title),
                        onClick = onFavouritesClick,
                    )
                }
            }
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
private fun NoteListMenuItem(
    @DrawableRes iconRes: Int,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
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

        NoteShimmerGrid()
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
private fun QuoteEmptyCard(
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(14.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = shape,
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.empty_quote_message),
            style = MaterialTheme.typography.bodyMedium,
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
    onToggleFavouriteClick: (Note) -> Unit,
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
                is LoadState.Empty -> QuoteEmptyCard()
                is LoadState.Success -> QuoteCard(quote = quoteState.data)
                is LoadState.Error -> QuoteErrorCard(onRetryClick = onRetryQuoteClick)
            }
        }

        if (notes.isEmpty()) item(span = { GridItemSpan(maxLineSpan) }) {
            EmptyStateView(
                message = stringResource(R.string.empty_notes_message),
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
                onFavouriteClick = { onToggleFavouriteClick(note) },
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
        isFavourite = true,
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
        isFavourite = true,
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
            onToggleFavouriteClick = {},
            onRetryNotesClick = {},
            onRetryQuoteClick = {},
            onMoreMenuClick = {},
            onDismissMoreMenu = {},
            onThemeClick = {},
            onRecycleBinClick = {},
            onFavouritesClick = {},
            onDismissThemeSheet = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NoteListMenuExpandedPreview() {
    NotesTheme {
        NoteListContent(
            state = noteListStateStub.copy(isMoreMenuExpanded = true),
            onCreateClick = {},
            onNoteClick = {},
            onToggleFavouriteClick = {},
            onRetryNotesClick = {},
            onRetryQuoteClick = {},
            onMoreMenuClick = {},
            onDismissMoreMenu = {},
            onThemeClick = {},
            onRecycleBinClick = {},
            onFavouritesClick = {},
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
            onToggleFavouriteClick = {},
            onRetryNotesClick = {},
            onRetryQuoteClick = {},
            onMoreMenuClick = {},
            onDismissMoreMenu = {},
            onThemeClick = {},
            onRecycleBinClick = {},
            onFavouritesClick = {},
            onDismissThemeSheet = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NoteListQuoteEmptyScreenPreview() {
    NotesTheme {
        NoteListContent(
            state = noteListStateStub.copy(quoteState = LoadState.Empty),
            onCreateClick = {},
            onNoteClick = {},
            onToggleFavouriteClick = {},
            onRetryNotesClick = {},
            onRetryQuoteClick = {},
            onMoreMenuClick = {},
            onDismissMoreMenu = {},
            onThemeClick = {},
            onRecycleBinClick = {},
            onFavouritesClick = {},
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
                notesState = LoadState.Empty,
            ),
            onCreateClick = {},
            onNoteClick = {},
            onToggleFavouriteClick = {},
            onRetryNotesClick = {},
            onRetryQuoteClick = {},
            onMoreMenuClick = {},
            onDismissMoreMenu = {},
            onThemeClick = {},
            onRecycleBinClick = {},
            onFavouritesClick = {},
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
            onToggleFavouriteClick = {},
            onRetryNotesClick = {},
            onRetryQuoteClick = {},
            onMoreMenuClick = {},
            onDismissMoreMenu = {},
            onThemeClick = {},
            onRecycleBinClick = {},
            onFavouritesClick = {},
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
            onToggleFavouriteClick = {},
            onRetryNotesClick = {},
            onRetryQuoteClick = {},
            onMoreMenuClick = {},
            onDismissMoreMenu = {},
            onThemeClick = {},
            onRecycleBinClick = {},
            onFavouritesClick = {},
            onDismissThemeSheet = {},
        )
    }
}
