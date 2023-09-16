package be.swsb.effit.backend.domain.query

interface Query<R> {
    fun asString(): String {
        return this.javaClass.name
    }
}
