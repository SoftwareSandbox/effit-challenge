package be.swsb.effit.challenge

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@DataJpaTest
@AutoConfigureTestEntityManager
class ChallengeRepositoryIntegrationTest {

    @Autowired
    lateinit var testEntityManager: TestEntityManager

    @Autowired
    lateinit var challengeRepository: ChallengeRepository

    @Test
    fun `findAll should return all of the Challenges that are persisted in the DB`() {
        val playboyChallenge = Challenge(name = "Playboy", points = 7, description = "playboy description")
        val picassoChallenge = Challenge(name = "Picasso", points = 3, description = "picasso description")
        testEntityManager.persist(playboyChallenge)
        testEntityManager.persist(picassoChallenge)

        val actual = challengeRepository.findAll()

        assertThat(actual).containsExactly(playboyChallenge, picassoChallenge)
    }
}