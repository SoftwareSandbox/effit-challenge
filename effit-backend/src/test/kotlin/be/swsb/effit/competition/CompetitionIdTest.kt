package be.swsb.effit.competition

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class CompetitionIdTest {

    @Test
    fun `CompetitionId from a string containing spaces, should remove spaces`() {
        val actual = CompetitionId("SnowCase 2018")

        Assertions.assertThat(actual.id).doesNotContainAnyWhitespaces()
    }

    @Test
    fun `CompetitionId from a string containing unsafe URL characters, should remove those characters`() {
        val unsafeCharacters = "\" < > # % { } | \\ ^ ~ [ ] `".split(" ")
        unsafeCharacters.forEach {
            val actual = CompetitionId("SnowCase${it}2018")
            Assertions.assertThat(actual.id).isEqualTo("SnowCase2018")
        }
    }

    @Test
    fun `CompetitionId from a string containing reserved URL characters, should remove those characters`() {
        val reservedCharacters = "; / ? : @ = &"
        reservedCharacters.forEach {
            val actual = CompetitionId("SnowCase${it}2018")
            Assertions.assertThat(actual.id).isEqualTo("SnowCase2018")
        }
    }

    @Test
    fun `CompetitionId from a string, should retain case sensitivity`() {
        val actual = CompetitionId("SnowCase2018")

        Assertions.assertThat(actual.id).isEqualTo("SnowCase2018")
    }

    @Test
    fun `CompetitionId's equality, includes case sensitivity`() {
        Assertions.assertThat(CompetitionId("SnowCase2018")).isEqualTo(CompetitionId("SnowCase2018"))
    }
}