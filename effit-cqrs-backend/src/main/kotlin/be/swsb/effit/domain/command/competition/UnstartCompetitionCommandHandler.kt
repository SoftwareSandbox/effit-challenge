package be.swsb.effit.domain.command.competition

import be.swsb.effit.adapter.sql.competition.CompetitionRepository
import be.swsb.effit.domain.command.CommandHandler
import be.swsb.effit.domain.core.competition.Competition
import be.swsb.effit.domain.core.competition.CompetitionId
import be.swsb.effit.domain.core.exceptions.EntityNotFoundDomainRuntimeException
import be.swsb.effit.domain.query.competition.FindCompetition
import be.swsb.effit.messaging.query.QueryExecutor
import org.springframework.stereotype.Component

@Component
class UnstartCompetitionCommandHandler(
        private val queryExecutor: QueryExecutor,
        private val competitionRepository: CompetitionRepository)
    : CommandHandler<Competition, UnstartCompetition> {

    override fun handle(command: UnstartCompetition): Competition {
        val competition = findCompetitionOrThrow(command.id)
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

    private fun findCompetitionOrThrow(competitionId: CompetitionId): Competition {
        return queryExecutor.execute(FindCompetition(competitionId))
                ?: throw EntityNotFoundDomainRuntimeException("Competition with id ${competitionId.id} not found")
    }

    override fun getCommandType(): Class<UnstartCompetition> {
        return UnstartCompetition::class.java
    }

}
