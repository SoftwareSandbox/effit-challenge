package be.swsb.effit.domain.core.competition.competitor

import be.swsb.effit.domain.core.exceptions.DomainValidationRuntimeException

data class CompetitorName(val name: String) {
    init {
        if (name.length > 50) {
            throw DomainValidationRuntimeException("A Competitor name must be less than 50 characters.")
        }
    }
}
