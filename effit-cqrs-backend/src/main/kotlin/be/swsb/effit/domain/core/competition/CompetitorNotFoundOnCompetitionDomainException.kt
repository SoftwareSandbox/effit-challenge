package be.swsb.effit.domain.core.competition

import be.swsb.effit.domain.core.exceptions.DomainRuntimeException
import be.swsb.effit.domain.core.exceptions.HttpStatusCode

class CompetitorNotFoundOnCompetitionDomainException
    : DomainRuntimeException(
        "Competitor not found on this competition",
        HttpStatusCode.`400`
)
