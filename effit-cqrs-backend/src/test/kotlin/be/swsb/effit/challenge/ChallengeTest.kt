package be.swsb.effit.challenge

import be.swsb.effit.domain.core.challenge.Challenge
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class ChallengeTest {

    @Test
    fun `should fail with negative points`() {
        Assertions.assertThatThrownBy {
            Challenge(name = "Playboy", points = -7, description = "ride down a slope with exposed torso")
        }.isInstanceOf(IllegalStateException::class.java)
        .hasMessage("Cannot create a Challenge with negative points")
    }

    @Test
    fun `should fail with 0 points`() {
        Assertions.assertThatThrownBy {
            Challenge(name = "Playboy", points = 0, description = "ride down a slope with exposed torso")
        }.isInstanceOf(IllegalStateException::class.java)
                .hasMessage("Cannot create a Challenge with 0 points")
    }
}
