package me.fidelep.ravennews

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import me.fidelep.ravennews.ui.screens.MainScreen
import me.fidelep.ravennews.ui.states.UiEvents
import me.fidelep.ravennews.ui.theme.RavenNewsTheme
import me.fidelep.ravennews.viewmodels.MainActivityViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val tag = MainActivity::class.simpleName

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RavenNewsTheme {
                MainScreen(
                    stories = viewModel.newsState.collectAsState(initial = listOf()).value,
                    onRefresh = { refreshNews() },
                    onItemClick = { url -> openInBrowser(url) },
                    onItemRemove = { storyId -> removeItem(storyId) },
                )
            }
        }

        observeUiState()
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            viewModel.uiState.collect {
                when (it) {
                    is UiEvents.ERROR -> {
                        Log.e(tag, it.toString())
                    }

                    UiEvents.LOADING -> {
                        Log.d(tag, "Loading state")
                    }

                    UiEvents.IDLE -> {
                        Log.d(tag, "Loading state stopped")
                    }
                }
            }
        }
    }

    private fun removeItem(storyId: Int) {
        viewModel.removeNew(storyId)
        Log.d(tag, "remove item [$storyId]")
    }

    private fun openInBrowser(url: String) {
        if (url.isNotEmpty()) {
            Log.d(tag, "open in browser[$url]")
        }

        Log.d(tag, "No source provided")
    }

    private fun refreshNews() {
        viewModel.getNews()
    }
}
