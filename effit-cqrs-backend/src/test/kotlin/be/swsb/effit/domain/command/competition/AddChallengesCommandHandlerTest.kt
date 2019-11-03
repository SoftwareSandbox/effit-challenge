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
class AddChallengesCommandHandlerTest {

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

    private lateinit var handler: AddChallengesCommandHandler

    @BeforeEach
    fun setUp() {
        handler = AddChallengesCommandHandler(queryExecutorMock, challengeRepositoryMock, competitionRepositoryMock)
    }

    @Test
    fun `handle | Created Challenge should be valid`() {
        val invalidChallengeToAdd = ChallengeToAdd(name = "Snarf's Challenge", points = 0, description = "whatever")
        assertThatExceptionOfType(DomainValidationRuntimeException::class.java)
                .isThrownBy { handler.handle(AddChallenges(CompetitionId("does not matter"), listOf(invalidChallengeToAdd))) }
    }

    @Test
    fun `handle | Create Challenges and add them to the found Competition`() {
        val competitionId = "ThundercatsCompetition"
        val existingCompetition = Competition.defaultCompetitionForTest(name = competitionId)
        `when`(queryExecutorMock.execute(FindCompetition(CompetitionId(competitionId)))).thenReturn(existingCompetition)
        val newlyCreatedAndSavedChallenge1 = Challenge.defaultChallengeForTest()
        val newlyCreatedAndSavedChallenge2 = Challenge.defaultChallengeForTest()
        `when`(challengeRepositoryMock.save(challengeCaptor.capture()))
                .thenReturn(newlyCreatedAndSavedChallenge1)
                .thenReturn(newlyCreatedAndSavedChallenge2)
        `when`(competitionRepositoryMock.save(competitionCaptor.capture())).thenReturn(existingCompetition)

        val challengeToAdd1 = ChallengeToAdd.defaultChallengeToAddForTest(name = "challenge1")
        val challengeToAdd2 = ChallengeToAdd.defaultChallengeToAddForTest(name = "challenge2")

        handler.handle(AddChallenges(CompetitionId(competitionId), listOf(challengeToAdd1, challengeToAdd2)))

        val challenge1ToBeSaved = challengeCaptor.allValues[0]
        assertThat(challenge1ToBeSaved.id).isNotNull()
        assertThat(challenge1ToBeSaved.name).isEqualTo(challengeToAdd1.name)
        assertThat(challenge1ToBeSaved.points).isEqualTo(challengeToAdd1.points)
        assertThat(challenge1ToBeSaved.description).isEqualTo(challengeToAdd1.description)

        val challenge2ToBeSaved = challengeCaptor.allValues[1]
        assertThat(challenge2ToBeSaved.id).isNotNull()
        assertThat(challenge2ToBeSaved.name).isEqualTo(challengeToAdd2.name)
        assertThat(challenge2ToBeSaved.points).isEqualTo(challengeToAdd2.points)
        assertThat(challenge2ToBeSaved.description).isEqualTo(challengeToAdd2.description)

        val competitionToBeSaved = competitionCaptor.value
        assertThat(competitionToBeSaved.challenges).containsExactly(newlyCreatedAndSavedChallenge1, newlyCreatedAndSavedChallenge2)
    }
}
