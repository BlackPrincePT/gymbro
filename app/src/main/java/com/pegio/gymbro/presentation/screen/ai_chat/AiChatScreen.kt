package com.pegio.gymbro.presentation.screen.ai_chat

import androidx.compose.foundation.background
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pegio.gymbro.R
import com.pegio.gymbro.presentation.model.AiChatMessage
import com.pegio.gymbro.presentation.theme.GymBroTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AiChatScreen(
    viewModel: AiChatViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val snackBarHostState = remember { SnackbarHostState() }


    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                is AiChatUiEffect.Failure -> {
                    snackBarHostState.showSnackbar(effect.e.toString())
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        LazyColumn(
            modifier = Modifier.weight(1f),
            state = listState,
        ) {
            items(uiState.messages) { message ->
                ChatBubble(message)
            }
            if (uiState.isLoading) {
                item {
                    ChatBubblePlaceholder()
                }
            }
        }

        LaunchedEffect(uiState.messages) {
            listState.animateScrollToItem(uiState.messages.size)
        }


        SnackbarHost(
            hostState = snackBarHostState,
            modifier = Modifier
                .padding(bottom = 16.dp)
        )

        ChatInput(
            text = uiState.inputText,
            onTextChange = { viewModel.onEvent(AiChatUiEvent.OnTextChanged(it)) },
            onSend = { viewModel.onEvent(AiChatUiEvent.OnSendMessage) }
        )

    }
}

@Composable
fun ChatBubble(message: AiChatMessage) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = if (message.isFromUser) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Text(
            text = message.text,
            modifier = Modifier
                .background(
                    if (message.isFromUser) Color.Blue else Color.Gray,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp),
            color = Color.White
        )
    }
}

@Composable
fun ChatBubblePlaceholder() {
    val placeholderMessage = AiChatMessage(text = "Typing...", isFromUser = false)

    ChatBubble(message = placeholderMessage)
}

@Composable
fun ChatInput(text: String, onTextChange: (String) -> Unit, onSend: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray, RoundedCornerShape(3.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = text,
            onValueChange = onTextChange,
            modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp, top = 12.dp, bottom = 12.dp)
                .heightIn(min = 56.dp, max = 56.dp),

            singleLine = true,
            placeholder = { Text(stringResource(R.string.chat_hint)) }
        )
        IconButton(
            onClick = onSend,
            modifier = Modifier.padding(10.dp),
            enabled = text.isNotBlank()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.send),
                contentDescription = "Send Icon",
                tint = if (text.isNotBlank()) Color.Black else Color.Gray
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewChatScreen() {
    GymBroTheme {
        AiChatScreen()
    }
}