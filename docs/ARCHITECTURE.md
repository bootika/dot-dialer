# Dot Dialer architecture

This document is a contract for new code, not a description of every legacy class inherited from
RPhone. Existing code is migrated behind these boundaries incrementally. A feature is not considered
migrated until its old path is removed and its replacement passes the required tests.

## Goals

- Keep Android framework code at the edges.
- Make business rules deterministic and fast to test on the JVM.
- Give every mutable data type one source of truth.
- Make dependency direction visible and enforceable through Gradle modules.
- Preserve working Telecom and OEM behavior while replacing legacy internals safely.
- Keep accessibility, privacy, performance and failure recovery as design inputs.

## Technology decision

Dot Dialer is a native Android application written in Kotlin. Jetpack Compose is the UI toolkit,
coroutines and `Flow` are the concurrency primitives, and Android platform APIs are accessed through
narrow adapters.

Flutter is not used. A dialer depends directly on Android Telecom, Contacts Provider, call services,
notifications, permissions, audio routing and OEM-specific behavior. Adding a cross-platform runtime
would not remove those Android integrations and would create an additional lifecycle boundary.

New packages use `io.github.bootika.dotdialer`. The inherited `dev.goodwy.rphone` namespace is
transitional and receives no new product logic unless a migration step requires a small adapter.

## Module map

The target is a modular monolith. Modules are introduced when they create a real boundary, not for
every screen or class.

```text
:app                         composition root, Android entry points, navigation
  |-- :feature:dialer        dial input, T9 presentation, call initiation
  |-- :feature:incall        in-call presentation and user actions
  |-- :feature:contacts      contact presentation and editing workflows
  |-- :feature:recents       call history presentation and actions
  |-- :feature:settings      application settings UI
  |-- :platform:telecom      Android Telecom and audio adapters
  |-- :platform:contacts     Contacts Provider adapter
  |-- :platform:persistence  Room, DataStore and secure file adapters
  `-- :core:domain           pure Kotlin rules, models and ports
```

Only `:app` composes the complete dependency graph. Feature modules may depend on `:core:domain` and
purpose-built UI modules. Platform modules implement ports owned by the domain. Core modules never
depend on features, platform modules, Android SDK classes, Compose, Room or DataStore.

The first enforced boundary is `:core:domain`, which is a Kotlin/JVM module. Its build configuration
has no Android dependency, enables progressive Kotlin mode and treats compiler warnings as errors.

## Layer rules

### UI

- A composable renders immutable `UiState` and sends typed user actions.
- A screen-level state holder owns orchestration; composables do not call repositories, DataStore,
  `ContentResolver`, `TelecomManager` or services.
- State travels down and events travel up. Navigation and transient effects are explicit.
- Configuration changes and process recreation are treated as normal runtime events.
- Accessibility semantics and haptic intent are part of component APIs, not afterthoughts.

### Domain

- Business rules are pure Kotlin whenever possible.
- Domain models do not expose `Context`, `Uri`, cursors, Room entities or Telecom objects.
- Use cases are introduced for reused or non-trivial workflows, not as pass-through boilerplate.
- State transitions are deterministic, explicit and idempotent where callbacks can be repeated.
- Time, randomness and dispatchers are injected when they affect behavior.

### Data and platform adapters

- A repository owns one coherent type of data and exposes domain models.
- A data source talks to exactly one external source: Contacts Provider, call log, Room, DataStore,
  filesystem or another Android service.
- Framework exceptions are translated into typed failures at the boundary. Empty broad catches are
  forbidden in new code.
- Long-running and blocking operations are main-safe by implementation, not by caller convention.

## State and concurrency

- `Flow`/`StateFlow` represents observable state; `suspend` functions represent one-shot work.
- Mutable state has one owner. Other components receive read-only views.
- Application and call-session state never lives only in an Activity, Service or composable.
- Every asynchronous callback carries enough identity to reject stale events.
- Cancellation is normal. It must not be converted to a generic failure or silently swallowed.
- Fire-and-forget persistence is allowed only for explicitly non-critical telemetry; user settings and
  user data must provide completion or failure semantics.

## Privacy and security

- Phone numbers, contacts, call logs, notes and recordings are sensitive data.
- Sensitive values are not written to logs, test reports or analytics.
- Persistent caches require a documented need, retention rule and protection model.
- Backup inclusion is explicit. Private stores are excluded or protected before public release.
- Exported files use explicit user consent and, when confidential, authenticated encryption.
- Permissions are requested at the last responsible moment and degraded behavior is supported.

## Testing contract

- Pure reducers, normalizers, ranking, T9 and policy code: exhaustive JVM unit tests.
- Repositories and platform adapters: contract and integration tests with controlled fakes or Android.
- Compose components: semantics, accessibility and important interaction-state tests.
- Critical user journeys: device tests for launch, process death, force-stop, permission revocation,
  rapid open/close, repeated callbacks, multi-SIM and interrupted calls.
- Every production regression receives a failing test before its fix where technically possible.

The root `qualityGate` Gradle task is the merge gate. New modules must attach their tests or checks to
that task in the same change that introduces the module.

## Module admission rule

A new module must have a clear owner and API, isolate Android/framework dependencies, be reusable by
more than one consumer, or materially improve independent testing/build isolation. Otherwise, use a
cohesive package inside the existing feature module. `common`, `helpers` and generic `utils` modules
are not accepted as dumping grounds.

Production declarations are `internal` by default. A type becomes public only when another module
needs it, and the public surface stays smaller than the implementation.

## Legacy migration

1. Capture current behavior with characterization tests.
2. Define the new domain API without Android types.
3. Implement a platform adapter behind that API.
4. Route one complete user flow through the new path.
5. Verify parity, failure paths, accessibility and performance.
6. Remove the replaced legacy path; do not maintain two permanent implementations.

We do not rewrite the entire application in one step. Telecom and OEM behavior is too costly to
rediscover at once. We replace bounded subsystems while keeping the application runnable and the
quality gate green after every change.
