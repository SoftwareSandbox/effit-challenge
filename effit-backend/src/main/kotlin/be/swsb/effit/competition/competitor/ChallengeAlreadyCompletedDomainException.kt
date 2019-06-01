package be.swsb.effit.competition.competitor

import be.swsb.effit.exceptions.DomainValidationRuntimeException

class ChallengeAlreadyCompletedDomainException(challengeName: String) :
        DomainValidationRuntimeException("This competitor already completed the $challengeName challenge.")