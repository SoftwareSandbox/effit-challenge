package be.swsb.effit.backend.messaging.command

interface Command<A> {
    fun asString(): String {
        return this.javaClass.name
    }
}
