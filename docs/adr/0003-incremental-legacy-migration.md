# ADR 0003: Replace inherited subsystems incrementally

- Status: accepted
- Date: 2026-09-22

## Context

The inherited implementation contains valuable Android and OEM behavior as well as tightly coupled
subsystems. A full rewrite would discard both the technical debt and the accumulated edge cases.

## Decision

Use a strangler migration: characterize old behavior, introduce a new interface and implementation,
move one complete flow, verify it, then remove the replaced path. The old implementation is a
temporary reference, not the target architecture.

## Consequences

- The application remains buildable throughout migration.
- Behavioral parity is evidence-based rather than assumed.
- Temporary adapters are permitted but must be named and tracked as transitional.
- Duplicate implementations are removed after cutover; they do not become a permanent compatibility
  layer.
