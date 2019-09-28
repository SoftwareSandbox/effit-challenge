package be.swsb.effit.domain.core.competition

import be.swsb.effit.domain.core.exceptions.DomainRuntimeException
import be.swsb.effit.domain.core.exceptions.HttpStatusCode

class UnableToAddCompetitorToStartedCompetitionDomainException
    : DomainRuntimeException(
        "Unable to add a competitor to a Competition that's already been started",
        HttpStatusCode.`400`)
