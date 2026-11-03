# HiTunes

HiTunes is an Android music app built on the
public [iTunes Search API](https://performance-partners.apple.com/search-api).
You can search songs, play their 30-second previews, see recently played songs, and open a song's
album.
Playback runs in a background media service, so it keeps going with the app closed and shows up in
the system media controls.

The same playback is shown on every screen type:

| Screen                                    | Layout                                                                                                                                                             |
|-------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Phone                                     | Song list with a mini player; tapping it opens the song page. Always portrait.                                                                                     |
| Tablet, portrait                          | Same as the phone.                                                                                                                                                 |
| Tablet, landscape                         | Song list on the left, playing song on the right.                                                                                                                  |
| Android Automotive (car screen)           | Song list, then a car song page: cover beside title and artist, seek bar, one row of large controls, and an Album button.                                          |
| Android Auto (phone projected to the car) | Drawn by Android Auto from the app's media library: browse **Recently played** and control playback.                                                               |
| Wear OS                                   | A remote for the song playing on the phone: cover, title, artist, play/pause, previous, next. With nothing playing it shows an empty state with disabled controls. |

## Project layout

- `app` – phone, tablet and Automotive app
- `wear` – Wear OS app; it shares the phone app's `applicationId` so the two can talk
- `feature/*` – song list, player and album screens, each split into `internal` (UI and view models)
  and `shared` (entry points)
- `domain`, `data` – use cases, repositories, Retrofit (remote) and Room (local) data sources
- `core/wearsync` – the phone ↔ watch messages, sent over the Wearable Data Layer
- `design` – theme, shared components and preview annotations
- Built with Kotlin, Jetpack Compose, Media3, Koin and Navigation Compose

## Requirements

- A recent Android Studio
- JDK 11 or newer
- Android SDK 37 (the phone app needs Android 9 / API 28+, the watch app Android 11 / API 30+)

The API endpoint comes from `secrets.defaults.properties`. Because the iTunes API is public, nothing
else is needed.
To use a different endpoint, set `BASE_ENDPOINT` in a `secrets.properties` file; that file is
git-ignored.

## Running on a phone or tablet

1. Open the project in Android Studio and let Gradle sync.
2. Pick the `app` run configuration and a phone or tablet: a device connected over USB or an
   emulator from **Device Manager**.
3. Press **Run**. Or, from a terminal:

   ```bash
   ./gradlew :app:installDebug
   ```

To see the two-pane layout, use a tablet (for example the *Pixel Tablet* emulator) and rotate it to
landscape. Phones stay in portrait.

## Running on Wear OS

The watch app is a companion: it shows what the phone is playing, so you need a phone and a watch
paired together.

1. **Create a watch emulator.** In **Device Manager**, choose **+** → **Create Virtual Device** → *
   *Wear OS** → *Wear OS Large Round*, with a Wear OS system image at API 34 or newer.
2. **Use a phone with Google Play.** Use a real phone or a phone emulator whose image includes the
   Play Store, and install the **Wear OS** app from the Play Store.
3. **Pair them.** In **Device Manager**, open the watch's ⋮ menu → **Pair Wearable**, pick the
   phone, and follow the wizard (it continues in the Wear OS app on the phone).
4. **Install both apps** from the same machine, so both are signed with the same debug key; the
   watch only talks to a phone app signed with the same key:

   ```bash
   ./gradlew :app:installDebug :wear:installDebug
   ```

   Or run the `app` configuration on the phone and the `wear` configuration on the watch.
5. Play a song on the phone. The watch shows it and can play, pause and skip. When playback stops,
   the watch goes back to its empty state.

## Running on Android Automotive (car screen)

Here the app runs directly on the car's own screen and uses its car layout.

1. In **SDK Manager** → **SDK Platforms**, check **Show Package Details** and install an **Android
   Automotive** system image (with or without Google Play).
2. In **Device Manager**, create a virtual device from the **Automotive** category, for example
   *Automotive (1024p landscape)*.
3. Run the `app` configuration on it, or:

   ```bash
   ./gradlew :app:installDebug
   ```

## Running on Android Auto (projected from a phone)

Android Auto draws its own media screens and fills them from the app's media library, so you test it
with the **Desktop Head Unit (DHU)**.

1. In **SDK Manager** → **SDK Tools**, install **Android Auto Desktop Head Unit Emulator**.
2. On the phone (a real phone, or an emulator with Google Play), install HiTunes and the **Android
   Auto** app.
3. Enable developer mode in Android Auto: open its settings, tap **Version** ten times, then in
   the ⋮ menu choose **Start head unit server**.
4. Forward the port and start the DHU:

   ```bash
   adb forward tcp:5277 tcp:5277
   ```

   ```bash
   $ANDROID_HOME/extras/google/auto/desktop-head-unit
   ```

5. On the DHU, open the media apps and pick **HiTunes**. Play a song on the phone first so that *
   *Recently played** has items to browse.

## Tests

```bash
./gradlew test
```

```bash
./gradlew verifyRoborazziDebug
```

The second command compares the Compose screens with the screenshots stored in each module's
`src/test/snapshots`. After an intended UI change, record them again with
`./gradlew recordRoborazziDebug`.
Both checks run on every pull request to `main` or `develop` (see `.github/workflows`).
