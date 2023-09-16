package be.swsb.effit.backend.domain.command.competition

import be.swsb.effit.backend.domain.command.CommandHandler
import be.swsb.effit.backend.domain.command.competition.CompetitionCommands.StartCompetition
import be.swsb.effit.backend.domain.core.competition.Competition
import be.swsb.effit.backend.domain.core.competition.CompetitionId
import be.swsb.effit.backend.domain.query.competition.CompetitionQueries.FindCompetition
import be.swsb.effit.backend.messaging.query.QueryExecutor

class StartCompetitionCommandHandler(
    private val queryExecutor: QueryExecutor,
    private val competitionRepository: CompetitionRepository)
    : CommandHandler<Competition, StartCompetition> {

    override fun handle(command: StartCompetition): Competition {
        val competition = findCompetition(command.id)
        return startCompetition(competition)
    }

    private fun startCompetition(competition: Competition): Competition {
        return if (competition.started) {
            competition
        } else {
            competition.start()
            competitionRepository.save(competition)
        }
    }

    private fun findCompetition(competitionId: CompetitionId): Competition {
        return queryExecutor.execute(FindCompetition(competitionId))
    }

    override fun getCommandType(): Class<StartCompetition> {
        return StartCompetition::class.java
    }

}
