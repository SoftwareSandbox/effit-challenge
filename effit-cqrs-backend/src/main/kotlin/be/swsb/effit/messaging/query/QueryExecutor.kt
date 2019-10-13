package be.swsb.effit.messaging.query

import be.swsb.effit.domain.query.Query
import be.swsb.effit.domain.query.QueryHandler
import org.springframework.stereotype.Service

@Service
class QueryExecutor(val registeredHandlers: List<QueryHandler<*,*>>) {

    @Suppress("UNCHECKED_CAST")
    fun <R> execute(query: Query<R>): R {
        val queryHandler = getQueryHandler(query) as QueryHandler<R, Query<R>>
        return queryHandler.handle(query)
    }

    private fun <R> getQueryHandler(query: Query<R>): QueryHandler<*,*> {
        return registeredHandlers.firstOrNull { it.getQueryType() == query.javaClass }
                ?: throw NoQueryHandlerException(query.asString())
    }
}
