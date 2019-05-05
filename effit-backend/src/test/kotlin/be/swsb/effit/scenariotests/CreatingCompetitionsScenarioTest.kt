package be.swsb.effit.scenariotests

import be.swsb.effit.EffitApplication
import be.swsb.effit.challenge.Challenge
import be.swsb.effit.competition.Competition
import be.swsb.effit.competition.CompetitionId
import be.swsb.effit.competition.CreateCompetition
import be.swsb.effit.util.toJson
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDate

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [EffitApplication::class])
@AutoConfigureMockMvc
class CreatingCompetitionsScenarioTest {

    @Autowired
    lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun `Challenges should get copied when being added to a Competition`() {
        val challengeToBeCreated = Challenge(name = "Picasso", points = 3, description = "Paint a mustache on a sleeping victim without getting caught")
        val createdChallengeLocation = createNewChallenge(challengeToBeCreated)

        val competition = CreateCompetition(name = "DummyCompetition",
                startDate = LocalDate.of(2018, 3, 16),
                endDate = LocalDate.of(2018, 3, 26))
        val competitionId = createNewCompetition(competition, challengeToBeCreated)

        // Competition 1's Challenge1 id should be different from the createdChallenge
        assertThat(retrieveCompetition(competitionId).challenges)
                .extracting<String> { it.id.toString() }
                .doesNotContain(createdChallengeLocation)
    }

    private fun retrieveCompetition(competitionId: CompetitionId): Competition {
        val mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/competition/{competitionId}", competitionId.id)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn()

        return objectMapper.readValue(mvcResult.response.contentAsString, Competition::class.java)
    }

    private fun createNewCompetition(competition: CreateCompetition, selectedChallenge: Challenge) : CompetitionId {
        val mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/competition")
                .content(competition.toJson(objectMapper))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isCreated)
                .andReturn()
        val competitionId = CompetitionId(mvcResult.response.getHeader(HttpHeaders.LOCATION)!!)
        addChallenges(competitionId, listOf(selectedChallenge))
        return competitionId
    }

    private fun addChallenges(competitionId: CompetitionId, challenges: List<Challenge>) {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition/{id}/addChallenges", competitionId.id)
                .content(challenges.toJson(objectMapper))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isAccepted)
    }

    private fun createNewChallenge(challengeToBeCreated: Challenge): String? {
        val mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/challenge")
                .content(challengeToBeCreated.toJson(objectMapper)))
                .andReturn()
        return mvcResult.response.getHeader(HttpHeaders.LOCATION)
    }

}