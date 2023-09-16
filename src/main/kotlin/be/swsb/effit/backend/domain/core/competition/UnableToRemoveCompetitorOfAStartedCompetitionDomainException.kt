package be.swsb.effit.backend.domain.core.competition

import be.swsb.effit.backend.domain.core.exceptions.DomainRuntimeException
import be.swsb.effit.backend.domain.core.exceptions.HttpStatusCode

class UnableToRemoveCompetitorOfAStartedCompetitionDomainException
    : DomainRuntimeException(
        "Unable to remove a competitor from a Competition that's already been started",
        HttpStatusCode.`400`)
