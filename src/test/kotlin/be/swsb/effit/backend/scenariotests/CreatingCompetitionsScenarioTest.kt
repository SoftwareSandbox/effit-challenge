package be.swsb.effit.backend.scenariotests

import be.swsb.effit.backend.domain.command.challenge.UpdateChallengeCommandHandler
import be.swsb.effit.backend.domain.command.competition.*
import be.swsb.effit.backend.domain.command.competition.competitor.AddCompetitorCommandHandler
import be.swsb.effit.backend.domain.command.competition.competitor.RemoveCompetitorCommandHandler
import be.swsb.effit.backend.domain.core.challenge.Challenge
import be.swsb.effit.backend.domain.core.challenge.ChallengeRepository
import be.swsb.effit.backend.domain.core.competition.Competition
import be.swsb.effit.backend.domain.core.competition.CompetitionAlreadyExistsDomainException
import be.swsb.effit.backend.domain.core.competition.CompetitionCreator
import be.swsb.effit.backend.domain.core.competition.CompetitionId
import be.swsb.effit.backend.domain.core.exceptions.DomainValidationRuntimeException
import be.swsb.effit.backend.domain.query.challenge.FindChallengeQueryHandler
import be.swsb.effit.backend.domain.query.competition.FindAllCompetitionsQueryHandler
import be.swsb.effit.backend.domain.query.competition.FindCompetitionQueryHandler
import be.swsb.effit.backend.domain.query.competition.MaybeFindCompetitionQueryHandler
import be.swsb.effit.backend.messaging.command.CommandExecutor
import be.swsb.effit.backend.messaging.query.QueryExecutor
import java.time.LocalDate
import java.util.*
import kotlin.test.Test
import kotlin.test.assertFailsWith

class CreatingCompetitionsScenarioTest {
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
    fun `Competition without a name should not be created`() {
        assertFailsWith(DomainValidationRuntimeException::class) {
            commandExecutor.execute(CreateCompetition("", LocalDate.now(), LocalDate.now()))
        }
    }

    @Test
    fun `Competition that ends up having an already existing CompetitionId should not be created`() {
        val competition = CreateCompetition(name = "Thundercats%Competition",
                startDate = LocalDate.of(2018, 3, 16),
                endDate = LocalDate.of(2018, 3, 26))
        scenarios.createNewCompetition(competition)

        val competitionWithIdThatAlreadyExists = CreateCompetition(name = "Thundercats Competition",
                startDate = LocalDate.of(2019, 1, 17),
                endDate = LocalDate.of(2019, 1, 27))

        assertFailsWith(CompetitionAlreadyExistsDomainException::class) {
            scenarios.createNewCompetition(competitionWithIdThatAlreadyExists)
        }
    }
//
//    @Test
//    fun `Adding Challenges, one of which is invalid, should not add any Challenge`() {
//        val challengeToBeCreated = ChallengeToAdd(name = "Picasso", points = 3, description = "Paint a mustache on a sleeping victim without getting caught")
//        val anotherChallengeToBeCreated = ChallengeToAdd(name = "SomethingElse", points = 5, description = "Something...")
//        val invalidChallengeToBeCreated = ChallengeToAdd(name = "Invalid Challenge", points = 0, description = "Invalid because points cannot be 0")
//
//        val competition = CreateCompetition(name = "InvalidChallengesComp",
//                startDate = LocalDate.of(2018, 3, 16),
//                endDate = LocalDate.of(2018, 3, 26))
//        val competitionId = scenarios.createNewCompetition(competition)
//        assertThat(scenarios.getCompetition(competitionId).challenges).isEmpty()
//
//        val challengesToBeAdded = listOf(challengeToBeCreated, invalidChallengeToBeCreated, anotherChallengeToBeCreated)
//        scenarios.mockMvc.perform(MockMvcRequestBuilders.post("/api/competition/{id}/addChallenges", competitionId.id)
//                .content(challengesToBeAdded.toJson(scenarios.objectMapper))
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isBadRequest)
//
//        assertThat(scenarios.getCompetition(competitionId).challenges).isEmpty()
//    }
//
//    @Test
//    fun `Challenges should get copied when hosting a Competition again`() {
//        val challengeToBeCreated = ChallengeToAdd(name = "Picasso", points = 3, description = "Paint a mustache on a sleeping victim without getting caught")
//
//        val competition = CreateCompetition(name = "DummyCompetition",
//                startDate = LocalDate.of(2018, 3, 16),
//                endDate = LocalDate.of(2018, 3, 26))
//        val competitionId = scenarios.createNewCompetition(competition, challengeToBeCreated)
//        val originalChallengeId = scenarios.getCompetition(competitionId).challenges.first().id
//
//        val hostedAgainCompetition = CreateCompetition(name = "DummyCompetitionThe2nd",
//                startDate = LocalDate.of(2018, 3, 16),
//                endDate = LocalDate.of(2018, 3, 26))
//        val hostedAgainCompetitionId = scenarios.createNewCompetition(hostedAgainCompetition, challengeToBeCreated)
//
//        assertThat(scenarios.getCompetition(hostedAgainCompetitionId).challenges.first().id)
//                .isNotEqualTo(originalChallengeId)
//    }
//
//    @Test
//    fun `Challenges can get updated when a Competition is not yet started`() {
//        val challengeToBeCreated = ChallengeToAdd(name = "Picasso", points = 3, description = "Paint a mustache on a sleeping victim without getting caught")
//
//        val competition = CreateCompetition(name = "CompetitionWChallenges",
//                startDate = LocalDate.of(2018, 3, 16),
//                endDate = LocalDate.of(2018, 3, 26))
//        val competitionId = scenarios.createNewCompetition(competition, challengeToBeCreated)
//
//        val challengeToUpdate = scenarios.getCompetition(competitionId).challenges.find { it.name == challengeToBeCreated.name }
//                ?: fail("Expected a challenge with name ${challengeToBeCreated.name}")
//
//        scenarios.updateChallenge(challengeToUpdate.copy(name = "Francisco"))
//
//        assertThat(scenarios.getCompetition(competitionId).challenges)
//                .extracting<String> { it.name }
//                .containsOnly("Francisco")
//    }
//
//    @Test
//    fun `Challenges can get updated when a Competition was unstarted`() {
//        val challengeToBeCreated = ChallengeToAdd(name = "Picasso", points = 3, description = "Paint a mustache on a sleeping victim without getting caught")
//
//        val competition = CreateCompetition(name = "UnstartedCompWChallenges",
//                startDate = LocalDate.of(2018, 3, 16),
//                endDate = LocalDate.of(2018, 3, 26))
//        val competitionId = scenarios.createNewCompetition(competition, challengeToBeCreated)
//        scenarios.addCompetitor("Snarf", competitionId)
//
//        scenarios.startCompetition(competitionId)
//        scenarios.unstartCompetition(competitionId)
//
//        val challengeToUpdate = scenarios.getCompetition(competitionId).challenges.find { it.name == challengeToBeCreated.name }
//                ?: fail("Expected a challenge with name ${challengeToBeCreated.name}")
//
//        scenarios.updateChallenge(challengeToUpdate.copy(name = "Francisco"))
//
//        assertThat(scenarios.getCompetition(competitionId).challenges)
//                .extracting<String> { it.name }
//                .containsOnly("Francisco")
//    }

}

