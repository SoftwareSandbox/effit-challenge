package be.swsb.effit.exceptions

import be.swsb.effit.util.RestApiExposed

data class EffitError(val message: String? = "Something went horribly wrong"): RestApiExposed