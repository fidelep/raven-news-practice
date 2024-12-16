package me.fidelep.ravennews.domain.interfaces

interface INewsPreferences {
    fun addToDeletedNews(deletedNews: Set<String>): Boolean

    fun getDeletedNews(): Set<String>
}
