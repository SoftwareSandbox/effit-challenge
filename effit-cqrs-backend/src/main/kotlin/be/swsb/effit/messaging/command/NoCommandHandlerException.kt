package be.swsb.effit.messaging.command

class NoCommandHandlerException(command: String) : RuntimeException("Could not find CommandHandler for query: $command")
