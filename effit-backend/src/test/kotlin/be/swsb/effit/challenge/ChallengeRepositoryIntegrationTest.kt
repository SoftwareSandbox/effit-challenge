package be.swsb.effit.challenge

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.spring.SpringListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class ChallengeRepositoryIntegrationTest : StringSpec() {
    override fun listeners() = listOf(SpringListener)

    @Autowired
    lateinit var challengeRepository: ChallengeRepository

    init {
        "findAll should return all of the Challenges that are persisted in the DB" {
            val playboyChallenge = Challenge(name = "Playboy", points = 7, description = "playboy description")
            val picassoChallenge = Challenge(name = "Picasso", points = 3, description = "picasso description")

            challengeRepository.save(playboyChallenge)
            challengeRepository.save(picassoChallenge)

            val actual = challengeRepository.findAll()

            actual shouldBe listOf(playboyChallenge, picassoChallenge)
        }

    }
}