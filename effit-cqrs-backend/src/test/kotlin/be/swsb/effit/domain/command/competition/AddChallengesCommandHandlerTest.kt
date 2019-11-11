package be.swsb.effit.domain.command.competition

import be.swsb.effit.adapter.sql.competition.CompetitionRepository
import be.swsb.effit.domain.core.challenge.Challenge
import be.swsb.effit.domain.core.challenge.fromChallengeToAdd
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
    private lateinit var competitionRepositoryMock: CompetitionRepository
    @Captor
    private lateinit var competitionCaptor: ArgumentCaptor<Competition>

    private lateinit var handler: AddChallengesCommandHandler

    @BeforeEach
    fun setUp() {
        handler = AddChallengesCommandHandler(queryExecutorMock, competitionRepositoryMock)
    }

    @Test
    fun `handle | Created Challenge should be valid`() {
        val invalidChallengeToAdd = ChallengeToAdd(name = "Snarf's Challenge", points = 0, description = "whatever")
        val existingCompetition = Competition.defaultCompetitionForTest(name = "ThundercatsCompetition")
        val competitionId = CompetitionId("ThundercatsCompetition")
        `when`(queryExecutorMock.execute(FindCompetition(competitionId))).thenReturn(existingCompetition)
        assertThatExceptionOfType(DomainValidationRuntimeException::class.java)
                .isThrownBy { handler.handle(AddChallenges(competitionId, listOf(invalidChallengeToAdd))) }
    }

    @Test
    fun `handle | Create Challenges and add them to the found Competition`() {
        val competitionId = "ThundercatsCompetition"
        val existingCompetition = Competition.defaultCompetitionForTest(name = competitionId)
        `when`(queryExecutorMock.execute(FindCompetition(CompetitionId(competitionId)))).thenReturn(existingCompetition)
        `when`(competitionRepositoryMock.save(competitionCaptor.capture())).thenReturn(existingCompetition)

        val challengeToAdd1 = ChallengeToAdd.defaultChallengeToAddForTest(name = "challenge1")
        val challengeToAdd2 = ChallengeToAdd.defaultChallengeToAddForTest(name = "challenge2")

        handler.handle(AddChallenges(CompetitionId(competitionId), listOf(challengeToAdd1, challengeToAdd2)))

        val competitionToBeSaved = competitionCaptor.value
        assertThat(competitionToBeSaved.challenges)
                .usingElementComparatorIgnoringFields("id")
                .containsExactly(
                        Challenge.fromChallengeToAdd(challengeToAdd1),
                        Challenge.fromChallengeToAdd(challengeToAdd2)
                )
    }
}
