package be.swsb.effit.backend.domain.core.competition

import be.swsb.effit.backend.domain.core.exceptions.DomainRuntimeException
import be.swsb.effit.backend.domain.core.exceptions.HttpStatusCode

class UnableToAddChallengeToStartedCompetitionDomainException
    : DomainRuntimeException(
        "Unable to add a challenge to a Competition that's already been started",
        HttpStatusCode.`400`)
