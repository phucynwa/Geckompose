# Geckompose

[![Maven](https://jitpack.io/v/phucynwa/Geckompose.svg)](https://jitpack.io/#phucynwa/Geckompose)
[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)

A Jetpack Compose wrapper around Mozilla's [GeckoView](https://mozilla.github.io/geckoview/), with an API modeled after [Accompanist WebView](https://google.github.io/accompanist/web/).

Geckompose lets you embed Gecko-powered web content in your Compose UI using the familiar `WebView` / `rememberWebViewState` style APIs.

## Features

- `GeckoView` composable with Accompanist-Web-style API (`state`, `navigator`, `client`, `chromeClient`)
- `WebViewState` for observing loading state, progress, page title, icon, and errors
- `WebViewNavigator` to control navigation (back, forward, reload, stop) from outside the composable
- Load URLs, HTML data, or POST requests
- Back press handling and state saving/restoration out of the box

## Setup

### Repository

The library is distributed via [JitPack](https://jitpack.io/#phucynwa/Geckompose). Add it to your `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        maven("https://jitpack.io")
    }
}
```

### Dependency

```kotlin
dependencies {
    implementation("com.github.phucynwa.Geckompose:lib:<version>")
}
```

> Check the [JitPack badge](https://jitpack.io/#phucynwa/Geckompose) above for the latest version.

## Usage

### Basic

```kotlin
val state = rememberWebViewState("https://mozilla.org")

GeckoView(
    state = state,
    modifier = Modifier.fillMaxSize()
)
```

### With a navigator

```kotlin
val state = rememberWebViewState("https://mozilla.org")
val navigator = rememberWebViewNavigator()

Column {
    Row {
        Button(onClick = { navigator.navigateBack() }, enabled = navigator.canGoBack) {
            Text("Back")
        }
        Button(onClick = { navigator.reload() }) {
            Text("Reload")
        }
    }

    LinearProgressIndicator(
        progress = {
            when (val s = state.loadingState) {
                is LoadingState.Loading -> s.progress
                else -> 1f
            }
        }
    )

    GeckoView(
        state = state,
        navigator = navigator,
        modifier = Modifier.fillMaxSize()
    )
}
```

### Custom client

```kotlin
class MyWebViewClient : AccompanistWebViewClient() {
    override fun onPageFinished(view: WebView, url: String?) {
        super.onPageFinished(view, url)
        // custom handling
    }
}

val state = rememberWebViewState("https://mozilla.org")
val client = remember { MyWebViewClient() }

GeckoView(state = state, client = client)
```

## Requirements

- Android minSdk 26+
- Kotlin Multiplatform project structure (currently targets Android; built with the Kotlin Multiplatform + Compose toolchain)

## Sample

See the [`sample`](sample/) module for a complete demo app.

## License

```
Copyright 2023 Phuc YNWA

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
