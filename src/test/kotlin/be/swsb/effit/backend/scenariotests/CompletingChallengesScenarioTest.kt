package be.swsb.effit.backend.scenariotests

import be.swsb.effit.backend.domain.aDefaultCompetitorForTest
import be.swsb.effit.backend.domain.command.challenge.UpdateChallengeCommandHandler
import be.swsb.effit.backend.domain.command.competition.*
import be.swsb.effit.backend.domain.command.competition.competitor.AddCompetitorCommandHandler
import be.swsb.effit.backend.domain.command.competition.competitor.RemoveCompetitorCommandHandler
import be.swsb.effit.backend.domain.core.challenge.ChallengeRepository
import be.swsb.effit.backend.domain.core.competition.CompetitionCreator
import be.swsb.effit.backend.domain.core.competition.competitor.Competitor
import be.swsb.effit.backend.domain.core.exceptions.EntityNotFoundDomainRuntimeException
import be.swsb.effit.backend.domain.query.challenge.FindChallengeQueryHandler
import be.swsb.effit.backend.domain.query.competition.FindAllCompetitionsQueryHandler
import be.swsb.effit.backend.domain.query.competition.FindCompetitionQueryHandler
import be.swsb.effit.backend.domain.query.competition.MaybeFindCompetitionQueryHandler
import be.swsb.effit.backend.messaging.command.CommandExecutor
import be.swsb.effit.backend.messaging.query.QueryExecutor
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertContains

class CompletingChallengesScenarioTest {
    private val competitionRepository: CompetitionRepository = InMemCompetitionRepository()
    private val challengeRepository: ChallengeRepository = InMemChallengeRepository()

    private val queryExecutor = QueryExecutor(
        listOf(
            FindAllCompetitionsQueryHandler(competitionRepository),
            FindCompetitionQueryHandler(competitionRepository),
            MaybeFindCompetitionQueryHandler(competitionRepository),
            FindChallengeQueryHandler(challengeRepository),
        )
    )

    private val competitionCreator = CompetitionCreator()

    private val commandExecutor = CommandExecutor(
        listOf(
            AddChallengesCommandHandler(queryExecutor, competitionRepository),
            AddCompetitorCommandHandler(queryExecutor, competitionRepository),
            CompleteChallengeCommandHandler(queryExecutor, competitionRepository),
            CreateCompetitionCommandHandler(queryExecutor, competitionCreator, competitionRepository),
            RemoveChallengeCommandHandler(queryExecutor, competitionRepository),
            RemoveCompetitorCommandHandler(queryExecutor, competitionRepository),
            StartCompetitionCommandHandler(queryExecutor, competitionRepository),
            UnstartCompetitionCommandHandler(queryExecutor, competitionRepository),
            UpdateChallengeCommandHandler(queryExecutor, challengeRepository),
        )
    )

    private val scenarios = Scenarios(commandExecutor, queryExecutor)

    @Test
    fun `Snarf completes the whinge challenge and gets awarded 4 points`() {
        val whingeChallenge =
            ChallengeToAdd(name = "Whinge", points = 4, description = "Whinge at any point during the day")

        val competition = CreateCompetition(
            name = "ThundercatsCompo 2019",
            startDate = LocalDate.of(2018, 3, 16),
            endDate = LocalDate.of(2018, 3, 26)
        )
        val competitionId = scenarios.createNewCompetition(competition)

        scenarios.addChallenges(competitionId, listOf(whingeChallenge))

        val snarfId = scenarios.addCompetitor("Snarf", competitionId)

        val fetchedWhingeChallenge = scenarios.getCompetition(competitionId).challenges
            .find { it.name == whingeChallenge.name }
            ?: throw EntityNotFoundDomainRuntimeException("Challenge ${whingeChallenge.name} not found in competition $competitionId")

        scenarios.startCompetition(competitionId)

        scenarios.completeChallenge(competitionId, fetchedWhingeChallenge.id, snarfId)

        val updatedThundercatsCompetition = scenarios.getCompetition(competitionId)

        val snarfWithCompletedChallenge: Competitor = aDefaultCompetitorForTest(id = snarfId, name = "Snarf")
        snarfWithCompletedChallenge.completeChallenge(fetchedWhingeChallenge)

        assertContains(updatedThundercatsCompetition.competitors, snarfWithCompletedChallenge)
    }
}