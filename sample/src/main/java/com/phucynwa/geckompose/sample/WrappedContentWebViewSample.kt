package com.phucynwa.geckompose.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.phucynwa.geckompose.GeckoView
import com.phucynwa.geckompose.rememberWebViewStateWithHTMLData
import kotlinx.coroutines.launch

class WrappedContentWebViewSample : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    var showSheet by remember { mutableStateOf(false) }
                    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    val scope = rememberCoroutineScope()
                    Box(Modifier.fillMaxSize()) {
                        Button(
                            onClick = {
                                showSheet = true
                            },
                            modifier = Modifier.align(Alignment.Center)
                        ) {
                            Text("Open Sheet")
                        }
                        if (showSheet) {
                            ModalBottomSheet(
                                onDismissRequest = {
                                    scope.launch {
                                        sheetState.hide()
                                        showSheet = false
                                    }
                                },
                                sheetState = sheetState,
                            ) {
                                WrappingWebContent("Hello")
                            }
                        }
                    }
                }
            }
        }
    }
}

/***
 * A sample WebView that is wrapping it's content height.
 * The sheet should be the size of the rendered content and not unbounded.
 */
@Composable
fun WrappingWebContent(
    body: String,
) {
    val webViewState = rememberWebViewStateWithHTMLData(
        data = "<!DOCTYPE html><html><body><p>$body</p></body></html>"
    )
    GeckoView(
        state = webViewState,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 1.dp), // A bottom sheet can't support content with 0 height.
        captureBackPresses = false,
    )
}
