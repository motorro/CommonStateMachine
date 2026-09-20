# Module implementation: base state
Use this reference to implement a common base state for the flow.
Take a look at the [class diagram](common-child-flow.puml) to get the idea about components purpose.
Take a look at the [example](../assets/example/implementation/state/BaseAuthState.kt).

## Step 1. Create a base class
Create a base abstract class that would be used as a common ancestor for the most of the logical states.
Follow the following common rules:

- Make the class internal to the implementation module.
- The name should start with `Base` prefix.
- The class should be abstract
- The class should extend the `com.motorro.commonstatemachine.coroutines.CoroutineState`
- The class should bind the generic parameters for the [gesture](data-and-ui-implementation.md#step-1-ui-gestures) and [ui-state](data-and-ui-implementation.md#step-2-ui-states) types.
- The class should take the [context](renderer-factory-context-interface-implementation.md#step-4-state-context) parameter and implement the context by delegate.

## Step 2. Add convenience methods to change the machine and UI states
The `com.motorro.commonstatemachine.CommonMachineState` subclass is a part of a state-machine. 
It updates the outside world with the following methods:

- `setUiState` - updates the current UI state, thus the screen to the user.
- `setMachineState` - switches the logical state in a hosting state-machine.

To reduce the boilerplate, create two protected inline convenience methods:

- `setMachineState(block: AuthStateFactory.() -> AuthState)` - runs the block on the factory and changes the machine state.
- `setUiState(block: AuthUiRenderer.() -> AuthUiStateImpl)` - runs the renderer and updates the UI state

Take a look at the [example](../assets/example/implementation/state/BaseAuthState.kt) for the implementation.

## Step 3. Basic gesture processing
The `com.motorro.commonstatemachine.CommonMachineState` processes the user gestures in the `doProcess` method. 
The concrete state usually only processes those gestures he is interested in.
Example: the login form state is interested only in gestures that are relevant to login actions, it does not process 
gestures of the logout screen.
If the project uses the logging framework, add a simple `doProcess` override to the base class: if the state gets the
unexpected gesture - log a warning message using the logging framework.

Take a look at the [example](../assets/example/implementation/state/BaseAuthState.kt) for the implementation.
