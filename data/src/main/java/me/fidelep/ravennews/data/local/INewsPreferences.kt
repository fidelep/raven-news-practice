package me.fidelep.ravennews.data.local

interface INewsPreferences {
    fun addToDeletedNews(deletedNews: Set<String>): Boolean

    fun getDeletedNews(): Set<String>
}
