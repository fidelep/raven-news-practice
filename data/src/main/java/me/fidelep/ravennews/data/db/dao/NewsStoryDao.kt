package me.fidelep.ravennews.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import me.fidelep.ravennews.data.db.model.NewsStoryEntity

@Dao
interface NewsStoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg stories: NewsStoryEntity)

    // We only want lastest data
    @Query("DELETE FROM ${NewsStoryEntity.TABLE_NAME}")
    suspend fun cleanTable()

    @Query("SELECT * FROM ${NewsStoryEntity.TABLE_NAME}")
    suspend fun getAll(): List<NewsStoryEntity>
}
