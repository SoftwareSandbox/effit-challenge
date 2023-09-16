package be.swsb.effit.ui.bulma

import be.swsb.effit.ui.components.utility.appendClasses
import kotlinx.html.*

fun FORM.InputMonth(classes : String? = null, name: String, labelText: String, isRequired: Boolean) {
    div(classes = "field".appendClasses(classes)) {
        div(classes = "control") {
            label(classes = "label") { +labelText }
            input(classes = "input", type = InputType.month, name = name) {
                required = isRequired
            }
        }
    }
}

fun FORM.InputEmail(
    classes: String? = null,
    name: String,
    labelText: String,
    emailPlaceholder: String
) {
    div(classes = "field".appendClasses(classes)) {
        label(classes = "label") { +labelText }
        div(classes = "control has-icons-left") {
            span(classes = "icon is-small is-left") {
                i(classes = "fas fa-envelope")
            }
            input(classes = "input", type = InputType.email, name = name) {
                placeholder = emailPlaceholder
            }
        }
    }
}

fun FORM.InputText(
    classes: String? = null,
    name: String,
    labelText: String,
    placeholder: String = "",
    isRequired: Boolean,
) {
    div(classes = "field".appendClasses(classes)) {
        label(classes = "label") { +labelText }
        div(classes = "control") {
            input(classes = "input", type = InputType.text, name = name) {
                this.placeholder = placeholder
                required = isRequired
            }
        }
    }
}

fun FORM.Checkbox(classes: String? = null, name: String, labelText: String) {
    div(classes = "field".appendClasses(classes)) {
        div(classes = "control") {
            label(classes = "checkbox") {
                input(type = InputType.checkBox, name = name)
                +" $labelText"
            }
        }
    }
}

fun FORM.SubmitButton(classes: String? = null, labelText: String) {
    div(classes = "field".appendClasses(classes)) {
        div(classes = "control") {
            button(classes = "button is-primary", type = ButtonType.submit) {
                +labelText
            }
        }
    }
}