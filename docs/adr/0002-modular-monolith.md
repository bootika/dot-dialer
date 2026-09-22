# ADR 0002: Modular monolith with enforced dependency direction

- Status: accepted
- Date: 2026-09-22

## Context

The inherited codebase is a single `:app` module with UI, Android integrations, persistence and
business rules frequently mixed in the same files. Package conventions alone cannot prevent those
dependencies from returning.

## Decision

Adopt a modular monolith. Pure rules live in Kotlin/JVM core modules, Android integrations live in
platform modules, and user-facing capabilities live in feature modules. `:app` is the composition
root. Add modules only when a boundary is valuable and cohesive.

## Consequences

- Gradle enforces important boundaries at compile time.
- Core tests run without Android or an emulator.
- Feature work can proceed independently with smaller public APIs.
- There is some Gradle and API-design overhead, so ultra-fine-grained modules are explicitly avoided.
