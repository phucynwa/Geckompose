package com.phucynwa.geckompose.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.phucynwa.geckompose.GeckoView
import com.phucynwa.geckompose.rememberSaveableWebViewState
import com.phucynwa.geckompose.rememberWebViewNavigator
import com.phucynwa.geckompose.rememberWebViewState

class WebViewSaveStateSample : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    val navController = rememberNavController()
                    Column(Modifier.fillMaxSize()) {
                        Row {
                            Button(onClick = { navController.popBackStack() }) {
                                Text("Home")
                            }
                            Button(onClick = { navController.navigate("detail") }) {
                                Text("Detail")
                            }
                        }

                        Spacer(modifier = Modifier.size(16.dp))

                        NavHost(navController = navController, startDestination = "home") {
                            composable("home") {
                                Home()
                            }
                            composable("detail") {
                                Detail()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Home() {
    val webViewState = rememberSaveableWebViewState()
    val navigator = rememberWebViewNavigator()

    LaunchedEffect(navigator) {
        val bundle = webViewState.viewState
        if (bundle == null) {
            // This is the first time load, so load the home page.
            navigator.loadUrl("https://wikipedia.com")
        }
    }

    GeckoView(
        state = webViewState, navigator = navigator, modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun Detail() {
    val webViewState = rememberWebViewState(url = "https://google.com")

    GeckoView(
        state = webViewState, modifier = Modifier.fillMaxSize()
    )
}
