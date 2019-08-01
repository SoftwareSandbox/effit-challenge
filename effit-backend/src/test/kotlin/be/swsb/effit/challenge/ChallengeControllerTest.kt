package be.swsb.effit.challenge

import be.swsb.effit.exceptions.EffitError
import be.swsb.effit.util.toJson
import be.swsb.test.effit.ControllerTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.*

class ChallengeControllerTest: ControllerTest() {

    @Autowired
    lateinit var challengeRepositoryMock: ChallengeRepository

    @Captor
    lateinit var challengeCaptor: ArgumentCaptor<Challenge>

    @Test
    fun `GET api challenge should return all Challenges`() {
        val expectedChallenges = listOf(Challenge.defaultChallengeForTest())

        Mockito.`when`(challengeRepositoryMock.findAll()).thenReturn(expectedChallenges)

        mockMvc.perform(MockMvcRequestBuilders.get("/api/challenge")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(expectedChallenges.toJson(objectMapper), true))
    }

    @Test
    fun `GET api challenge id should return no challenge found for given id when challenge does not exist`() {
        val givenId = UUID.randomUUID()

        Mockito.`when`(challengeRepositoryMock.findById(givenId)).thenReturn(Optional.empty())

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

        Mockito.`when`(challengeRepositoryMock.findById(givenId)).thenReturn(Optional.of(expectedChallenge))

        mockMvc.perform(MockMvcRequestBuilders.get("/api/challenge/{id}", givenId.toString())
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(expectedChallenge.toJson(objectMapper), true))
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