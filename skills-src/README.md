# AI skills
A set of AI skills for working with [CommonStateMachine](..)'s Common Child Flow architecture: scaffolding a new
feature module, extending one that already exists, building a standalone demo app to show a feature off or test
it on its own, and setting up AI-driven UI tests for one. Point your AI assistant at the one you need and
describe what you want in plain language - each skill asks for anything it's missing.

Skills can be installed straight from this repository with the [`skills` CLI](https://github.com/vercel-labs/skills), without cloning the repo:

```bash
# List the skills available in this repository
npx skills add motorro/commonstatemachine --list

# Install a specific skill
npx skills add motorro/commonstatemachine --skill commonstatemachine-commonchildflow-boilerplate
```

## Assumed project architecture
These skills assume a particular shape for the surrounding project. None of them requires exactly this layout -
each skill checks the actual project before guessing anything project-specific - but this is the architecture the
examples below, and the skills' own vocabulary ("domain", "use-case", "DI framework"), refer to:

- A **domain module** (`domain` in the example) that declares the use-case interfaces, their input/output models,
  and the domain exception types - pure contracts, no implementation. Feature modules call these interfaces
  without depending on wherever their real implementations happen to live, which is what lets a demo app (see
  the demo skill below) fake them with fixtures instead. The interfaces don't have to live in one shared
  `domain` module specifically - a feature can just as well use interfaces declared in another feature's own
  `api` module - these skills only care that the interfaces exist and can be faked, not where they're declared.
- A **common UI module** (`appcore` in the example) with the shared design system - theme, typography, and common
  composables such as an app bar or a loading indicator - that every feature's views, and any demo app, reuse
  instead of rebuilding their own.
- One or more **Common Child Flow feature modules**, each split into an `api` module (its
  `Gesture`/`UiState`/`Input`/`Result`/`DataApi`/`UiApi` contracts) and one or more `implementation` modules (its
  states, state-factory, DI module and views) - see the
  [Common Child Flow architecture](../README.md#common-child-flow-api) for how these fit together.
- An **app module** - the composition root that starts the application, wires up the DI container, and hosts the
  feature flows via proxies.
- Some **DI framework** (Koin in the example, but Hilt, Dagger, or none at all are equally fine - every skill
  checks what the project actually uses rather than assuming Koin) that ties the use-case implementations, the
  feature implementations, and the app together.

A typical layout looks like this:

```
my-project
├── domain               # Use-case interfaces, domain models, domain exceptions - no implementation
├── appcore              # Shared UI: theme, design system, common composables
├── auth                 # One Common Child Flow feature module...
│  ├── api
│  └── implementation
├── friendlist            # ...and another
│  ├── api
│  └── implementation
└── app                   # Composition root: Application, MainActivity, DI wiring
```

For the complete, runnable version of this layout - the one every skill's examples below refer to - see
[`examples/skills`](../examples/skills).

## Create a Common Child Flow module boilerplate
[Module boilerplate skill](commonstatemachine/common-child-flow/boilerplate/SKILL.md): creates a set of API/implementation 
feature module boilerplate following the [Common Child Flow](../README.md#common-child-flow-api). 
  - Defines gesture/ui-states sealed structures
  - Binds data and UI APIs
  - Builds a basic tool set for the module implementation
  - Creates a test structure and mocks for the module implementation
  - Builds a DI container for the module implementation

For example, to try the skill with the [example project](../examples/skills), try creating a module to display a list
of friends as soon as the user is authenticated:

> Create a `friendlist` feature module that loads and displays a list of friends.
> - Use `com.motorro.commonstatemachine.skills.domain.friends.GetFriendList` as the data source, staying subscribed
>   to updates after the first load.
> - Show a loading state until the first emission, then the friend list.
> - On failure, check the error: offer a retry for an I/O error, otherwise terminate the flow.
> - Map errors to the domain at the usecase boundary with `toAppException` from
>   `com.motorro.commonstatemachine.skills.domain.exception`.
> - Logical states: `list` (with the subscription) and `error`.
> - Views: list, loading, error — built with the shared theme, dimensions and composables from the `appcore` module.
> - Wire up DI modules for the data and UI APIs, add state tests, and create proxies (gesture, UI-state, module flow)
>   to embed the flow in the main application module.

## Add a new state to an existing Common Child Flow module
[Add-state skill](commonstatemachine/common-child-flow/add-state/SKILL.md): adds one more logical state to a
feature-implementation module that already exists, without touching the module's overall boilerplate. A new
state doesn't have to be a new screen - it can just as easily be an invisible step that hands off to the next
one on its own.
  - Infers the new state's gesture, UI-state and interstate data from a screen design or a text description
  - Creates the state, its state-factory method, and its view
  - Creates a unit test for the new state
  - Wires up the transition from an existing state, whether it's triggered by a gesture or by that state's own
    work completing

For example, continuing the `friendlist` module from the example above, try adding a details screen:

> Add a `details` state to the `friendlist` module that shows a single friend's info.
> - Reached when the user clicks a friend in the list: the `list` state should handle that click and switch to
>   the new state, passing the clicked friend's id.
> - Show the friend's name and status; offer a back gesture to return to the list.

## Build a demo app for a Common Child Flow module
[Demo-app skill](commonstatemachine/common-child-flow/demo/SKILL.md): builds a small, internal-only app that
launches one existing feature module on its own, without the rest of the app around it - handy to demo a
feature to stakeholders before the real app exists, or to test it end-to-end by hand or with a UI-testing tool.
  - Fakes the feature's domain use-cases with fixtures, so it runs without a real backend
  - Implements any specific failure scenario the user describes (for example "fail the first call with an I/O
    error, then succeed") using the real domain exception types
  - Reuses the project's shared UI/theme module and whichever DI framework the project already uses, rather
    than assuming either
  - Wires up a minimal `Application`, `Activity` and `CommonFlowViewModel` to launch the flow standalone
  - Can build several variants of the same demo side by side - different fixture scenarios, different nested
    implementations - as Gradle product flavors of one module, so each installs as its own app on the device

### Simple demo: one feature, one scenario
For example, continuing the `friendlist` module from the examples above, try demoing it on its own:

> Create a demo app for the `friendlist` module so I can show it to stakeholders.
> - Fake `GetFriendList` so it emits three or four friends after a short delay, then fails once with an I/O
>   error on the next emission before recovering, so I can check the `list` state's retry path.
> - Reuse the `appcore` module's theme and app bar, the same way the feature's own views do.

### Multiple demos side by side: A-B testing and scenario comparisons
Say `friendlist` hosts a `frienddetails` child flow of its own (proxied the same way the example app hosts
`auth`), and you want to compare two things at once rather than build and rebuild a single demo by hand:

> Build two variants of the `friendlist` demo side by side so I can compare the `frienddetails` flow's two
> implementations: `friendDetailsV1` and `friendDetailsV2`.
> - Use Gradle product flavors of the same demo module instead of separate modules - name them after the
>   implementations they demo.
> - Keep `friendlist`'s own fixtures and the shared UI identical across both variants; only the nested
>   `frienddetails` dependency differs per flavor.
> - Give each flavor its own `applicationIdSuffix` and app name so both install side by side on the same
>   device at once.

The same approach works for comparing fixture behavior instead of implementations - for example a `happyPath`
variant where every use-case succeeds, next to a `connectionErrors` variant where the same use-cases fail once
with an I/O error before succeeding on retry, so you can demo both the normal flow and its error handling
without switching builds.

## Set up AI-driven UI tests for a Common Child Flow module
[Journeys skill](commonstatemachine/common-child-flow/journeys/SKILL.md): sets up an
[Android Journeys](https://developer.android.com/studio/preview/journeys) test suite for an existing feature
module - a small host app, fed by fixtures, plus natural-language `.journey.xml` files that Gemini executes and
verifies against it. Independent of the demo-app skill, though it follows the same overall shape.
  - Guesses the feature's real screens and possible actions from its gestures, UI states and composables, rather
    than inventing them
  - Proposes journey scenarios (happy path, validation cases, failure cases) for you to confirm before writing
    any files
  - Fakes the feature's domain use-cases with fixtures that match what a scenario actually needs - including
    fixtures that branch on a specific typed value, not just a call counter
  - Wires up the AGP test-suite Gradle block and a minimal host app, reusing the project's shared UI module and
    DI framework

For example, continuing the `friendlist` module from the examples above:

> Set up journey tests for the `friendlist` module.
> - Cover the happy path (friends load and display) and the retry path after an I/O error on the first load.
> - Suggest any other scenarios worth covering once you've looked at the module's states and gestures.