<div align="center">

# Dot Dialer

An open-source Android dialer focused on reliable call lifecycle handling, fast contact search,
accessibility and restrained, system-consistent haptics.

</div>

> Dot Dialer is in foundation work and is not ready for daily use or public release yet.

## Current focus

- deterministic call state, without stuck call UI or notifications;
- automated unit, Android and lifecycle-abuse testing;
- accessible Compose components and scalable UI;
- a reusable contact index and T9 search foundation;
- secure preferences, private contacts and backup/restore.

## Build and verify

Requirements: JDK 17 and an Android SDK containing platform 37.

```shell
./gradlew :app:compileFossDebugKotlin \
  :app:testFossDebugUnitTest \
  :app:compileFossDebugAndroidTestKotlin \
  :app:lintFossDebug
```

See [testing](docs/TESTING.md) and [accessibility and haptics](docs/ACCESSIBILITY_AND_HAPTICS.md).

## Upstream and license

Dot Dialer is based on [Goodwy/RPhone](https://github.com/Goodwy/RPhone). The original history and
copyright notices are retained; details are in [UPSTREAM.md](UPSTREAM.md).

The source is licensed under [GNU GPL version 3](LICENSE). Selling binaries is permitted by the
license, while recipients retain the GPL rights to the corresponding source.
