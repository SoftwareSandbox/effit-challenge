package be.swsb.effit

import org.springframework.stereotype.Repository

@Repository
class ChallengeRepository {
    fun findAll(): List<Challenge> {
        return emptyList()
    }
}