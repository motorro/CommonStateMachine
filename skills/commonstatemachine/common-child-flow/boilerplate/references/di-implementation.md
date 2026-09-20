# Module implementation: API implementation and DI
Use this reference to:

- Create a [Data API](data-and-ui-api.md#step-1-data-flow) implementation.
- Create a [UI API](data-and-ui-api.md#step-2-ui-flow) implementation.
- If the project uses a DI framework to provide dependencies, implement DI modules.

## Step 1. Implement Data API
Create the Data API implementation:

- Create an internal class and implement the interface of the Data API. Depending on the DI/locator used, pass the state-factory implementation
  to constructor or get a reference to it using the DI framework used.
- Override the `init` function. Create the factory, bind the incoming flow-host, create starting state.
  Cast the resulting state to the correct API return type.
- Override the `getDefaultUiState` create a function to return the appropriate default [view-state](data-and-ui-implementation.md#step-2-ui-states):
  - Loading
  - An empty form
  - Any state you consider good as a default
- If the backwards [navigation gesture](data-and-ui-implementation.md#step-1-ui-gestures) is there, override the `getBackGesture` function and return it.

See the [example](../assets/example/implementation/AuthDataApiImpl.kt) for our sample authentication Data API.

## Step 2. Implement UI API
Create the UI API implementation:

- Create an internal class and implement the interface of the UI API.
- Override the `Screen` composable using the [component](ui-view-implementation.md#step-1-implement-scaffolding-screen) function. Cast the type of the 
  UI state to match the one in the UI API
  
See the [example](../assets/example/implementation/AuthUiApiImpl.kt) for our sample authentication UI API.

## Step 3. Define DI module(s) (optional).
Check the project setup or the user input. If the project uses DI to provide dependencies, implement the required modules.
Hints:

- Make sure all individual state factories are DI-enabled. See the example [StateFactory](../assets/example/implementation/state/PreloadingState.kt).
- Make sure the main state-factory is DI-enabled. See the example [AuthStateFactoryImpl](../assets/example/implementation/state/AuthStateFactoryImpl.kt)
- Make sure the UI-renderer is DI-enabled. See the example [AuthUiRendererImpl](../assets/example/implementation/ui/AuthUiRendererImpl.kt).
- Make sure all interfaces are bound to the respectful implementations.
- Make sure the API implementations from the previous chapters are bound.
- Pay attention to (if used with frameworks like Hilt):
  - Data API needs to be bound to the view-model module or similar.
  - UI API needs to be bound to the view module (Activity, Fragment, etc.)
- The resulting modules should be public for the consumer.
