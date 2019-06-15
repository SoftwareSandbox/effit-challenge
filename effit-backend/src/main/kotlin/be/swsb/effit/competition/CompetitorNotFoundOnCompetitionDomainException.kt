package be.swsb.effit.competition

import be.swsb.effit.competition.competitor.Competitor
import be.swsb.effit.exceptions.DomainRuntimeException
import be.swsb.effit.exceptions.HttpStatusCode

class CompetitorNotFoundOnCompetitionDomainException(competitor: Competitor)
    : DomainRuntimeException(
        "${competitor.name} not found on this competition",
        HttpStatusCode.`400`
)