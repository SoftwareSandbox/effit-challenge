package be.swsb.effit.messaging.query

class NoQueryHandlerException(query: String) : RuntimeException("Could not find QueryHandler for query: $query")
