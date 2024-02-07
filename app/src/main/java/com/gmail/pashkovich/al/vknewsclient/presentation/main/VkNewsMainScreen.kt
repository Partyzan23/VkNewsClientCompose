package com.gmail.pashkovich.al.vknewsclient.presentation.main

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.gmail.pashkovich.al.vknewsclient.navigation.AppNavGraph
import com.gmail.pashkovich.al.vknewsclient.navigation.rememberNavigationState
import com.gmail.pashkovich.al.vknewsclient.presentation.ViewModelFactory
import com.gmail.pashkovich.al.vknewsclient.presentation.comments.CommentsScreen
import com.gmail.pashkovich.al.vknewsclient.presentation.news.NewsFeedScreen

@Composable
fun MainScreen() {

    val navigationState = rememberNavigationState()

    Scaffold(
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navigationState.navHostController.currentBackStackEntryAsState()
                val items = listOf(
                    NavigationItem.Home,
                    NavigationItem.Favorite,
                    NavigationItem.Profile
                )
                items.forEach { item ->
                    val selected = navBackStackEntry?.destination?.hierarchy?.any {
                        it.route == item.screen.route
                    } ?: false
                    BottomNavigationItem(
                        selected = selected,
                        onClick = {
                            if (!selected) {
                                navigationState.navigateTo(item.screen.route)
                            }
                        },
                        label = {
                            Text(text = stringResource(id = item.titleResId))
                        },
                        icon = {
                            Icon(
                                item.icon,
                                contentDescription = null
                            )
                        },
                        selectedContentColor = MaterialTheme.colors.onPrimary,
                        unselectedContentColor = MaterialTheme.colors.onSecondary
                    )
                }
            }
        }
    ) { paddingValues ->
        AppNavGraph(
            navHostController = navigationState.navHostController,
            newsFeedScreenContent = {
                NewsFeedScreen(
                    paddingValues = paddingValues,
                    onCommentClickListener = { feedPost ->
                        navigationState.navigateToComments(feedPost)
                    }
                )

            },
            commentsScreenContent = { feedPost ->
                CommentsScreen(
                    feedPost = feedPost,
                    onBackPressed = {
                        navigationState.navHostController.popBackStack()
                    }
                )
            },
            favoriteScreenContent = { Text(text = "Profile") },
            profileScreenContent = { Text(text = "Favorite") }
        )
    }
}
