package be.swsb.effit.competition

import org.springframework.stereotype.Component
import java.lang.IllegalStateException

@Component
class CompetitionCreator {
    fun from(createCompetition: CreateCompetition): Competition {
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
                -> throw IllegalStateException("Cannot create a Competition without both a start and end date")
        }
    }

}
