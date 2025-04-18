package com.pegio.feed.presentation.screen.postdetails.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.pegio.common.presentation.util.popNavigate
import com.pegio.feed.presentation.screen.postdetails.PostDetailsScreen
import kotlinx.serialization.Serializable

@Serializable
data object PostDetailsRoute

fun NavController.navigateToFeed() = navigate(route = PostDetailsRoute)
fun NavController.popNavigateToFeed() = popNavigate(route = PostDetailsRoute)

fun NavGraphBuilder.postDetailsScreen(

) {
    composable<PostDetailsRoute> {
        PostDetailsScreen(

        )
    }
}