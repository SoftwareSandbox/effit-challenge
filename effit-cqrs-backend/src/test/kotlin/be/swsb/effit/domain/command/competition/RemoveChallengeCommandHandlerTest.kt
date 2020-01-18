package be.swsb.effit.domain.command.competition

import be.swsb.effit.adapter.sql.competition.CompetitionRepository
import be.swsb.effit.domain.core.competition.Competition
import be.swsb.effit.domain.core.competition.CompetitionId
import be.swsb.effit.domain.core.competition.defaultCompetitionForTest
import be.swsb.effit.domain.core.competition.findChallenge
import be.swsb.effit.domain.query.competition.FindCompetition
import be.swsb.effit.messaging.query.QueryExecutor
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class RemoveChallengeCommandHandlerTest {

    @Mock
    private lateinit var queryExecutor: QueryExecutor
    @Mock
    private lateinit var competitionRepository: CompetitionRepository

    private lateinit var handler: RemoveChallengeCommandHandler

    @BeforeEach
    fun setUp() {
        handler = RemoveChallengeCommandHandler(queryExecutor, competitionRepository)
    }

    @Test
    fun `handle | should remove the given challenge id`() {
        val givenCompetitionId = "SnowCase2018"
        val competitionId = CompetitionId(givenCompetitionId)

        val challengeThatWillGetDeleted = ChallengeToAdd.defaultChallengeToAddForTest(name = "ChallengeToBeDeleted")
        val someOtherChallenge = ChallengeToAdd.defaultChallengeToAddForTest(name = "Picasso")
        val someCompetition = Competition.defaultCompetitionForTest(challenges = listOf(challengeThatWillGetDeleted, someOtherChallenge))
        val challengeExpectedToBeDeleted = someCompetition.findChallenge("ChallengeToBeDeleted")
        val givenChallengeId = challengeExpectedToBeDeleted.id

        `when findByCompetitionIdentifier then return`(competitionId, someCompetition)
        `when`(competitionRepository.save(any(Competition::class.java))).thenReturn(someCompetition)

        handler.handle(RemoveChallenge(competitionId, givenChallengeId))

        assertThat(someCompetition.challenges).doesNotContain(challengeExpectedToBeDeleted)
        verify(competitionRepository).save(someCompetition)
    }

    private fun `when findByCompetitionIdentifier then return`(givenCompetitionId: CompetitionId, someCompetition: Competition) {
        `when`(queryExecutor.execute(FindCompetition(givenCompetitionId))).thenReturn(someCompetition)
    }

}
