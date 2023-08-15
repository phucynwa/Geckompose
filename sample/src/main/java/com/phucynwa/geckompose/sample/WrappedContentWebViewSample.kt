package com.phucynwa.geckompose.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.phucynwa.geckompose.GeckoView
import com.phucynwa.geckompose.rememberWebViewStateWithHTMLData
import kotlinx.coroutines.launch

class WrappedContentWebViewSample : ComponentActivity() {
    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    val sheetState = rememberModalBottomSheetState(
                        initialValue = ModalBottomSheetValue.Hidden
                    )
                    ModalBottomSheetLayout(
                        sheetState = sheetState,
                        sheetContent = {
                            WrappingWebContent("Hello")
                        }
                    ) {
                        val scope = rememberCoroutineScope()
                        Box(Modifier.fillMaxSize()) {
                            Button(onClick = {
                                scope.launch { sheetState.show() }
                            }, Modifier.align(Alignment.Center)) {
                                Text("Open Sheet")
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
    body: String
) {
    val webViewState = rememberWebViewStateWithHTMLData(
        data = "<!DOCTYPE html><html><body><p>$body</p></body></html>"
    )
    GeckoView(
        state = webViewState,
        modifier = Modifier.fillMaxWidth()
            .heightIn(min = 1.dp), // A bottom sheet can't support content with 0 height.
        captureBackPresses = false,
    )
}
