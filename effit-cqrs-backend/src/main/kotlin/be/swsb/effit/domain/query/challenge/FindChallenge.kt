package be.swsb.effit.domain.query.challenge

import be.swsb.effit.domain.core.challenge.Challenge
import be.swsb.effit.domain.query.Query
import java.util.*

data class FindChallenge(val challengeId: UUID) : Query<Challenge?>
