# AGENTS.md

## Project Overview
**CommonStateMachine** is a lightweight, non-opinionated Kotlin Multiplatform (KMP) library that implements a finite-state machine pattern for MVI (Model-View-Intent) architectures.

### Key Architecture Concepts
- **`CommonStateMachine`**: Acts as a bridge between the active logical state and the UI. `FlowStateMachine` exports state updates as a Kotlin `StateFlow`.
- **`CommonMachineState`**: Encapsulates state-bound business logic, processes user gestures (`G`), emits UI states (`U`), and handles transitions to other states. Supports state-scoped coroutines via `CoroutineState`.
- **StateMachine Class diagram**: Take a look at the framework [class diagram](doc/StateMachine.puml).
- **State Separation & DI Abstractions**:
  - **Use-cases**: Business logic isolated from view logic.
  - **View-state Renderer**: Decouples UI rendering logic from logical state transitions.
  - **Context & State Factories**: Common `Context` delegates dependencies to states; dedicated factories (`StateFactory`) create states using inter-state dynamic data.
- **Multi-Module Support (`ProxyMachineState`)**:
  - Wraps nested feature flows within a parent state machine.
  - Bridges heterogeneous gesture and UI state systems through mapping adapters (`mapGesture`, `mapUiState`).
  - Standardized via `commonflow` modules (`commonflow-data`, `commonflow-compose`, `commonflow-viewmodel`).
  - Take a look at the [proxy lifecycle diagram](doc/ProxyLifecycle.puml).
- **Parallel Machine Composition (`MultiMachineState`)**:
  - Runs multiple state machines concurrently or selectively in parallel using `ProxyMachineContainer`.
  - Manages active/dormant machine lifecycles via `MachineLifecycle`.

---

## Build and Verification
If you need to test if everything in the project compiles and unit tests pass:

1. Take the GitHub action: `.github/workflows/check.yml`.
2. Find and execute the Gradle command from the step: `Check with gradle`.

---

## Examples
Example projects demonstrating various architectures, state machine features, and usage patterns are located in the `examples` folder:
- **`examples/lce`**: Basic Load-Content-Error pattern.
- **`examples/welcome`**: Multi-module on-boarding flow.
- **`examples/multi`**: Parallel state machines (`parallel`), bottom navbar navigation (`navbar`), and mixed gesture/UI state composition (`mixed`).
- **`examples/lifecycle`**: View lifecycle tracking and operation pausing on lifecycle changes.
- **`examples/di`**: Dynamic child flow binding with Hilt.
- **`examples/skills`**: Multi-module example demonstrating custom skills implementation.

---

## Skills
There are two folders managing skills:
- **`skills-src`**: Source files used to develop and maintain skills. **Always edit skill files in `skills-src`**, never directly in `skills/`.
  - **Shared resources**: Reusable files and templates are stored in `.shared/` subfolders.
  - **Asset symlinks**: Code examples in `assets/` subfolders are symlinked from `examples/skills/` so they remain compilable and testable.
- **`skills`**: Contains complete, self-contained generated skills for distribution (e.g. `npx skills`). Do not edit files here directly—they are automatically generated from `skills-src/` via [the build script](skills-src/build_skills.sh) (executed automatically by [.githooks/pre-commit](.githooks/pre-commit) upon commit).

---

## Coding conventions
- Use KDoc for documenting classes, interfaces, and public functions.
- Follow standard Kotlin coding conventions and style guidelines.
- Add copyright headers at the top of Kotlin source files.
- When checking for boolean `false`, use the `.not()` extension function instead of `!` for clarity (e.g., `if (condition.not()) { ... }`).
- Use Yoda style for equality comparisons (e.g., `if ("a" == b) { ... }`).
