package be.swsb.effit.adapter.sql.challenge

import be.swsb.effit.domain.core.challenge.Challenge
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
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

    @Test
    fun `save of a Challenge with an already existing ID updates the Challenge`() {
        val playboyChallenge = Challenge(name = "Playboy", points = 7, description = "playboy description")
        testEntityManager.persist(playboyChallenge)
        testEntityManager.flush()
        testEntityManager.clear()

        val updatedChallengeWithSameId = Challenge(id = playboyChallenge.id, name = "Snarfboy", points = 6, description = "playboy description")
        challengeRepository.save(updatedChallengeWithSameId)
        testEntityManager.flush()
        testEntityManager.clear()

        val actual = challengeRepository.findAll()

        assertThat(actual).containsExactly(updatedChallengeWithSameId)
    }
}
