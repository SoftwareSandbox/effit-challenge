package be.swsb.effit.backend.messaging.command

import be.swsb.effit.backend.domain.command.Command
import be.swsb.effit.backend.domain.command.CommandHandler

class CommandExecutor(private val registeredHandlers: List<CommandHandler<*, *>>) {

//    @Transactional
    fun <A> execute(command: Command<A>): A {
        val commandHandler = getCommandHandler(command) as CommandHandler<A, Command<A>>
        return commandHandler.handle(command)
    }

    private fun <R> getCommandHandler(command: Command<R>): CommandHandler<*,*> {
        return registeredHandlers.firstOrNull { it.getCommandType() == command.javaClass }
                ?: throw NoCommandHandlerException(command.asString())
    }
}

class NoCommandHandlerException(command: String) : RuntimeException("Could not find CommandHandler for query: $command")
