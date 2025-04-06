package com.pegio.gymbro.presentation.activity

data class TopBarState(
    val title: String = "",
    val navigationIcon: TopBarAction? = null,
    val actions: List<TopBarAction> = emptyList()
)