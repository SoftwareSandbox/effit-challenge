package be.swsb.effit.adapter.ui.challenge

import be.swsb.effit.adapter.ui.util.toJson
import be.swsb.effit.domain.command.challenge.UpdateChallenge
import be.swsb.effit.domain.core.challenge.Challenge
import be.swsb.effit.domain.core.challenge.defaultChallengeForTest
import be.swsb.effit.domain.query.challenge.FindChallenge
import be.swsb.effit.messaging.command.CommandExecutor
import be.swsb.effit.messaging.query.QueryExecutor
import be.swsb.test.effit.ControllerTest
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

class ChallengeControllerTest: ControllerTest() {

    @Autowired
    lateinit var commandExecutorMock: CommandExecutor
    @Autowired
    lateinit var queryExecutorMock: QueryExecutor

    @Test
    fun `GET api challenge id should return specific Challenge for given id`() {
        val givenId = UUID.randomUUID()
        val expectedChallenge = Challenge.defaultChallengeForTest(id = givenId)

        Mockito.`when`(queryExecutorMock.execute(FindChallenge(givenId))).thenReturn(expectedChallenge)

        mockMvc.perform(get("/api/challenge/{id}", givenId.toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedChallenge.toJson(objectMapper), true))
    }

    @Test
    fun `PUT api_challenge_challengeId should update given challenge`() {
        val givenId = UUID.randomUUID()
        val updateChallenge = UpdateChallenge(givenId, "Pablo", 7, "snarf snarf")

        mockMvc.perform(put("/api/challenge/{challengeId}", givenId)
                .content(updateChallenge.toJson(objectMapper))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)

        verify(commandExecutorMock).execute(updateChallenge)
    }

}
