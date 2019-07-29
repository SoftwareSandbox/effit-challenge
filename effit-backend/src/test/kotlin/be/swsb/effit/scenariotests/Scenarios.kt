package be.swsb.effit.scenariotests

import be.swsb.effit.challenge.Challenge
import be.swsb.effit.competition.Competition
import be.swsb.effit.competition.CompetitionId
import be.swsb.effit.competition.competitor.Competitor
import be.swsb.effit.competition.CreateCompetition
import be.swsb.effit.competition.competitor.CompleterId
import be.swsb.effit.competition.competitor.defaultCompetitorForTest
import be.swsb.effit.util.toJson
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.*

class Scenarios(val mockMvc: MockMvc,
                val objectMapper: ObjectMapper) {

    fun createNewChallenge(challengeToBeCreated: Challenge): String? {
        val mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/challenge")
                .content(challengeToBeCreated.toJson(objectMapper)))
                .andReturn()
        return mvcResult.response.getHeader(HttpHeaders.LOCATION)
    }

    fun createNewCompetition(competition: CreateCompetition, selectedChallenge: Challenge? = null) : CompetitionId {
        val mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/competition")
                .content(competition.toJson(objectMapper))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isCreated)
                .andReturn()
        val competitionId = CompetitionId(mvcResult.response.getHeader(HttpHeaders.LOCATION)!!)
        selectedChallenge?.let { addChallenges(competitionId, listOf(it))}
        return competitionId
    }

    private fun retrieveCompetition(competitionId: CompetitionId): ResultActions {
        return mockMvc.perform(MockMvcRequestBuilders.get("/api/competition/{competitionId}", competitionId.id)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
    }

    fun getCompetition(competitionId: CompetitionId): Competition {
        val competitionAsJson = retrieveCompetition(competitionId).andReturn().response.contentAsString
        return objectMapper.readValue(competitionAsJson, Competition::class.java)
    }

    fun addChallenges(competitionId: CompetitionId, challenges: List<Challenge>) {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition/{id}/addChallenges", competitionId.id)
                .content(challenges.toJson(objectMapper))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isAccepted)
    }

    fun addCompetitor(competitorName: String, competitionId: CompetitionId): UUID {
        val addedCompetitor = Competitor.defaultCompetitorForTest(name = competitorName)
        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition/{competitionId}/addCompetitor", competitionId.id)
                .content(addedCompetitor.toJson(objectMapper))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isAccepted)

        assertThat(getCompetition(competitionId).competitors)
                .extracting<UUID> { it.id }
                .containsOnly(addedCompetitor.id)

        return addedCompetitor.id
    }

    fun completeChallenge(competitionId: CompetitionId, challengeId: UUID, competitorId: UUID) {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition/{competitionId}/complete/{challengeId}",
                competitionId.id,
                challengeId.toString()
        )
                .content(CompleterId(competitorId).toJson(objectMapper))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isAccepted)
    }

    fun startCompetition(competitionId: CompetitionId) {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition/{competitionId}/start", competitionId.id)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isAccepted)
    }
}