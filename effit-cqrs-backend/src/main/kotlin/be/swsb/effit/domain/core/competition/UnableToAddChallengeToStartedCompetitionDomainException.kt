package be.swsb.effit.domain.core.competition

import be.swsb.effit.domain.core.exceptions.DomainRuntimeException
import be.swsb.effit.domain.core.exceptions.HttpStatusCode

class UnableToAddChallengeToStartedCompetitionDomainException
    : DomainRuntimeException(
        "Unable to add a challenge to a Competition that's already been started",
        HttpStatusCode.`400`)
