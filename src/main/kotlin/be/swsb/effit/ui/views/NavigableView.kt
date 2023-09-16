package be.swsb.effit.ui.views

import kotlinx.html.DIV

sealed class NavigableView(
    val id: String,
    val url: String,
    val render: DIV.() -> Unit
)