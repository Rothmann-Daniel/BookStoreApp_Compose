package com.danielrothmann.bookstoreapp.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danielrothmann.bookstoreapp.data.Book
import com.danielrothmann.bookstoreapp.data.local_db.repository.FavoritesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val _favoriteBooks = MutableStateFlow<List<Book>>(emptyList())
    val favoriteBooks: StateFlow<List<Book>> = _favoriteBooks.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            favoritesRepository.getAllFavorites().collect { books ->
                _favoriteBooks.value = books
                _isLoading.value = false
            }
        }
    }

    fun addToFavorites(book: Book) {
        viewModelScope.launch {
            favoritesRepository.addToFavorites(book)
        }
    }

    fun removeFromFavorites(book: Book) {
        viewModelScope.launch {
            favoritesRepository.removeFromFavorites(book.id)
        }
    }

    fun toggleFavorite(book: Book, isFavorite: Boolean) {
        viewModelScope.launch {
            if (isFavorite) {
                favoritesRepository.removeFromFavorites(book.id)
            } else {
                favoritesRepository.addToFavorites(book)
            }
        }
    }

    fun clearAllFavorites() {
        viewModelScope.launch {
            favoritesRepository.clearAllFavorites()
        }
    }
}