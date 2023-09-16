package be.swsb.effit.ui.components

import be.swsb.effit.ui.views.NavigableView
import kotlinx.html.SECTION
import kotlinx.html.div
import kotlinx.html.id

fun SECTION.View(navigableView: NavigableView) =
    div {
        this.id = navigableView.id
        navigableView.render(this)
    }