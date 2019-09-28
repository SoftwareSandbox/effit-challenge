package be.swsb.effit.competition

import be.swsb.effit.exceptions.DomainRuntimeException
import be.swsb.effit.exceptions.HttpStatusCode

class UnableToRemoveCompetitorOfAStartedCompetitionDomainException
    : DomainRuntimeException(
        "Unable to remove a competitor from a Competition that's already been started",
        HttpStatusCode.`400`)