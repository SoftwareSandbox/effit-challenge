package be.swsb.effit.challenge

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ChallengeRepository : JpaRepository<Challenge, UUID>