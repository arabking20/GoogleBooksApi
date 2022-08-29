package com.example.googlebooksapi.model.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {

    @GET(END_POINT)
    suspend fun getBook(
        @Query (Q_ARG)bookInput: String,
        @Query(RESULTS_ARG) bookSize: String,
        @Query(PRINT_TYPE_ARG) printType: String
    ): Response<BookResponse>
}

//https://www.googleapis.com/
// books/v1/volumes
// ?q=bible
// &maxResults=10
// &printType=BOOKS