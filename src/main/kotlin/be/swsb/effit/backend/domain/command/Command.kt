package be.swsb.effit.backend.domain.command

interface Command<A> {
    fun asString(): String {
        return this.javaClass.name
    }
}
