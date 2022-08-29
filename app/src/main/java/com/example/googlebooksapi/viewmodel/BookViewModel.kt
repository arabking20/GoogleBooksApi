package com.example.googlebooksapi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.googlebooksapi.model.Empty
import com.example.googlebooksapi.model.Repository
import com.example.googlebooksapi.model.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(private val repository: Repository): ViewModel() {
private val _uiState: MutableStateFlow<UIState> = MutableStateFlow(Empty)
    val uiState: StateFlow<UIState> get() = _uiState
    fun searchBook(bookQuery: String,
                   bookQuerySize: String,
                   selectedPrintType: String) {
        viewModelScope.launch {
            repository.getBookByName(
                bookQuery,
                bookQuerySize,
                selectedPrintType).collect{
                    _uiState.value = it
            }
        }
    }
}