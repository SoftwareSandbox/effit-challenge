package be.swsb.effit.competition

import be.swsb.effit.challenge.Challenge
import be.swsb.effit.challenge.ChallengeRepository
import be.swsb.effit.exceptions.EffitError
import be.swsb.effit.util.toJson
import be.swsb.test.effit.ControllerTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDate
import java.util.*

class CompetitionControllerTest : ControllerTest() {

    @Autowired
    lateinit var competitionRepositoryMock: CompetitionRepository
    @Autowired
    lateinit var challengeRepositoryMock: ChallengeRepository

    @MockBean
    lateinit var competitionCreatorMock: CompetitionCreator

    @Test
    fun `GET api_competition should return all Competitions`() {
        val expectedCompetitions = listOf(Competition.competition("SnowCase2018", LocalDate.now(), LocalDate.now().plusDays(10)))

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
        val expectedCompetitionWithChallenges = Competition.competition("SnowCase2018", LocalDate.now(), LocalDate.now().plusDays(10))
        expectedCompetitionWithChallenges.addChallenge(Challenge(name = "Picasso", points = 3, description = "snarf"))

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

        val competitionToCreate = Competition.competition(name = "Snowcase 2018",
                startDate = LocalDate.of(2018, 3, 15),
                endDate = LocalDate.of(2018, 3, 25))
        val createdCompetition = Competition.competition(name = "Snowcase 2018",
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
        val competitionToCreate = Competition.competition(name = "Snowcase 2018",
                startDate = LocalDate.of(2018, 3, 15),
                endDate = LocalDate.of(2018, 3, 25))

        val competitionIdThatAlreadyExists = competitionToCreate.competitionId
        val expectedError = EffitError("Sorry, there's already a competition that has a generated CompetitionId of ${competitionIdThatAlreadyExists.id}. Try entering a (slightly) different name.")

        Mockito.`when`(competitionCreatorMock.from(createCompetition)).thenReturn(competitionToCreate)
        Mockito.`when`(competitionRepositoryMock.findByCompetitionIdentifier(competitionIdThatAlreadyExists))
                .thenReturn(Competition.competitionWithoutEndDate(name = "Snowcase 2018", startDate = LocalDate.of(2018, 3, 1)))

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
        val competitionToCreate = Competition.competition(name = "Snowcase 2018",
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
    fun `POST api_competition_addChallenges should add given Challenges to the given Competition`() {
        val challenge1 = Challenge(name = "FirstChallenge", points = 3, description = "1st")
        val challenge2 = Challenge(name = "SecondChallenge", points = 4, description = "2nd")
        val givenChallenges = listOf(challenge1, challenge2)
        val persistedChallenge1 = challenge1.copy(id = UUID.randomUUID())
        val persistedChallenge2 = challenge2.copy(id = UUID.randomUUID())

        val requestedCompetitionIdAsString = "Snarf"

        val thundercatsComp = Competition.competitionWithoutEndDate("Thundercats", LocalDate.now())
        Mockito.`when`(competitionRepositoryMock.findByCompetitionIdentifier(CompetitionId(requestedCompetitionIdAsString)))
                .thenReturn(thundercatsComp)
        Mockito.`when`(challengeRepositoryMock.save(challenge1)).thenReturn(persistedChallenge1)
        Mockito.`when`(challengeRepositoryMock.save(challenge2)).thenReturn(persistedChallenge2)

        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition/{id}/addChallenges", requestedCompetitionIdAsString)
                .content(givenChallenges.toJson(objectMapper))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isAccepted)

        assertThat(thundercatsComp.challenges).containsExactly(persistedChallenge1, persistedChallenge2)
        Mockito.verify(competitionRepositoryMock).save(thundercatsComp)
    }

    @Test
    fun `POST api_competition_addChallenges should return 404 when no matching Competition found for given CompetitionId`() {
        val requestedCompetitionIdAsString = "SnowCase2018"

        Mockito.`when`(competitionRepositoryMock.findByCompetitionIdentifier(CompetitionId(requestedCompetitionIdAsString))).thenReturn(null)

        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition/{competitionId}/addChallenges", requestedCompetitionIdAsString)
                .content(listOf(Challenge(name = "FirstChallenge", points = 3, description = "1st")).toJson(objectMapper))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound)
    }

    @Test
    fun `POST api_competition_compId should return 404 when no competition was found`() {
        val givenCompetitionId = "SnowCase2018"

        Mockito.`when`(competitionRepositoryMock.findByCompetitionIdentifier(CompetitionId(givenCompetitionId))).thenReturn(null)

        val expectedError = EffitError("Competition with id $givenCompetitionId not found")
        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition/{competitionId}/complete/{challengeId}", givenCompetitionId, "challengeId")
                .content(Competitor(name = "Snarf", totalScore = 0).toJson(objectMapper))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound)
                .andExpect(content().json(expectedError.toJson(objectMapper), true))
    }

    @Test
    fun `POST api_competition_compId_complete_challengeId should return 404 when no challenge was found`() {
        val givenCompetitionId = "Thundercats"
        val givenChallengeId = UUID.randomUUID().toString()

        val thundercatsComp = Competition.competitionWithoutEndDate("Thundercats", LocalDate.now())
        thundercatsComp.addChallenge(Challenge(id = UUID.randomUUID(), name = "FirstChallenge", points = 3, description = "1st"))

        Mockito.`when`(competitionRepositoryMock.findByCompetitionIdentifier(CompetitionId(givenCompetitionId))).thenReturn(thundercatsComp)

        val expectedError = EffitError("Challenge with id $givenChallengeId not found in competition $givenCompetitionId")
        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition/{competitionId}/complete/{challengeId}",
                givenCompetitionId, givenChallengeId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(Competitor(name = "Snarf", totalScore = 0).toJson(objectMapper))
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound)
                .andExpect(content().json(expectedError.toJson(objectMapper), true))
    }

    @Test
    fun `POST api_competition_compId_complete_challengeId should add given CompetitorName to Competitions' Competitors`() {
        val givenCompetitionId = "Thundercats"
        val givenChallengeId = UUID.randomUUID()

        val thundercatsComp = Competition.competitionWithoutEndDate("Thundercats", LocalDate.now())
        thundercatsComp.addChallenge(Challenge(id = givenChallengeId, name = "FirstChallenge", points = 3, description = "1st"))

        Mockito.`when`(competitionRepositoryMock.findByCompetitionIdentifier(CompetitionId(givenCompetitionId))).thenReturn(thundercatsComp)

        val snarf = Competitor(name = "Snarf", totalScore = 0)
        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition/{competitionId}/complete/{challengeId}",
                givenCompetitionId, givenChallengeId.toString())
                .content(snarf.toJson(objectMapper))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isAccepted)

        assertThat(thundercatsComp.competitors)
                .usingElementComparatorIgnoringFields("id")
                .containsExactly(Competitor(name = "Snarf", totalScore = 0))
    }

    @Test
    fun `POST api_competition_compId_complete_challengeId should add challenge score to given Competitors total score`() {

    }

    @Test
    fun `POST api_competition_compId_complete_challengeId should return 400 when Competitor already finished given challenge`() {

    }

    //`POST api_competition_compId_complete_challengeId should return 404 when no challenge was found`
    //`POST api_competition_compId_complete_challengeId should add given CompetitorName to Competitions' Competitors`
    //`POST api_competition_compId_complete_challengeId should add challenge score to given Competitors total score`
    //`POST api_competition_compId_complete_challengeId should return 400 when Competitor already finished given challenge`
}