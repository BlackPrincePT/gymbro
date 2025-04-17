package com.pegio.common.presentation.state

data class TopBarState(
    val title: String = "",
    val navigationIcon: TopBarAction? = null,
    val actions: List<TopBarAction> = emptyList()
)