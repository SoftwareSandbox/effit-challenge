package be.swsb.effit.domain.query.competition

import be.swsb.effit.domain.core.competition.Competition
import be.swsb.effit.domain.query.Query

data class FindAllCompetitions(val snarf: Boolean): Query<List<Competition>>
