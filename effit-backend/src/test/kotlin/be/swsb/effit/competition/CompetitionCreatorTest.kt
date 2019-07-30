package be.swsb.effit.competition

import be.swsb.effit.exceptions.DomainValidationRuntimeException
import org.assertj.core.api.Assertions.*
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
        assertThatExceptionOfType(DomainValidationRuntimeException::class.java)
                .isThrownBy {
                    CompetitionCreator().from(CreateCompetition("unimportant", null, null))
                }
                .withMessage("Cannot create a Competition without both a start and end date")
    }

    @Test
    fun `from - name is null, cannot create a Competition without a name`() {
        val startDate = LocalDate.now().minusDays(8)
        val endDate = LocalDate.now().plusDays(8)
        assertThatExceptionOfType(DomainValidationRuntimeException::class.java)
                .isThrownBy {
                    CompetitionCreator().from(CreateCompetition(null, startDate, endDate))
                }
                .withMessage("Cannot create a Competition without a name")
    }

    @Test
    fun `from - name is blank, cannot create a Competition without a name`() {
        val startDate = LocalDate.now().minusDays(8)
        val endDate = LocalDate.now().plusDays(8)
        assertThatExceptionOfType(DomainValidationRuntimeException::class.java)
                .isThrownBy {
                    CompetitionCreator().from(CreateCompetition(" ", startDate, endDate))
                }
                .withMessage("Cannot create a Competition without a name")
    }
}