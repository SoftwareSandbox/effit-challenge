package be.swsb.effit.backend.messaging.command

interface CommandHandler<out R, C : Command<out R>> {
    fun handle(command: C): R
    fun getCommandType(): Class<C>
}
