package be.swsb.effit.backend.domain.command.challenge

import be.swsb.effit.backend.domain.RestApiExposed
import be.swsb.effit.backend.domain.command.Command
import be.swsb.effit.backend.domain.core.challenge.Challenge
import java.util.*

sealed class ChallengeCommands {
    data class UpdateChallenge(val id: UUID,
                               val name: String,
                               val points: Int,
                               val description: String) : RestApiExposed, Command<Challenge>
}