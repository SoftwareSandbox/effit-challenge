package be.swsb.effit.ui.views

import be.swsb.effit.ui.components.utility.loremIpsum
import kotlinx.html.DIV
import kotlinx.html.div
import kotlinx.html.img
import kotlinx.html.p

data object HomeView : NavigableView("home", "/", ::homeView)

private fun homeView(div: DIV) = div.apply {
    div(classes = "container") {
        p { +"This is the home view..." }
        p { +loremIpsum() }
    }
}

