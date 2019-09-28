package be.swsb.effit.adapter.ui.exceptions

import be.swsb.effit.adapter.ui.util.RestApiExposed

data class EffitError(val message: String? = "Something went horribly wrong"): RestApiExposed
