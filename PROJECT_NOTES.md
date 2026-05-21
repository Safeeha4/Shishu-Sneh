# Shishu-Sneh Android App

This is a native Android/Kotlin implementation scaffolded from `Readme.txt`.

## What is implemented

- Kotlin Android app module with a single native `MainActivity`.
- Dark theme matching the requested Shishu-Sneh palette.
- Splash, language selection, baby profile setup, dashboard, growth chart, vaccination calendar, feeding tracker, profile, and offline AI guidance screens.
- SQLite persistence for baby profile, growth entries, vaccine records, and feeding sessions schema.
- Indian infant vaccination schedule generation from the baby's date of birth.
- Profile validation for name, date of birth, weight, gender, and optional height.

## Build note

Android Studio is installed on this machine, but the Android SDK is not discoverable in the usual locations (`ANDROID_HOME` is unset and no `local.properties` exists yet). Install/select an Android SDK in Android Studio, then create `local.properties` like:

```properties
sdk.dir=C\:\\Users\\dell\\AppData\\Local\\Android\\Sdk
```

After that, run:

```powershell
$env:JAVA_HOME='C:\Program Files\Android\Android Studio1\jbr'
& 'C:\Users\dell\.gradle\wrapper\dists\gradle-8.14.3-bin\cv11ve7ro1n3o1j4so8xd9n66\gradle-8.14.3\bin\gradle.bat' assembleDebug
```
