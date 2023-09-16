package be.swsb.effit.ui.bulma

import be.swsb.effit.ui.htmx.hxBoost
import kotlinx.html.*

fun NAV.NavbarMenu(block: DIV.() -> Unit) =
    div("navbar-menu is-size-5") {
        block()
    }

fun DIV.NavbarStart(block: DIV.() -> Unit) =
    div("navbar-start") {
        block()
    }

fun DIV.NavbarEnd(block: DIV.() -> Unit) =
    div("navbar-end") {
        block()
    }

fun DIV.NavbarLink(href: String = "/", block: A.() -> Unit) {
    hxBoost()
    a(classes = "navbar-item is-size-5", href = href) {
        block()
    }
}

fun DIV.NavbarDivider() =
    hr("navbar-divider")

fun DIV.NavbarDropdown(label: String, block: DIV.() -> Unit) =
    div("navbar-item has-dropdown is-hoverable") {
        a(classes = "navbar-link") {
            +label
        }
        div("navbar-dropdown") {
            block()
        }
    }
