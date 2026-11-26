package com.example.notes.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.notes.presentation.features.favourites.FavouritesScreen
import com.example.notes.presentation.features.notecreate.NoteCreateScreen
import com.example.notes.presentation.features.notedetail.NoteDetailScreen
import com.example.notes.presentation.features.notelist.NoteListScreen
import com.example.notes.presentation.features.recyclebin.RecycleBinScreen
import org.koin.compose.koinInject

private const val TRANSITION_DURATION_MS = 250

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    navigator: AppNavigator = koinInject(),
) {
    LaunchedEffect(navController, navigator) {
        navigator.actions.collect { action ->
            when (action) {
                is NavigationAction.Navigate -> navController.navigate(action.screen) {
                    launchSingleTop = true
                }
                is NavigationAction.NavigateBack -> if (navController.previousBackStackEntry != null) navController.popBackStack()
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.NoteListRoute,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(TRANSITION_DURATION_MS),
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(TRANSITION_DURATION_MS),
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(TRANSITION_DURATION_MS),
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(TRANSITION_DURATION_MS),
            )
        },
    ) {
        composable<Screen.NoteListRoute> {
            NoteListScreen()
        }

        composable<Screen.NoteCreateRoute> {
            NoteCreateScreen()
        }

        composable<Screen.NoteDetailRoute> {
            NoteDetailScreen()
        }

        composable<Screen.RecycleBinRoute> {
            RecycleBinScreen()
        }

        composable<Screen.FavouritesRoute> {
            FavouritesScreen()
        }
    }
}
