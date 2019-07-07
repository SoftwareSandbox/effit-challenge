package be.swsb.effit.competition

import be.swsb.effit.exceptions.DomainRuntimeException
import be.swsb.effit.exceptions.HttpStatusCode

class UnableToAddCompetitorToStartedCompetitionDomainException
    : DomainRuntimeException(
        "Unable to add a competitor to a Competition that's already been started",
        HttpStatusCode.`400`)