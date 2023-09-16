package be.swsb.effit.ui.components.utility

fun String.appendClasses(classes: String?) =
    if (classes != null) "$this $classes"
    else this
