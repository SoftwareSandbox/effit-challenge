package be.swsb.effit.backend.domain.command.competition

import be.swsb.effit.backend.domain.core.competition.Competition
import be.swsb.effit.backend.domain.core.competition.CompetitionId

interface CompetitionRepository {
    fun save(competition: Competition): Competition
    fun findAll(): List<Competition>
    fun findByCompetitionIdentifier(id: CompetitionId): Competition?
}