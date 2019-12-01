package be.swsb.effit.adapter.ui.competition

import be.swsb.effit.adapter.sql.competition.CompetitionRepository
import be.swsb.effit.adapter.sql.competition.competitor.CompetitorRepository
import be.swsb.effit.adapter.ui.competition.competitor.CompleterId
import be.swsb.effit.adapter.ui.exceptions.EffitError
import be.swsb.effit.adapter.ui.util.toJson
import be.swsb.effit.domain.command.competition.*
import be.swsb.effit.domain.command.competition.competitor.CompetitorId
import be.swsb.effit.domain.command.competition.competitor.CompetitorName
import be.swsb.effit.domain.core.competition.*
import be.swsb.effit.domain.core.competition.competitor.Competitor
import be.swsb.effit.domain.core.competition.competitor.defaultCompetitorForTest
import be.swsb.effit.domain.query.competition.FindAllCompetitions
import be.swsb.effit.domain.query.competition.FindCompetition
import be.swsb.effit.messaging.command.CommandExecutor
import be.swsb.effit.messaging.query.QueryExecutor
import be.swsb.test.effit.ControllerTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDate
import java.util.*
import java.util.UUID.randomUUID

class CompetitionControllerTest : ControllerTest() {

    @Autowired
    lateinit var competitionRepositoryMock: CompetitionRepository
    @Autowired
    lateinit var competitorRepositoryMock: CompetitorRepository
    @Autowired
    lateinit var queryExecutorMock: QueryExecutor
    @Autowired
    private lateinit var commandExecutorMock: CommandExecutor

    @Test
    fun `GET api_competition should return all Competitions`() {
        val expectedCompetitions = listOf(Competition.defaultCompetitionForTest())
        `when`(queryExecutorMock.execute(FindAllCompetitions)).thenReturn(expectedCompetitions)

        mockMvc.perform(MockMvcRequestBuilders.get("/api/competition")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(expectedCompetitions.toJson(objectMapper), true))
    }

    @Test
    fun `GET api_competition_name should return the competition with matching competition id`() {
        val requestedCompetitionIdAsString = "SnowCase2018"
        val expectedCompetitionWithChallenges = Competition.defaultCompetitionForTest(
                challenges = listOf(ChallengeToAdd.defaultChallengeToAddForTest())
        )
        `when findByCompetitionIdentifier then return`(requestedCompetitionIdAsString, expectedCompetitionWithChallenges)

        mockMvc.perform(MockMvcRequestBuilders.get("/api/competition/{competitionId}", requestedCompetitionIdAsString)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(expectedCompetitionWithChallenges.toJson(objectMapper), true))
    }

    @Test
    fun `POST api_competition should be able to create a new Competition and save it`() {
        val createCompetition = CreateCompetition(name = "Snowcase 2018",
                startDate = LocalDate.of(2018, 3, 15),
                endDate = LocalDate.of(2018, 3, 25))
        val createdCompetition = Competition.defaultCompetitionForTest(name = "Snowcase 2018",
                startDate = LocalDate.of(2018, 3, 15),
                endDate = LocalDate.of(2018, 3, 25))
        `when`(commandExecutorMock.execute(createCompetition)).thenReturn(createdCompetition)

        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition")
                .content(createCompetition.toJson(objectMapper))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated)
                .andExpect(header().string(HttpHeaders.LOCATION, createdCompetition.competitionId.id))
    }

    @Test
    fun `POST api_competition_competitionId_start should start a Competition`() {
        val competitionId = "ThundercatsCompetition"
        val existingCompetition = Competition.defaultCompetitionWithChallengesAndCompetitorsForTest(name = competitionId)
        `when`(commandExecutorMock.execute(StartCompetition(CompetitionId(competitionId)))).thenReturn(existingCompetition)

        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition/{competitionId}/start", competitionId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isAccepted)
    }

    @Test
    fun `POST api_competition_competitionId_unstart should undo starting a Competition`() {
        val competitionId = "ThundercatsCompetition"
        val existingCompetition = Competition.defaultStartedCompetition(name = competitionId)
        `when`(commandExecutorMock.execute(UnstartCompetition(CompetitionId(competitionId)))).thenReturn(existingCompetition)

        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition/{competitionId}/unstart", competitionId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isAccepted)
    }

    @Test
    fun `POST api_competition_addChallenges should add given Challenges to the given Competition`() {
        val challenge1 = ChallengeToAdd.defaultChallengeToAddForTest(name = "FirstChallenge")
        val challenge2 = ChallengeToAdd.defaultChallengeToAddForTest(name = "SecondChallenge")
        val givenChallenges = listOf(challenge1, challenge2)

        val requestedCompetitionIdAsString = "Thundercats"
        val requestedCompetitionId = CompetitionId(requestedCompetitionIdAsString)

        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition/{id}/addChallenges", requestedCompetitionIdAsString)
                .content(givenChallenges.toJson(objectMapper))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isAccepted)

        verify(commandExecutorMock).execute(AddChallenges(requestedCompetitionId, givenChallenges))
    }

    @Test
    fun `POST api_competition_compId_addCompetitor should add given Competitor to Competitions' Competitors`() {
        val givenCompetitionId = "Thundercats"

        val snarf = Competitor.defaultCompetitorForTest(name = "Snarf")
        `when`(competitorRepositoryMock.save(snarf)).thenReturn(snarf)

        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition/{competitionId}/addCompetitor", givenCompetitionId)
                .content(snarf.toJson(objectMapper))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isAccepted)

        verify(commandExecutorMock).execute(AddCompetitor(CompetitionId(givenCompetitionId), CompetitorName("Snarf")))
    }

    @Test
    fun `POST api_competition_compId_complete_challengeId should return 404 when no challenge was found`() {
        val successfulCompetitor = CompleterId(randomUUID())
        val givenCompetitionId = "SnowCase2018"
        val givenChallengeId = randomUUID().toString()

        val compWithoutChallenges = Competition.defaultCompetitionForTest(name = givenCompetitionId)
        `when findByCompetitionIdentifier then return`(givenCompetitionId, compWithoutChallenges)

        val expectedError = EffitError("Competition with id $givenCompetitionId has no challenge with id $givenChallengeId")

        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition/{competitionId}/complete/{challengeId}",
                givenCompetitionId,
                givenChallengeId
        )
                .content(successfulCompetitor.toJson(objectMapper))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound)
                .andExpect(content().json(expectedError.toJson(objectMapper), true))
    }

    @Test
    fun `POST api_competition_compId_complete_challengeId should add challenge given Competitors completedChallenges`() {
        val givenCompetitionId = "SnowCase2018"

        val compWithChallenges = Competition.defaultCompetitionForTest(
                name = givenCompetitionId,
                competitors = listOf(CompetitorName("Snarf")),
                challenges = listOf(ChallengeToAdd.defaultChallengeToAddForTest(name = "Picasso")),
                started = true)
        val snarf = compWithChallenges.findCompetitor("Snarf")
        val successfulCompetitor = CompleterId(snarf.id)
        val picassoChallenge = compWithChallenges.findChallenge("Picasso")
        val givenChallengeId = picassoChallenge.id
        `when findByCompetitionIdentifier then return`(givenCompetitionId, compWithChallenges)

        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition/{competitionId}/complete/{challengeId}",
                givenCompetitionId,
                givenChallengeId.toString()
        )
                .content(successfulCompetitor.toJson(objectMapper))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isAccepted)

        assertThat(snarf.completedChallenges)
                .containsExactly(picassoChallenge)
        verify(competitionRepositoryMock).save(compWithChallenges)
    }

    @Test
    fun `POST api_competition_compId_complete_challengeId should return 400 when Competitor already finished given challenge`() {
        val givenCompetitionId = "SnowCase2018"

        val compWithChallenges = Competition.defaultCompetitionForTest(
                name = givenCompetitionId,
                competitors = listOf(CompetitorName("Snarf")),
                challenges = listOf(ChallengeToAdd.defaultChallengeToAddForTest(name = "Picasso")),
                started = true
        )
        val picassoChallenge = compWithChallenges.findChallenge("Picasso")
        val givenChallengeId = picassoChallenge.id
        val snarf = compWithChallenges.findCompetitor("Snarf")
        snarf.completeChallenge(picassoChallenge)
        val successfulCompetitor = CompleterId(snarf.id)
        `when findByCompetitionIdentifier then return`(givenCompetitionId, compWithChallenges)

        val expectedError = EffitError("This competitor already completed the ${picassoChallenge.name} challenge.")
        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition/{competitionId}/complete/{challengeId}",
                givenCompetitionId,
                givenChallengeId.toString()
        )
                .content(successfulCompetitor.toJson(objectMapper))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest)
                .andExpect(content().json(expectedError.toJson(objectMapper), true))

        verify(competitionRepositoryMock, never()).save(compWithChallenges)
    }

    @Test
    fun `api_competition_competitionId_removeChallenge_challengeId should remove the given challenge id`() {
        val givenCompetitionId = "SnowCase2018"

        val challengeThatWillGetDeleted = ChallengeToAdd.defaultChallengeToAddForTest(name = "ChallengeToBeDeleted")
        val someOtherChallenge = ChallengeToAdd.defaultChallengeToAddForTest(name = "Picasso")
        val someCompetition = Competition.defaultCompetitionForTest(challenges = listOf(challengeThatWillGetDeleted, someOtherChallenge))
        val challengeExpectedToBeDeleted = someCompetition.findChallenge("ChallengeToBeDeleted")
        val givenChallengeId = challengeExpectedToBeDeleted.id

        `when findByCompetitionIdentifier then return`(givenCompetitionId, someCompetition)

        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition/{competitionId}/removeChallenge/{challengeId}",
                givenCompetitionId,
                givenChallengeId)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk)

        assertThat(someCompetition.challenges)
                .doesNotContain(challengeExpectedToBeDeleted)
    }

    @Test
    fun `POST api_competition_compId_removeCompetitor should remove the given competitor id`() {
        val givenCompetitionId = "SnowCase2018"
        val snarfId = CompetitorId(UUID.randomUUID())

        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition/{competitionId}/removeCompetitor", givenCompetitionId)
                .content(snarfId.toJson(objectMapper))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isAccepted)

        verify(commandExecutorMock).execute(RemoveCompetitor(CompetitionId(givenCompetitionId), snarfId))
    }

    private fun `when findByCompetitionIdentifier then return`(requestedCompetitionIdAsString: String, expectedCompetition: Competition) {
        `when`(queryExecutorMock.execute(FindCompetition(CompetitionId(requestedCompetitionIdAsString)))).thenReturn(expectedCompetition)
    }
}
