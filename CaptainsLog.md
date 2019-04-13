## 2019/04/13 - Having Jackson automagically create a new UUID for a Challenge is juuuuuust fine.
At least for now.

There **is** a typealias for when I need to actually use that type instead of just using a `Challenge`.

## 2019/04/13 - Fixing repository mocks in ControllerTests
`@WebMvcTest(controller = SomeController::class)` would still try to create beans for JpaRepositories, and consequently fail on trying to find an entityManager.

I fixed it before by manually providing `@MockBean`s for unused repositories, but that would expand quite fast into duplicate `@MockBean` fields in tests.

I tried using `excludeAutoConfig...` which didn't work T_T

Eventually provided my own `@ContextConfiguration` for tests, that lists all repositories as `@MockBean`s, so I only have to do that once.

I also needed to have that configuration actually scan my Controller classes.

The remaining `@WebMvcTest` is now only used to setup `MockMvc`.

Mocking repositories that are used in ControllerTests now works by simply `@Autowired`ing the needed collaborating repositories.


## 2019/04/13 - backing properties on Jpa classes determine JpaRepository convention functions
For backing property:
```
@Embedded
private var _competitionId: CompetitionId
```
The convention function would look like:
```
fun findBy_competitionId(competitionId: CompetitionId): Competition?
```

IF you're lucky, Spring will try and suggest which backing property you meant when writing the convention function.
If not, you end up getting an exception that looks like:
```
Caused by: org.springframework.data.mapping.PropertyReferenceException: No property competitionIdentifier found for type Competition!
```

Where `competitionIdentifier` relates to the name in the convention function you wrote.

## 2019/04/11 - Use router-link instead of @click="router.push()"
`<router-link>` has a `tag` attribute that replaces the element with that tag:
```
<router-link to="/challenges" tag="v-list-tile">
```

## 2019/04/11 - Backing properties
To make sure exposed lists are not mutable externally, provide backing property like so:

```
private var _challenges: List<Challenge> = emptyList()
val challenges: List<Challenge>
    get() = _challenges
```

## 2019/04/07 - JUnit 5 to the rescue
Using JUnit 5 did help fix our problem with configuration loading...

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