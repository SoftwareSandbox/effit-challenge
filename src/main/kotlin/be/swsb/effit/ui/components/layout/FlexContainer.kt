package be.swsb.effit.ui.components.layout

import kotlinx.html.*

fun BODY.FlexContainer(id: String, block: DIV.() -> Unit) =
    div(classes = "is-flex is-flex-direction-column") {
        this.id = id
        block()
    }