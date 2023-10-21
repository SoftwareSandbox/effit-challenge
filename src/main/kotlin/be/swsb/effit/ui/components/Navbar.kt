package be.swsb.effit.ui.components

import be.swsb.effit.plugins.UserInfo
import be.swsb.effit.ui.bulma.*
import be.swsb.effit.ui.views.*
import kotlinx.html.*
import kotlinx.html.div

fun DIV.Navbar(userInfo: UserInfo?) = nav("navbar is-primary") {
    NavbarMenu {
        NavbarStart {
            NavbarLink(href = HomeView.url) { +"Home" }
            NavbarDropdown("More") {
                NavbarLink(href = AboutView.url) { +"About" }
                NavbarDivider()
                NavbarLink { +"Report an issue" }
            }
        }
        NavbarEnd {
            if (userInfo == null) Login()
            else Profile(userInfo = userInfo)
        }
    }
}

private fun DIV.Profile(userInfo: UserInfo) {
    div("navbar-item has-dropdown is-hoverable") {
        a(classes = "navbar-link") {
            div(classes = "media") {
                div(classes = "media-left") {
                    figure(classes = "image is-48x48") {
                        img(alt = "Placeholder image") {
                            src = userInfo.picture
                        }
                    }
                }
                div(classes = "media-content") {
                    p(classes = "is-4") {
                        +userInfo.name
                    }
                }
            }
        }
        div("navbar-dropdown") {
            NavbarLink(href = "logout") { +"Log out" }
        }
    }
}

private fun DIV.Login() {
    // Because /profile is protected,
    // when a user is not authenticated,
    // the withUser() "guard" should redirect to /login and start the authentication process
    NavbarLink(href = "profile") { +"Log in" }
}