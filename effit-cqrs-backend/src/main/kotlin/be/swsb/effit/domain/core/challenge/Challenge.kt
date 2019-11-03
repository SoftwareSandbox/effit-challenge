package be.swsb.effit.domain.core.challenge

import be.swsb.effit.adapter.ui.util.RestApiExposed
import be.swsb.effit.domain.core.exceptions.DomainValidationRuntimeException
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Challenge(@Id val id: UUID = UUID.randomUUID(),
                     val name: String,
                     val points: Int,
                     val description: String) : RestApiExposed {

    init {
        if (points < 0) throw DomainValidationRuntimeException("Cannot create a Challenge with negative points")
        if (points == 0) throw DomainValidationRuntimeException("Cannot create a Challenge with 0 points")
    }

    companion object
}
