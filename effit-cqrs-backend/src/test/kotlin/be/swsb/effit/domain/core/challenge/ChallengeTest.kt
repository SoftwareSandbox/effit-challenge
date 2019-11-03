package be.swsb.effit.domain.core.challenge

import be.swsb.effit.domain.core.exceptions.DomainValidationRuntimeException
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class ChallengeTest {

    @Test
    fun `should fail with negative points`() {
        assertThatThrownBy {
            Challenge(name = "Playboy", points = -7, description = "ride down a slope with exposed torso")
        }.isInstanceOf(DomainValidationRuntimeException::class.java)
        .hasMessage("Cannot create a Challenge with negative points")
    }

    @Test
    fun `should fail with 0 points`() {
        assertThatThrownBy {
            Challenge(name = "Playboy", points = 0, description = "ride down a slope with exposed torso")
        }.isInstanceOf(DomainValidationRuntimeException::class.java)
                .hasMessage("Cannot create a Challenge with 0 points")
    }
}
