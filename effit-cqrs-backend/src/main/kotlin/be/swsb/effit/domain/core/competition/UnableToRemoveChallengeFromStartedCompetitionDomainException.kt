package be.swsb.effit.domain.core.competition

import be.swsb.effit.domain.core.exceptions.DomainRuntimeException
import be.swsb.effit.domain.core.exceptions.HttpStatusCode

class UnableToRemoveChallengeFromStartedCompetitionDomainException
    : DomainRuntimeException(
        "Unable to remove a challenge from a Competition that's already been started",
        HttpStatusCode.`400`)
