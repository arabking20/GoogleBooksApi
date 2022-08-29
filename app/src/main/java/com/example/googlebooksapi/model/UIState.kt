package com.example.googlebooksapi.model

import com.example.googlebooksapi.model.remote.BookResponse

sealed interface UIState

data class Response(val data: BookResponse): UIState

data class Loading(val isLoading: Boolean= true): UIState

data class Failure(val reasoon: String): UIState

object Empty : UIState