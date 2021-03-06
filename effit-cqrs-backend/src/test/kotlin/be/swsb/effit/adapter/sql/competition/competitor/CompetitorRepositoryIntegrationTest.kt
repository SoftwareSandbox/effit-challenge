package be.swsb.effit.adapter.sql.competition.competitor

import be.swsb.effit.domain.command.competition.ChallengeToAdd
import be.swsb.effit.domain.command.competition.defaultChallengeToAddForTest
import be.swsb.effit.domain.core.challenge.Challenge
import be.swsb.effit.domain.core.challenge.fromChallengeToAdd
import be.swsb.effit.domain.core.competition.competitor.Competitor
import be.swsb.effit.domain.core.competition.competitor.defaultCompetitorForTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@DataJpaTest
@AutoConfigureTestEntityManager
class CompetitorRepositoryIntegrationTest {

    @Autowired
    lateinit var competitorRepository: CompetitorRepository
    @Autowired
    lateinit var testEntityManager: TestEntityManager

    @Test
    fun `A Competitor can be persisted and retrieved`() {
        val snarf = Competitor.defaultCompetitorForTest(name = "Snarf")

        competitorRepository.save(snarf)
        testEntityManager.flush()
        testEntityManager.clear()

        assertThat(testEntityManager.find(Competitor::class.java, snarf.id))
                .isEqualToIgnoringGivenFields(snarf, "_completedChallenges")
    }

    @Test
    fun `A Competitor can have completed Challenges`() {
        val challenge = Challenge.fromChallengeToAdd(ChallengeToAdd.defaultChallengeToAddForTest())
        testEntityManager.persistAndFlush(challenge)
        val anotherChallenge = Challenge.fromChallengeToAdd(ChallengeToAdd.defaultChallengeToAddForTest())
        testEntityManager.persistAndFlush(anotherChallenge)

        val snarf = Competitor.defaultCompetitorForTest(name = "Snarf")
        competitorRepository.save(snarf)
        testEntityManager.flush()
        testEntityManager.clear()

        snarf.completeChallenge(challenge)
        competitorRepository.save(snarf)
        testEntityManager.flush()
        testEntityManager.clear()

        val fetchedSnarf = testEntityManager.find(Competitor::class.java, snarf.id)
        assertThat(fetchedSnarf.completedChallenges)
                .containsExactly(challenge)
                .doesNotContain(anotherChallenge)
    }

    @Test
    fun `A Competitor can not have a non-persisted Challenge as a completed Challenge`() {
        val challenge = Challenge.fromChallengeToAdd(ChallengeToAdd.defaultChallengeToAddForTest())

        val snarf = Competitor.defaultCompetitorForTest(name = "Snarf")
        competitorRepository.save(snarf)
        testEntityManager.flush()
        testEntityManager.clear()

        snarf.completeChallenge(challenge)
        assertThatExceptionOfType(JpaObjectRetrievalFailureException::class.java)
                .isThrownBy {
                    competitorRepository.save(snarf)
                    testEntityManager.flush()
                    testEntityManager.clear()
                }


        val fetchedSnarf = testEntityManager.find(Competitor::class.java, snarf.id)
        assertThat(fetchedSnarf.completedChallenges)
                .doesNotContain(challenge)
    }
}
