package me.fidelep.ravennews.ui.navigation

sealed class Screens(
    val route: String,
) {
    object Main : Screens("main_route")

    object Web : Screens("web_route/{url}")
}
