package be.swsb.effit.domain.core.competition

import be.swsb.effit.domain.command.competition.CreateCompetition
import be.swsb.effit.domain.core.exceptions.DomainValidationRuntimeException
import org.springframework.stereotype.Component

@Component
class CompetitionCreator {
    fun from(createCompetition: CreateCompetition): Competition {
        if (createCompetition.name.isNullOrBlank()) {
            throw DomainValidationRuntimeException("Cannot create a Competition without a name")
        }
        if (createCompetition.name.length > 25) {
            throw DomainValidationRuntimeException("Cannot create a Competition with a name longer than 25 characters")
        }

        return when {
            createCompetition.startDate != null && createCompetition.endDate != null
                -> Competition.competition(name = createCompetition.name,
                    startDate = createCompetition.startDate,
                    endDate = createCompetition.endDate)
            createCompetition.startDate == null && createCompetition.endDate != null
                -> Competition.competitionWithoutStartDate(name = createCompetition.name, endDate = createCompetition.endDate)
            createCompetition.startDate != null && createCompetition.endDate == null
                -> Competition.competitionWithoutEndDate(name = createCompetition.name, startDate = createCompetition.startDate)
            else
                -> throw DomainValidationRuntimeException("Cannot create a Competition without both a start and end date")
        }
    }

}
