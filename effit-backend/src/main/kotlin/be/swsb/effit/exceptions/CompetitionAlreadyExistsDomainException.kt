package be.swsb.effit.exceptions

import be.swsb.effit.competition.CompetitionId

class CompetitionAlreadyExistsDomainException(competitionId: CompetitionId) :
        DomainRuntimeException(status = HttpStatusCode.`400`,
                message = "Sorry, there's already a competition that has a generated CompetitionId of ${competitionId.id}. Try entering a (slightly) different name."
        )
