package be.swsb.effit.ui.views

import kotlinx.html.DIV
import kotlinx.html.a
import kotlinx.html.p

data object AboutView : NavigableView("about", "/about", ::aboutView)

private fun aboutView(div: DIV) = div.apply {
    p { +"It helps you organise competitions among friends that feature challenges. More specifically it facilitates administration." }
    p { +"Get started now!" }
    p {
        a("competitions/create") { +"Create a new competition" }
        +", or start from "
        a(href = "competitions") { +"an existing one" }
        +"."
    }
    p { +"Don't forget to invite your friends and remember to play nice!" }
}

