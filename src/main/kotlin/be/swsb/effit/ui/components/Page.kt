package be.swsb.effit.ui.components

import kotlinx.html.*

fun HTML.page(bodyDefinition: BODY.() -> Unit) {
    head {
        title { +"Effit Challenge" }
        meta { charset = "UTF-8" }
        link(rel = "stylesheet", href = "https://cdn.jsdelivr.net/npm/bulma@0.9.4/css/bulma.min.css", type = "text/css")
        link(rel = "stylesheet", href = "https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css", type = "text/css")
        link(rel = "icon", type = "image/png", href = "/static/favicon-16x16.png") { sizes = "16x16" }
        link(rel = "icon", type = "image/png", href = "/static/favicon-32x32.png") { sizes = "32x32" }
        script {
            src = "https://unpkg.com/htmx.org@1.8.4"
            integrity = "sha384-wg5Y/JwF7VxGk4zLsJEcAojRtlVp1FKKdGy1qN+OMtdq72WRvX/EdRdqg/LOhYeV"
            attributes["crossorigin"] = "anonymous"
        }
        script {
            src = "https://unpkg.com/htmx.org/dist/ext/multi-swap.js"
        }
    }
    body {
        attributes["hx-ext"] = "multi-swap"
        bodyDefinition()
    }
}