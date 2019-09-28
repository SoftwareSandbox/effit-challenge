package be.swsb.effit.domain.core.competition

import be.swsb.effit.domain.core.exceptions.DomainRuntimeException
import be.swsb.effit.domain.core.exceptions.HttpStatusCode

class CompetitionAlreadyExistsDomainException(competitionId: CompetitionId) :
        DomainRuntimeException(status = HttpStatusCode.`400`,
                message = "Sorry, there's already a competition that has a generated CompetitionId of ${competitionId.id}. Try entering a (slightly) different name."
        )
