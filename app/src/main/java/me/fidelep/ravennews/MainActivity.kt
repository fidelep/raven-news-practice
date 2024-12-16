package me.fidelep.ravennews

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import me.fidelep.ravennews.ui.navigation.Screens
import me.fidelep.ravennews.ui.screens.MainScreen
import me.fidelep.ravennews.ui.screens.WebScreen
import me.fidelep.ravennews.ui.states.UiEvents
import me.fidelep.ravennews.ui.theme.RavenNewsTheme
import me.fidelep.ravennews.viewmodels.MainActivityViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val tag = MainActivity::class.simpleName

    private val viewModel: MainActivityViewModel by viewModels()
    private var isRefreshing = false

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RavenNewsTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()

                Scaffold(topBar = {
                    if (navBackStackEntry?.destination?.route != Screens.Main.route) {
                        TopAppBar(
                            navigationIcon = {
                                IconButton(onClick = { navController.popBackStack() }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                        contentDescription = "go back",
                                    )
                                }
                            },
                            title = { Text("Back") },
                        )
                    }
                }) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screens.Main.route,
                        modifier = Modifier.padding(innerPadding),
                    ) {
                        composable(Screens.Main.route) {
                            viewModel.getNews()

                            MainScreen(
                                stories = viewModel.newsState.collectAsState(initial = listOf()).value,
                                isRefreshing,
                                onRefresh = { refreshNews() },
                                onItemClick = { url -> openInBrowser(url, navController) },
                                onItemRemove = { storyId -> removeItem(storyId) },
                            )
                        }

                        composable(Screens.Web.route) { navBackStackEntry ->
                            val url = navBackStackEntry.arguments?.getString("url")

                            url?.let { WebScreen(it) }
                        }
                    }
                }
            }
        }

        observeUiState()
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            viewModel.uiState.collect {
                when (it) {
                    is UiEvents.ERROR -> {
                        isRefreshing = false
                        Log.e(tag, it.toString())
                    }

                    UiEvents.LOADING -> {
                        isRefreshing = true
                        Log.d(tag, "Loading state")
                    }

                    UiEvents.IDLE -> {
                        isRefreshing = false
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

    private fun openInBrowser(
        url: String,
        navController: NavController,
    ) {
        if (url.isEmpty()) {
            Toast.makeText(this, "Url to story wasn't provided", Toast.LENGTH_SHORT).show().also {
                Log.d(tag, "No source provided")
            }
            return
        }

        isRefreshing = true

        val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
        navController
            .navigate(Screens.Web.route.replace("{url}", encodedUrl)) {
                popUpTo(Screens.Main.route) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }.also {
                Log.d(tag, "open in browser[$url]")
                isRefreshing = false
            }
    }

    private fun refreshNews() {
        viewModel.getNews()
        Log.d(tag, "Refreshing")
    }
}
