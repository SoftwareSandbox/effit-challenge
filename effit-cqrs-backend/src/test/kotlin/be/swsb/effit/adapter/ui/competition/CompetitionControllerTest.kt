package be.swsb.effit.adapter.ui.competition

import be.swsb.effit.adapter.sql.challenge.ChallengeRepository
import be.swsb.effit.adapter.sql.competition.CompetitionRepository
import be.swsb.effit.adapter.sql.competition.competitor.CompetitorRepository
import be.swsb.effit.adapter.ui.competition.competitor.CompleterId
import be.swsb.effit.adapter.ui.exceptions.EffitError
import be.swsb.effit.adapter.ui.util.toJson
import be.swsb.effit.domain.command.competition.CreateCompetition
import be.swsb.effit.domain.command.competition.StartCompetition
import be.swsb.effit.domain.command.competition.UnstartCompetition
import be.swsb.effit.domain.core.challenge.Challenge
import be.swsb.effit.domain.core.challenge.defaultChallengeForTest
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
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDate
import java.util.UUID.randomUUID

class CompetitionControllerTest : ControllerTest() {

    @Autowired
    lateinit var competitionRepositoryMock: CompetitionRepository
    @Autowired
    lateinit var competitorRepositoryMock: CompetitorRepository
    @Autowired
    lateinit var challengeRepositoryMock: ChallengeRepository
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
                challenges = listOf(Challenge.defaultChallengeForTest())
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
    fun `POST api_competition_competitionId_unstart should return 202 when Competition was not started`() {
        val competitionId = "ThundercatsCompetition"
        val existingCompetition = Competition.defaultCompetitionForTest(name = competitionId, started = false)
        `when findByCompetitionIdentifier then return`(competitionId, existingCompetition)

        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition/{competitionId}/unstart", competitionId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isAccepted)
    }

    @Test
    fun `POST api_competition_addChallenges should add given Challenges to the given Competition`() {
        val challenge1 = Challenge.defaultChallengeForTest(name = "FirstChallenge")
        val challenge2 = Challenge.defaultChallengeForTest(name = "SecondChallenge")
        val givenChallenges = listOf(challenge1, challenge2)

        val requestedCompetitionIdAsString = "Thundercats"

        val thundercatsComp = Competition.defaultCompetitionForTest(name = requestedCompetitionIdAsString)
        `when findByCompetitionIdentifier then return`(requestedCompetitionIdAsString, thundercatsComp)
        `when`(challengeRepositoryMock.save(ArgumentMatchers.any(Challenge::class.java))).thenReturn(challenge1)

        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition/{id}/addChallenges", requestedCompetitionIdAsString)
                .content(givenChallenges.toJson(objectMapper))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isAccepted)

        val challengesCaptor = ArgumentCaptor.forClass(Challenge::class.java)

        verify(challengeRepositoryMock, times(2)).save(challengesCaptor.capture())

        verify(competitionRepositoryMock).save(thundercatsComp)
        assertThat(challengesCaptor.allValues[0].name).isEqualTo("FirstChallenge")
        assertThat(challengesCaptor.allValues[0].id).isNotEqualTo(challenge1.id)
        assertThat(challengesCaptor.allValues[1].name).isEqualTo("SecondChallenge")
        assertThat(challengesCaptor.allValues[1].id).isNotEqualTo(challenge2.id)
    }

    @Test
    fun `POST api_competition_compId_addCompetitor should add given Competitor to Competitions' Competitors`() {
        val givenCompetitionId = "Thundercats"
        val thundercatsComp = Competition.defaultCompetitionForTest(name = givenCompetitionId)
        `when findByCompetitionIdentifier then return`(givenCompetitionId, thundercatsComp)

        val snarf = Competitor.defaultCompetitorForTest()
        `when`(competitorRepositoryMock.save(snarf)).thenReturn(snarf)

        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition/{competitionId}/addCompetitor", givenCompetitionId)
                .content(snarf.toJson(objectMapper))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isAccepted)

        assertThat(thundercatsComp.competitors)
                .usingElementComparatorIgnoringFields("id")
                .containsExactly(snarf)

        val inOrder = inOrder(competitorRepositoryMock, competitionRepositoryMock)
        inOrder.verify(competitorRepositoryMock).save(snarf)
        inOrder.verify(competitionRepositoryMock).save(thundercatsComp)
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
        val competitorId = randomUUID()
        val successfulCompetitor = CompleterId(competitorId)
        val givenCompetitionId = "SnowCase2018"
        val givenChallengeId = randomUUID()

        val picassoChallenge = Challenge.defaultChallengeForTest(id = givenChallengeId)
        val compWithChallenges = Competition.defaultCompetitionForTest(
                name = givenCompetitionId,
                competitors = listOf(Competitor.defaultCompetitorForTest(id = competitorId)),
                challenges = listOf(picassoChallenge),
                started = true)
        `when findByCompetitionIdentifier then return`(givenCompetitionId, compWithChallenges)

        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition/{competitionId}/complete/{challengeId}",
                givenCompetitionId,
                givenChallengeId.toString()
        )
                .content(successfulCompetitor.toJson(objectMapper))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isAccepted)

        assertThat(compWithChallenges.competitors.find { it.id == competitorId }?.completedChallenges)
                .containsExactly(picassoChallenge)
        verify(competitionRepositoryMock).save(compWithChallenges)
    }

    @Test
    fun `POST api_competition_compId_complete_challengeId should return 400 when Competitor already finished given challenge`() {
        val competitorId = randomUUID()
        val successfulCompetitor = CompleterId(competitorId)
        val givenCompetitionId = "SnowCase2018"
        val givenChallengeId = randomUUID()

        val picassoChallenge = Challenge.defaultChallengeForTest(id = givenChallengeId)
        val snarf = Competitor.defaultCompetitorForTest(id = competitorId, completedChallenges = listOf(picassoChallenge))

        val compWithChallenges = Competition.defaultCompetitionForTest(
                name = givenCompetitionId,
                competitors = listOf(snarf),
                challenges = listOf(picassoChallenge),
                started = true
        )
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
        val givenChallengeId = randomUUID()

        val challengeThatWillGetDeleted = Challenge.defaultChallengeForTest(id = givenChallengeId)
        val someOtherChallenge = Challenge.defaultChallengeForTest(id = randomUUID())
        val someCompetition = Competition.defaultCompetitionForTest(challenges = listOf(challengeThatWillGetDeleted, someOtherChallenge))
        `when findByCompetitionIdentifier then return`(givenCompetitionId, someCompetition)

        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition/{competitionId}/removeChallenge/{challengeId}",
                givenCompetitionId,
                givenChallengeId)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk)

        assertThat(someCompetition.challenges)
                .doesNotContain(challengeThatWillGetDeleted)
                .contains(someOtherChallenge)
    }

    @Test
    fun `POST api_competition_compId_removeCompetitor should remove the given competitor id`() {
        val givenCompetitionId = "SnowCase2018"

        val snarf = Competitor.defaultCompetitorForTest()
        val someCompetition = Competition.defaultCompetitionForTest(competitors = listOf(snarf))
        `when findByCompetitionIdentifier then return`(givenCompetitionId, someCompetition)

        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition/{competitionId}/removeCompetitor", givenCompetitionId)
                .content(snarf.toJson(objectMapper))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isAccepted)

        assertThat(someCompetition.competitors).isEmpty()

        verify(competitionRepositoryMock).save(someCompetition)
    }

    @Test
    fun `POST api_competition_compId_removeCompetitor should return 400 when no competitor found for given id`() {
        val givenCompetitionId = "SnowCase2018"

        val snarf = Competitor.defaultCompetitorForTest()
        val someCompetition = Competition.defaultCompetitionForTest(competitors = listOf(snarf))
        `when findByCompetitionIdentifier then return`(givenCompetitionId, someCompetition)

        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition/{competitionId}/removeCompetitor", givenCompetitionId)
                .content(Competitor.defaultCompetitorForTest().toJson(objectMapper))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest)

        assertThat(someCompetition.competitors).containsExactly(snarf)

        verify(competitionRepositoryMock, never()).save(ArgumentMatchers.any(Competition::class.java))
    }

    private fun `when findByCompetitionIdentifier then return`(requestedCompetitionIdAsString: String, expectedCompetition: Competition) {
        `when`(queryExecutorMock.execute(FindCompetition(CompetitionId(requestedCompetitionIdAsString)))).thenReturn(expectedCompetition)
    }
}
