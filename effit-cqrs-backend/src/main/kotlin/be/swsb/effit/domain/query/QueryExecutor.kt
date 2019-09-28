package be.swsb.effit.domain.query

import org.springframework.stereotype.Service

@Service
class QueryExecutor {
    fun <T> execute(query: Query): T? {
        return null
    }
}
