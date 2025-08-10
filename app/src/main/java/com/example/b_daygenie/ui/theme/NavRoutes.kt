package com.example.b_daygenie.ui.theme

sealed class NavRoutes(val route: String) {
    data object Authorization : NavRoutes("authorization")
    data object Home : NavRoutes("home")
    data object ViewProfile : NavRoutes("view_profile")
    data object EditProfile : NavRoutes("edit_profile")
}