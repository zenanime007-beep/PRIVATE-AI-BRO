# PRIVATE AI – BRO

Your Voice. Your Intelligence. Your Private World.

An Android-first, privacy-focused personal voice assistant.

## Source archive required

The complete v0.6.0 project is supplied as `BRO-source.zip`. Place that archive at the repository root before the Android workflow can run. The workflow extracts it into a temporary `project/` directory and builds the debug APK.

## Build

1. Upload `BRO-source.zip` to the root of this repository.
2. Open **Actions**.
3. Select **PRIVATE AI BRO Android Build**.
4. Run the workflow, or push a commit to `main`.
5. Download the `PRIVATE-AI-BRO-debug-apk` artifact if the build succeeds.

The workflow tries the full build first, then the no-wake and minimal variants. The selected variant is recorded in the build logs.

## Development status

- Android build workflow is configured.
- The v0.6.0 source archive has not yet been added to this repository.
- No APK build has been confirmed yet.
- A successful CI build is not a substitute for installation, permission, battery, RAM, security, and regression testing on a physical device.

## Principles

- Kotlin and native Android APIs.
- Local-first processing; no mandatory paid APIs or cloud backend.
- Confirmation for sensitive actions.
- Optimize for Android 12 and 4 GB RAM devices.
