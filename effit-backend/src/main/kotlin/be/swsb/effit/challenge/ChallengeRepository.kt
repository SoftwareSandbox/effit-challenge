package be.swsb.effit.challenge

import org.springframework.stereotype.Repository

@Repository
class ChallengeRepository {
    fun findAll(): List<Challenge> {
        return emptyList()
    }
}