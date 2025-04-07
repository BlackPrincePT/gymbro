package com.pegio.gymbro.presentation.screen.ai_chat

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.pegio.gymbro.R
import com.pegio.gymbro.presentation.activity.TopBarAction
import com.pegio.gymbro.presentation.activity.TopBarState
import com.pegio.gymbro.presentation.core.theme.GymBroTheme
import com.pegio.gymbro.presentation.model.UiAiMessage
import com.pegio.gymbro.presentation.util.CollectLatestEffect

@Composable
fun AiChatScreen(
    onBackClick: () -> Unit,
    onSetupTopBar: (TopBarState) -> Unit,
    viewModel: AiChatViewModel = hiltViewModel()
) {

    SetupTopBar(onSetupTopBar, viewModel::onEvent)

    CollectLatestEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            is AiChatUiEffect.Failure -> {}
            AiChatUiEffect.NavigateBack -> onBackClick.invoke()
        }
    }

    AiChatContent(
        state = viewModel.uiState,
        onEvent = viewModel::onEvent,
        modifier = Modifier
    )
}

@Composable
private fun AiChatContent(
    state: AiChatUiState,
    onEvent: (AiChatUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { onEvent(AiChatUiEvent.OnImageSelected(it)) }
        }

    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(state.messages.size - 1)
        }
    }

    LaunchedEffect(listState.firstVisibleItemIndex) {
        if (listState.firstVisibleItemIndex == 0) {
            onEvent(AiChatUiEvent.LoadMoreMessages)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = modifier
                .fillMaxSize()
        ) {

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
            ) {
                items(state.messages) { message ->
                    ChatBubble(message)
                }

                if (state.isLoading) {
                    item { ChatBubblePlaceholder() }
                }
            }
            state.selectedImageUri?.let { uri ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.25f)
                        .height(90.dp)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    AsyncImage(
                        model = uri,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    IconButton(
                        onClick = { onEvent(AiChatUiEvent.OnRemoveImage) },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.remove_image),
                            tint = Color.Red
                        )
                    }
                }
            }



            ChatInput(
                text = state.inputText,
                onTextChange = { onEvent(AiChatUiEvent.OnTextChanged(it)) },
                onSend = { onEvent(AiChatUiEvent.OnSendMessage()) },
                onImageSelect = { galleryLauncher.launch(input = "image/*") }
            )
        }
        // Placeholder loading this needs to be improved
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(enabled = true, onClick = {})
            )
        }
    }
}

@Composable
fun ChatBubble(message: UiAiMessage) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = if (message.isFromUser) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Column(
            modifier = Modifier
                .background(
                    if (message.isFromUser) Color.Blue else Color.Gray,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp)
        ) {
            if (message.imageUrl != null) {
                AsyncImage(
                    model = message.imageUrl,
                    contentDescription = stringResource(R.string.chat_image),
                    modifier = Modifier
                        .fillMaxWidth(0.69f)
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            if (message.text.isNotBlank()) {
                Text(
                    text = message.text,
                    color = Color.White,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun ChatBubblePlaceholder() {
    val placeholderMessage = UiAiMessage(text = stringResource(R.string.typing), isFromUser = false)

    ChatBubble(message = placeholderMessage)
}

@Composable
fun ChatInput(
    text: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit,
    onImageSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray, RoundedCornerShape(3.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onImageSelect, modifier = Modifier.padding(4.dp)) {
            Icon(
                imageVector = Icons.Default.AttachFile,
                contentDescription = stringResource(R.string.attach_image),
                tint = Color.Black,
                modifier = Modifier.size(28.dp)
            )
        }

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
                imageVector = Icons.AutoMirrored.Default.Send,
                contentDescription = stringResource(R.string.send_icon),
                tint = if (text.isNotBlank()) Color.Black else Color.Gray
            )
        }
    }
}

@Composable
private fun SetupTopBar(
    onSetupTopBar: (TopBarState) -> Unit,
    onEvent: (AiChatUiEvent) -> Unit
) {
    LaunchedEffect(Unit) {
        onSetupTopBar(
            TopBarState(
                navigationIcon = TopBarAction(
                    icon = Icons.AutoMirrored.Default.ArrowBack,
                    onClick = { onEvent(AiChatUiEvent.OnBackClick) }
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AiChatPreview() {
    GymBroTheme {
        AiChatContent(state = AiChatUiState(), onEvent = {})
    }
}