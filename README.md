# PRIVATE AI – BRO

Your Voice. Your Intelligence. Your Private World.

An Android-first, privacy-focused personal voice assistant.

## Source layout

- `BRO-source.zip` is the preserved base Android project (v0.6.0).
- `source/` contains the approved UI overrides layered over the archive during CI.
- The workflow extracts the archive into a temporary `project/` directory, overlays `source/`, applies the documented compatibility and wake-word configuration fixes, and builds a debug APK.
- The original orb renderer and artwork are not regenerated or replaced by the workflow.

## Build

1. Open **Actions** → **PRIVATE AI BRO Android Build**.
2. Run the workflow manually, or push a commit to `main`.
3. Download `PRIVATE-AI-BRO-debug-apk` after a successful run.
4. If the build fails, inspect `PRIVATE-AI-BRO-build-logs`.

The workflow builds the debug variant only. It does not produce a signed release APK.

## Development status

- The source archive and UI override files are present in the repository.
- CI compilation must succeed before an APK is considered build-verified.
- A successful CI build is not a substitute for installation, permission, wake-word, battery, RAM, security, and regression testing on a physical Android device.
- Tamil offline speech recognition and Tamil TTS depend on the speech services and language data installed on the device.
- The current command engine is primarily rule-based; a full autonomous AI-agent planner/provider integration is a separate implementation milestone, not a feature to claim as complete.

## Principles

- Kotlin and native Android APIs.
- Local-first processing; no mandatory paid APIs or cloud backend.
- Confirmation for sensitive actions.
- Optimize for Android 12 and 4 GB RAM devices.
- Preserve the approved orb artwork and renderer unless the owner explicitly requests a change.
