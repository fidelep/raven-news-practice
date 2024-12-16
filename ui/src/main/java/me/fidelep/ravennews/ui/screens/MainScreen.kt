package me.fidelep.ravennews.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import me.fidelep.ravennews.domain.models.NewsStoryModel
import me.fidelep.ravennews.ui.views.StoryListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    stories: List<NewsStoryModel>,
    isLoading: Boolean,
    onRefresh: () -> Unit,
    onItemClick: (String) -> Unit,
    onItemRemove: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pullState =
        rememberPullToRefreshState()
    val isRefreshing by remember {
        mutableStateOf(isLoading)
    }

    Surface(modifier = modifier) {
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            state = pullState,
            contentAlignment = Alignment.TopCenter,
        ) {
            LazyColumn(
                modifier =
                    Modifier
                        .fillMaxSize(),
            ) {
                items(stories) { story ->
                    val dismissState =
                        rememberSwipeToDismissBoxState(
                            confirmValueChange = {
                                if (it != SwipeToDismissBoxValue.EndToStart) {
                                    return@rememberSwipeToDismissBoxState false
                                }

                                onItemRemove(story.id)
                                return@rememberSwipeToDismissBoxState true
                            },
                            positionalThreshold = { it * .3f },
                        )

                    if (dismissState.currentValue != SwipeToDismissBoxValue.EndToStart) {
                        StoryListItem(
                            storyModel = story,
                            dismissState = dismissState,
                            { onItemClick(story.url) },
                        )
                        HorizontalDivider(color = Color.Gray)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    val mockedList = mutableListOf<NewsStoryModel>()
    for (i in 1..10) {
        mockedList.add(
            NewsStoryModel(
                id = i,
                author = "Author full name",
                title = "Title of the given article $i might be way too large",
                createdAt = "30h",
                url = "url",
            ),
        )
    }

    MainScreen(stories = mockedList, true, {}, {}, {})
}
