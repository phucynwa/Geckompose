package com.phucynwa.geckompose.sample

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.phucynwa.geckompose.AccompanistWebViewClient
import com.phucynwa.geckompose.GeckoView
import com.phucynwa.geckompose.LoadingState
import com.phucynwa.geckompose.WebView
import com.phucynwa.geckompose.rememberWebViewNavigator
import com.phucynwa.geckompose.rememberWebViewState
import com.phucynwa.geckompose.sample.ui.theme.GeckomposeTheme

class BasicWebViewSample : ComponentActivity() {
    val initialUrl = "https://google.com"
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GeckomposeTheme {
                val state = rememberWebViewState(url = initialUrl)
                val navigator = rememberWebViewNavigator()
                var textFieldValue by remember(state.lastLoadedUrl) {
                    mutableStateOf(state.lastLoadedUrl)
                }

                Column {
                    TopAppBar(
                        title = { Text(text = "WebView Sample") },
                        navigationIcon = {
                            if (navigator.canGoBack) {
                                IconButton(onClick = { navigator.navigateBack() }) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "Back"
                                    )
                                }
                            }
                        }
                    )

                    Row {
                        Box(modifier = Modifier.weight(1f)) {
                            if (state.errorsForCurrentRequest.isNotEmpty()) {
                                Image(
                                    imageVector = Icons.Default.Error,
                                    contentDescription = "Error",
                                    colorFilter = ColorFilter.tint(Color.Red),
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd)
                                        .padding(8.dp)
                                )
                            }

                            OutlinedTextField(
                                value = textFieldValue ?: "",
                                onValueChange = { textFieldValue = it },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Button(
                            onClick = {
                                textFieldValue?.let {
                                    navigator.loadUrl(it)
                                }
                            },
                            modifier = Modifier.align(Alignment.CenterVertically)
                        ) {
                            Text("Go")
                        }
                    }

                    val loadingState = state.loadingState
                    if (loadingState is LoadingState.Loading) {
                        LinearProgressIndicator(
                            progress = loadingState.progress,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // A custom WebViewClient and WebChromeClient can be provided via subclassing
                    val webClient = remember {
                        object : AccompanistWebViewClient() {
                            override fun onPageStarted(
                                view: WebView,
                                url: String?,
                                favicon: Bitmap?
                            ) {
                                super.onPageStarted(view, url, favicon)
                                Log.d("Accompanist WebView", "Page started loading for $url")
                            }
                        }
                    }

                    GeckoView(
                        state = state,
                        modifier = Modifier
                            .weight(1f),
                        navigator = navigator,
                        onCreated = { webView ->
                            webView.settings?.allowJavascript = true
                        },
                        client = webClient
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun WebViewPreview() {
    GeckomposeTheme {
        Column {
            Text("Preview should still load but WebView will be grey box.")
            GeckoView(
                state = rememberWebViewState(url = "localhost"),
                modifier = Modifier.height(100.dp)
            )
        }
    }
}
