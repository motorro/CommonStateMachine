# AI skills
Some AI skills for your convenience:

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

For example, continuing the `friendlist` module from the examples above, try demoing it on its own:

> Create a demo app for the `friendlist` module so I can show it to stakeholders.
> - Fake `GetFriendList` so it emits three or four friends after a short delay, then fails once with an I/O
    >   error on the next emission before recovering, so I can check the `list` state's retry path.
> - Reuse the `appcore` module's theme and app bar, the same way the feature's own views do.