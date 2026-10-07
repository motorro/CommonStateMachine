---
name: commonstatemachine-commonchildflow-journeys
description: Use this skill to set up an Android Journeys (AI-driven, natural-language UI test) suite for an
  existing CommonStateMachine Common Child Flow feature module. Use it whenever the user asks to write UI tests,
  journey tests, or AI-driven tests for a feature module - for example "Write journey tests for the auth
  module", "Set up Journeys to test the friendlist flow end-to-end", or "Create Gemini-driven UI tests for the
  payment flow". It builds a small standalone host app fed by fixtures (so the suite runs without a backend),
  guesses the feature's actual screens and possible actions from its gestures, UI states and composables, and
  proposes journey scenarios for the user to confirm before writing the `.journey.xml` files themselves. This
  skill is independent of the demo-app skill - it isn't a prerequisite for it and doesn't build on it - but
  follows the same overall approach and reuses the same fixture-writing guide. If the feature module doesn't
  exist yet, use the boilerplate skill first - this skill only wires up a way to test one that's already there.
metadata:
  author: Motorro
  last-updated: '2026-10-06'
  keywords:
    - android
    - architecture
    - cross-platform
    - kotlin-multiplatform
    - mvi
    - state-machine
    - feature-flow
    - testing
    - ui-testing
    - journeys
    - gemini
    - ai-testing
---

## Description
Use this skill to set up an [Android Journeys](https://developer.android.com/studio/preview/journeys) test
suite for an existing Common Child Flow feature: a small host application module, fed by fixture (fake) domain
use-cases so it runs without a real backend, with natural-language `.journey.xml` files that describe user
flows for Gemini to execute and verify - no view-id locators or Espresso scripts to write or maintain.

This is a separate skill from the demo-app skill - not a prerequisite for it, and not built on top of it - but
it follows the same overall shape (a small host app with fixtures) and reuses the same fixture-writing guide, so
a project that has both skills will see a family resemblance between a `demo` module and a `journey` module.

For a deeper dive into the state machine mechanics and common components, refer to the [Core Architecture](references/ARCHITECTURE.md).
For a deeper dive into the common child flow philosophy and design, refer to the [Common Child Flow Architecture](references/COMMON-CHILD-FLOW.md).
For the Journeys framework's own file format, how to run a suite, and its one-time Gemini authentication setup,
refer to [Journey authoring and running](references/journey-authoring.md).

## Example
This instruction uses [example authentication module](assets/example), together with its domain module and a 
complete example journey module. All examples, schemes and code samples refer to this example; adjust names and packages to your actual need.

## Strategy
You will create one small Gradle application module that hosts an existing feature flow (an API module and one
of its implementations) on its own screen, fed by fixture domain use-cases, with an AGP test suite that runs
natural-language `.journey.xml` files against it:

```
my-project              # Project folder
├── auth                # Existing feature folder - module must already exist
│  ├── api
│  ├── implementation
│  └── journey          # New: the journey test host module
└── ...
```

Take a look at the [class diagram](references/common-child-flow.puml) for an overview of how the API,
implementation, and a hosting application relate to each other.

## Gathering the requirements for the journey module
Before creating the module, work out what you need from what the user gave you and from the project:

- The target feature: which existing Common Child Flow module (an `api` module and one of its
  `implementation` modules) is this suite for? It must already exist - if it doesn't, use the boilerplate skill
  first to create it, then come back here. See Step 1.
- The common UI module: look for an existing shared UI/theme Gradle module in the project (search for a
  top-level `Theme` composable wrapping `MaterialTheme`, and shared design components such as an app bar or a
  loading indicator - `appcore` in the example). Reuse its theme and components in the host app instead of
  inventing new ones. If the project has no such module, fall back to plain `MaterialTheme` and a `TopAppBar`.
- The feature's actual screens and possible actions: trace them from the feature's own gestures, UI states and
  composables rather than assuming - see Step 2.
- Journey scenarios: did the user already describe the scenarios they want tested (a happy path, a specific
  validation or failure case), or should you propose some from what Step 2 finds? Either way, confirm the final
  list with the user before writing any files - see Step 3.
- The domain use-cases and models the feature actually depends on: trace the feature implementation's
  constructor-injected dependencies back to the `domain` module interfaces they call (its state-factory and
  states are the place to look - see Step 5). These are exactly what you need to fake in Step 6.
- Nested feature dependencies: does the feature host another Common Child Flow module of its own via a proxy -
  the same way the example app module hosts `auth`? If so, find out whether the user wants the real
  implementation wired in, or a lightweight Data/UI API mock instead - see Step 5.
- Dependency injection: does the project use a DI framework (Koin, Hilt, Dagger, or similar), or is it wired
  by hand (plain factory functions/constructors)? Look at the feature's own implementation module and an
  existing app module to find out which, and wire the fixtures the same way - don't introduce a new DI
  framework just for this module. See Step 7.
- Did the user ask for anything beyond the happy-path screen (for example starting the flow already
  authenticated, or a specific `init` input)? Use it when constructing `MainViewModel` in Step 9.
- What should happen when the flow completes? Default to showing a short result screen with what the flow
  actually produced, the same way the demo skill's host app does - a journey step can then verify the result
  text directly instead of only checking that the app closed. See Step 8.

If the user gave you only part of this, use what you have and ask clarifying questions about anything still
ambiguous rather than guessing silently.

Follow the following common rules when writing code:

- Follow the project coding conventions or use Kotlin default
- Follow the project naming conventions or use Kotlin default
- If the project uses KDoc as a coding convention - add it to the created types, methods, properties
- Add TODO comments wherever you default a fixture to "just succeed" so the user knows it's a placeholder

## Step 1. Confirm the target feature and module structure
Identify the feature's `api` module (its `Gesture`, `UiState`, `Input`, `Result`, `DataApi`, `UiApi` types) and
the specific `implementation` module to test, if there are several. If the feature module doesn't exist yet,
stop here and use the boilerplate skill instead.

Create the new module directory next to the feature's existing `api`/`implementation` modules, following the
project's module-naming convention (`journey` in the example). Use the Gradle setup common to other application
modules in the project if one exists to copy from.

## Step 2. Guess the module's UI flow from its gestures, UI states, and composables
A journey step is matched against what Gemini actually sees on screen, so the journey text has to describe the
feature's real screens, fields, labels and conditions - not an abstraction of them. Build that picture before
writing anything, by looking at:

- The feature's *concrete* UI-state implementation, not the empty marker interface its `api` module declares
  (`AuthUiState` in the example is just `interface AuthUiState` - the real shape lives in `implementation`, as
  `AuthUiStateImpl.Form`/`.Loading`/`.Error` and similar). Each concrete subtype is one screen; its fields are
  what that screen actually shows.
- The feature's *concrete* gesture implementation, the same way (`AuthGestureImpl` rather than the bare
  `AuthGesture` marker) - each gesture is one thing the user can do, and which screen's state accepts it tells
  you which screen that action belongs to.
- The UI renderer's composables (for example `AuthFormScreen`, `AuthLoadingScreen`, `AuthErrorScreen`, wired
  together by `AuthUiRendererImpl`) for the actual button and field labels - read them from the feature's own
  string resources, not invented text - and for the conditions that show, hide, enable or disable an element (a
  Skip button only when `canSkip`, a Retry label instead of Close only when `canRetry`, a Login button disabled
  until `loginEnabled`).

Put this together into a short map: for each screen, what it shows, what can be done from it, and which gesture
leads to which next screen. Steps 3 and 11 draw their wording directly from this map.

## Step 3. Suggest and confirm journey scenarios
Using the screen map from Step 2, and the domain use-cases and fixture behavior from Steps 5-6, propose a short
list of scenarios rather than silently picking one - typically:

- the happy path all the way through to the flow's result screen
- one scenario per client-side condition the UI itself enforces (a field validation message, a button that
  stays disabled until input is valid)
- one scenario per domain-level failure a fixture can produce (an error screen, a retry path) - if the user
  described a specific scenario for Step 6, that's a scenario here too
- one scenario per branch of a nested feature dependency, if Step 5 found one with more than one outcome

If the user already gave you their own list of scenarios, use it instead of inventing one, but still sanity-check
each one against the screen map from Step 2 (a scenario that refers to a field or button that doesn't exist
can't be written faithfully). Confirm the final list with the user before Step 11 writes any files - this is a
design decision, not something to guess silently.

## Step 4. Create the journey module's Gradle file
This is an internal testing tool, not something that ships to end users - keep it minimal rather than matching
the production app's compatibility range. Set `minSdk` to a recent platform version (the latest one or two major
Android releases) instead of the project's real `minSdk`, the same reasoning as the demo skill applies here too.

Set up the module as an Android application (not a library), including the dependencies it actually needs:

- The common UI module identified above (for the shared theme and components)
- The `domain` module (for the real domain exception and model types the fixtures use)
- The feature's `api` and `implementation` modules
- The base state-machine components, coroutine extensions, and the common child-flow data/compose/viewmodel
  modules used elsewhere in the project
- A logging library if the project uses one (Napier in the example) so fixtures can narrate what they're doing
- The project's DI framework and its Android/Compose/ViewModel integration artifacts (Koin in the example)

See [the example journey module's build file](assets/example/journey/build.gradle.kts) for a concrete
dependency list.

Then add the AGP test-suite block that actually runs the journeys:

```kotlin
android {
    testOptions {
        suites {
            create("journeys") {
                targets {
                    create("default") { }
                }
                useJunitEngine {
                    inputs += listOf(com.android.build.api.dsl.AgpTestSuiteInputParameters.TESTED_APKS)
                    includeEngines += listOf("journeys-test-engine")
                    enginesDependencies(libs.junit.platform.launcher)
                    enginesDependencies(libs.junit.platform.engine)
                    enginesDependencies(libs.journeys.junit.engine)
                }
                targetVariants += listOf("debug")
            }
        }
    }
}
```

The suite's name (`journeys` here) doubles as the convention folder name AGP scans for `.journey.xml` files
(`src/journeys/`) - keep them matching, or rename both together. `targetVariants` points the suite at the plain
`debug` build, since this module has no product flavors (see Step 1) - there's only one variant to target.

Check the project's version catalog (`gradle/libs.versions.toml`) for `journeys-junit-engine`,
`junit-platform-launcher`, and `junit-platform-engine` aliases, and add them if the project doesn't have them
yet (`com.android.tools.journeys:journeys-junit-engine`, `org.junit.platform:junit-platform-launcher`,
`org.junit.platform:junit-platform-engine`).

## Step 5. Trace the domain use-cases the feature depends on
Look at the feature implementation's state-factory implementation (for example `AuthStateFactoryImpl`) and the
states/state-factories it wires together. Any constructor parameter whose type lives in the `domain` package
(not the feature's own `api`/`implementation` packages) is a domain use-case or domain-provided value that this
module must fake, because in a real app it would come from a `usecase` module implementation that this module
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
- **Create a minimal Data/UI API mock** - when the real implementation pulls in things you don't want in a test
  host (its own backend calls, its own heavy DI graph), or the user explicitly wants that nested feature faked
  too. Implement just enough of its `DataApi`/`UiApi` interfaces to satisfy the hosting proxy: a `DataApi.init()`
  that returns an ad-hoc `CommonMachineState` which completes immediately with a fixed result (similar to the
  `terminated` ad-hoc state in [the example `AuthStateFactoryImpl`](assets/example/implementation/state/AuthStateFactoryImpl.kt)),
  and a `UiApi.Screen` that renders a simple placeholder - a label is enough.

If the user didn't say which approach they want for a nested dependency like this, ask - it's exactly the kind
of decision that shouldn't be guessed silently.

## Step 6. Create fixtures for the domain use-cases
Follow [this guide](references/fixture-patterns.md) to fake every domain use-case identified in Step 5.

A journey's fixtures need to produce the exact outcomes its confirmed scenarios (Step 3) check for - and 
a scenario that depends on *what was entered* (a specific username and password, say) needs a fixture that 
actually branches on its input and matches against a known, fixed value, rather than one that always takes 
the same path regardless of what's passed in (see the guide's "Matching a specific input" section). Keep 
those expected values as named constants so the journey files from Step 11 can refer to the exact same values.

## Step 7. Create the Application class and the fixture DI wiring
First confirm which DI approach the project actually uses - don't assume Koin. Check the feature's own
implementation module (does it have `@Module`/`@Factory`-style annotations, a Hilt `@Module`/`@Binds` class, a
Dagger component, or just plain constructor calls somewhere?) and copy that same approach for wiring the
fixtures, so this module looks like a natural, smaller version of how the real app would do it.

If the project uses Koin (as in the example), create an `Application` subclass that starts the DI container,
and a DI module that includes the feature's own implementation module plus the fixture bindings from Step 6.
See [the example `App.kt`](assets/example/journey/src/main/kotlin/com/motorro/commonstatemachine/examples/skills/auth/journey/App.kt)
and [`AppModule.kt`](assets/example/journey/src/main/kotlin/com/motorro/commonstatemachine/examples/skills/auth/journey/AppModule.kt):

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
    // @Single fixture bindings from Step 6 go here
}
```

If the project uses Hilt/Dagger, create the equivalent `@Module`/`@InstallIn` (or component) that provides the
fixtures alongside the feature's own modules, following that project's existing module structure. If the
project wires dependencies by hand with no DI framework, skip a separate module entirely: construct the
fixtures directly where you construct the feature's `DataApi`/`UiApi` implementations (typically in the
`Application` class or a small factory object), and pass them straight into the feature's constructors.

This module never needs the flavor-scoped source-set split the demo skill uses for comparing several variants
(see Step 1) - there's only ever one `AppModule`, in `src/main`.

## Step 8. Create the main Activity
Create a single `ComponentActivity` that sets Compose content wrapped in the common UI module's theme, injects
the feature's `UiApi` and a `CommonFlowViewModel`, and renders the flow with `CommonFlowComposition` inside a
`Scaffold` using the common UI module's app bar, wiring the Android back gesture through `navigationBackHandler`.

Rather than closing the activity the moment the flow's `finish` callback fires, capture the `Result` value it
hands you in a bit of remembered state and swap to a small result screen that shows it, with a button that exits
from there - the same pattern the demo skill uses. This matters even more here than in a demo: it gives a
journey something concrete to verify (the result screen's actual text) instead of only being able to check that
the app closed:

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val (result, setResult) = remember { mutableStateOf<AuthResult?>(null) }

            SkillsTheme {
                Scaffold(
                    topBar = { SkillsAppBar(title = stringResource(R.string.app_name), topLevel = true) }
                ) { paddingValues ->
                    if (null == result) {
                        MainScreen(modifier = Modifier.padding(paddingValues), onComplete = setResult)
                    } else {
                        ResultScreen(result) { finish() }
                    }
                }
            }
        }
    }
}

@Composable
private fun MainScreen(modifier: Modifier = Modifier, onComplete: (AuthResult) -> Unit) {
    val viewModel: MainViewModel = koinViewModel()
    val uiApi: AuthUiApi = koinInject()

    CommonFlowComposition(
        viewModel = viewModel,
        navigationBackHandler = { enabled, onBack -> BackHandler(enabled, onBack) },
        content = { state, onGesture -> uiApi.Screen(state = state, onGesture = onGesture, modifier = modifier) },
        finish = { onComplete(it) }
    )
}

@Composable
private fun ResultScreen(result: AuthResult, onComplete: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Dimensions.medium, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            style = MaterialTheme.typography.bodyLarge,
            text = stringResource(R.string.result_authenticated, result.authenticated)
        )
        Button(onClick = onComplete) {
            Text(text = stringResource(R.string.btn_exit))
        }
    }
}
```

See [the example `MainActivity.kt`](assets/example/journey/src/main/kotlin/com/motorro/commonstatemachine/examples/skills/auth/journey/MainActivity.kt)
for the full file. Write the result screen's text from the feature's actual `Result` type, and phrase a
"verify the result" journey action (Step 11) against that same text. As with the demo skill, putting this text
in `strings.xml` is just the example's own style, not a rule - a hardcoded string works just as well for an
internal test host.

## Step 9. Create the main ViewModel
Create a trivial `CommonFlowViewModel` subclass that takes the feature's `DataApi` via constructor injection and
passes the right `init` value for the feature's `Input` type:

```kotlin
@KoinViewModel
class MainViewModel(api: AuthDataApi) : CommonFlowViewModel<AuthGesture, AuthUiState, AuthInput, AuthResult>(
  api = api,
  init = AuthInput(skippable = true)
)
```

If the `Input` type needs more than a default value to make sense for testing (for example data that normally
comes from a parent flow), ask the user what to pass, or use a clearly-marked placeholder value.

## Step 10. Create the Android manifest and resources
Add a minimal `AndroidManifest.xml` declaring the single launcher activity, and the small resource set an
Android application module needs (`app_name` in `strings.xml`, a launcher icon, and the standard backup/data-
extraction/network-security XML files). Keep this minimal: it's an internal test host, not a production app
icon. With `minSdk` set to a recent version (Step 4), adaptive icons are guaranteed to be available, so a
single `mipmap-anydpi-v26/ic_launcher.xml` (plus its round variant) referencing a vector drawable is enough -
don't generate the legacy per-density PNG sets (`mipmap-hdpi`, `-mdpi`, `-xhdpi`, `-xxhdpi`, `-xxxhdpi`) that
exist only for backwards compatibility with older Android versions this module doesn't need to support. Copy
the manifest/resource shape from another demo/app module in the project if one exists, adjusting the app name
and dropping anything that's there only for legacy-device support; otherwise create a minimal set from scratch.

## Step 11. Create the journey files
Write one `.journey.xml` file per scenario confirmed in Step 3, under `src/journeys/` (matching the suite name
from Step 4), each with a `name`, a `description`, and an ordered list of `action` steps written as plain,
present-tense sentences - what to wait for, what to type or tap, what to verify - using the exact field labels,
button text and conditions found in Step 2, never internal identifiers:

```xml
<?xml version="1.0" encoding="utf-8"?>
<journey name="Auth: Happy authentication">
    <description xml:space="preserve">Test authentication happy path with valid credentials</description>
    <actions xml:space="preserve">
        <action>Wait for the authentication form to open.</action>
        <action>Type "user" into the username field and "password" into the password field.</action>
        <action>Tap the Login button to submit the form.</action>
        <action>Verify that authentication succeeds and the screen displays "authenticated: true".</action>
    </actions>
</journey>
```

Keep each file to one scenario - a happy path and a validation-error case are two files, not one file with
branches - so a failing run points at one specific thing that broke. See
[the example journey files](assets/example/journey/src/journeys) for the scenarios this walkthrough produces,
and [Journey authoring and running](references/journey-authoring.md) for the full format reference, how to run
a suite locally, and the one-time Gemini authentication setup.

## Step 12. Register the module (if the project needs it)
If the project registers Gradle modules explicitly (check `settings.gradle.kts`), add the new journey module
there following the existing naming convention.
