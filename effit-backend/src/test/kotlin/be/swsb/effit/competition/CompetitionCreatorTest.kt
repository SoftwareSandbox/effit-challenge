package be.swsb.effit.competition

import be.swsb.effit.exceptions.DomainValidationRuntimeException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.time.LocalDate

class CompetitionCreatorTest {

    @Test
    fun `from - without startDate delegates properly`() {
        val endDate = LocalDate.now().plusDays(5)
        val actual = CompetitionCreator().from(CreateCompetition("unimportant", null, endDate))
        assertThat(actual.startDate).isNotNull()
    }

    @Test
    fun `from - without endDate delegates properly`() {
        val startDate = LocalDate.now().minusDays(8)
        val actual = CompetitionCreator().from(CreateCompetition("unimportant", startDate, null))
        assertThat(actual.endDate).isNotNull()
    }

    @Test
    fun `from - with both start and endDate not null delegates properly`() {
        val startDate = LocalDate.now().minusDays(8)
        val endDate = LocalDate.now().plusDays(8)
        val actual = CompetitionCreator().from(CreateCompetition("unimportant", startDate, endDate))
        assertThat(actual.startDate).isNotNull()
        assertThat(actual.endDate).isNotNull()
    }

    @Test
    fun `from - Cannot create a Competition without both a start and end date`() {
        assertThatThrownBy {
            CompetitionCreator().from(CreateCompetition("unimportant", null, null))
        }
                .isInstanceOf(DomainValidationRuntimeException::class.java)
                .hasMessage("Cannot create a Competition without both a start and end date")
    }
}