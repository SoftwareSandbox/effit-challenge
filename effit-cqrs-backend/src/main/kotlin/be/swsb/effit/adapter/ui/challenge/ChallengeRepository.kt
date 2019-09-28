package be.swsb.effit.adapter.ui.challenge

import be.swsb.effit.challenge.Challenge
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ChallengeRepository : JpaRepository<Challenge, UUID>
