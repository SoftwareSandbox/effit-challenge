package be.swsb.effit.domain.command.competition

import be.swsb.effit.adapter.sql.competition.CompetitionRepository
import be.swsb.effit.domain.command.CommandHandler
import be.swsb.effit.domain.core.competition.Competition
import be.swsb.effit.domain.core.competition.CompetitionAlreadyExistsDomainException
import be.swsb.effit.domain.core.competition.CompetitionCreator
import be.swsb.effit.domain.query.competition.MaybeFindCompetition
import be.swsb.effit.messaging.query.QueryExecutor
import org.springframework.stereotype.Component

@Component
class CreateCompetitionCommandHandler(
        private val queryExecutor: QueryExecutor,
        private val competitionCreator: CompetitionCreator,
        private val competitionRepository: CompetitionRepository)
    : CommandHandler<Competition, CreateCompetition> {
    override fun handle(command: CreateCompetition): Competition {
        val competitionToBeCreated = competitionCreator.from(command)
        queryExecutor.execute(MaybeFindCompetition(competitionToBeCreated.competitionId))
                ?.let { throw CompetitionAlreadyExistsDomainException(competitionToBeCreated.competitionId) }
        return competitionRepository.save(competitionToBeCreated)
    }

    override fun getCommandType(): Class<CreateCompetition> {
        return CreateCompetition::class.java
    }
}
