package com.example.googlebooksapi.model

import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getBookByName(bookTitle: String,
                      bookSize : String,
                      printType: String): Flow<UIState>
}