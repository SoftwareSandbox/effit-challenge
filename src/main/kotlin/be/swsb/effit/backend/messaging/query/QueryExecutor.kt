package be.swsb.effit.backend.messaging.query

class QueryExecutor(private val registeredHandlers: List<QueryHandler<*, *>>) {

    fun <R> execute(query: Query<R>): R {
        val queryHandler = getQueryHandler(query) as QueryHandler<R, Query<R>>
        return queryHandler.handle(query)
    }

    private fun <R> getQueryHandler(query: Query<R>): QueryHandler<*, *> {
        return registeredHandlers.firstOrNull { it.getQueryType() == query.javaClass }
                ?: throw NoQueryHandlerException(query.asString())
    }
}

class NoQueryHandlerException(query: String) : RuntimeException("Could not find QueryHandler for query: $query")