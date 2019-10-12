package be.swsb.effit.messaging.query

import be.swsb.effit.domain.query.Query
import be.swsb.effit.domain.query.QueryHandler
import org.springframework.stereotype.Component

@Component
class QueryExecutor(val registeredHandlers: List<QueryHandler<*,*>>) {

    @Suppress("UNCHECKED_CAST")
    fun <R> execute(query: Query<R>): R {
        val queryHandler = getQueryHandler(query) as QueryHandler<R, Query<R>>?

        return queryHandler
                ?. handle(query)
                ?: throw NoQueryHandlerException(query.asString())
    }

    private fun <R> getQueryHandler(query: Query<R>): QueryHandler<*,*>? {
        return registeredHandlers.firstOrNull { it.getQueryType() == query.javaClass }
    }
}
