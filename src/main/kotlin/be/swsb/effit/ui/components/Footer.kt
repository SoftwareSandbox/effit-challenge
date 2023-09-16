package be.swsb.effit.ui.components

import be.swsb.effit.ui.components.utility.appendClasses
import kotlinx.html.*

fun DIV.Footer(id: String = "footer", classes : String? = null) =
    footer("footer".appendClasses(classes)) {
        this.id = id
        div("has-text-centered") {
            p {
                strong { +"Effit" }
                +" made with ❤️ by "
                a {
                    href = "https://github.com/sch3lp"
                    +"Sch3lp"
                }
            }
        }
    }