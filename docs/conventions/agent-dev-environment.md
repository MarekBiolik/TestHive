# Agent development environment

How agents should navigate TestHive work after foundations.

## Skills (in-repo)

| Skill | Use for |
|-------|---------|
| `ui-ux` | Flows, a11y, UX contracts |
| `design-tokens` | Palette / type / space SSOT |
| `android-compose` | `apps/android/` Compose UI |
| `android-emulator` | AVD install/launch smoke |
| `core-kmp` | Shared `core/` domain |
| `platform-delivery` | Multi-area core-first delivery |
| `qa-testing` | What to run before claiming green |

Located under `.cursor/skills/<name>/SKILL.md`.

## Tooling expectations

- **JDK 17+** for Gradle (`JAVA_HOME` if `java` missing from PATH)
- **Android SDK** via `local.properties` → `sdk.dir`
- **adb / emulator** on PATH; prefer AVD `Medium_Phone_API_37.0`
- **pnpm** for `apps/web`

## Appearance

Neon Glass Learning — [visual-style-neon-glass](../specs/visual-style-neon-glass.md), [ADR 0007](../decisions/0007-design-tokens-ssot.md). Do not invent a parallel palette in UI.

## Related

- [testing](testing.md)
- [git-workflow](git-workflow.md)
- Always-on rules under `.cursor/rules/`
