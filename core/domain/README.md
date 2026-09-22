# `:core:domain`

Pure Kotlin business rules shared by Android entry points and features.

Allowed dependencies:

- Kotlin standard library;
- coroutines and Flow;
- small platform-independent libraries with a documented reason.

Forbidden dependencies:

- Android SDK and AndroidX;
- Compose;
- Room, DataStore or serialization formats tied to persistence;
- framework models such as `Context`, `Uri`, `Cursor` and `Call`;
- feature and platform modules.

The module boundary enforces most of this list because it is built as Kotlin/JVM and has no Android
libraries on its classpath.
