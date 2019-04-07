## 2019/04/06 - @AutoConfiguration misery
Somehow tests are loading beans defined in separate configuration classes.

We'll try to use JUnit instead of kotlin-test to get that fixed. :pray:

## 2019/04/06 - No-arg plugin not working?
Had to manually add default constructors to fix our RepositoryIntegrationTest T_T

Really need to resolve this issue!

## 2019/04/06 - Using functional package names instead of technical ones

## 2019/04/06 - WebMvcTest vs. SpringBootTest
`@SpringBootTest` requires your test `@Configuration` to be in a separate class (probably because of initialization order).

`@WebMvcTest` can interpret localized `@TestConfiguration` classes, but requires you to define your _Controller under test_ in the annotation itself.

## 2019/04/06 - (Modular) Monolith
Because I want to prove to myself that starting out from a monolith should not necessarily mean I get stuck in that architecture.

## 2019/04/06 - Started this project
Live streaming on https://twitch.tv/sch3lpsc2