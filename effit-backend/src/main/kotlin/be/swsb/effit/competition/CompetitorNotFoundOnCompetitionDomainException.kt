package be.swsb.effit.competition

import be.swsb.effit.exceptions.DomainRuntimeException
import be.swsb.effit.exceptions.HttpStatusCode

class CompetitorNotFoundOnCompetitionDomainException
    : DomainRuntimeException(
        "Competitor not found on this competition",
        HttpStatusCode.`400`
)