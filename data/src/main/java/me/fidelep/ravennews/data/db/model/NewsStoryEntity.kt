package me.fidelep.ravennews.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import me.fidelep.ravennews.data.db.model.NewsStoryEntity.Companion.TABLE_NAME
import me.fidelep.ravennews.domain.models.NewsStoryModel

@Entity(tableName = TABLE_NAME)
data class NewsStoryEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_OBJECT_ID)
    val objId: String,
    @ColumnInfo(name = COLUMN_STORY_ID)
    val storyId: Int,
    @ColumnInfo(name = COLUMN_AUTHOR)
    val author: String,
    @ColumnInfo(name = COLUMN_TITLE)
    val title: String?,
    @ColumnInfo(name = COLUMN_CREATED_AT)
    val createdAt: String?,
    @ColumnInfo(name = COLUMN_STORY_URL)
    val storyUrl: String?,
) {
    companion object {
        const val TABLE_NAME = "news_story"
        const val COLUMN_OBJECT_ID = "object_id"
        const val COLUMN_STORY_ID = "story_id"
        const val COLUMN_AUTHOR = "author"
        const val COLUMN_TITLE = "title"
        const val COLUMN_CREATED_AT = "created_at"
        const val COLUMN_STORY_URL = "story_url"
    }
}

fun NewsStoryEntity.Companion.fromModel(story: NewsStoryModel) =
    NewsStoryEntity(
        objId = story.objId,
        storyId = story.id,
        author = story.author,
        title = story.title,
        createdAt = story.createdAt,
        storyUrl = story.url,
    )

fun NewsStoryEntity.toModel() =
    NewsStoryModel(
        objId = objId,
        id = storyId,
        author = author,
        title = title ?: "",
        createdAt = createdAt ?: "",
        url = storyUrl ?: "",
    )
