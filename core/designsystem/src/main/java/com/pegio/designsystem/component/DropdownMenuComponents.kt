package com.pegio.designsystem.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun <T> GymBroDropdownMenu(
    isExpanded: Boolean,
    options: List<T>,
    label: String,
    onSelectionChanged: (T) -> Unit,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    selectedOption: String? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = onExpandedChange
    ) {
        GymBroTextField(
            value = selectedOption.orEmpty(),
            onValueChange = { },
            readOnly = true,
            label = label,
            trailingIcon = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)

                    if (trailingIcon != null) {
                        Spacer(modifier = Modifier.width(4.dp))
                        trailingIcon()
                    }
                }
            },
            modifier = modifier
                .menuAnchor(type = MenuAnchorType.PrimaryEditable)
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        onSelectionChanged(selectionOption)
                        onExpandedChange(false)
                    },
                    text = { Text(text = selectionOption?.toString().orEmpty()) }
                )
            }
        }
    }
}

@Composable
fun <T> GymBroDropdownMenu(
    isExpanded: Boolean,
    options: List<T>,
    label: String,
    onSelectionChanged: (T) -> Unit,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    selectedOption: String? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    @StringRes errorRes: Int? = null
) {
    Column {
        GymBroDropdownMenu(
            isExpanded = isExpanded,
            options = options,
            label = label,
            onSelectionChanged = onSelectionChanged,
            onExpandedChange = onExpandedChange,
            modifier = modifier,
            selectedOption = selectedOption,
            trailingIcon = trailingIcon
        )

        errorRes?.let {
            Text(
                text = stringResource(id = errorRes),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DropdownMenuPreview() {
    GymBroDropdownMenu(
        isExpanded = false,
        options = emptyList<String>(),
        label = "Categories",
        onSelectionChanged = { },
        onExpandedChange = { },
        modifier = Modifier.fillMaxWidth()
    )
}