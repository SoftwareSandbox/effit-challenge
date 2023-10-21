package be.swsb.effit.ui.views

import kotlinx.html.DIV
import kotlinx.html.div
import kotlinx.html.p

data object ProfileView : NavigableView("profile", "/profile", ::profileView)

private fun profileView(div: DIV) = div.apply {
    div(classes = "container") {
        p { +"snarf" }
    }
}