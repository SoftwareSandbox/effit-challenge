package be.swsb.effit.backend.messaging.query

interface Query<R> {
    fun asString(): String {
        return this.javaClass.name
    }
}
