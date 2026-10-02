# Creating fixtures for a demo app's domain use-cases

A demo app must run the feature flow without a real backend, so every domain use-case interface the feature
implementation depends on (see Step 3 of the main guide) needs a fake (fixture) implementation, wired into DI
from the demo app's own application module - the same place a real app would instead wire the production
`usecase` module's implementations.

## Default: succeed quietly

If the user gave no specific scenario for a use-case, make its fixture succeed on every call, after a short
`delay()` (one to two seconds) so the demo still shows its loading UI realistically. Return or construct a
plausible instance of the domain model the interface returns - don't leave it `null` or throw
`NotImplementedError`. Add a short comment noting this is a fixture default the user can change.

## Specific scenario: fail then succeed (or fail a fixed number of times)

When the user describes something like "let it fail the first time with an IO error, so I can check the retry
logic", implement the fixture as a stateful fake: a mutable counter captured in its closure, advanced on every
call, branching on its value. Use the *real* domain exception types (from the `domain.exception` package, e.g.
`IOException`, `AuthenticationException` - never a generic `RuntimeException`) so the feature's own
error-mapping code is exercised the same way it would be against a real backend. Log what's happening, tagged
with the interface's simple name, so the emulated branch is visible while demoing:

```kotlin
@Single
fun getPasswordRequirements(): GetPasswordRequirements {
    var attempt = 1
    val tag = GetPasswordRequirements::class.simpleName!!
    return object : GetPasswordRequirements {
        override suspend fun invoke(): PasswordRequirements {
            delay(2.seconds)
            return when (attempt) {
                1 -> {
                    Napier.d(tag = tag) { "Emulating non-fatal error" }
                    attempt = 2
                    throw IOException("Network error")
                }
                else -> {
                    Napier.d(tag = tag) { "Emulating success" }
                    attempt = 1
                    PasswordRequirements(regex = "^.{8,}$".toRegex(), description = "Minimum eight characters")
                }
            }
        }
    }
}
```

Repeat this pattern for every use-case the user wants to control, cycling the counter back to its first value
after the sequence completes so the demo can be replayed without restarting the app.

## Always-fail scenario

For "it should always fail with X", skip the counter and just throw the requested exception every time (still
behind a `delay()`), optionally logging that this is a permanent emulated failure.

## Multi-step scenarios

If the user describes a longer sequence across several use-cases (for example "preloading succeeds, but the
authentication call fails twice before succeeding"), give each use-case its own independent counter - don't
share one counter across fixtures unless the user is explicitly describing a single shared piece of state (like
a session).

## Wiring the fixtures in

Wire every fixture using whichever approach the project already uses for DI (see Step 5 of the main guide) -
don't introduce a new framework just for the demo. With Koin, bind each fixture with a singleton-scoped
function inside the demo app's own module, the one that `includes` the feature's own implementation module;
that `includes` relationship is what pulls in the feature's states, factories and API implementations, and the
fixtures declared alongside it supply the domain-level dependencies those classes expect but don't themselves
provide. With Hilt/Dagger, provide the same fixtures from an equivalent `@Module`/`@Provides` (or `@Binds`) set
installed alongside the feature's own module. With no DI framework at all, construct the fixtures directly
where the feature's `DataApi`/`UiApi` implementations are constructed, and pass them straight in as
constructor arguments.
