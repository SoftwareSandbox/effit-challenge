package be.swsb.effit.ui.components

import be.swsb.effit.ui.components.utility.appendClasses
import be.swsb.effit.ui.views.NavigableView
import kotlinx.html.DIV
import kotlinx.html.id
import kotlinx.html.section

const val RoutedContentId = "routed-content"

fun DIV.RoutedMainContent(classes: String? = null, navigableView: NavigableView) =
    section("section".appendClasses(classes)) {
        this.id = RoutedContentId
        View(navigableView)
    }
