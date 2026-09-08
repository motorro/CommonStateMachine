# Module initialization data and flow result
Use this reference to create the required input data and define the flow result.

## Mechanics
The [Common Child Flow](https://github.com/motorro/CommonStateMachine/tree/master#common-child-flow-api) runs a
child state-machine flow in a parent state-machine to get some result, display some UI, etc.
Example: the main application flow could run a child flow in-place to log the user-in.

- The child flow could be initialized by some data passed from parent. 
- The child flow could return some data back to the parent on completion.

## Input
If the user asks to provide any input - create an immutable type - a data class or a sealed structure.
If no input type explicitly required - confirm and use `Unit`
Take a look at the [class diagram](../assets/common-child-flow.puml) for more details.
Take a look at the [example](../assets/example/api/AuthInput.kt) - common API definition

## Result
If the user asks to provide a flow result - create an immutable type - a data class or a sealed structure.
If no result type explicitly required - confirm and use `Unit`
Take a look at the [class diagram](../assets/common-child-flow.puml) for more details.
Take a look at the [example](../assets/example/api/AuthResult.kt) - common API definition
