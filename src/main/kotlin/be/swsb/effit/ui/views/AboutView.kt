package be.swsb.effit.ui.views

import kotlinx.html.DIV
import kotlinx.html.p

data object AboutView : NavigableView("about", "/about", ::aboutView)

private fun aboutView(div: DIV) = div.apply {
    p { +"This is the about view..." }
    p { +"Snarf snarf" }
}

