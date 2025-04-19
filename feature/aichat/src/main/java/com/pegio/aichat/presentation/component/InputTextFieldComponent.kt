package com.pegio.aichat.presentation.component

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun InputTextField(
    text: String, onTextChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    TextField(
        value = text,
        onValueChange = onTextChange,
        modifier = modifier
            .padding(start = 5.dp, top = 12.dp, bottom = 12.dp, end = 5.dp)
            .heightIn(min = 56.dp, max = 56.dp),
        singleLine = true,
        placeholder = { Text(placeholder) }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewInputTextField() {

    InputTextField(
        text = "fwafwafwa",
        onTextChange = {},
        placeholder = "holding place"
    )
}
