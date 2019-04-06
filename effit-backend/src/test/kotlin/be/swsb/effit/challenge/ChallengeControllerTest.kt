package be.swsb.effit.challenge

import be.swsb.effit.challenge.Challenge
import be.swsb.effit.challenge.ChallengeController
import be.swsb.effit.challenge.ChallengeRepository
import com.fasterxml.jackson.databind.ObjectMapper
import io.kotlintest.specs.StringSpec
import io.kotlintest.spring.SpringListener
import io.mockk.every
import io.mockk.mockk
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(controllers = [ChallengeController::class])
@AutoConfigureMockMvc
class ChallengeControllerTest : StringSpec() {
    override fun listeners() = listOf(SpringListener)

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var challengeRepository: ChallengeRepository

    @TestConfiguration
    class ChallengeControllerTestConfig {
        @Bean
        fun challengeRepositoryMock() = mockk<ChallengeRepository>()
    }

    init {
        "GET /api/challenge should return all Challenges" {
            val expectedChallenges = listOf(Challenge("Playboy", 7, "ride down a slope with exposed torso"))

            every { challengeRepository.findAll() } returns expectedChallenges

            mockMvc.perform(MockMvcRequestBuilders.get("/api/challenge")
                    .accept(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk)
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(content().json(toJson(expectedChallenges), true))
        }
    }

    fun toJson(jsonObject: Any): String {
        return ObjectMapper().writeValueAsString(jsonObject)
    }
}