package be.swsb.effit.competition

import be.swsb.effit.domain.core.challenge.Challenge
import be.swsb.effit.adapter.sql.challenge.ChallengeRepository
import be.swsb.effit.adapter.sql.competition.CompetitionRepository
import be.swsb.effit.challenge.defaultChallengeForTest
import be.swsb.effit.domain.core.competition.competitor.Competitor
import be.swsb.effit.adapter.sql.competition.competitor.CompetitorRepository
import be.swsb.effit.adapter.ui.competition.competitor.CompleterId
import be.swsb.effit.competition.competitor.defaultCompetitorForTest
import be.swsb.effit.domain.command.competition.CreateCompetition
import be.swsb.effit.domain.core.competition.CompetitionCreator
import be.swsb.effit.domain.core.competition.CompetitionId
import be.swsb.effit.exceptions.EffitError
import be.swsb.effit.util.toJson
import be.swsb.test.effit.ControllerTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
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

    @MockBean
    lateinit var competitionCreatorMock: CompetitionCreator

    @Test
    fun `GET api_competition should return all Competitions`() {
        val expectedCompetitions = listOf(Competition.defaultCompetitionForTest())

        Mockito.`when`(competitionRepositoryMock.findAll()).thenReturn(expectedCompetitions)

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

        Mockito.`when`(competitionRepositoryMock.findByCompetitionIdentifier(CompetitionId(requestedCompetitionIdAsString))).thenReturn(expectedCompetitionWithChallenges)

        mockMvc.perform(MockMvcRequestBuilders.get("/api/competition/{competitionId}", requestedCompetitionIdAsString)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(expectedCompetitionWithChallenges.toJson(objectMapper), true))
    }

    @Test
    fun `GET api_competition_name should return 404 when no matching Competition found for given name`() {
        val requestedCompetitionIdAsString = "SnowCase2018"

        Mockito.`when`(competitionRepositoryMock.findByCompetitionIdentifier(CompetitionId(requestedCompetitionIdAsString))).thenReturn(null)

        mockMvc.perform(MockMvcRequestBuilders.get("/api/competition/{competitionId}", requestedCompetitionIdAsString)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound)
    }

    @Test
    fun `POST api_competition, should be able to create a new Competition and save it`() {
        val createCompetition = CreateCompetition(name = "Snowcase 2018",
                startDate = LocalDate.of(2018, 3, 15),
                endDate = LocalDate.of(2018, 3, 25))

        val competitionToCreate = Competition.defaultCompetitionForTest(name = "Snowcase 2018",
                startDate = LocalDate.of(2018, 3, 15),
                endDate = LocalDate.of(2018, 3, 25))
        val createdCompetition = Competition.defaultCompetitionForTest(name = "Snowcase 2018",
                startDate = LocalDate.of(2018, 3, 15),
                endDate = LocalDate.of(2018, 3, 25))

        Mockito.`when`(competitionCreatorMock.from(createCompetition)).thenReturn(competitionToCreate)
        Mockito.`when`(competitionRepositoryMock.save(ArgumentMatchers.any(Competition::class.java))).thenReturn(createdCompetition)

        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition")
                .content(createCompetition.toJson(objectMapper))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated)
                .andExpect(header().string(HttpHeaders.LOCATION, createdCompetition.competitionId.id))
    }

    @Test
    fun `POST api_competition should throw CompetitionAlreadyExistsException when CompetitionId already exists`() {
        val createCompetition = CreateCompetition(name = "Snowcase 2018",
                startDate = LocalDate.of(2018, 3, 15),
                endDate = LocalDate.of(2018, 3, 25))
        val competitionToCreate = Competition.defaultCompetitionForTest(name = "Snowcase 2018",
                startDate = LocalDate.of(2018, 3, 15),
                endDate = LocalDate.of(2018, 3, 25))

        val competitionIdThatAlreadyExists = competitionToCreate.competitionId
        val expectedError = EffitError("Sorry, there's already a competition that has a generated CompetitionId of ${competitionIdThatAlreadyExists.id}. Try entering a (slightly) different name.")

        Mockito.`when`(competitionCreatorMock.from(createCompetition)).thenReturn(competitionToCreate)
        Mockito.`when`(competitionRepositoryMock.findByCompetitionIdentifier(competitionIdThatAlreadyExists))
                .thenReturn(competitionToCreate)

        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition")
                .content(createCompetition.toJson(objectMapper))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest)
                .andExpect(content().json(expectedError.toJson(objectMapper), true))
    }

    @Test
    fun `POST api_competition should return 500 when competition was unable to be created`() {
        val createCompetition = CreateCompetition(name = "Snowcase 2018",
                startDate = LocalDate.of(2018, 3, 15),
                endDate = LocalDate.of(2018, 3, 25))

        Mockito.doThrow(IllegalStateException::class.java).`when`(competitionCreatorMock).from(createCompetition)

        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition")
                .content(createCompetition.toJson(objectMapper))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is5xxServerError)
    }

    @Test
    fun `POST api_competition should return 500 when competition was unable to be saved`() {
        val createCompetition = CreateCompetition(name = "Snowcase 2018",
                startDate = LocalDate.of(2018, 3, 15),
                endDate = LocalDate.of(2018, 3, 25))
        val competitionToCreate = Competition.defaultCompetitionForTest(name = "Snowcase 2018",
                startDate = LocalDate.of(2018, 3, 15),
                endDate = LocalDate.of(2018, 3, 25))

        Mockito.`when`(competitionCreatorMock.from(createCompetition)).thenReturn(competitionToCreate)
        Mockito.doThrow(IllegalStateException::class.java).`when`(competitionRepositoryMock).save(competitionToCreate)

        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition")
                .content(createCompetition.toJson(objectMapper))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is5xxServerError)
    }

    @Test
    fun `POST api_competition_competitionId_start should start a Competition`() {
        val existingCompetition = Competition.defaultCompetitionWithChallengesAndCompetitorsForTest(name = "ThundercatsCompetition")

        Mockito.`when`(competitionRepositoryMock.findByCompetitionIdentifier(CompetitionId("ThundercatsCompetition")))
                .thenReturn(existingCompetition)

        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition/{competitionId}/start", "ThundercatsCompetition")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isAccepted)

        assertThat(existingCompetition.started).isTrue()
    }

    @Test
    fun `POST api_competition_competitionId_start should return 202 when Competition was already started`() {
        val existingCompetition = Competition.defaultStartedCompetition(name = "ThundercatsCompetition")

        Mockito.`when`(competitionRepositoryMock.findByCompetitionIdentifier(CompetitionId("ThundercatsCompetition")))
                .thenReturn(existingCompetition)

        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition/{competitionId}/start", "ThundercatsCompetition")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isAccepted)
    }

    @Test
    fun `POST api_competition_competitionId_start should return 404 when no matching Competition found for given CompetitionId`() {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition/{competitionId}/start", "NonExistingCompetitionId")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound)
    }

    @Test
    fun `POST api_competition_competitionId_unstart should undo starting a Competition`() {
        val existingCompetition = Competition.defaultStartedCompetition(name = "ThundercatsCompetition")

        Mockito.`when`(competitionRepositoryMock.findByCompetitionIdentifier(CompetitionId("ThundercatsCompetition")))
                .thenReturn(existingCompetition)

        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition/{competitionId}/unstart", "ThundercatsCompetition")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isAccepted)

        assertThat(existingCompetition.started).isFalse()
    }

    @Test
    fun `POST api_competition_competitionId_unstart should return 202 when Competition was not started`() {
        val existingCompetition = Competition.defaultCompetitionForTest(name = "ThundercatsCompetition", started = false)

        Mockito.`when`(competitionRepositoryMock.findByCompetitionIdentifier(CompetitionId("ThundercatsCompetition")))
                .thenReturn(existingCompetition)

        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition/{competitionId}/unstart", "ThundercatsCompetition")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isAccepted)
    }

    @Test
    fun `POST api_competition_competitionId_unstart should return 404 when no matching Competition found for given CompetitionId`() {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition/{competitionId}/unstart", "NonExistingCompetitionId")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound)
    }

    @Test
    fun `POST api_competition_addChallenges should add given Challenges to the given Competition`() {
        val challenge1 = Challenge.defaultChallengeForTest(name = "FirstChallenge")
        val challenge2 = Challenge.defaultChallengeForTest(name = "SecondChallenge")
        val givenChallenges = listOf(challenge1, challenge2)

        val requestedCompetitionIdAsString = "Thundercats"

        val thundercatsComp = Competition.defaultCompetitionForTest(name = requestedCompetitionIdAsString)
        Mockito.`when`(competitionRepositoryMock.findByCompetitionIdentifier(CompetitionId(requestedCompetitionIdAsString)))
                .thenReturn(thundercatsComp)
        Mockito.`when`(challengeRepositoryMock.save(ArgumentMatchers.any(Challenge::class.java))).thenReturn(challenge1)

        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition/{id}/addChallenges", requestedCompetitionIdAsString)
                .content(givenChallenges.toJson(objectMapper))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isAccepted)

        val challengesCaptor = ArgumentCaptor.forClass(Challenge::class.java)

        Mockito.verify(challengeRepositoryMock, times(2)).save(challengesCaptor.capture())

        Mockito.verify(competitionRepositoryMock).save(thundercatsComp)
        assertThat(challengesCaptor.allValues[0].name).isEqualTo("FirstChallenge")
        assertThat(challengesCaptor.allValues[0].id).isNotEqualTo(challenge1.id)
        assertThat(challengesCaptor.allValues[1].name).isEqualTo("SecondChallenge")
        assertThat(challengesCaptor.allValues[1].id).isNotEqualTo(challenge2.id)
    }

    @Test
    fun `POST api_competition_addChallenges should return 404 when no matching Competition found for given CompetitionId`() {
        val requestedCompetitionIdAsString = "SnowCase2018"

        Mockito.`when`(competitionRepositoryMock.findByCompetitionIdentifier(CompetitionId(requestedCompetitionIdAsString))).thenReturn(null)

        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition/{competitionId}/addChallenges", requestedCompetitionIdAsString)
                .content(listOf(Challenge.defaultChallengeForTest(name = "FirstChallenge", points = 3, description = "1st")).toJson(objectMapper))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound)
    }

    @Test
    fun `POST api_competition_compId_addCompetitor should return 404 when no competition was found`() {
        val givenCompetitionId = "SnowCase2018"

        Mockito.`when`(competitionRepositoryMock.findByCompetitionIdentifier(CompetitionId(givenCompetitionId))).thenReturn(null)

        val expectedError = EffitError("Competition with id $givenCompetitionId not found")
        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition/{competitionId}/addCompetitor", givenCompetitionId)
                .content(Competitor.defaultCompetitorForTest().toJson(objectMapper))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound)
                .andExpect(content().json(expectedError.toJson(objectMapper), true))
    }

    @Test
    fun `POST api_competition_compId_addCompetitor should add given Competitor to Competitions' Competitors`() {
        val givenCompetitionId = "Thundercats"

        val thundercatsComp = Competition.defaultCompetitionForTest(name = givenCompetitionId)

        Mockito.`when`(competitionRepositoryMock.findByCompetitionIdentifier(CompetitionId(givenCompetitionId))).thenReturn(thundercatsComp)

        val snarf = Competitor.defaultCompetitorForTest()

        Mockito.`when`(competitorRepositoryMock.save(snarf)).thenReturn(snarf)

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
    fun `POST api_competition_compId_complete_challengeId should return 404 when no competition was found`() {
        val givenCompetitionId = "SnowCase2018"

        Mockito.`when`(competitionRepositoryMock.findByCompetitionIdentifier(CompetitionId(givenCompetitionId))).thenReturn(null)

        val expectedError = EffitError("Competition with id $givenCompetitionId not found")
        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition/{competitionId}/complete/{challengeId}",
                givenCompetitionId,
                randomUUID().toString())
                .content(CompleterId(randomUUID()).toJson(objectMapper))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound)
                .andExpect(content().json(expectedError.toJson(objectMapper), true))
    }

    @Test
    fun `POST api_competition_compId_complete_challengeId should return 404 when no challenge was found`() {
        val successfulCompetitor = CompleterId(randomUUID())
        val givenCompetitionId = "SnowCase2018"
        val givenChallengeId = randomUUID().toString()

        val compWithoutChallenges = Competition.defaultCompetitionForTest(name = givenCompetitionId)

        Mockito.`when`(competitionRepositoryMock.findByCompetitionIdentifier(CompetitionId(givenCompetitionId))).thenReturn(compWithoutChallenges)

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

        Mockito.`when`(competitionRepositoryMock.findByCompetitionIdentifier(CompetitionId(givenCompetitionId))).thenReturn(compWithChallenges)

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

        Mockito.`when`(competitionRepositoryMock.findByCompetitionIdentifier(CompetitionId(givenCompetitionId))).thenReturn(compWithChallenges)

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

        `when`(competitionRepositoryMock.findByCompetitionIdentifier(CompetitionId(givenCompetitionId)))
                .thenReturn(someCompetition)

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
    fun `api_competition_competitionId_removeChallenge_challengeId should return 404 when no competition found for given id`() {
        val givenCompetitionId = "SnowCase2018"

        Mockito.`when`(competitionRepositoryMock.findByCompetitionIdentifier(CompetitionId(givenCompetitionId))).thenReturn(null)

        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition/{competitionId}/removeChallenge/{challengeId}",
                givenCompetitionId,
                randomUUID())
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound)
    }

    @Test
    fun `POST api_competition_compId_removeCompetitor should remove the given competitor id`() {
        val givenCompetitionId = "SnowCase2018"

        val snarf = Competitor.defaultCompetitorForTest()
        val someCompetition = Competition.defaultCompetitionForTest(competitors = listOf(snarf))

        `when`(competitionRepositoryMock.findByCompetitionIdentifier(CompetitionId(givenCompetitionId))).thenReturn(someCompetition)

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

        `when`(competitionRepositoryMock.findByCompetitionIdentifier(CompetitionId(givenCompetitionId))).thenReturn(someCompetition)

        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition/{competitionId}/removeCompetitor", givenCompetitionId)
                .content(Competitor.defaultCompetitorForTest().toJson(objectMapper))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest)

        assertThat(someCompetition.competitors).containsExactly(snarf)

        verify(competitionRepositoryMock, never()).save(ArgumentMatchers.any(Competition::class.java))
    }

    @Test
    fun `POST api_competition_compId_removeCompetitor should return 404 when no competition found for given competition id`() {
        val givenCompetitionId = "SnowCase2018"

        Mockito.`when`(competitionRepositoryMock.findByCompetitionIdentifier(CompetitionId(givenCompetitionId))).thenReturn(null)

        val expectedError = EffitError("Competition with id $givenCompetitionId not found")
        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition/{competitionId}/removeCompetitor", givenCompetitionId)
                .content(Competitor.defaultCompetitorForTest().toJson(objectMapper))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound)
                .andExpect(content().json(expectedError.toJson(objectMapper), true))
    }
}
