package be.swsb.effit.backend.domain.command.competition

import be.swsb.effit.backend.messaging.command.CommandHandler
import be.swsb.effit.backend.domain.core.competition.Competition
import be.swsb.effit.backend.domain.core.competition.CompetitionId
import be.swsb.effit.backend.domain.query.competition.CompetitionQueries.FindCompetition
import be.swsb.effit.backend.messaging.query.QueryExecutor

class UnstartCompetitionCommandHandler(
    private val queryExecutor: QueryExecutor,
    private val competitionRepository: CompetitionRepository
) : CommandHandler<Competition, UnstartCompetition> {

    override fun handle(command: UnstartCompetition): Competition {
        val competition = findCompetition(command.id)
        return unstartCompetition(competition)
    }

    private fun unstartCompetition(competition: Competition): Competition {
        return if (!competition.started) {
            competition
        } else {
            competition.unstart()
            competitionRepository.save(competition)
        }
    }

    private fun findCompetition(competitionId: CompetitionId): Competition {
        return queryExecutor.execute(FindCompetition(competitionId))
    }

    override fun getCommandType(): Class<UnstartCompetition> {
        return UnstartCompetition::class.java
    }

}
