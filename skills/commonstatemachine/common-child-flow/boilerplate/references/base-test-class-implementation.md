# Module implementation: base test class
Use this reference to get the idea snd create a base abstract uni-test class.
This class will handle common test-mocks and initialization for the most of machine-states unit-tests:

- state-machine mock
- factory mock
- renderer mock
- context mock

Most of the machine-state test will subclass this base class.

## Step 1. Base test class
Create an abstract internal class and call `BaseStateTest`.

## Step 2. Protected properties
Create the following protected properties to use in child test-classes:

- `stateMachine` - `com.motorro.commonstatemachine.CommonStateMachine` mock bound with [implementation gesture and ui-state types](data-and-ui-implementation.md)
- `stateFactory` - [machine state factory mock](renderer-factory-context-interface-implementation.md#step-2-state-factory-interface)
- `renderer` - [ui state renderer mock](renderer-factory-context-interface-implementation.md#step-4-ui-renderer-interface)
- `flowHost` - `com.motorro.commonstatemachine.flow.data.CommonFlowHost` mock bound with the return type
- `context` - [interstate context mock](renderer-factory-context-interface-implementation.md#step-4-state-context)
- `nextState` - a bound `com.motorro.commonstatemachine.CommonMachineState` type to use with the state-factory mock.
- `dispatcher` - a coroutine `TestDispatcher` that will be used in test functions.

## Step 3. Mock initialization
You will need to initialize all the mocks listed in the previous state and mock some of the functions there:

1. Create the `init` function and mark it with the annotation to run before the test run.
2. Initialize `dispatcher` with the `UnconfinedTestDispatcher` if not instructed otherwise.
3. Initialize coroutines `Main` dispatcher with the test dispatcher from the previous step.
4. Initialize the `stateMachine` with the relaxed mock.
5. Initialize the `stateFactory` with the strict mock.
6. Initialize the `flowHost` with the relaxed mock.
7. Initialize the `renderer` with the strict mock.
8. Initialize the `nextState` with the bound type alias.
9. Initialize the `context` with the object and override the properties with the mocks from the previous steps.
10. Create the empty open fun `doInit` and call it from the `init` function. Subclasses will override `doInit` 
    and initialize themselves with the mocks already created.
11. Create `deinit` function and call `Dispatchers.resetMain()` there.
12. Create the common coroutine test function wrapping `runTest` and using the test dispatcher from the second step.
    Subclasses will use this function to run the tests.

Take a look at the [example](../assets/example/implementation-test/state/BaseStateTest.kt) of such a class.
