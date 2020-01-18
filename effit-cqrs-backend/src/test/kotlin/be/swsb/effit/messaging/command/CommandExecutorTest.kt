package be.swsb.effit.messaging.command

import be.swsb.effit.domain.command.Command
import be.swsb.effit.domain.command.CommandHandler
import be.swsb.effit.messaging.query.Snarf
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(value = [SpringExtension::class])
@ContextConfiguration(classes = [
    CommandExecutor::class,
    MakeSnarfSitCommandHandler::class
])
class CommandExecutorTest {

    @Bean
    fun makeSnarfSitCommandHandler(): MakeSnarfSitCommandHandler {
        return MakeSnarfSitCommandHandler()
    }

    @Autowired
    lateinit var commandExecutor: CommandExecutor

    @Test
    fun `execute | when no handler found, throw exception`() {
        assertThatExceptionOfType(NoCommandHandlerException::class.java)
                .isThrownBy { commandExecutor.execute(MakeSnarfSleep) }
    }

    @Test
    fun `execute | when handler found, executes handler and returns result`() {
        val result = commandExecutor.execute(MakeSnarfSit)
        assertThat(result).isNull()
    }
}

object MakeSnarfSleep: Command<Snarf?>
object MakeSnarfSit: Command<Snarf?>

class MakeSnarfSitCommandHandler: CommandHandler<Snarf?, MakeSnarfSit> {
    override fun handle(command: MakeSnarfSit): Snarf? {
        return null
    }
    override fun getCommandType(): Class<MakeSnarfSit> {
        return MakeSnarfSit::class.java
    }
}
