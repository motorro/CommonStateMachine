---
name: commonstatemachine-commonchildflow-demo
description: Use this skill to build a small standalone demo/debug application that launches one existing
  CommonStateMachine Common Child Flow feature module on its own, without building the full app around it. Use
  it whenever the user asks to demo, debug, showcase, or try out a feature module in isolation - for example
  "Create a demo app for the auth module", "Let me run the friendlist flow on its own to show stakeholders", or
  "Set up a debug app so I can test the payment flow end-to-end with Maestro/Journey". It fakes the feature's
  domain use-cases with fixtures so the flow runs without a backend, including fixtures for specific failure
  scenarios the user describes (for example "make the first login attempt fail with a network error, so I can
  check the retry logic"). If the feature module doesn't exist yet, use the boilerplate skill first - this
  skill only wires up a way to run one that's already there.
metadata:
  author: Motorro
  last-updated: '2026-10-02'
  keywords:
    - android
    - architecture
    - cross-platform
    - kotlin-multiplatform
    - mvi
    - state-machine
    - feature-flow
    - demo-app
    - debug
    - fixtures
---

## Description
Use this skill to build a small debug/demo application module that hosts an existing Common Child Flow feature
on its own screen, so it can be launched without the rest of the app. This is useful to demonstrate a feature to
stakeholders before the surrounding app exists, and to test a feature end-to-end by hand or with a UI-testing
tool (Maestro, Journey, etc.) without depending on a real backend.

For a deeper dive into the state machine mechanics and common components, refer to the [Core Architecture](references/ARCHITECTURE.md).
For a deeper dive into the common child flow philosophy and design, refer to the [Common Child Flow Architecture](references/COMMON-CHILD-FLOW.md).

## Example
This instruction uses the same [example authentication module](assets/example) as the boilerplate and add-state
skills, together with its domain module and a complete example demo app. All examples, schemes and code samples
refer to this example; adjust names and packages to your actual need.

## Strategy
You will create one small Gradle application module that hosts an existing feature flow (an API module and one
of its implementations) on its own screen, fed by fixture (fake) domain use-cases so the flow runs without a
real backend:

```
my-project              # Project folder
├── auth                # Existing feature folder - module must already exist
│  ├── api
│  ├── implementation
│  └── demo             # New: the demo/debug application module
└── ...
```

Take a look at the [class diagram](references/common-child-flow.puml) for an overview of how the API,
implementation, and a hosting application relate to each other.

## Gathering the requirements for the demo app
Before creating the module, work out what you need from what the user gave you and from the project:

- The target feature: which existing Common Child Flow module (an `api` module and one of its
  `implementation` modules) is this demo for? It must already exist - if it doesn't, use the boilerplate skill
  first to create it, then come back here.
- The common UI module: look for an existing shared UI/theme Gradle module in the project (search for a
  top-level `Theme` composable wrapping `MaterialTheme`, and shared design components such as an app bar or a
  loading indicator - `appcore` in the example). Reuse its theme and components in the demo instead of
  inventing new ones. If the project has no such module, fall back to plain `MaterialTheme` and a `TopAppBar`.
- The domain use-cases and models the feature actually depends on: trace the feature implementation's
  constructor-injected dependencies back to the `domain` module interfaces they call (its state-factory and
  states are the place to look - see Step 3). These are exactly what you need to fake in Step 4.
- Nested feature dependencies: does the feature host another Common Child Flow module of its own via a proxy -
  the same way the example app module hosts `auth`? If so, find out whether the user wants the real
  implementation wired in, or a lightweight Data/UI API mock instead - see Step 3.
- Fixture behavior: did the user ask for a specific scenario (for example "let the usecase fail the first
  time with an IO error, so I can check the retry logic")? If so, implement exactly that behavior using the
  real domain exception/model types. If the user gave no specifics, default to a fixture that just succeeds
  (with a short delay to emulate a real call) and say so in a comment, so it's easy to find and change later.
- Dependency injection: does the project use a DI framework (Koin, Hilt, Dagger, or similar), or is it wired
  by hand (plain factory functions/constructors)? Look at the feature's own implementation module and an
  existing app module to find out which, and wire the fixtures the same way - don't introduce a new DI
  framework just for the demo. See Step 5.
- Did the user ask for anything beyond the happy-path screen (for example starting the flow already
  authenticated, or a specific `init` input)? Use it when constructing `MainViewModel` in Step 7.

If the user gave you only part of this, use what you have and ask clarifying questions about anything still
ambiguous rather than guessing silently.

Follow the following common rules when writing code:

- Follow the project coding conventions or use Kotlin default
- Follow the project naming conventions or use Kotlin default
- If the project uses KDoc as a coding convention - add it to the created types, methods, properties
- Add TODO comments wherever you default a fixture to "just succeed" so the user knows it's a placeholder

## Step 1. Confirm the target feature and module structure
Identify the feature's `api` module (its `Gesture`, `UiState`, `Input`, `Result`, `DataApi`, `UiApi` types) and
the specific `implementation` module to demo, if there are several. If the feature module doesn't exist yet,
stop here and use the boilerplate skill instead.

Create the new module directory next to the feature's existing `api`/`implementation` modules, following the
project's module-naming convention (`demo` in the example). Use the Gradle setup common to other application
modules in the project if one exists to copy from.

## Step 2. Create the demo module's Gradle file
This is an internal tool, not something that ships to end users - keep it minimal rather than matching the
production app's compatibility range. Set `minSdk` to a recent platform version (the latest one or two major
Android releases) instead of the project's real `minSdk`: a higher floor means you can skip legacy-compatibility
concerns later, like generating a full set of per-density launcher icons (see Step 8).

Set up the module as an Android application (not a library), including the dependencies it actually needs:

- The common UI module identified above (for the shared theme and components)
- The `domain` module (for the real domain exception and model types the fixtures use)
- The feature's `api` and `implementation` modules
- The base state-machine components, coroutine extensions, and the common child-flow data/compose/viewmodel
  modules used elsewhere in the project
- A logging library if the project uses one (Napier in the example) so fixtures can narrate what they're doing
- The project's DI framework and its Android/Compose/ViewModel integration artifacts (Koin in the example)

See [the example demo module's build file](assets/example/demo/build.gradle.kts) for a concrete dependency list.

## Step 3. Trace the domain use-cases the feature depends on
Look at the feature implementation's state-factory implementation (for example `AuthStateFactoryImpl`) and the
states/state-factories it wires together. Any constructor parameter whose type lives in the `domain` package
(not the feature's own `api`/`implementation` packages) is a domain use-case or domain-provided value that the
demo app must fake, because in a real app it would come from a `usecase` module implementation that the demo
doesn't depend on.

While you're looking at those same states, also check for a different kind of dependency: a constructor
parameter whose type is a `DataApi`/`UiApi` pair from a *different* feature's `api` module, together with a
`ProxyMachineState` inside this feature's own states. That means this feature hosts another Common Child Flow
module of its own - exactly how the example app module hosts `auth`. If you find one, it needs its own
decision, separate from the domain-use-case fixtures above:

- **Use the real implementation** - the simplest option when that other feature module already exists in the
  project and doesn't itself need faking. Add it as a dependency and wire its real `DataApi`/`UiApi`
  implementation the normal way, the same as any other implementation module. Ask the user which
  implementation to use if there's more than one, or it's not obvious from the project.
- **Create a minimal Data/UI API mock** - when the real implementation pulls in things you don't want in a
  demo (its own backend calls, its own heavy DI graph), or the user explicitly wants that nested feature faked
  too. Implement just enough of its `DataApi`/`UiApi` interfaces to satisfy the hosting proxy: a `DataApi.init()`
  that returns an ad-hoc `CommonMachineState` which completes immediately with a fixed result (similar to the
  `terminated` ad-hoc state in [the example `AuthStateFactoryImpl`](assets/example/implementation/state/AuthStateFactoryImpl.kt)),
  and a `UiApi.Screen` that renders a simple placeholder - a label is enough. This mock is deliberately much
  thinner than a real implementation module: it doesn't need its own states, gestures, or UI-states, just
  enough to hand the hosting proxy state a result so the flow can continue.

If the user didn't say which approach they want for a nested dependency like this, ask - it's exactly the kind
of decision that shouldn't be guessed silently.

## Step 4. Create fixtures for the domain use-cases
Follow [this guide](references/fixture-patterns.md) to fake every domain use-case identified in Step 3,
including any specific failure scenario the user described in the requirements-gathering step.

## Step 5. Create the Application class and the fixture DI wiring
First confirm which DI approach the project actually uses - don't assume Koin. Check the feature's own
implementation module (does it have `@Module`/`@Factory`-style annotations, a Hilt `@Module`/`@Binds` class, a
Dagger component, or just plain constructor calls somewhere?) and copy that same approach for wiring the
fixtures, so the demo looks like a natural, smaller version of how the real app would do it.

If the project uses Koin (as in the example), create an `Application` subclass that starts the DI container,
and a DI module that includes the feature's own implementation module plus the fixture bindings from Step 4.
See [the example `App.kt`](assets/example/demo/src/main/kotlin/com/motorro/commonstatemachine/examples/skills/auth/demo/App.kt):

```kotlin
@KoinApplication(modules = [AppModule::class])
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Napier.base(DebugAntilog())
        startKoin<App> {
            allowOverride(false)
            androidLogger()
            androidContext(this@App)
        }
    }
}

@Module(includes = [AuthModule::class])
@ComponentScan
class AppModule {
    // @Single fixture bindings from Step 4 go here
}
```

If the project uses Hilt/Dagger, create the equivalent `@Module`/`@InstallIn` (or component) that provides the
fixtures alongside the feature's own modules, following that project's existing module structure. If the
project wires dependencies by hand with no DI framework, skip a separate module entirely: construct the
fixtures directly where you construct the feature's `DataApi`/`UiApi` implementations (typically in the
`Application` class or a small factory object), and pass them straight into the feature's constructors.

## Step 6. Create the main Activity
Create a single `ComponentActivity` that sets Compose content wrapped in the common UI module's theme, injects
the feature's `UiApi` and a `CommonFlowViewModel`, and renders the flow with `CommonFlowComposition` inside a
`Scaffold` using the common UI module's app bar. See [the example `MainActivity.kt`](assets/example/demo/src/main/kotlin/com/motorro/commonstatemachine/examples/skills/auth/demo/MainActivity.kt) for the full pattern,
including wiring the Android back gesture through `navigationBackHandler` and finishing the activity when the flow's `finish` callback fires.

## Step 7. Create the main ViewModel
Create a trivial `CommonFlowViewModel` subclass that takes the feature's `DataApi` via constructor injection and
passes the right `init` value for the feature's `Input` type:

```kotlin
@KoinViewModel
class MainViewModel(api: AuthDataApi) : CommonFlowViewModel<AuthGesture, AuthUiState, AuthInput, AuthResult>(
  api = api,
  init = AuthInput(skippable = true)
)
```

If the `Input` type needs more than a default value to make sense for a demo (for example data that normally
comes from a parent flow), ask the user what to pass, or use a clearly-marked placeholder value.

## Step 8. Create the Android manifest and resources
Add a minimal `AndroidManifest.xml` declaring the single launcher activity, and the small resource set an
Android application module needs (`app_name` in `strings.xml`, a launcher icon, and the standard backup/data-
extraction/network-security XML files). Keep this minimal: it's an internal tool, not a production app icon.
With `minSdk` set to a recent version (Step 2), adaptive icons are guaranteed to be available, so a single
`mipmap-anydpi-v26/ic_launcher.xml` (plus its round variant) referencing a vector drawable is enough - don't
generate the legacy per-density PNG sets (`mipmap-hdpi`, `-mdpi`, `-xhdpi`, `-xxhdpi`, `-xxxhdpi`) that exist
only for backwards compatibility with older Android versions this demo doesn't need to support. Copy the
manifest/resource shape from another demo/app module in the project if one exists, adjusting the app name and
dropping anything that's there only for legacy-device support; otherwise create a minimal set from scratch.

## Step 9. Register the module (if the project needs it)
If the project registers Gradle modules explicitly (check `settings.gradle.kts`), add the new demo module there
following the existing naming convention.
