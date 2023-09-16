package be.swsb.effit.backend.domain.query.challenge

import be.swsb.effit.backend.domain.core.challenge.Challenge
import be.swsb.effit.backend.domain.query.Query
import java.util.*

data class FindChallenge(val challengeId: UUID) : Query<Challenge>
