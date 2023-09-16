package be.swsb.effit.backend.domain.command.competition

import be.swsb.effit.backend.messaging.command.CommandHandler
import be.swsb.effit.backend.domain.core.competition.Competition
import be.swsb.effit.backend.domain.core.competition.CompetitionAlreadyExistsDomainException
import be.swsb.effit.backend.domain.core.competition.CompetitionCreator
import be.swsb.effit.backend.domain.query.competition.CompetitionQueries.MaybeFindCompetition
import be.swsb.effit.backend.messaging.query.QueryExecutor

class CreateCompetitionCommandHandler(
    private val queryExecutor: QueryExecutor,
    private val competitionCreator: CompetitionCreator,
    private val competitionRepository: CompetitionRepository
) : CommandHandler<Competition, CreateCompetition> {

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
