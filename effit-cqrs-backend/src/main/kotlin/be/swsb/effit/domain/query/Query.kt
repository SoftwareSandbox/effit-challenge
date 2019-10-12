package be.swsb.effit.domain.query

interface Query<R> {
    fun asString(): String {
        return this.javaClass.name
    }
}
