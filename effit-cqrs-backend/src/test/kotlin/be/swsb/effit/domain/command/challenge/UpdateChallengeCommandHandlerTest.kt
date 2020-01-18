package be.swsb.effit.domain.command.challenge

import be.swsb.effit.adapter.sql.challenge.ChallengeRepository
import be.swsb.effit.domain.core.challenge.Challenge
import be.swsb.effit.domain.core.challenge.defaultChallengeForTest
import be.swsb.effit.domain.query.challenge.FindChallenge
import be.swsb.effit.messaging.query.QueryExecutor
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.any
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class UpdateChallengeCommandHandlerTest {

    @Mock
    private lateinit var challengeRepository: ChallengeRepository
    @Mock
    private lateinit var queryExecutor: QueryExecutor
    @Captor
    private lateinit var challengeCaptor: ArgumentCaptor<Challenge>

    private lateinit var handler: UpdateChallengeCommandHandler

    @BeforeEach
    fun setUp() {
        handler = UpdateChallengeCommandHandler(queryExecutor, challengeRepository)
    }

    @Test
    fun `handle | updates found challenge with values from the command`() {
        val givenId = UUID.randomUUID()
        val existingChallenge = Challenge.defaultChallengeForTest(id = givenId, name = "Pablo", points = 3, description = "Lion-O is the Best-O")

        `when`(queryExecutor.execute(FindChallenge(givenId))).thenReturn(existingChallenge)
        `when`(challengeRepository.save(any(Challenge::class.java))).thenReturn(existingChallenge)

        handler.handle(UpdateChallenge(givenId, "Picasso", 7, "snarf snarf"))

        verify(challengeRepository).save(challengeCaptor.capture())

        val persistedChallenge = challengeCaptor.value
        assertThat(persistedChallenge.id).isEqualTo(existingChallenge.id)
        assertThat(persistedChallenge.name).isEqualTo("Picasso")
        assertThat(persistedChallenge.points).isEqualTo(7)
        assertThat(persistedChallenge.description).isEqualTo("snarf snarf")
    }
}
