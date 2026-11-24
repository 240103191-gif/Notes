package com.example.notes.presentation.features.theme

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.notes.R
import com.example.notes.domain.model.AppTheme
import com.example.notes.presentation.theme.NotesTheme
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeChangeBottomSheet(
    onDismiss: () -> Unit,
    viewModel: ThemeChangeViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        dragHandle = {
            ThemeSheetDragHandle()
        },
    ) {
        ThemeChangeSheetContent(
            state = state,
            onThemeSelect = viewModel::onThemeSelect,
        )
    }
}

@Composable
private fun ThemeSheetDragHandle() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp, bottom = 10.dp),
        contentAlignment = Alignment.Center,
    ) {
        Spacer(
            modifier = Modifier
                .width(40.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(MaterialTheme.colorScheme.onSurfaceVariant),
        )
    }
}

@Composable
private fun ThemeChangeSheetContent(
    state: ThemeChangeState,
    onThemeSelect: (AppTheme) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            text = stringResource(R.string.choose_theme_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        ThemeOptionItem(
            labelRes = R.string.theme_dark,
            isSelected = state.selectedTheme == AppTheme.DARK,
            onClick = { onThemeSelect(AppTheme.DARK) },
        )

        HorizontalDivider(
            modifier = Modifier.padding(start = 16.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant,
        )

        ThemeOptionItem(
            labelRes = R.string.theme_light,
            isSelected = state.selectedTheme == AppTheme.LIGHT,
            onClick = { onThemeSelect(AppTheme.LIGHT) },
        )
    }
}

@Composable
private fun ThemeOptionItem(
    @StringRes labelRes: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier.size(24.dp),
            contentAlignment = Alignment.Center,
        ) {
            if (isSelected) Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(R.drawable.ic_check),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = stringResource(labelRes),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ThemeChangeBottomSheetDarkPreview() {
    NotesTheme(appTheme = AppTheme.DARK) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp),
                ),
        ) {
            ThemeSheetDragHandle()
            ThemeChangeSheetContent(
                state = ThemeChangeState(selectedTheme = AppTheme.DARK),
                onThemeSelect = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ThemeChangeBottomSheetLightPreview() {
    NotesTheme(appTheme = AppTheme.LIGHT) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp),
                ),
        ) {
            ThemeSheetDragHandle()
            ThemeChangeSheetContent(
                state = ThemeChangeState(selectedTheme = AppTheme.LIGHT),
                onThemeSelect = {},
            )
        }
    }
}
