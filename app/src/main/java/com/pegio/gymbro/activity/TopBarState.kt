package com.pegio.gymbro.activity

data class TopBarState(
    val title: String = "",
    val navigationIcon: TopBarAction? = null,
    val actions: List<TopBarAction> = emptyList()
)