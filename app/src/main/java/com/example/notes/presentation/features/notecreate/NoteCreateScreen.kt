package com.example.notes.presentation.features.notecreate

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.notes.R
import com.example.notes.presentation.components.TitleTopBar
import com.example.notes.presentation.theme.NotesTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun NoteCreateScreen(
    viewModel: NoteCreateViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    NoteCreateContent(
        state = state,
        onTitleChange = viewModel::onTitleChange,
        onContentChange = viewModel::onContentChange,
        onBackClick = viewModel::onBackClick,
        onSaveClick = viewModel::onSaveClick,
    )
}

@Composable
private fun NoteCreateContent(
    state: NoteCreateState,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TitleTopBar(
                title = state.title,
                onTitleChange = onTitleChange,
                onBackClick = onBackClick,
                onSaveClick = onSaveClick,
                hasDelete = false,
            )
        },
    ) { innerPadding ->
        TextField(
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

@Preview(showBackground = true)
@Composable
private fun NoteCreateEmptyScreenPreview() {
    NotesTheme {
        NoteCreateContent(
            state = NoteCreateState(),
            onTitleChange = {},
            onContentChange = {},
            onBackClick = {},
            onSaveClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NoteCreateFilledScreenPreview() {
    NotesTheme {
        NoteCreateContent(
            state = NoteCreateState(
                title = "asdfghjk",
                content = "qwertyuiop asdfghjkl zxcvbnm lkjhgfdsa mnbvcxz",
            ),
            onTitleChange = {},
            onContentChange = {},
            onBackClick = {},
            onSaveClick = {},
        )
    }
}
