package me.fidelep.ravennews.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.fidelep.ravennews.domain.models.NewsStoryModel

@Composable
fun StoryListItem(
    storyModel: NewsStoryModel,
    dismissState: SwipeToDismissBoxState,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        modifier =
            modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Color.White)
                .padding(8.dp)
                .clickable(onClick = onItemClick),
        backgroundContent = { DismissBackground() },
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(Color.White),
        ) {
            Text(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                style = MaterialTheme.typography.titleLarge,
                text = storyModel.title,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Light,
                fontSize = 16.sp,
                text = "${storyModel.author} - ${storyModel.createdAt}",
            )
        }
    }
}

@Composable
fun DismissBackground(modifier: Modifier = Modifier) {
    Box(
        modifier =
            modifier
                .background(Color.Red)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterEnd,
    ) {
        Text(
            text = "Delete",
            color = Color.White,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StoryListItemPreview() {
    val mockedStory =
        NewsStoryModel(
            author = "Author full name",
            title = "Title of the given article might be way too large",
            createdAt = "30h",
            url = "url",
        )
    StoryListItem(mockedStory, rememberSwipeToDismissBoxState(), {})
}
