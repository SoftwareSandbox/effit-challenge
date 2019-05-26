package be.swsb.effit.scenariotests

import be.swsb.effit.EffitApplication
import be.swsb.effit.challenge.Challenge
import be.swsb.effit.competition.CompetitionId
import be.swsb.effit.competition.CreateCompetition
import be.swsb.effit.util.toJson
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDate
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [EffitApplication::class])
@AutoConfigureMockMvc
class CreatingCompetitionsScenarioTest {

    @Autowired
    lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var objectMapper: ObjectMapper

    lateinit var scenarios: Scenarios

    @BeforeEach
    fun setUp() {
        scenarios = Scenarios(mockMvc, objectMapper)
    }

    @Test
    fun `Competition that ends up having an already existing CompetitionId should not be created`() {
        val competition = CreateCompetition(name = "Thundercats%Competition",
                startDate = LocalDate.of(2018, 3, 16),
                endDate = LocalDate.of(2018, 3, 26))
        val competitionWithIdThatAlreadyExists = CreateCompetition(name = "Thundercats Competition",
                startDate = LocalDate.of(2019, 1, 17),
                endDate = LocalDate.of(2019, 1, 27))

        scenarios.createNewCompetition(competition)

        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition")
                .content(competitionWithIdThatAlreadyExists.toJson(objectMapper))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andReturn()
    }

    @Test
    fun `Challenges should get copied when being added to a Competition`() {
        val challengeToBeCreated = Challenge(name = "Picasso", points = 3, description = "Paint a mustache on a sleeping victim without getting caught")
        val createdChallengeLocation = scenarios.createNewChallenge(challengeToBeCreated)

        val competition = CreateCompetition(name = "DummyCompetition",
                startDate = LocalDate.of(2018, 3, 16),
                endDate = LocalDate.of(2018, 3, 26))
        val competitionId = scenarios.createNewCompetition(competition, challengeToBeCreated)

        assertThat(scenarios.getCompetition(competitionId).challenges)
                .extracting<String> { it.id.toString() }
                .doesNotContain(createdChallengeLocation)
    }

}