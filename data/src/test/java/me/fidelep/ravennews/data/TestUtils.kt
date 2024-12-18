package me.fidelep.ravennews.data

import me.fidelep.ravennews.data.api.models.GetNewsResponse
import me.fidelep.ravennews.data.api.models.NewsStoryDTO
import me.fidelep.ravennews.data.db.model.NewsStoryEntity
import me.fidelep.ravennews.data.db.model.toModel
import me.fidelep.ravennews.domain.models.NewsStoryWrapper

fun getMockedStoryEntities() =
    mutableListOf<NewsStoryEntity>().apply {
        for (i in 1..10) {
            add(
                NewsStoryEntity(
                    "$i",
                    i,
                    "Author $i",
                    "Title $i",
                    "2024-12-16",
                    "url $i",
                ),
            )
        }
    }

fun getMockedStoryDtos() =
    mutableListOf<NewsStoryDTO>().apply {
        for (i in 1..10) {
            add(
                NewsStoryDTO(
                    "$i",
                    i,
                    "Author $i",
                    "Title $i",
                    "2024-12-16",
                    "url $i",
                ),
            )
        }
    }

fun getMockedSuccess() = NewsStoryWrapper.Success(getMockedStoryEntities().map { it.toModel() })

fun getMockedApiResponse() = GetNewsResponse(getMockedStoryDtos())
