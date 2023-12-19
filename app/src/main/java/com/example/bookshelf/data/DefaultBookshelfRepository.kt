package com.example.bookshelf.data

import android.util.Log
import com.example.bookshelf.model.Book
import com.example.bookshelf.network.BookshelfApiService

/**
 * Default Implementation of repository that retrieves volumes data from underlying data source.
 */
class DefaultBookshelfRepository(
    private val bookshelfApiService: BookshelfApiService
) : BookshelfRepository {
    /** Retrieves list of Volumes from underlying data source */
    // Notes: List<Book>? NULLABLE
    override suspend fun getBooks(query: String): List<Book>? {
        Log.d("a", "a1")
        return try {
            Log.d("a", "a2")
            val res = bookshelfApiService.getBooks(query)
            Log.d("a", "a3")
            if (res.isSuccessful) {
                res.body()?.items ?: emptyList()
            } else {
                Log.d("a", "a4")
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun getBook(id: String): Book? {
        return try {
            val res = bookshelfApiService.getBook(id)
            if (res.isSuccessful) {
                res.body()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}