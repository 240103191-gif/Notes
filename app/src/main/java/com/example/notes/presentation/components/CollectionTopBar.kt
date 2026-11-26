package com.example.notes.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.notes.R
import com.example.notes.domain.model.LoadState
import com.example.notes.domain.model.Note

@Composable
fun CollectionTopBar(
    title: String,
    notesState: LoadState<List<Note>>,
    onBackClick: () -> Unit,
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
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(24.dp)
                    .clickable(onClick = onBackClick),
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
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
