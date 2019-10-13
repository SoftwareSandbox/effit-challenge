package be.swsb.effit.messaging.query

import be.swsb.effit.adapter.sql.competition.CompetitionRepository
import be.swsb.effit.domain.query.Query
import be.swsb.effit.domain.query.QueryHandler
import be.swsb.effit.domain.query.competition.FindByCompetitionIdQueryHandler
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Bean
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(value = [SpringExtension::class])
@ContextConfiguration(classes = [
    QueryExecutor::class,
    FindAllSnarfsQueryHandler::class
    ])
class QueryExecutorTest {

    @MockBean
    private lateinit var competitionRepository: CompetitionRepository

    @Bean
    fun findAllSnarfsQueryHandler(): FindAllSnarfsQueryHandler {
        return FindAllSnarfsQueryHandler()
    }

    @Autowired
    lateinit var queryExecutor: QueryExecutor

    @Test
    fun `execute | when no handler found, throw exception`() {
        assertThatExceptionOfType(NoQueryHandlerException::class.java)
                .isThrownBy { queryExecutor.execute(GetRidOfSnarfs()) }
    }

    @Test
    fun `execute | when handler found, executes handler and returns result`() {
        val result = queryExecutor.execute(FindAllSnarfs())
        assertThat(result).isNull()
    }
}

data class Snarf(val name: String)
class GetRidOfSnarfs : Query<Snarf>
class FindAllSnarfs : Query<Snarf?>

class FindAllSnarfsQueryHandler : QueryHandler<Snarf?, FindAllSnarfs> {
    override fun handle(query: FindAllSnarfs): Snarf? {
        return null
    }

    override fun getQueryType(): Class<FindAllSnarfs> {
        return FindAllSnarfs::class.java
    }
}
