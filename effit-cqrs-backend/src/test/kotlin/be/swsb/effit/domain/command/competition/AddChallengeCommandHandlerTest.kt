package be.swsb.effit.domain.command.competition

import be.swsb.effit.adapter.sql.challenge.ChallengeRepository
import be.swsb.effit.adapter.sql.competition.CompetitionRepository
import be.swsb.effit.domain.core.challenge.Challenge
import be.swsb.effit.domain.core.challenge.defaultChallengeForTest
import be.swsb.effit.domain.core.competition.Competition
import be.swsb.effit.domain.core.competition.CompetitionId
import be.swsb.effit.domain.core.competition.defaultCompetitionForTest
import be.swsb.effit.domain.core.exceptions.DomainValidationRuntimeException
import be.swsb.effit.domain.query.competition.FindCompetition
import be.swsb.effit.messaging.query.QueryExecutor
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class AddChallengeCommandHandlerTest {

    @Mock
    private lateinit var queryExecutorMock: QueryExecutor
    @Mock
    private lateinit var challengeRepositoryMock: ChallengeRepository
    @Mock
    private lateinit var competitionRepositoryMock: CompetitionRepository
    @Captor
    private lateinit var competitionCaptor: ArgumentCaptor<Competition>
    @Captor
    private lateinit var challengeCaptor: ArgumentCaptor<Challenge>

    private lateinit var handler: AddChallengeCommandHandler

    @BeforeEach
    fun setUp() {
        handler = AddChallengeCommandHandler(queryExecutorMock, challengeRepositoryMock, competitionRepositoryMock)
    }

    @Test
    fun `handle | Created Challenge should be valid`() {
        val invalidChallengeToAdd = ChallengeToAdd(name = "Snarf's Challenge", points = 0, description = "whatever")
        assertThatExceptionOfType(DomainValidationRuntimeException::class.java)
                .isThrownBy { handler.handle(AddChallenge(CompetitionId("does not matter"), invalidChallengeToAdd)) }
    }

    @Test
    fun `handle | Create Challenge and add it to the found Competition`() {
        val competitionId = "ThundercatsCompetition"
        val existingCompetition = Competition.defaultCompetitionForTest(name = competitionId)
        `when`(queryExecutorMock.execute(FindCompetition(CompetitionId(competitionId)))).thenReturn(existingCompetition)
        val newlyCreatedAndSavedChallenge = Challenge.defaultChallengeForTest()
        `when`(challengeRepositoryMock.save(challengeCaptor.capture())).thenReturn(newlyCreatedAndSavedChallenge)
        `when`(competitionRepositoryMock.save(competitionCaptor.capture())).thenReturn(existingCompetition)

        val challengeToAdd = ChallengeToAdd.defaultChallengeToAddForTest()

        handler.handle(AddChallenge(CompetitionId(competitionId), challengeToAdd))

        val challengeToBeSaved = challengeCaptor.value
        assertThat(challengeToBeSaved.id).isNotNull()
        assertThat(challengeToBeSaved.name).isEqualTo(challengeToAdd.name)
        assertThat(challengeToBeSaved.points).isEqualTo(challengeToAdd.points)
        assertThat(challengeToBeSaved.description).isEqualTo(challengeToAdd.description)

        val competitionToBeSaved = competitionCaptor.value
        assertThat(competitionToBeSaved.challenges).containsExactly(newlyCreatedAndSavedChallenge)
    }
}
