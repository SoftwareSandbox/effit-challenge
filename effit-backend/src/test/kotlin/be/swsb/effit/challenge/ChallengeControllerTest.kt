package be.swsb.effit.challenge

import be.swsb.effit.util.toJson
import be.swsb.test.effit.ControllerTest
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.*

class ChallengeControllerTest: ControllerTest() {

    @Autowired
    lateinit var challengeRepositoryMock: ChallengeRepository

    @Test
    fun `GET api challenge should return all Challenges`() {
        val expectedChallenges = listOf(Challenge(name = "Playboy", points = 7, description = "ride down a slope with exposed torso"))

        Mockito.`when`(challengeRepositoryMock.findAll()).thenReturn(expectedChallenges)

        mockMvc.perform(MockMvcRequestBuilders.get("/api/challenge")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(expectedChallenges.toJson(objectMapper), true))
    }

    @Test
    fun `POST api challenge should save the created challenge and return its id in the location header`() {
        val createChallengeJson = CreateChallenge(name = "Snarf", points = 3, description = "snarf snarf")
        val createdChallengeId = UUID.randomUUID()
        Mockito.`when`(challengeRepositoryMock.save(createChallengeJson))
                .thenReturn(Challenge(id = createdChallengeId, name = "Snarf", points = 3, description = "snarf snarf"))

        mockMvc.perform(MockMvcRequestBuilders.post("/api/challenge")
                .content(createChallengeJson.toJson(objectMapper))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated)
                .andExpect(header().string(HttpHeaders.LOCATION, createdChallengeId.toString()))
    }

    @Test
    fun `POST api challenge should return 500 when repository fails to save`() {
        val createChallengeJson = CreateChallenge(name = "Snarf", points = 3, description = "snarf snarf")
        Mockito.doThrow(IllegalStateException::class.java).`when`(challengeRepositoryMock).save(createChallengeJson)

        mockMvc.perform(MockMvcRequestBuilders.post("/api/challenge")
                .content(createChallengeJson.toJson(objectMapper))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is5xxServerError)
    }
}