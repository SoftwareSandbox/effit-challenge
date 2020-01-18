package be.swsb.effit.domain.command.challenge

import be.swsb.effit.adapter.ui.util.RestApiExposed
import be.swsb.effit.domain.command.Command
import be.swsb.effit.domain.core.challenge.Challenge
import java.util.*

sealed class ChallengeCommands {
    data class UpdateChallenge(val id: UUID,
                               val name: String,
                               val points: Int,
                               val description: String) : RestApiExposed, Command<Challenge>
}

typealias UpdateChallenge = ChallengeCommands.UpdateChallenge
