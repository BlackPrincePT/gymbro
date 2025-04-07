package com.pegio.gymbro.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pegio.gymbro.domain.core.Error
import com.pegio.gymbro.presentation.util.toStringResId

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun <T> DropdownMenu(
    options: List<T>,
    onSelectionChanged: (T) -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    error: Error? = null,
    selectedOption: String? = null
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column {
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = it }
        ) {
            TextField(
                readOnly = true,
                value = selectedOption.orEmpty(),
                onValueChange = { },
                label = label,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                isError = error != null,
                modifier = modifier
                    .fillMaxWidth()
                    .menuAnchor(type = MenuAnchorType.PrimaryEditable)
            )

            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false }
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        onClick = {
                            onSelectionChanged(selectionOption)
                            isExpanded = false
                        },
                        text = { Text(text = selectionOption.toString()) }
                    )
                }
            }
        }

        error?.let {
            Text(
                text = stringResource(id = error.toStringResId()),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Preview
@Composable
private fun DropdownMenuPreview() {
    DropdownMenu(
        options = emptyList<String>(),
        onSelectionChanged = { },
        label = { Text(text = "Categories") }
    )
}