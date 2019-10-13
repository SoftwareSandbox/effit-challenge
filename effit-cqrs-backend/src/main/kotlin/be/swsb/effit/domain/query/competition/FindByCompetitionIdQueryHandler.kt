package be.swsb.effit.domain.query.competition

import be.swsb.effit.domain.core.competition.Competition
import be.swsb.effit.domain.query.QueryHandler

class FindByCompetitionIdQueryHandler: QueryHandler<Competition?, FindByCompetitionId> {
    override fun handle(query: FindByCompetitionId): Competition? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getQueryType(): Class<FindByCompetitionId> {
        return FindByCompetitionId::class.java
    }
}
