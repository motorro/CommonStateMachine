# Module implementation: state tests
Use this reference to create tests for the [created machine states](state-implementation.md).
`CommonMachineState` tests ensure that state transitions, UI state mapping, and side effects are correctly handled.
Typical task would be the following:

1. If the [base-test class](base-test-class-implementation.md) is not already available at the same module, add it.
2. Create a test class and extend the base-test class.
3. Define the mocks needed for the test: use-cases, apis, etc. as the lateinit vars.
4. Override the `doInit` function and initialize the mocks.
5. Create test functions using the `test` function defined in the base class.

## Note: Mocking use-cases and suspend functions that should never complete
Some states call supplied use-cases to load some data and update the machine state accordingly.
Usually the use-case contain some suspend function to do that.
You may want this call to never end. For example - to check the `Back` gesture terminates the flow
while the user awaits for a long loading operation.
In those case use the empty `suspendCancellableCoroutine` to emulate the operation:
```kotlin
// Mock creation
private val requirementsMock: GetPasswordRequirements = mock()

// Suspending the load operation
everySuspend { requirementsMock.invoke() } calls { suspendCancellableCoroutine { } }
```
Example: the [test](../assets/example/implementation-tests/state/PreloadingErrorStateTest.kt) for the [PreloadingState](../assets/example/implementation/state/PreloadingState.kt):

- The state asynchronously loads some data with the use-case at startup.
- You need to check the `Back` pressed while loading terminates.

## Step 1. Testing the initial rendering
If the state updates the UI, check the UI is updated on state startup:

- If the UI-renderer is defined in the module - mock the corresponding method, check the calling arguments and a call to `CommonStateMachine::setUiState`.
- If there's no UI-renderer and the state updates the ui directly, evaluate the `CommonStateMachine::setUiState` argument directly.

Example: the [test](../assets/example/implementation-tests/state/PreloadingErrorStateTest.kt) for the [PreloadingState](../assets/example/implementation/state/PreloadingState.kt):

- The state asynchronously loads some data with the use-case at startup.
- You need to check the state displays `Loading`.

# Step 2. Testing the asynchronous outcomes
If the state uses any use-cases or asynchronous operations run at startup, check their outcome:

- UI updates if any.
- state changes if any.


Example: the [test](../assets/example/implementation-tests/state/PreloadingErrorStateTest.kt) for the [PreloadingState](../assets/example/implementation/state/PreloadingState.kt):

- The state asynchronously loads some data with the use-case at startup.
- You need to check the success: state updates the interstate data and proceeds.
- You need to check the failure: state switches to the error state.

## Step 3. Testing gestures
If the state processes any gestures in `doProcess` callback, check each gesture path:

- use [fixtures](test-fixtures-implementation.md) if there are any for gestures.
- check the UI changes if any.
- check interstate data changes if any.
- check machine state changes if any.

Example 1: the [test](../assets/example/implementation-tests/state/FormStateTest.kt) for the [FormState](../assets/example/implementation/state/FormState.kt):

- checks the gestures change the interstate data.
- checks the UI updates.
- checks the data validation and state transfer.

Example 2: the [test](../assets/example/implementation-tests/state/PreloadingErrorStateTest.kt) for the [PreloadingErrorState](../assets/example/implementation/state/PreloadingErrorState.kt):

- checks the `Back` gesture changes the machine state.
- checks `Action` gesture conditionally changes the machine-state depending on the error.
- checks the data validation and state transfer.
