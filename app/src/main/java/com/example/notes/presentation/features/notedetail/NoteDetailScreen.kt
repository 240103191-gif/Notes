package com.example.notes.presentation.features.notedetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.notes.R
import com.example.notes.domain.model.LoadState
import com.example.notes.domain.model.Note
import com.example.notes.presentation.components.ErrorStateView
import com.example.notes.presentation.components.TitleTopBar
import com.example.notes.presentation.components.shimmer
import com.example.notes.presentation.theme.NotesTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun NoteDetailScreen(
    viewModel: NoteDetailViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    NoteDetailContent(
        state = state,
        onTitleChange = viewModel::onTitleChange,
        onContentChange = viewModel::onContentChange,
        onBackClick = viewModel::onBackClick,
        onSaveClick = viewModel::onSaveClick,
        onDeleteClick = viewModel::onDeleteClick,
        onRetryClick = viewModel::onRetryClick,
    )
}

@Composable
private fun NoteDetailContent(
    state: NoteDetailState,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onRetryClick: () -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            when (state.noteState) {
                is LoadState.Loading -> NoteDetailShimmerTopBar()
                is LoadState.Error -> NoteDetailErrorTopBar(onBackClick = onBackClick)
                is LoadState.Success -> TitleTopBar(
                    title = state.title,
                    onTitleChange = onTitleChange,
                    onBackClick = onBackClick,
                    onSaveClick = onSaveClick,
                    hasDelete = true,
                    onDeleteClick = onDeleteClick,
                )
            }
        },
    ) { innerPadding ->
        when (state.noteState) {
            is LoadState.Loading -> NoteDetailShimmerView(
                modifier = Modifier.padding(innerPadding),
            )
            is LoadState.Error -> ErrorStateView(
                modifier = Modifier.padding(innerPadding),
                message = stringResource(R.string.error_loading_note),
                onRetryClick = onRetryClick,
            )
            is LoadState.Success -> TextField(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                value = state.content,
                onValueChange = onContentChange,
                placeholder = {
                    Text(
                        text = stringResource(R.string.note_content_placeholder),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Normal,
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface,
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.onBackground,
                ),
            )
        }
    }
}

@Composable
private fun NoteDetailShimmerTopBar(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .shimmer(),
            )
            Spacer(modifier = Modifier.width(12.dp))
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(0.55f)
                    .height(22.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .shimmer(),
            )
            Spacer(modifier = Modifier.weight(1f))
            Spacer(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .shimmer(),
            )
            Spacer(modifier = Modifier.width(16.dp))
            Spacer(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .shimmer(),
            )
        }

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outline,
        )
    }
}

@Composable
private fun NoteDetailErrorTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier
                .size(24.dp)
                .clickable(onClick = onBackClick),
            painter = painterResource(R.drawable.ic_arrow_back),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Composable
private fun NoteDetailShimmerView(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .height(16.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmer(),
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth(0.84f)
                .height(16.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmer(),
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth(0.68f)
                .height(16.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmer(),
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth(0.46f)
                .height(16.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmer(),
        )
    }
}

private val sampleDetailNote = Note(
    id = 1L,
    title = "Template and plan",
    content = "Spec kit helps structure clean project specifications and plans.",
    createdAt = 1700000500000L,
    formattedDate = "18.11.2026, 14:15",
)

@Preview(showBackground = true)
@Composable
private fun NoteDetailFilledScreenPreview() {
    NotesTheme {
        NoteDetailContent(
            state = NoteDetailState(
                noteState = LoadState.Success(sampleDetailNote),
                title = sampleDetailNote.title,
                content = sampleDetailNote.content,
            ),
            onTitleChange = {},
            onContentChange = {},
            onBackClick = {},
            onSaveClick = {},
            onDeleteClick = {},
            onRetryClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NoteDetailUntitledScreenPreview() {
    NotesTheme {
        NoteDetailContent(
            state = NoteDetailState(
                noteState = LoadState.Success(sampleDetailNote.copy(title = "")),
                title = "",
                content = sampleDetailNote.content,
            ),
            onTitleChange = {},
            onContentChange = {},
            onBackClick = {},
            onSaveClick = {},
            onDeleteClick = {},
            onRetryClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NoteDetailShimmerScreenPreview() {
    NotesTheme {
        NoteDetailContent(
            state = NoteDetailState(
                noteState = LoadState.Loading,
            ),
            onTitleChange = {},
            onContentChange = {},
            onBackClick = {},
            onSaveClick = {},
            onDeleteClick = {},
            onRetryClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NoteDetailErrorScreenPreview() {
    NotesTheme {
        NoteDetailContent(
            state = NoteDetailState(
                noteState = LoadState.Error(),
            ),
            onTitleChange = {},
            onContentChange = {},
            onBackClick = {},
            onSaveClick = {},
            onDeleteClick = {},
            onRetryClick = {},
        )
    }
}
