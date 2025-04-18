package com.pegio.aichat.presentation.screen.ai_chat

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pegio.aichat.presentation.component.AttachImageButton
import com.pegio.aichat.presentation.component.InputTextField
import com.pegio.aichat.presentation.component.SendButton
import com.pegio.aichat.presentation.model.UiAiMessage
import com.pegio.common.presentation.state.TopBarAction
import com.pegio.common.presentation.state.TopBarState
import com.pegio.common.presentation.util.CollectLatestEffect
import com.pegio.designsystem.component.MessageImage

@Composable
fun AiChatScreen(
    onBackClick: () -> Unit,
    onSetupTopBar: (TopBarState) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    viewModel: AiChatViewModel = hiltViewModel()
) {

    SetupTopBar(onSetupTopBar, viewModel::onEvent)
    val context = LocalContext.current

    CollectLatestEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            is AiChatUiEffect.Failure -> onShowSnackbar(context.getString(effect.errorRes), null)
            AiChatUiEffect.NavigateBack -> onBackClick()
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
                    MessageImage(
                        imageUrl = uri.toString(),
                        modifier = modifier.fillMaxSize()
                    )

                    IconButton(
                        onClick = { onEvent(AiChatUiEvent.OnRemoveImage) },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove image",
                            tint = Color.Red
                        )
                    }
                }
            }


            ChatInput(
                text = state.inputText,
                onTextChange = { onEvent(AiChatUiEvent.OnTextChanged(it)) },
                onSend = { onEvent(AiChatUiEvent.OnSendMessage()) },
                onImageSelect = { galleryLauncher.launch("image/*") },
                isLoading = state.isLoading
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
            if (message.isUploading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.69f)
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            if (message.imageUrl != null) {
                MessageImage(
                    imageUrl = message.imageUrl,
                    modifier = Modifier
                        .fillMaxWidth(0.69f)
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp)),
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
    val placeholderMessage =
        UiAiMessage(text = "Typing...", isFromUser = false)

    ChatBubble(message = placeholderMessage)
}

@Composable
fun ChatInput(
    text: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit,
    onImageSelect: () -> Unit,
    isLoading: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray, RoundedCornerShape(3.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AttachImageButton(onImageSelect = onImageSelect)
        InputTextField(
            text = text,
            onTextChange = onTextChange,
            placeholder = "Type a message…",
            modifier = Modifier.weight(1f)
        )
        SendButton(text = text, onSend = onSend, isLoading = isLoading)
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
    AiChatContent(state = AiChatUiState(), onEvent = {})
}