package be.swsb.effit.scenariotests

import be.swsb.effit.challenge.Challenge
import be.swsb.effit.competition.Competition
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.Ignore
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import java.time.LocalDate
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
@AutoConfigureMockMvc
@Ignore
class CreatingCompetitions {

    @Autowired
    lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun `Challenges should get copied when being added to a Competition`() {
        // After Competition 1 has been created with e.g. Challenge 1,
        val challengeToBeCreated = Challenge(name = "Picasso", points = 3, description = "Paint a mustache on a sleeping victim without getting caught")
        val location = createNewChallenge(challengeToBeCreated)

        val competition = Competition.competition(name = "Snowcase 2018",
                startDate = LocalDate.of(2018, 3, 16),
                endDate = LocalDate.of(2018, 3, 26))
        createNewCompetition(competition)
        competition.addChallenge(challengeToBeCreated)

        // and Challenge 1 gets modified,
        modifyChallenge(Challenge(id = challengeToBeCreated.id, name = "SomethingElse", points = 7, description = "Snarf"))

        // Competition1's Challenge1 should not get modified
        assertThat(retrieveCompetition(competition.id).challenges).containsExactly(challengeToBeCreated)
    }

    private fun retrieveCompetition(id: UUID): Competition {
        //actually retrieve competition
        return Competition.competitionWithoutEndDate("dummy", startDate = LocalDate.of(2018, 3, 16))
    }

    private fun modifyChallenge(challenge: Challenge) {
        //mockMvc.perform(put("/api/challenge/${challenge.id}")).content(toJson(challenge))
    }

    private fun createNewCompetition(competition: Competition) {
        //mockMvc.perform(post("/api/competition")).content(toJson(competition))
    }

    private fun createNewChallenge(challengeToBeCreated: Challenge): String? {
        val mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/challenge")
                .content(toJson(challengeToBeCreated)))
                .andReturn()
        return mvcResult.response.getHeader(HttpHeaders.LOCATION)
    }

    fun toJson(jsonObject: Any): String {
        return objectMapper.writeValueAsString(jsonObject)
    }
}