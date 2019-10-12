package be.swsb.effit.domain.query.challenge

import be.swsb.effit.adapter.sql.challenge.ChallengeRepository
import be.swsb.effit.domain.core.challenge.Challenge
import be.swsb.effit.domain.core.challenge.defaultChallengeForTest
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoRule
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.repository.findByIdOrNull
import java.util.*
import java.util.UUID.randomUUID

@ExtendWith(MockitoExtension::class)
class FindChallengeQueryHandlerTest {

    @Mock
    private lateinit var challengeRepositoryMock: ChallengeRepository

    private lateinit var handler: FindChallengeQueryHandler

    @BeforeEach
    internal fun setUp() {
        handler = FindChallengeQueryHandler(challengeRepositoryMock)
    }

    @Test
    fun `handle | repository finds Challenge for given UUID, Challenge is returned`() {
        val challengeId = randomUUID()
        val defaultChallengeForTest = Challenge.defaultChallengeForTest()
        `when`(challengeRepositoryMock.findById(challengeId)).thenReturn(Optional.of(defaultChallengeForTest))

        val actual = handler.handle(FindChallenge(challengeId))

        assertThat(actual).isEqualTo(defaultChallengeForTest)
    }

    @Test
    fun `handle | repository finds no Challenge for given UUID, no Challenge is returned`() {
        val challengeId = randomUUID()
        `when`(challengeRepositoryMock.findById(challengeId)).thenReturn(Optional.empty())

        val actual = handler.handle(FindChallenge(challengeId))

        assertThat(actual).isNull()
    }
}
