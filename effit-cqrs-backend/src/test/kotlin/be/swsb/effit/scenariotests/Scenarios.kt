package be.swsb.effit.scenariotests

import be.swsb.effit.adapter.ui.competition.competitor.CompleterId
import be.swsb.effit.adapter.ui.util.toJson
import be.swsb.effit.domain.command.competition.ChallengeToAdd
import be.swsb.effit.domain.command.competition.CreateCompetition
import be.swsb.effit.domain.core.challenge.Challenge
import be.swsb.effit.domain.core.competition.Competition
import be.swsb.effit.domain.core.competition.CompetitionId
import be.swsb.effit.domain.core.competition.competitor.Competitor
import be.swsb.effit.domain.core.competition.competitor.defaultCompetitorForTest
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.*

class Scenarios(val mockMvc: MockMvc,
                val objectMapper: ObjectMapper) {

    fun createNewCompetition(competition: CreateCompetition, selectedChallenge: ChallengeToAdd? = null) : CompetitionId {
        val mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/competition")
                .content(competition.toJson(objectMapper))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated)
                .andReturn()
        val competitionId = CompetitionId(mvcResult.response.getHeader(HttpHeaders.LOCATION)!!)
        selectedChallenge?.let { addChallenges(competitionId, listOf(it))}
        return competitionId
    }

    private fun retrieveCompetition(competitionId: CompetitionId): ResultActions {
        return mockMvc.perform(MockMvcRequestBuilders.get("/api/competition/{competitionId}", competitionId.id)
                .accept(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
    }

    fun getCompetition(competitionId: CompetitionId): Competition {
        val competitionAsJson = retrieveCompetition(competitionId).andReturn().response.contentAsString
        return objectMapper.readValue(competitionAsJson, Competition::class.java)
    }

    fun addChallenges(competitionId: CompetitionId, challenges: List<ChallengeToAdd>) {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition/{id}/addChallenges", competitionId.id)
                .content(challenges.toJson(objectMapper))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isAccepted)
    }

    fun addCompetitor(competitorName: String, competitionId: CompetitionId): UUID {
        val addedCompetitor = Competitor.defaultCompetitorForTest(name = competitorName)
        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition/{competitionId}/addCompetitor", competitionId.id)
                .content(addedCompetitor.toJson(objectMapper))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isAccepted)

        val foundCompetitor = getCompetition(competitionId).competitors.find { it.name == addedCompetitor.name }
        assertThat(foundCompetitor).isNotNull

        return foundCompetitor!!.id
    }

    fun completeChallenge(competitionId: CompetitionId, challengeId: UUID, competitorId: UUID) {
        getChallenge(challengeId) //mimick the fact that a Challenge was selected in the UI
        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition/{competitionId}/complete/{challengeId}",
                competitionId.id,
                challengeId.toString()
        )
                .content(CompleterId(competitorId).toJson(objectMapper))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isAccepted)
    }

    fun startCompetition(competitionId: CompetitionId) {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition/{competitionId}/start", competitionId.id)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isAccepted)
    }

    fun unstartCompetition(competitionId: CompetitionId) {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/competition/{competitionId}/unstart", competitionId.id)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isAccepted)
    }

    fun updateChallenge(updatedChallenge: Challenge) {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/challenge/{challengeId}",
                updatedChallenge.id)
                .content(updatedChallenge.toJson(objectMapper))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)
    }

    fun getChallenge(requestedChallengeId: UUID): Challenge {
        val challengeAsJson = mockMvc.perform(MockMvcRequestBuilders.get("/api/challenge/{challengeId}", requestedChallengeId.toString())
                .accept(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn().response.contentAsString
        return objectMapper.readValue(challengeAsJson, Challenge::class.java)
    }
}
