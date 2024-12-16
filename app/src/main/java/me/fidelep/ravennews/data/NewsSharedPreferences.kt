package me.fidelep.ravennews.data

import android.content.Context
import me.fidelep.ravennews.data.local.INewsPreferences

class NewsSharedPreferences(
    context: Context,
) : INewsPreferences {
    private val pref = context.getSharedPreferences("news_preferences", Context.MODE_PRIVATE)

    override fun addToDeletedNews(deletedNews: Set<String>): Boolean =
        with(pref.edit()) {
            putStringSet(DELETED_NEWS, deletedNews)
            commit()
        }

    override fun getDeletedNews(): Set<String> = pref.getStringSet(DELETED_NEWS, setOf()) ?: setOf()

    companion object {
        private const val DELETED_NEWS = "deleted_news"
    }
}
