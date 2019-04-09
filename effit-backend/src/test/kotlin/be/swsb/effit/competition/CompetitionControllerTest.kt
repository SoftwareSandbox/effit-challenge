package be.swsb.effit.competition

import be.swsb.effit.challenge.ChallengeRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate

@ExtendWith(SpringExtension::class)
@WebMvcTest(controllers = [CompetitionController::class])
class CompetitionControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockBean
    lateinit var competitionRepositoryMock: CompetitionRepository
    @MockBean
    lateinit var challengeRepositoryMock: ChallengeRepository

    @Test
    fun `GET api competition should return all Competitions`() {
        val expectedCompetitions = listOf(Competition.competition("SnowCase2018", LocalDate.now(), LocalDate.now().plusDays(10)))

        Mockito.`when`(competitionRepositoryMock.findAll()).thenReturn(expectedCompetitions)

        mockMvc.perform(MockMvcRequestBuilders.get("/api/competition")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(toJson(expectedCompetitions), true))
    }

    @Test
    fun `GET api competition name should return the competition with matching name`() {
        val requestedCompetitionName = "SnowCase2018"
        val expectedCompetition = Competition.competition(requestedCompetitionName, LocalDate.now(), LocalDate.now().plusDays(10))

        Mockito.`when`(competitionRepositoryMock.findByName(requestedCompetitionName)).thenReturn(expectedCompetition)

        mockMvc.perform(MockMvcRequestBuilders.get("/api/competition/{name}", requestedCompetitionName)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(toJson(expectedCompetition), true))
    }

    @Test
    fun `GET api competition name should return 404 when no matching Competition found for given name`() {
        val requestedCompetitionName = "SnowCase2018"

        Mockito.`when`(competitionRepositoryMock.findByName(requestedCompetitionName)).thenReturn(null)

        mockMvc.perform(MockMvcRequestBuilders.get("/api/competition/{name}", requestedCompetitionName)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().`is`(404))
    }

    fun toJson(jsonObject: Any): String {
        return objectMapper.writeValueAsString(jsonObject)
    }
}