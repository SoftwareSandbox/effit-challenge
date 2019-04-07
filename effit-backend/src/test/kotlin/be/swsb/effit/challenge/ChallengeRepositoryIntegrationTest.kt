package be.swsb.effit.challenge

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@DataJpaTest
class ChallengeRepositoryIntegrationTest {

    @Autowired
    lateinit var challengeRepository: ChallengeRepository

    @Test
    fun `findAll should return all of the Challenges that are persisted in the DB`() {
        val playboyChallenge = Challenge(name = "Playboy", points = 7, description = "playboy description")
        val picassoChallenge = Challenge(name = "Picasso", points = 3, description = "picasso description")

        challengeRepository.save(playboyChallenge)
        challengeRepository.save(picassoChallenge)

        val actual = challengeRepository.findAll()

        assertThat(actual).containsExactly(playboyChallenge, picassoChallenge)
    }
}