package be.swsb.effit.ui.bulma

import kotlinx.html.HtmlBlockTag
import kotlinx.html.i
import kotlinx.html.span

fun HtmlBlockTag.Spinner(classes: String, text: String? = null) {
    span(classes = classes) {
        span(classes = "icon-text") {
            span(classes = "icon has-text-success") {
                i(classes = "fas fa-regular fa-spinner fa-spin") {}
            }
            if (text != null) {
                span { +text }
            }
        }
    }
}