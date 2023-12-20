package com.example.bookshelf.ui.screens.search_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.bookshelf.R
import com.example.bookshelf.model.Book
import com.example.bookshelf.ui.screens.ErrorScreen
import com.example.bookshelf.ui.screens.LoadingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: BookshelfViewModel,
    retryAction: () -> Unit,
    onDetailsClick: (Book) -> Unit,
) {
    val uiState = viewModel.uiState.collectAsState().value
    val uiStateQuery = viewModel.uiStateSearch.collectAsState().value
    Column{
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            OutlinedTextField(
                value = uiStateQuery.query,
                onValueChange = { viewModel.updateQuery(it) },
                singleLine = true,
                placeholder = {
                    Text(text = stringResource(R.string.search_placeholder))
                }
            )

            Spacer(modifier = Modifier.width(16.dp))

            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { viewModel.getBooks(uiStateQuery.query)}
            )
        }

        if (uiStateQuery.searchStarted) {
            when (uiState) {
                is QueryUiState.Loading -> LoadingScreen()

                is QueryUiState.Success -> SearchResults(
                    viewModel = viewModel,
                    bookshelfList = uiState.bookshelfList,
                    modifier = modifier,
                    onDetailsClick = onDetailsClick,
                )
                is QueryUiState.Error ->
                    ErrorScreen(retryAction = retryAction, modifier)
            }
        }
    }
}


