package com.example.bookshelf.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bookshelf.Routes
import com.example.bookshelf.R
import com.example.bookshelf.ui.screens.detail_screen.DetailScreen
import com.example.bookshelf.ui.screens.detail_screen.DetailsViewModel
import com.example.bookshelf.ui.screens.search_screen.SearchScreen
import com.example.bookshelf.ui.screens.search_screen.BookshelfViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookshelfApp(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = Routes.valueOf(
        backStackEntry?.destination?.route ?: Routes.QueryScreen.name
    )
    val canNavigateBack = navController.previousBackStackEntry != null

    Scaffold(
        topBar = {
            MyTopAppBar(
                currentScreen = currentScreen,
                canNavigateBack = canNavigateBack,
                onNavigateUpClicked = { navController.navigateUp() }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
            ) {
                NavHost(
                    navController = navController,
                    modifier = modifier
                )
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
    currentScreen: Routes,
    canNavigateBack: Boolean,
    onNavigateUpClicked: () -> Unit
) {
    TopAppBar(
        title = { Text(text = currentScreen.title) },
        navigationIcon = {
            if (canNavigateBack){
                IconButton(
                    onClick = onNavigateUpClicked
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(id = R.string.btn_try_again)
                    )
                }
            }
        }
    )
}

@Composable
fun NavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val viewModel: BookshelfViewModel = viewModel(factory = BookshelfViewModel.Factory)

    NavHost(
        navController = navController,
        startDestination = Routes.QueryScreen.name,
        modifier = modifier
    ) {

        composable(route = Routes.QueryScreen.name) {
            SearchScreen(
                viewModel = viewModel,
                retryAction = { viewModel.getBooks() },
                onDetailsClick = {
                    viewModel.selectedBookId = it.id
                    navController.navigate(Routes.DetailScreen.name)
                },
            )
        }

        composable(route = Routes.DetailScreen.name) {
            val detailViewModel: DetailsViewModel = viewModel(factory = DetailsViewModel.Factory)
            detailViewModel.getBook(viewModel.selectedBookId)
            DetailScreen(
                viewModel = detailViewModel,
                retryAction = { detailViewModel.getBook(viewModel.selectedBookId) },
            )
        }
    }
}
