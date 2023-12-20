package com.example.bookshelf.data

import com.example.bookshelf.data.BookshelfRepository
import com.example.bookshelf.network.BookshelfApiService

interface AppContainer {
    val bookshelfApiService: BookshelfApiService
    val bookshelfRepository: BookshelfRepository
}
