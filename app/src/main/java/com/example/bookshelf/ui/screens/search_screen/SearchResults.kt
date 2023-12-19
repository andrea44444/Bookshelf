package com.example.bookshelf.ui.screens.search_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bookshelf.R
import com.example.bookshelf.model.Book

@Composable
fun SearchResults(
    viewModel: BookshelfViewModel,
    bookshelfList: List<Book>,
    modifier: Modifier = Modifier,
    onDetailsClick: (Book) -> Unit,
) {
    if (!bookshelfList.isEmpty()) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(150.dp),
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(24.dp),
        ) {
            items(
                items = bookshelfList,
            ) {
                GridItem(
                    book = it,
                    onDetailsClick = onDetailsClick,
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GridItem(
    book: Book,
    modifier: Modifier = Modifier,
    onDetailsClick: (Book) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        onClick = { onDetailsClick(book) },
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)

    ) {
        Column(
            modifier = Modifier
                .padding(18.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                modifier = Modifier
                    .aspectRatio(.6f),
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(book.volumeInfo.imageLinks?.thumbnail)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                error = painterResource(id = R.drawable.ic_broken_image),
                placeholder = painterResource(id = R.drawable.loading_img),
                contentScale = ContentScale.FillBounds
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ExpandButton(
                    onClick = { expanded = !expanded },
                    expanded = expanded
                )
            }
            if (expanded) {
                Column() {
                    Text(
                        text = stringResource(R.string.book_title, book.volumeInfo.title),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = stringResource(R.string.book_subtitle, book.volumeInfo.subtitle),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = stringResource(R.string.book_authors, book.volumeInfo.allAuthors()),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }

}


@Composable
fun ExpandButton(
    onClick: () -> Unit,
    expanded: Boolean
) {
    IconButton(
        onClick = onClick
    ) {
        Icon(
            imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
            contentDescription = null
        )

    }
}
