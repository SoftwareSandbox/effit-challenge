package be.swsb.effit

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.spring.SpringListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [EffitApplication::class])
@AutoConfigureMockMvc
class ChallengeControllerTest : StringSpec() {
    override fun listeners() = listOf(SpringListener)

    @Autowired
    lateinit var mockMvc: MockMvc

    init {
        "GET /api/challenge should return all Challenges" {
            val expectedChallenges = listOf(Challenge("Playboy", 7, "ride down a slope with exposed torso"))

            mockMvc.perform(MockMvcRequestBuilders.get("/api/challenge")
                    .accept(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk)
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(content().json(toJson(expectedChallenges), true))
                    .andReturn()
        }
    }

    fun toJson(jsonObject: Any): String {
        return ObjectMapper().writeValueAsString(jsonObject)
    }
}