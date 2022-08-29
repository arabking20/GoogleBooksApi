package com.example.googlebooksapi.model

import com.example.googlebooksapi.model.remote.BookApi
import com.example.googlebooksapi.model.remote.BookService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.delay

class RepositoryImpl constructor
    (private val bookService: BookApi): Repository {
    override fun getBookByName(bookTitle: String,
                               bookSize: String,
                               printType: String): Flow<UIState> {
        return flow {
            // todo verify if user is authenticated
            // todo verify if device is ONLINE
            // request data, using bookService.getBookTitle(bookTitle)
            // emit loading
            // evalute if data is success emit Success
            // else emit error

            emit(Loading())

            delay(600)
            val response = bookService.api.getBook(bookTitle, bookSize, printType)

            if (response.isSuccessful) {
                response.body()?.let { emit(Response(it)) } ?: emit(Empty)
            } else {
                emit(Failure(response.message()))
            }
        }
    }

}

