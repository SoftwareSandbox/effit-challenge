package be.swsb.effit.challenge

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RunWith(SpringRunner::class)
@WebMvcTest(controllers = [ChallengeController::class])
class ChallengeControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var challengeRepositoryMock: ChallengeRepository

    @Test
    fun `GET api challenge should return all Challenges`() {
        val expectedChallenges = listOf(Challenge(name = "Playboy", points = 7, description = "ride down a slope with exposed torso"))

        Mockito.`when`(challengeRepositoryMock.findAll()).thenReturn(expectedChallenges)

        mockMvc.perform(MockMvcRequestBuilders.get("/api/challenge")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(toJson(expectedChallenges), true))
    }

    fun toJson(jsonObject: Any): String {
        return ObjectMapper().writeValueAsString(jsonObject)
    }
}