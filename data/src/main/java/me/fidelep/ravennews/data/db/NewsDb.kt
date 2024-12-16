package me.fidelep.ravennews.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import me.fidelep.ravennews.data.db.NewsDb.Companion.DB_VERSION
import me.fidelep.ravennews.data.db.dao.NewsStoryDao
import me.fidelep.ravennews.data.db.model.NewsStoryEntity

@Database(
    entities = [NewsStoryEntity::class],
    version = DB_VERSION,
    exportSchema = true,
)
abstract class NewsDb : RoomDatabase() {
    abstract fun newsDao(): NewsStoryDao

    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "db_news"
    }
}
