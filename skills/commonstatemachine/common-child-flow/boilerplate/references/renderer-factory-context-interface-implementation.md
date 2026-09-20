# Module implementation: factory interface, ui-renderer interface, context
Use this reference to create auxiliary interfaces and classes:

- state factory
- UI-state renderer
- Interstate context

Take a look at the [class diagram](common-child-flow.puml) to get the idea about components purpose.

## Step 1. CommonMachineState binding
The framework main building block is a `com.motorro.commonstatemachine.CommonMachineState` - it is a logical state of a state-machine running the flow.
The generic class should be bound with two parameters:

- [Gesture](data-and-ui-implementation.md#step-1-ui-gestures)
- [UI-state](data-and-ui-implementation.md#step-2-ui-states)

Create an internal type-alias and put it to the `state` sub-package.
Take a look at the [example](../assets/example/implementation/state/AuthState.kt).

## Step 2. State factory interface
Whenever it is a time to switch to another state, the current state does not create the new state directly - it delegates
the state creation to the state-factory:

- State-factory foundation is an interface with the methods like: `createLoading`, `createForm`, etc.
- Factory functions may pass some parameters: [interstate data](data-and-ui-implementation.md#step-3-interstate-data), errors, auxiliary data.
- Machine-states get the reference to the state factory and use it to create the new state.
- State-factory interface is internal in the module.

Your task here is to:

- Create a state-factory internal interface
- Put the interface `state` sub-package of the implementation module.
- Every factory function should return the `CommonMachineState` binding type created in the previous step.
- Create some startup function to build the initializing state. The method should include the [input](input-and-result-api.md#step-1-input) as a parameter.
- Create `terminated` method with the [result](input-and-result-api.md#step-2-result) as a parameter.

Take a look at the [example](../assets/example/implementation/state/AuthState.kt).

## Step 3. UI renderer interface
States are encouraged to use a separate renderer interface that translates the [interstate data](data-and-ui-implementation.md#step-3-interstate-data) 
to the [UI state](data-and-ui-implementation.md#step-2-ui-states).

Follow these common rules:
- Make the created interface internal.
- Put the interface into the `ui` sub-package of the implementation module.
- Create a function for every UI-state in a sealed system.
- Every function should return the sealed class ui-state type.
- Provide an [interstate data](data-and-ui-implementation.md#step-3-interstate-data) parameter if the result state is a data class.
- Provide more parameters if data is required to build the ui-state, and it could not be built using the data-state.
  Example: when creating some error view-state, add an error parameter. The calling state will provide it separately.

Take a look at the [example](../assets/example/implementation/ui/AuthUiRenderer.kt).

## Step 4. State context
To make the tools created above accessible, create an accessor interface - a context.
The state being created will receive that context in a constructor and use it to access factory, renderer, etc.

Follow these common rules:
- Make the created interface internal.
- Put the interface into the `state` sub-package of the implementation module.
- Add properties resolving to the following tools:
  - state factory
  - ui-renderer
  - flow host bound with the [result type](input-and-result-api.md#step-2-result) used to interact with the parent state.

Take a look at the [example](../assets/example/implementation/state/AuthContext.kt).

## Step 5. State factory implementation
Add an implementation class to implement the factory interface from the previous step.
The implementation will:

- Receive state-factories in constructor and use them to create the states with complex dependencies (see the `preloading` implementation in the [example](../assets/example/implementation/state/AuthStateFactoryImpl.kt)).
- Create simple states locally (see the `preloadError` implementation in the [example](../assets/example/implementation/state/AuthStateFactoryImpl.kt))
- For the even simpler states you may want to implement them ad-hoc (see the `terminated` implementation in the [example](../assets/example/implementation/state/AuthStateFactoryImpl.kt)).

Follow these common rules:

- Create the class in the implementation module.
- If required by the DI framework used in project, add required inject annotations.
- Inject external factories and dependencies required by the state to the factory constructor. 
  Try to use lazy provider injection to delay the initialization.
- Pass the flow host to the factory constructor.
- Build the private context object to pass to the child states.

See the [example](../assets/example/implementation/state/AuthStateFactoryImpl.kt).
