package be.swsb.effit.frameworktests

import be.swsb.effit.EffitApplication
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [EffitApplication::class])
@AutoConfigureMockMvc
class JacksonObjectMapperTest {

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun `empty strings are considered null to jackson`() {
        val emptyStringAsNullFeature = objectMapper
                .deserializationConfig
                .isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)

        assertThat(emptyStringAsNullFeature).isTrue()
    }
}