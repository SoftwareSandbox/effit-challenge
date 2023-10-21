package be.swsb.effit.ui

import be.swsb.effit.plugins.withUser
import be.swsb.effit.ui.pages.EffitUi
import be.swsb.effit.ui.views.AboutView
import be.swsb.effit.ui.views.HomeView
import be.swsb.effit.ui.views.NavigableView
import be.swsb.effit.ui.views.ProfileView
import io.ktor.client.*
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureUI(httpClient: HttpClient) {
    routing {
        uiRoutes(httpClient)
    }
}

fun Routing.uiRoutes(httpClient: HttpClient) {
    fun Routing.registerGetFor(view: NavigableView) {
        route(view.url) {
            get {
                withUser(httpClient) { userInfo ->
                    EffitUi(navigableView = view, userInfo = userInfo)
                }
            }
        }
    }

    val navigableViews = listOf(HomeView, AboutView, ProfileView)
    navigableViews.forEach(::registerGetFor)
}

