package me.fidelep.ravennews.ui.screens

import android.view.ViewGroup
import android.webkit.WebView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun WebScreen(
    url: String,
    modifier: Modifier = Modifier,
) {
    AndroidView(modifier = modifier.fillMaxSize(), factory = {
        WebView(it).apply {
            layoutParams =
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
        }
    }, update = {
        it.loadUrl(url)
    })
}

@Preview
@Composable
private fun WebScreenPreview() {
    val mockedUrl = "www.example.com"
    WebScreen(url = mockedUrl)
}
