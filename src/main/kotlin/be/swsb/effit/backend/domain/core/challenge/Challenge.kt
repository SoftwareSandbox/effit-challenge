package be.swsb.effit.backend.domain.core.challenge

import be.swsb.effit.backend.domain.core.exceptions.DomainValidationRuntimeException
import java.util.*

data class Challenge(val id: UUID = UUID.randomUUID(),
                     val name: String,
                     val points: Int,
                     val description: String) {
    init {
        if (points < 0) throw DomainValidationRuntimeException("Cannot create a Challenge with negative points")
        if (points == 0) throw DomainValidationRuntimeException("Cannot create a Challenge with 0 points")
    }
}