package be.swsb.effit.domain.query.competition

import be.swsb.effit.domain.core.competition.Competition
import be.swsb.effit.domain.query.QueryHandler

class FindAllCompetitionsQueryHandler: QueryHandler<List<Competition>, FindAllCompetitions> {
    override fun handle(query: FindAllCompetitions): List<Competition> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getQueryType(): Class<FindAllCompetitions> {
        return FindAllCompetitions::class.java
    }
}
