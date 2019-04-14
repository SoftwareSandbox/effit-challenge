package be.swsb.effit.competition

import be.swsb.effit.WebMvcTestConfiguration
import be.swsb.effit.challenge.Challenge
import be.swsb.effit.util.toJson
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDate

@ExtendWith(SpringExtension::class)
@WebMvcTest
@ContextConfiguration(classes = [WebMvcTestConfiguration::class])
class CompetitionControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var competitionRepositoryMock: CompetitionRepository

    @MockBean
    lateinit var competitionCreatorMock: CompetitionCreator

    @Test
    fun `GET api competition should return all Competitions`() {
        val expectedCompetitions = listOf(Competition.competition("SnowCase2018", LocalDate.now(), LocalDate.now().plusDays(10)))

        Mockito.`when`(competitionRepositoryMock.findAll()).thenReturn(expectedCompetitions)

        mockMvc.perform(MockMvcRequestBuilders.get("/api/competition")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(expectedCompetitions.toJson(objectMapper), true))
    }

    @Test
    fun `GET api competition name should return the competition with matching name`() {
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
    fun `GET api competition name should return 404 when no matching Competition found for given name`() {
        val requestedCompetitionIdAsString = "SnowCase2018"

        Mockito.`when`(competitionRepositoryMock.findByCompetitionIdentifier(CompetitionId(requestedCompetitionIdAsString))).thenReturn(null)

        mockMvc.perform(MockMvcRequestBuilders.get("/api/competition/{competitionId}", requestedCompetitionIdAsString)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().`is`(404))
    }

    @Test
    fun `POST api competition, should be able to create a new Competition and save it`() {
        val createCompetition = CreateCompetition(name = "Snowcase 2018",
                startDate = LocalDate.of(2018,3,15),
                endDate = LocalDate.of(2018,3,25))

        val competitionToCreate = Competition.competition(name = "Snowcase 2018",
                startDate = LocalDate.of(2018,3,15),
                endDate = LocalDate.of(2018,3,25))
        val createdCompetition = Competition.competition(name = "Snowcase 2018",
                startDate = LocalDate.of(2018,3,15),
                endDate = LocalDate.of(2018,3,25))

        Mockito.`when`(competitionCreatorMock.from(createCompetition)).thenReturn(competitionToCreate)
        Mockito.`when`(competitionRepositoryMock.save(ArgumentMatchers.any(Competition::class.java))).thenReturn(createdCompetition)

        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition")
                .content(createCompetition.toJson(objectMapper))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated)
                .andExpect(header().string(HttpHeaders.LOCATION, createdCompetition.id.toString()))
    }

    @Test
    fun `POST api competition should return 500 when competition was unable to be created`() {
        val createCompetition = CreateCompetition(name = "Snowcase 2018",
                startDate = LocalDate.of(2018,3,15),
                endDate = LocalDate.of(2018,3,25))

        Mockito.doThrow(IllegalStateException::class.java).`when`(competitionCreatorMock).from(createCompetition)

        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition")
                .content(createCompetition.toJson(objectMapper))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is5xxServerError)
    }

    @Test
    fun `POST api competition should return 500 when competition was unable to be saved`() {
        val createCompetition = CreateCompetition(name = "Snowcase 2018",
                startDate = LocalDate.of(2018,3,15),
                endDate = LocalDate.of(2018,3,25))
        val competitionToCreate = Competition.competition(name = "Snowcase 2018",
                startDate = LocalDate.of(2018,3,15),
                endDate = LocalDate.of(2018,3,25))

        Mockito.`when`(competitionCreatorMock.from(createCompetition)).thenReturn(competitionToCreate)
        Mockito.doThrow(IllegalStateException::class.java).`when`(competitionRepositoryMock).save(competitionToCreate)

        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition")
                .content(createCompetition.toJson(objectMapper))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is5xxServerError)
    }


}