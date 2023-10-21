package be.swsb.effit.ui.pages

import be.swsb.effit.plugins.UserInfo
import be.swsb.effit.ui.components.Footer
import be.swsb.effit.ui.components.Navbar
import be.swsb.effit.ui.components.RoutedMainContent
import be.swsb.effit.ui.components.layout.FlexContainer
import be.swsb.effit.ui.components.page
import be.swsb.effit.ui.views.NavigableView
import kotlinx.html.HTML
import kotlinx.html.id


private const val EffitUiPage = "EffitUiPage"

fun HTML.EffitUi(id: String = "EffitUi", navigableView: NavigableView, userInfo: UserInfo?) = page {
    FlexContainer(EffitUiPage) {
        this.id = id
        Navbar(userInfo)
        RoutedMainContent(classes = "is-flex-grow-5", navigableView = navigableView)
        Footer()
    }
}