package be.swsb.effit.competition

import be.swsb.effit.domain.core.competition.CompetitionId
import be.swsb.effit.exceptions.DomainValidationRuntimeException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Test

class CompetitionIdTest {

    @Test
    fun `CompetitionId from an empty string, should throw a validation exception`() {
        assertThatExceptionOfType(DomainValidationRuntimeException::class.java)
                .isThrownBy { CompetitionId("") }
                .withMessage("Cannot create a CompetitionId from an empty name.")
        assertThatExceptionOfType(DomainValidationRuntimeException::class.java)
                .isThrownBy { CompetitionId(" ") }
                .withMessage("Cannot create a CompetitionId from an empty name.")
    }

    @Test
    fun `CompetitionId from a string with only unsafe URL characters, should throw a validation exception`() {
        assertThatExceptionOfType(DomainValidationRuntimeException::class.java)
                .isThrownBy { CompetitionId(" < > # %") }
                .withMessage("Cannot create a CompetitionId from an empty name.")
    }

    @Test
    fun `CompetitionId from a string containing spaces, should remove spaces`() {
        val actual = CompetitionId("SnowCase 2018")

        assertThat(actual.id).doesNotContainAnyWhitespaces()
    }

    @Test
    fun `CompetitionId from a string containing unsafe URL characters, should remove those characters`() {
        val unsafeCharacters = "\" < > # % { } | \\ ^ ~ [ ] `".split(" ")
        unsafeCharacters.forEach {
            val actual = CompetitionId("SnowCase${it}2018")
            assertThat(actual.id).isEqualTo("SnowCase2018")
        }
    }

    @Test
    fun `CompetitionId from a string containing reserved URL characters, should remove those characters`() {
        val reservedCharacters = "; / ? : @ = &".split(" ")
        reservedCharacters.forEach {
            val actual = CompetitionId("SnowCase${it}2018")
            assertThat(actual.id).isEqualTo("SnowCase2018")
        }
    }

    @Test
    fun `CompetitionId from a string, should retain case sensitivity`() {
        val actual = CompetitionId("SnowCase2018")

        assertThat(actual.id).isEqualTo("SnowCase2018")
    }

    @Test
    fun `CompetitionId's equality, includes case sensitivity`() {
        assertThat(CompetitionId("SnowCase2018")).isEqualTo(CompetitionId("SnowCase2018"))
        assertThat(CompetitionId("SnowCase2018")).isNotEqualTo(CompetitionId("snowcase2018"))
    }

    @Test
    fun `CompetitionId's equality, ignore removed characters`() {
        val spaceRemoved = listOf(" ")
        val reservedCharacters = listOf(";", "/", "?", ":", "@", "=", "&")
        val unsafeCharacters = listOf("\"", "<", ">", "#", "%", "{", "}", "|", "\\", "^", "~", "[", "]", "`")
        val removedCharacters = spaceRemoved + reservedCharacters + unsafeCharacters
        removedCharacters.forEach {
            assertThat(CompetitionId("SnowCase${it}2018")).isEqualTo(CompetitionId("SnowCase2018"))
        }
    }
}
