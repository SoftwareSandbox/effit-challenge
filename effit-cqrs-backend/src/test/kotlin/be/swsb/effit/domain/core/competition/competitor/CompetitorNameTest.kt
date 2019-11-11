package be.swsb.effit.domain.core.competition.competitor

import be.swsb.effit.domain.core.exceptions.DomainValidationRuntimeException
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.assertj.core.internal.bytebuddy.utility.RandomString
import org.junit.jupiter.api.Test

class CompetitorNameTest {
    @Test
    fun `A Competitor Name must not be longer than 50 characters`() {
        assertThatExceptionOfType(DomainValidationRuntimeException::class.java)
                .isThrownBy { CompetitorName(RandomString.make(51)) }
    }
}
