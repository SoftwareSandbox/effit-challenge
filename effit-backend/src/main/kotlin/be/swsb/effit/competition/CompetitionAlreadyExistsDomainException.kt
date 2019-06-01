package be.swsb.effit.competition

import be.swsb.effit.exceptions.DomainRuntimeException
import be.swsb.effit.exceptions.HttpStatusCode

class CompetitionAlreadyExistsDomainException(competitionId: CompetitionId) :
        DomainRuntimeException(status = HttpStatusCode.`400`,
                message = "Sorry, there's already a competition that has a generated CompetitionId of ${competitionId.id}. Try entering a (slightly) different name."
        )
