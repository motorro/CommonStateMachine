# CommonStateMachine [![Check](https://github.com/motorro/CommonStateMachine/actions/workflows/check.yml/badge.svg?branch=master)](https://github.com/motorro/CommonStateMachine/actions/workflows/check.yml) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.motorro.commonstatemachine/commonstatemachine/badge.png)](https://repo1.maven.org/maven2/com/motorro/commonstatemachine/commonstatemachine/)

## Introduction

An MVI pattern of architecting modern applications has been getting more and more popular in the 
recent time. There are a lot of articles describing the pattern but to recap let's see the key points
that make up the approach:

- **Model** - A model holds the representation of the data state and changes it with reducing logic.
  Data changes are propagated to the view layer as a stream of complete view-states.
- **View** - The view layer observes user actions and view-state changes from the model. As a result,  
  it sets the intention for the triggered UI gesture passing it to the model to process.
- **Intent** - A representation of user's gestures that changes the state of the model. View handles
  widget interactions and provides a stream of gestures to the model through the unified interface.
  
Key advantages of MVI:

- Single source of truth - one set of data and logic to define complete view-state
- Unidirectional data flow
- Thorough and complete testing of all logic with unit tests

However (as with any technology) there are some downsides that you come across along with your app growth:
- Too much of overkill for simple functions like LCE (Load/Content/Error) display
- Too much reducer logic based on if/else of the current data state which plays badly in complex 
  multi-step scenarios.
- Quite a learning curve to grasp the technology

The simple pattern presented by this project aims to overcome the above drawbacks and to give you 
more freedom to choose technology and to build a cohesive logic and data processing in a 
"less-opinionated" way.

Key features:
- [Well-known approach](https://en.wikipedia.org/wiki/Finite-state_machine) - nothing new :)
- **No restriction** on your coding approach, technology stack and style
- A great way to isolate and test your logic - [low coupling and high cohesion](https://www.geeksforgeeks.org/software-engineering-coupling-and-cohesion/)
- Should work well in kotlin-multiplatform projects
- Easy to integrate with multi-module architecture
- Designed for [Jetpack Compose](https://developer.android.com/jetpack/compose) but it is not a restriction
- May (if you like to) work as a navigation library
- Explicit `Back` gesture management with the total control of yours

## Dependencies

The project has a very simple core to implement yourself but you could also grab the latest core
version like that:

```groovy
dependencies {
    // Base state-machine components
    implementation "com.motorro.commonstatemachine:commonstatemachine:x.x.x"
    // Coroutine extensions (optional)
    implementation "com.motorro.commonstatemachine:coroutines:x.x.x"
}
```

Multiplatform:

```kotlin
val commonMain by getting {
    dependencies {
      // Base state-machine components
      implementation("com.motorro.commonstatemachine:commonstatemachine:x.x.x")
      // Coroutine extensions (optional)
      implementation("com.motorro.commonstatemachine:coroutines:x.x.x")
    }
}
```

## Examples

- [LCE](lce) - basic example of Load-Content-Error application
- [Welcome](welcome) - multi-module example of user on-boarding flow

## The basic task - Load-Content-Error

Let's start with a basic example. Imagine we need to implement the classic master-detail view of
items with the following screen flow:
![LCE flow](doc/screenshots/flow.png)

Let's break down business requirements...

We have four application _logical_ states which correspond to _screen_ states for this application:

- Item list - the list of items to load is displayed. User clicks an item to load it's contents.
- Loading item - the network operation is running. User waits for operation to complete.
- Item content - the loaded item content is displayed. User may return back to item list.
- Item load error - the load operation has failed and we have a choice to retry load or to quit the 
  application.  

### States and transitions

The state diagram with the corresponding transitions will be the following:
![LCE state diagram](http://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/motorro/CommonStateMachine/master/doc/lceState.puml)

The diagram above, as you can see, has two types of `inter-state` transitions:

- ![#f03c15](https://via.placeholder.com/15/f03c15/f03c15.png) Red - user Intentions: clicks, 
  swipes and other interactive `Gestures` that are originated by application user.
- ![#1589F0](https://via.placeholder.com/15/1589F0/1589F0.png) Blue - transitions made by application
  logic: content display, errors, etc.

Each logical state may transition to another logical state as a result of `Gesture` or state's internal
logic.

Let's take a look at which `Gestures` each logical state processes and how they transition 
logical states:


| Logical state        | Ui-State  | Gesture/Event | Next state | Output                           |
| -------------------- | --------- | ------------- | ---------- | -------------------------------- |
| ItemList             | ItemList  | Back          | Terminated | Finishes activity                |
|                      |           | ItemClicked   | Loading    | Loads requested item             |
| Loading              | Loading   | Back          | Item list  | Cancels load and returns to list |
|                      |           | onContent     | Content    | Displays loaded item             | 
|                      |           | onError       | Error      | Displays load error              |
| Content              | Item      | Back          | Item list  | Returns to the item list         |
| Error                | Error     | Back          | Item list  | Returns to the item list         |
|                      |           | Retry         | Loading    | Retries load operation           |
|                      |           | Exit          | Terminated | Finishes activity                |

Each logical state should be able to:

- Update `UiState`
- Process some **relevant** user interactions - `Gestures` while ignoring irrelevant
- Hold some internal data state
- Transition to another logical state passing some of the shared data between

Logical state may be implemented as a self-contained controller having an input, output and 
internal data and a set of rules to process gestures, to reduce data and to pass it to the next 
state when logic falls behind what's relevant for this state.

### State machine

First of all we need some kind of a bridge between the current logical state and the outside world.
The [state machine](commonstatemachine/src/commonMain/kotlin/com/motorro/commonstatemachine/CommonStateMachine.kt)
should be able to:

- Hold the active logical state
- Transition between states
- Delegate gesture processing to the current state
- Propagate UI-state changes to the outside world
- Clean-up all resources on shutdown

![State machine](http://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/motorro/CommonStateMachine/master/doc/StateMachine.puml)

Methods:

- `process(gesture: G)` - Called by view upon user action. Delegated to current state.
- `clear()` - Called by view/framework to cleanup resources. Like in `onCleared` of `ViewModel`.
- `setMachineState(machineState: CommonMachineState<G, U>)`- Called by active state to transition 
  to the new one.
- `setUiState(uiState: U)` - Called by active state to update view.

The concrete state machine implementation provides a way to update the view with a new UI state.
For example the [FlowStateMachine](coroutines/src/commonMain/kotlin/com/motorro/commonstatemachine/coroutines/FlowStateMachine.kt)
exports UI state changes through `uiState` shared flow:

```kotlin
open class FlowStateMachine<G: Any, U: Any>(init: () -> CommonMachineState<G, U>) : CommonStateMachine.Base<G, U>(init) {
  private val mediator = MutableSharedFlow<U>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

  /**
   * UI state
   */
  val uiState: SharedFlow<U> = mediator

  final override fun setUiState(uiState: U) {
    mediator.tryEmit(uiState)
  }
}
```

### State

The base state class has three interaction methods:

- `doProcess(gesture: G)` - Called by the state-machine to process gesture.
- `setUiState(uiState: U)` - Call from within your state implementation to update UI State.
- `setMachineState(machineState: CommonMachineState<G, U>)` - Call from implementation to transition
  to the new state

and two lifecycle methods:

- `doStart()` - Called by the state-machine when your state becomes active. 
- `doClear()` - Called by the state machine when your current state is about to be destroyed either
  by replacing by the new state or when state-machine is about to be destroyed.

The state lives between `doStart` and `doClear` calls. You could safely call interaction methods
and expect gesture processing calls within that period. Make sure to cleanup all your pending
operations in `doClear` handler. For example, the [CoroutineState](coroutines/src/commonMain/kotlin/com/motorro/commonstatemachine/coroutines/CoroutineState.kt)
provides you the `stateScope` coroutine scope that is being cancelled in `doClear`:

```kotlin
abstract class CoroutineState<G: Any, U: Any>: CommonMachineState<G, U>() {
  protected val stateScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

  override fun doClear() {
    stateScope.cancel()
  }
}
```

### Implementation

Let's implement our application now. For this simple example we will skip some handy abstractions 
like `state-factory`, `context` and `renderer` that help you to build your states and separate 
concerns. Will talk about it later.

#### Item list state

[ItemListState](lce/src/main/java/com/motorro/statemachine/lce/model/state/ItemListState.kt) is a 
starting state for our application. It displays the list of items to load. The list is hardcoded for
this example so we just emit a complete view-state when started:

```kotlin
private val items = listOf(
  ItemId.LOADS_CONTENT to "Item that loads",
  ItemId.FAILS_WITH_ERROR to "Item that fails to load"
)

override fun doStart() {
    setUiState(LceUiState.ItemList(items.map { ItemModel(it.first, it.second) }))
}
```

Handle relevant gestures by transitioning the state-machine to the newly created states.
The `LoadingState` constructor accepts an id of item to load as an inter-state common data.

```kotlin
override fun doProcess(gesture: LceGesture) = when(gesture) {
    is LceGesture.ItemClicked -> onItemClicked(gesture.id)
    is LceGesture.Back -> onBack()
    else -> super.doProcess(gesture)
}

private fun onItemClicked(id: ItemId) {
    setMachineState(LoadingState(id))
}

private fun onBack() {
    setMachineState(TerminatedState())
}
```

#### Item loading state

[LoadingState](lce/src/main/java/com/motorro/statemachine/lce/model/state/LoadingState.kt) emulates 
an asynchronous operation:

```kotlin
override fun doStart() {
    setUiState(LceUiState.Loading)
    load()
}

private fun load() {
    stateScope.launch(Dispatchers.Default) {
        // Run a request in a state-bound scope
        // ...
        withContext(Dispatchers.Main) {
            // Handle result 
        }
    }
}
```

Depending on the item we pass the state transitions to either a `ContentState` or an `ErrorState`
passing either the mock content or the error occurred as an inter-state data:

```kotlin
private fun toContent() {
    setMachineState(ContentState("Some item data..."))
}

private fun toError() {
    setMachineState(ErrorState(id, IOException("Failed to load item")))
}
```

#### Item contents state

[ContentState](lce/src/main/java/com/motorro/statemachine/lce/model/state/ContentState.kt) is very
simple. It just sets the UI state to display data passed to the constructor and handles a `Back` 
gesture to return to the item list:

```kotlin
override fun doStart() { 
    setUiState(LceUiState.Item(contents))
}

override fun doProcess(gesture: LceGesture) = when (gesture) {
  LceGesture.Back -> onBack()
  else -> super.doProcess(gesture)
}

private fun onBack() {
  setMachineState(ItemListState())
}
```

#### Error state
The [ErrorState](lce/src/main/java/com/motorro/statemachine/lce/model/state/ErrorState.kt) gives a
user the ability to retry item load or to exit the app. Also it handles `Back` gesture to return to
the item list. Handling all user interactions through your state machine gives you a precise control
on what happens next. The item ID passed in the constructor as an inter-state data makes it possible
to preserve user's selection and to restart loading from scratch.

```kotlin
// Inter-state data:
// - failed - an ID of item failed to load
// - error - the error to display
class ErrorState(private val failed: ItemId, private val error: Throwable) : LceLogicalState() {

  override fun doStart() {
    // Display error...   
    setUiState(LceUiState.Error(error))
  }

  override fun doProcess(gesture: LceGesture) = when (gesture) {
    LceGesture.Back -> onBack()
    LceGesture.Retry -> onRetry()
    LceGesture.Exit -> onExit()
    else -> super.doProcess(gesture)
  }

  private fun onRetry() {
    // Pass the failed item ID to the loading state to initialize it
    setMachineState(LoadingState(failed))
  }

  private fun onBack() {
    setMachineState(ItemListState())
  }

  private fun onExit() {
    setMachineState(TerminatedState())
  }
}
```

#### Wiring with the application

Now that we have all states in place let's connect them together with a state machine. We need some
place to retain a machine through the application flow so let's wrap it to the [Jetpack ViewModel](lce/src/main/java/com/motorro/statemachine/lce/model/LceViewModel.kt) 
which is common now:

```kotlin
class LceViewModel : ViewModel() {
    /**
     * Creates initial state for state-machine
     * You could process a deep-link here or restore from a saved state
     */
    private fun initStateMachine(): CommonMachineState<LceGesture, LceUiState> = ItemListState()

    /**
     * State-machine instance
     */
    private val stateMachine = FlowStateMachine(::initStateMachine)

    /**
     * UI State to export
     */
    val state: SharedFlow<LceUiState> = stateMachine.uiState

    /**
     * Delegates gesture processing to the state-machine and the active state
     */
    fun process(gesture: LceGesture) {
        stateMachine.process(gesture)
    }

    /**
     * Runs when ViewModel is about to be destroyed
     */
    override fun onCleared() {
        stateMachine.clear()
    }
}
```

All we need to do here is:

- to create a state-machine instance
- to figure out the initial state that machine will start from
- to wire ui-state and gesture processing with the outside world

And here is an abstract of the [view](lce/src/main/java/com/motorro/statemachine/lce/ui/LceScreen.kt) 
that interacts with the model:

```kotlin
@Composable
fun LceScreen(onExit: @Composable () -> Unit) {
  val model: LceViewModel = viewModel()
  val state = model.state.collectAsState(LceUiState.Loading)

  // Process back gestures with a model
  BackHandler(onBack = { model.process(Back) })

  when (val uiState = state.value) {
    LceUiState.Loading -> Loading()
    
    is LceUiState.ItemList -> ItemList(
      state = uiState,
      onItemClicked = { model.process(ItemClicked(it)) }
    )
    
    is LceUiState.Error -> LoadError(
      state = uiState,
      onRetry = { model.process(Retry) },
      onBack = { model.process(Back) },
      onExit = { model.process(Exit) }
    )
    
    is LceUiState.Item -> ItemDetails(state = uiState)
    
    LceUiState.Terminated -> onExit()
  }
}
```

Compose library plays greatly here but you could easily adapt a fragment transaction or a 
recycler view architecture as well.

### Result

As you can see the state-machine pattern may be a good choice in implementing your MVI architecture.
It produces a clean and easy to grasp step-by-step logic with well-separated concerns and easy and
[thorough](lce/src/test/java/com/motorro/statemachine/lce/model/state) testing. The pattern also 
attempts to be as non-opinionated as possible. Each state is a black-box with a defined contract and
developers may choose the most suitable tools to implement each one without affecting the other. The
example above is a very basic one. However you could do things a bit more clean by using some of the 
additional abstractions (see below).

## Handy abstractions to mix-in

In the basic example above all the work was done by the state objects. They did:

- running a "network operation"
- view-state data rendering
- next state creation

That is a quite a huge responsibility which might not be so good in terms of coupling and testing.
So let's introduce some abstractions that will lift the burden off the state's shoulders.

### Use-cases

By use-case I assume any business logic external to your view logic implemented in a state. Be it 
some network operation or some other "use-case" - provide it to your state and use them as you like.
There is nothing new here - I'm sure you already use the approach in your flavour of 
Clean Architecture or similar. Example of using an external use-case could be found in 
[welcome example](login/src/main/java/com/motorro/statemachine/login/model/state/CredentialsCheckState.kt):

```kotlin
class CredentialsCheckState(private val checkCredentials: CheckCredentials) {

    // State logic
    
    override fun doStart() {
        stateScope.launch {
            // Runs use-case 
            val valid = checkCredentials()
        }
    }
}
```

### View-state renderer

Preparing the complex view-state from your state data might be a non-trivial task in applications
with complex interface. Moving a coupling to the view-state and data structures from your state logic
might be a good idea. Testing the exact view-state creation would be much easier if you make it 
as more or less a clean function. Also your logic states may share the same rendering logic so 
externalizing it would play greatly in terms of code reuse. For example the same view-state 
rendering is used by [PasswordEntryState](login/src/main/java/com/motorro/statemachine/login/model/state/PasswordEntryState.kt)
and [ErrorState](login/src/main/java/com/motorro/statemachine/login/model/state/ErrorState.kt) of 
welcome example. You could inject your renderer in a state factory or get it from common context
(see below).

### State factories and dependency provision

Creating new states explicitly to pass them to the state-machine later (like in the basic example)
is not a good idea in terms of coupling and dependency provision. 

The machine state, when created, may require three main classes of dependencies:

- State-specific dependencies like use-cases state operates.
- Inter-state data e.g. data loaded in a previous state, common data state, etc.
- Common dependencies for all states in machine: renderers, resource providers, factories

You are free to choose the way to provide dependencies however let's take a look at the approach
that I came to while using the state-machine pattern.

#### State-specific dependencies

To provide dependencies that are specific to each particular state I go with dedicated state 
factories that are injected with your DI framework. Let's take an [example above](#use-cases) and
extend it with a state-factory:

```kotlin
class CredentialsCheckState(private val checkCredentials: CheckCredentials) {

     // State logic

    /**
     * Dedicated state factory
     */
    @LoginScope
    class Factory @Inject constructor(private val checkCredentials: CheckCredentials) {
        operator fun invoke(): LoginState = CredentialsCheckState(
            checkCredentials
        )
    }
}
```

#### Inter-state data

By inter-state data I assume any dynamic data that is passed between states. It may be a product of
some calculation, user-generated data, etc. The most obvious way is providing it through the state 
constructor:

```kotlin
class CredentialsCheckState(
    private data: LoginDataState,
    private val checkCredentials: CheckCredentials
) {

    /**
     * Should have valid email at this point
     */
    private val email = requireNotNull(data.commonData.email) {
        "Email is not provided"
    }

    /**
     * Should have valid password at this point
     */
    private val password = requireNotNull(data.password) {
        "Password is not provided"
    }
}
```

#### Dependencies common to all states of a state-machine

Common dependencies may include renderers, state factories, common external interfaces and anything
else that is required by all states that make up the state-machine. For convenience and to save the
number of constructor params I suggest to bind them to some common interface and provide it as a 
whole. Let's name it a common [Context](login/src/main/java/com/motorro/statemachine/login/model/state/LoginContext.kt):

```kotlin
interface LoginContext {
    /**
     * Common state factory (see below)
     */
    val factory: LoginStateFactory

    /**
     * External interface
     */
    val host: WelcomeFeatureHost

    /**
     * UI-state renderer
     */
    val renderer: LoginRenderer
}
```

Than you could provide it to your state through the constructor parameters. To make things even
easier let's make some [base state](login/src/main/java/com/motorro/statemachine/login/model/state/LoginState.kt)
for the state-machine assembly and use a delegation to provide each context dependency:

```kotlin
abstract class LoginState(
    context: LoginContext
): CoroutineState<LoginGesture, LoginUiState>(), LoginContext by context {

    override fun doProcess(gesture: LoginGesture) {
        Timber.w("Unsupported gesture: %s", gesture)
    }
}
```

Thus every sub-class of the `LoginState` has any context dependency at hand by getting it from the 
corresponding property as if the were provided explicitly:

```kotlin
class CredentialsCheckState(
    context: LoginContext,
    private val data: LoginDataState,
    private val checkCredentials: CheckCredentials
) : LoginState(context) {

    override fun doStart() {
        // Use a context-provided dependency
        setUiState(renderer.renderLoading(data))
    }

    /**
     * Factory updated to pass common context 
     */
    @LoginScope
    class Factory @Inject constructor(private val checkCredentials: CheckCredentials) {
        operator fun invoke(
            context: LoginContext,
            data: LoginDataState
        ): LoginState = CredentialsCheckState(
            context,
            data,
            checkCredentials
        )
    }
}
```

#### Common state factory

As I've already mentioned, creating new states explicitly to pass them to the state-machine later 
(like in the basic example) is not a good idea in terms of coupling and dependency provision.

Let's move it away from our machine states by introducing a common [factory interface](login/src/main/java/com/motorro/statemachine/login/model/state/LoginStateFactory.kt):
that fill take the responsibility to provide dependencies and abstract our state creation logic:

```kotlin
interface LoginStateFactory {
  /**
   * Enter existing user password
   * @param data Login data state
   */
  fun passwordEntry(data: LoginDataState): LoginState

  /**
   * Checks email/password
   * @param data Data state
   */
  fun checking(data: LoginDataState): LoginState

  /**
   * Password error screen
   */
  fun error(data: LoginDataState, error: Throwable): LoginState
}
```

Each factory method here will accept **only** the inter-state data providing both context and 
state-specific dependencies implicitly. This will decouple state logic from the concrete 
implementations and increase our [testability](login/src/test/java/com/motorro/statemachine/login/model/state/BaseStateTest.kt)
greatly. 

The exact factory implementation that binds together all data and dependencies will look like that:

```kotlin
@LoginScope
class LoginStateFactoryImpl @Inject constructor(
    host: WelcomeFeatureHost, // External interface
    renderer: LoginRenderer, // Renderer
    private val createCredentialsCheck: CredentialsCheckState.Factory // Concrete state factory
) : LoginStateFactory {

    // Dependencies common for each state provided through the context
    private val context: LoginContext = object : LoginContext {
        override val factory: LoginStateFactory = this@Impl
        override val host: WelcomeFeatureHost = host
        override val renderer: LoginRenderer = renderer
    }

    override fun passwordEntry(data: LoginDataState): LoginState {
        // Create explicitly
        return PasswordEntryState(context, data) 
    }

    override fun checking(data: LoginDataState): LoginState {
        // Use provided state-factory
        return createCredentialsCheck(context, data)
    }

    override fun error(data: LoginDataState, error: Throwable): LoginState {
        // Create explicitly
        return ErrorState(context, data, error)
    }
}
```

The factory is made available to your machine states through the common context:
```kotlin
class CredentialsCheckState(context: LoginContext) : LoginState(context) {
    
    // State logic...
  
    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: LoginGesture) = when(gesture) {
        LoginGesture.Back -> onBack()
        else -> super.doProcess(gesture)
    }

    private fun onBack() {
        // Use provided factory to create a new state
        setMachineState(factory.passwordEntry(data))
    }
}
```

Then we could mock the factory in our tests and check state transitions:

```kotlin
class CredentialsCheckStateTest {
    private val data = LoginDataState()
    private val factory: LoginStateFactory = mockk()
    private val passwordEntry: LoginState = mockk()

    @Test
    fun returnsToPasswordEntryOnBack() = runTest {
      every { factory.passwordEntry(any()) } returns passwordEntry

      state.start(stateMachine)
      state.process(LoginGesture.Back)

      verify { stateMachine.setMachineState(passwordEntry) }
      verify { factory.passwordEntry(data) }
    }
}
```

We can also provide the state factory to the `ViewModel` and use it to initialize our state-machine:

```kotlin
@HiltViewModel
class LoginViewModel @Inject constructor(private val factory: LoginStateFactory) : ViewModel() {

    /**
     * Creates initializing state
     */
    private fun initializeStateMachine(): CommonMachineState<WelcomeGesture, WelcomeUiState> {
        // Obtain data required to start from a saved-state handle or injection
        val commonData: LoginDataState = LoginDataState()
        return factory.passwordEntry(commonData)
    }    
  
    /**
     * State machine
     */
    private val stateMachine = FlowStateMachine(::initializeStateMachine)
}

```



