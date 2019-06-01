package be.swsb.effit.competition.competitor

import be.swsb.effit.challenge.Challenge
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
        val snarf = Competitor(name = "Snarf")

        competitorRepository.save(snarf)
        testEntityManager.flush()
        testEntityManager.clear()

        assertThat(testEntityManager.find(Competitor::class.java, snarf.id))
                .isEqualToIgnoringGivenFields(snarf, "_completedChallenges")
    }

    @Test
    fun `A Competitor can have completed Challenges`() {
        val challenge = Challenge(name = "whinge", points = 4, description = "whatevs")
        testEntityManager.persistAndFlush(challenge)
        val anotherChallenge = Challenge(name = "whinge", points = 4, description = "different challenge")
        testEntityManager.persistAndFlush(anotherChallenge)

        val snarf = Competitor(name = "Snarf")
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
        val challenge = Challenge(name = "whinge", points = 4, description = "whatevs")

        val snarf = Competitor(name = "Snarf")
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