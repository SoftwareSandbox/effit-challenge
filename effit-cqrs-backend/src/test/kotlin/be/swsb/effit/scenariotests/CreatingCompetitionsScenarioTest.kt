package be.swsb.effit.scenariotests

import be.swsb.effit.EffitCqrsApplication
import be.swsb.effit.domain.core.challenge.Challenge
import be.swsb.effit.domain.command.competition.CreateCompetition
import be.swsb.effit.adapter.ui.util.toJson
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDate

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [EffitCqrsApplication::class])
@AutoConfigureMockMvc
@ActiveProfiles("test")
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
    fun `Competition without a name should not be created`() {
        val emptyStringAsNullFeature = objectMapper
                .deserializationConfig
                .isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
        assertThat(emptyStringAsNullFeature).isTrue()

        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition")
                .content("{" +
                        "\"name\": \"\"," +
                        "\"startDate\": \"2018-03-16\"," +
                        "\"endDate\": \"2018-03-16\"," +
                        "}")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andExpect(MockMvcResultMatchers.content().string(""))
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
    fun `Challenges should get copied when hosting a Competition again`() {
        val challengeToBeCreated = Challenge(name = "Picasso", points = 3, description = "Paint a mustache on a sleeping victim without getting caught")

        val competition = CreateCompetition(name = "DummyCompetition",
                startDate = LocalDate.of(2018, 3, 16),
                endDate = LocalDate.of(2018, 3, 26))
        val competitionId = scenarios.createNewCompetition(competition, challengeToBeCreated)
        val originalChallengeId = scenarios.getCompetition(competitionId).challenges.first().id

        val hostedAgainCompetition = CreateCompetition(name = "DummyCompetitionThe2nd",
                startDate = LocalDate.of(2018, 3, 16),
                endDate = LocalDate.of(2018, 3, 26))
        val hostedAgainCompetitionId = scenarios.createNewCompetition(hostedAgainCompetition, challengeToBeCreated)

        assertThat(scenarios.getCompetition(hostedAgainCompetitionId).challenges.first().id)
                .isNotEqualTo(originalChallengeId)
    }

    @Test
    fun `Challenges can get updated when a Competition is not yet started`() {
        val challengeToBeCreated = Challenge(name = "Picasso", points = 3, description = "Paint a mustache on a sleeping victim without getting caught")

        val competition = CreateCompetition(name = "CompetitionWChallenges",
                startDate = LocalDate.of(2018, 3, 16),
                endDate = LocalDate.of(2018, 3, 26))
        val competitionId = scenarios.createNewCompetition(competition, challengeToBeCreated)

        val challengeToUpdate = scenarios.getCompetition(competitionId).challenges.find { it.name == challengeToBeCreated.name }
                ?: fail("Expected a challenge with name ${challengeToBeCreated.name}")

        scenarios.updateChallenge(challengeToUpdate.copy(name="Francisco"))

        assertThat(scenarios.getCompetition(competitionId).challenges)
                .extracting<String> { it.name }
                .containsOnly("Francisco")
    }

    @Test
    fun `Challenges can get updated when a Competition was unstarted`() {
        val challengeToBeCreated = Challenge(name = "Picasso", points = 3, description = "Paint a mustache on a sleeping victim without getting caught")

        val competition = CreateCompetition(name = "CompetitionWChallenges",
                startDate = LocalDate.of(2018, 3, 16),
                endDate = LocalDate.of(2018, 3, 26))
        val competitionId = scenarios.createNewCompetition(competition, challengeToBeCreated)
        scenarios.addCompetitor("Snarf", competitionId)

        scenarios.startCompetition(competitionId)
        scenarios.unstartCompetition(competitionId)

        val challengeToUpdate = scenarios.getCompetition(competitionId).challenges.find { it.name == challengeToBeCreated.name }
                ?: fail("Expected a challenge with name ${challengeToBeCreated.name}")

        scenarios.updateChallenge(challengeToUpdate.copy(name="Francisco"))

        assertThat(scenarios.getCompetition(competitionId).challenges)
                .extracting<String> { it.name }
                .containsOnly("Francisco")
    }

}
