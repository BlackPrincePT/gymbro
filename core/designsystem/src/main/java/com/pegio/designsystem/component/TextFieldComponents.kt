package com.pegio.designsystem.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    @StringRes error: Int? = null,
    isNumberOnly: Boolean = false
) {
    val keyboardType = if (isNumberOnly) KeyboardType.Number else KeyboardType.Unspecified

    Column(modifier = modifier) {
        TextField(
            value = value,
            onValueChange = { newValue ->
                if (isNumberOnly) {
                    if (newValue.all(Char::isDigit))
                        onValueChange.invoke(newValue)
                } else {
                    onValueChange.invoke(newValue)
                }
            },
            isError = error != null,
            label = label,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        error?.let {
            Text(
                text = stringResource(id = error),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Preview
@Composable
private fun FormTextFieldPreview() {
    FormTextField(
        value = "Pitiful Android Developer",
        onValueChange = { },
        label = { Text(text = "Username") },
        error = null
    )
}