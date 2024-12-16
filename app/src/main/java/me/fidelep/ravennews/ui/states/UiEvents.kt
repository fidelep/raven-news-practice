package me.fidelep.ravennews.ui.states

sealed class UiEvents {
    data class ERROR(
        val code: Int? = null,
        val message: String? = null,
    ) : UiEvents()

    object LOADING : UiEvents()

    object IDLE : UiEvents()
}
