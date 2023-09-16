package be.swsb.effit.backend.scenariotests

import be.swsb.effit.backend.domain.command.competition.CompetitionRepository
import be.swsb.effit.backend.domain.core.challenge.Challenge
import be.swsb.effit.backend.domain.core.challenge.ChallengeRepository
import be.swsb.effit.backend.domain.core.competition.Competition
import be.swsb.effit.backend.domain.core.competition.CompetitionId
import java.util.*


class InMemCompetitionRepository : CompetitionRepository {
    private val competitions = mutableMapOf<UUID, Competition>()

    override fun save(competition: Competition): Competition =
        competition.also { competitions[competition.id] = it }

    override fun findAll(): List<Competition> =
        competitions.values.toList()

    override fun findByCompetitionIdentifier(id: CompetitionId): Competition? =
        competitions.values.toList().firstOrNull { it.competitionId == id }

}

class InMemChallengeRepository : ChallengeRepository {
    private val challenges = mutableMapOf<UUID, Challenge>()

    override fun save(challenge: Challenge): Challenge =
        challenge.also { challenges[challenge.id] = it }

    override fun findByIdOrNull(challengeId: UUID): Challenge? =
        challenges[challengeId]

}