package be.swsb.effit.backend.domain.core.competition

import be.swsb.effit.backend.domain.core.exceptions.DomainRuntimeException
import be.swsb.effit.backend.domain.core.exceptions.HttpStatusCode

class CompetitorNotFoundOnCompetitionDomainException
    : DomainRuntimeException(
        "Competitor not found on this competition",
        HttpStatusCode.`400`
)
