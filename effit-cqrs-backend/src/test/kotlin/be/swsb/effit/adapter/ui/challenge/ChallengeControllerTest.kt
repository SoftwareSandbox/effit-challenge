package be.swsb.effit.adapter.ui.challenge

import be.swsb.effit.adapter.sql.challenge.ChallengeRepository
import be.swsb.effit.adapter.ui.exceptions.EffitError
import be.swsb.effit.adapter.ui.util.toJson
import be.swsb.effit.domain.core.challenge.Challenge
import be.swsb.effit.domain.core.challenge.defaultChallengeForTest
import be.swsb.effit.domain.query.challenge.FindChallenge
import be.swsb.effit.messaging.query.QueryExecutor
import be.swsb.test.effit.ControllerTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

class ChallengeControllerTest: ControllerTest() {

    @Autowired
    lateinit var challengeRepositoryMock: ChallengeRepository
    @Autowired
    lateinit var queryExecutorMock: QueryExecutor

    @Captor
    lateinit var challengeCaptor: ArgumentCaptor<Challenge>

    @Test
    fun `GET api challenge id should return no challenge found for given id when challenge does not exist`() {
        val givenId = UUID.randomUUID()

        Mockito.`when`(queryExecutorMock.execute(FindChallenge(givenId))).thenReturn(null)

        mockMvc.perform(MockMvcRequestBuilders.get("/api/challenge/{id}", givenId.toString())
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(EffitError("Challenge with id $givenId not found").toJson(objectMapper), true))
    }

    @Test
    fun `GET api challenge id should return specific Challenge for given id`() {
        val givenId = UUID.randomUUID()
        val expectedChallenge = Challenge.defaultChallengeForTest(id = givenId)

        Mockito.`when`(queryExecutorMock.execute(FindChallenge(givenId))).thenReturn(expectedChallenge)

        mockMvc.perform(MockMvcRequestBuilders.get("/api/challenge/{id}", givenId.toString())
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(expectedChallenge.toJson(objectMapper), true))
    }

    @Test
    fun `PUT api_challenge_challengeId should return 404 when no matching Challenge found for given ChallengeId`() {
        val givenId = UUID.randomUUID()

        Mockito.`when`(challengeRepositoryMock.findById(givenId)).thenReturn(Optional.empty())

        mockMvc.perform(MockMvcRequestBuilders.put("/api/challenge/{challengeId}", givenId)
                .content(Challenge.defaultChallengeForTest().toJson(objectMapper))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound)
    }

    @Test
    fun `PUT api_challenge_challengeId should update given challenge`() {
        val givenId = UUID.randomUUID()
        val oldChallenge = Challenge.defaultChallengeForTest(id = givenId, name = "Pablo")

        Mockito.`when`(challengeRepositoryMock.findById(givenId)).thenReturn(Optional.of(oldChallenge))

        mockMvc.perform(MockMvcRequestBuilders.put("/api/challenge/{challengeId}", givenId)
                .content(oldChallenge.copy(name = "Picasso").toJson(objectMapper))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk)

        Mockito.verify(challengeRepositoryMock).save(challengeCaptor.capture())
        val persistedChallenge = challengeCaptor.value
        assertThat(persistedChallenge.name).isEqualTo("Picasso")
        assertThat(persistedChallenge.points).isEqualTo(oldChallenge.points)
        assertThat(persistedChallenge.description).isEqualTo(oldChallenge.description)
        assertThat(persistedChallenge.id).isEqualTo(oldChallenge.id)
    }

}
