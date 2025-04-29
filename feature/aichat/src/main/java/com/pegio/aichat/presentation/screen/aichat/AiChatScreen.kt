package com.pegio.aichat.presentation.screen.aichat

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pegio.aichat.R
import com.pegio.aichat.presentation.component.AttachImageButton
import com.pegio.aichat.presentation.component.SendButton
import com.pegio.aichat.presentation.model.UiAiMessage
import com.pegio.aichat.presentation.screen.aichat.state.AiChatUiEffect
import com.pegio.aichat.presentation.screen.aichat.state.AiChatUiEvent
import com.pegio.aichat.presentation.screen.aichat.state.AiChatUiState
import com.pegio.common.presentation.components.MessageImage
import com.pegio.common.presentation.state.TopBarAction
import com.pegio.common.presentation.state.TopBarState
import com.pegio.common.presentation.util.CollectLatestEffect
import com.pegio.common.presentation.util.rememberGalleryLauncher

@Composable
fun AiChatScreen(
    viewModel: AiChatViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onSetupTopBar: (TopBarState) -> Unit,
    onShowSnackbar: suspend (String) -> Unit
) {
    val context = LocalContext.current

    val launchGallery = rememberGalleryLauncher(
        onImageSelected = { viewModel.onEvent(AiChatUiEvent.OnImageSelected(it)) }
    )

    SetupTopBar(onSetupTopBar, viewModel::onEvent)

    CollectLatestEffect(viewModel.uiEffect) { effect ->
        when (effect) {

            is AiChatUiEffect.ShowSnackbar -> onShowSnackbar(context.getString(effect.errorRes))
            AiChatUiEffect.NavigateBack -> onBackClick()
            AiChatUiEffect.LaunchGallery -> launchGallery()
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
                            contentDescription = stringResource(R.string.feature_aichat_remove_image),
                            tint = Color.Red
                        )
                    }
                }
            }

            HorizontalDivider()

            ChatInput(
                text = state.inputText,
                onTextChange = { onEvent(AiChatUiEvent.OnTextChanged(it)) },
                onSend = { onEvent(AiChatUiEvent.OnSendMessage()) },
                onImageSelect = { onEvent(AiChatUiEvent.OnLaunchGallery) },
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
        UiAiMessage(text = stringResource(R.string.feature_aichat_typing), isFromUser = false)

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
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AttachImageButton(onImageSelect = onImageSelect)

        TextField(
            value = text,
            onValueChange = onTextChange,
            placeholder = { Text(text = stringResource(R.string.feature_aichat_type_a_message)) },
            modifier = Modifier.weight(1f),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        SendButton(text = text, onSend = onSend, isLoading = isLoading)
    }
}


@Composable
private fun SetupTopBar(
    onSetupTopBar: (TopBarState) -> Unit,
    onEvent: (AiChatUiEvent) -> Unit
) {
    val title = stringResource(R.string.feature_aichat_your_gym_bro)

    LaunchedEffect(Unit) {
        onSetupTopBar(
            TopBarState(
                title = title,
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