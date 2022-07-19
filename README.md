# CommonStateMachine [![Check](https://github.com/motorro/CommonStateMachine/actions/workflows/check.yml/badge.svg?branch=master)](https://github.com/motorro/CommonStateMachine/actions/workflows/check.yml) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.motorro.commonstatemachine/commonstatemachine/badge.png)](https://repo1.maven.org/maven2/com/motorro/commonstatemachine/commonstatemachine/)

A way to handle logic and UI state in Android applications and other platforms with 
[Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html).

Key features:
- [Well-known approach](https://en.wikipedia.org/wiki/Finite-state_machine) - nothing new :)
- **No restriction** on your coding approach, technology stack and style
- A great way to isolate and test your logic - [low coupling and high cohesion](https://www.geeksforgeeks.org/software-engineering-coupling-and-cohesion/)
- Should work well in kotlin-multiplatform projects ;)
- Easy to integrate with multi-module architecture
- Designed for [Jetpack Compose](https://developer.android.com/jetpack/compose) but it is not a restriction
- May (if you like to) work as a navigation library
- Explicit `Back` gesture management with the total control of yours

## Examples
- [LCE](lce) - basic example of Load-Content-Error application
- [Welcome](welcome) - multi-module example of user on-boarding flow

## Introduction
Let's start with a basic example. Imagine we need to implement the classic master-detail view of
items with the following screen flow:
![LCE flow](doc/screenshots/flow.png)



