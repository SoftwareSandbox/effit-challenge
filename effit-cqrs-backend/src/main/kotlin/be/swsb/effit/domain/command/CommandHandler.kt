package be.swsb.effit.domain.command

interface CommandHandler<out R, Q : Command<out R>> {
    fun handle(command: Q): R
    fun getCommandType(): Class<Q>
}
