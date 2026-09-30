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