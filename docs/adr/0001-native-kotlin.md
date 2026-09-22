# ADR 0001: Native Kotlin for Dot Dialer

- Status: accepted
- Date: 2026-09-22

## Context

Dot Dialer is an Android dialer whose critical behavior depends on Telecom callbacks, services,
Contacts Provider, notifications, permissions, audio routing, process lifecycle and OEM variations.
The inherited application is already written in Kotlin with Jetpack Compose.

## Decision

Use native Kotlin, Jetpack Compose, coroutines and Flow. Keep Android APIs behind narrow adapters and
place business rules in pure Kotlin modules.

## Consequences

- Platform behavior is available without a cross-platform bridge.
- Pure business logic remains independently testable and potentially reusable.
- Android remains the product platform; an eventual iOS application would share specifications and
  algorithms deliberately, not force Telecom behavior through a common UI runtime.
- Kotlin and Android dependencies are upgraded deliberately through tested changes, not automatically.
