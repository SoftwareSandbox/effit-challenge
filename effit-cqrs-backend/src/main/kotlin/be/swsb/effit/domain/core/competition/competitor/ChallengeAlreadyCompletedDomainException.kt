package be.swsb.effit.domain.core.competition.competitor

import be.swsb.effit.domain.core.exceptions.DomainValidationRuntimeException

class ChallengeAlreadyCompletedDomainException(challengeName: String) :
        DomainValidationRuntimeException("This competitor already completed the $challengeName challenge.")
