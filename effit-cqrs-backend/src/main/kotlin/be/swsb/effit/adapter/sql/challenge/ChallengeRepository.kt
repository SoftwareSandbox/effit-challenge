package be.swsb.effit.adapter.sql.challenge

import be.swsb.effit.domain.core.challenge.Challenge
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ChallengeRepository : JpaRepository<Challenge, UUID>
