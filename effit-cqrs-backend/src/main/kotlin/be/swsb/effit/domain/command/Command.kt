package be.swsb.effit.domain.command

interface Command<A> {
    fun asString(): String {
        return this.javaClass.name
    }
}
