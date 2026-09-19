---
name: commonstatemachine-commonchildflow-boilerplate
description: Use this skill to make a boilerplate for the new feature-flow using 
  the [CommonStateMachine](https://github.com/motorro/CommonStateMachine) architecture.
  If the user asks you something like "Create a new authentication flow feature" - use this skill.
metadata:
  author: Motorro
  last-updated: '2026-08-12'
  keywords:
  - android
  - architecture
  - cross-platform
  - kotlin-multiplatform
  - mvi
  - state-machine
  - feature-flow
---

## Description
Use this skill to add a new feature boilerplate to the app based on the [CommonStateMachine](https://github.com/motorro/CommonStateMachine) architecture.
following the [Common Child Flow](https://github.com/motorro/CommonStateMachine/tree/master#common-child-flow-api) design. This design sets a common approach to use proxy state machines for a feature-flow.

## Example
This instruction uses an example authentication module. All examples, schemes and code samples refer
to such an example. Adjust the names and packages to your actual need.

## Strategy
You will create a set of gradle modules that together form a feature:

- The API module: contains the definition of input and output data types for the feature. 
  Example: authentication API that provides the basic interface to the authentication feature.
- One or more implementation modules: contains the implementation of the API.
  Example: login implementation module - implements API with login/password functionality.

Take a look at the [class diagram](assets/common-child-flow.puml). It contains an overview
between all the interfaces, classes and objects in a setup using the Authentication module example.

Follow the following common rules when writing code:

- Follow the project coding conventions or use Kotlin default
- Follow the project naming conventions or use Kotlin default
- If the project uses KDoc as a coding convention - add it to the created types, methods, properties
- Add TODO comments in dummy classes and functions to point the user to provide the actual implementation

## Step 1. Module directory structure creation
Create the following module structure if not instructed otherwise:
```
my-project            # Project folder. Common package: com.motorro.statemachine
├── app               # Example: an application module, package: com.motorro.statemachine.app
├── auth              # Required: a base directory for a new feature (authentication)
│  ├── api            # Required: an API gradle module that describes the feature, package: com.motorro.statemachine.auth.api
│  └── implementation # Required: an implementation gradle module that implements the API, package: com.motorro.statemachine.auth.implementation
└── ...               
```

- Use the Gradle setup common for the other modules in a project
- Include the following dependencies if not provided already:
  - Base state-machine components: "com.motorro.commonstatemachine:commonstatemachine:x.x.x"
  - Coroutine extensions (if using coroutines): "com.motorro.commonstatemachine:coroutines:x.x.x"
  - Common data API (state machine child flow): "com.motorro.commonstatemachine:commonflow-data:x.x.x" 
  - Common UI API (Compose API if used): "com.motorro.commonstatemachine:commonflow-compose:x.x.x"
- Include test dependencies that are used across the project and coroutines test dependencies if used.
- Include DI dependencies that are used across the project.
- Include Logging dependencies that are used across the project.
- Include Compose native or multiplatform (depending on the project setup or the user choice) dependencies to the implementation modules.
- If the user asks you to create some specific implementation or several implementations of the same API, infer the names
  and create several modules. For example, the user may ask you to create two authentication implementations of the same Authentication API:

  - `password` - an implementation for the password authentication
  - `social` - and implementation for the social network authentication

  If so - create several implementation modules, use the same API module as a base for them.
- Include `api` as a dependency in the implementation modules.

## Step 2. Create an API module code
Create a package for the feature API. By default, use the common application package adding the feature name and `api`.
See the module structure above for an example.
Then follow these guides for the API module implementation:

1. [UI gesture definitions and UI state definitions API](references/gesture-and-ui-state-api.md)
2. [Feature-flow input and result API](references/input-and-result-api.md)
3. [Feature-flow data and UI API](references/data-and-ui-api.md)

## Step 3. Create implementation module
Here you will create an API implementation. 
If several implementations are requested by user (step 1) - repeat for each implementation.
Create a package for the feature implementation. By default, use the common application package adding the feature name and `implementation`.
If several implementations are requested by user (step 1) - name according to the implementation name.
Then follow these guides for the module implementation:

1. [Create gestures, ui-states, data-state](references/data-and-ui-implementation.md).
2. [Create common-state binding, ui-renderer, state factory and context interfaces](references/renderer-factory-context-interface-implementation.md).
3. [Create basic test fixtures for the data-classes created in the previous state](references/test-fixtures-implementation.md).
4. [Create an abstract base state](references/renderer-factory-context-interface-implementation.md).
5. [Create a base abstract unit-test class](references/renderer-factory-context-interface-implementation.md).

## Step 4. Create the machine states
For the user to kick-off faster, we will create some machine-states:

- If the user gave you the instructions about what would be the state-flow, or what states does he need - create the states for him. 
  Implement the required functionality or place TODOs in place of the code if it is not clear what to do exactly.
  Don't hesitate to ask the user to clarify what he needs.
- If the user didn't give you any specifics, create a classic LCE example:
  - create a dummy preloading state
  - create a dummy content state
  - create a dummy error state

Refer to [this manual](references/state-implementation.md) to get the idea of the state mechanics.
Repeat this step for every state you need to create.
Create a unit-test for every created machine-state according to [this manual](references/state-test-implementation.md)

## Step 5. Create basic Compose views
To make the module complete, create some views for the created UI-states if user didn't tell you
otherwise or has given you some special instructions, screenshots, etc.
Follow these steps to implement the views:

1. [Implement UI renderer if used](references/ui-renderer-implementation.md)
2. [Implement sample composables](references/ui-view-implementation.md)