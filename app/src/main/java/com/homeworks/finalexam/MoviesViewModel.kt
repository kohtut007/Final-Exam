package com.homeworks.finalexam

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MoviesViewModel : ViewModel() {
    private val repository = MoviesRepository(TmdbApi.create())

    private val _popular = MutableLiveData<List<Movie>>()
    val popular: LiveData<List<Movie>> = _popular

    private val _nowPlaying = MutableLiveData<List<Movie>>()
    val nowPlaying: LiveData<List<Movie>> = _nowPlaying

    private val _upcoming = MutableLiveData<List<Movie>>()
    val upcoming: LiveData<List<Movie>> = _upcoming

    private val _favoriteState = MutableLiveData<Unit>()
    val favoriteState: LiveData<Unit> = _favoriteState

    private val favoritesObserver = Observer<Set<Int>> {
        markLists()
        _favoriteState.value = Unit
    }

    init {
        FavoritesStore.favorites.observeForever(favoritesObserver)
    }

    fun loadAll() {
        viewModelScope.launch {
            val pop = repository.fetchPopular()
            _popular.value = applyFavoriteState(pop)
        }
        viewModelScope.launch {
            val np = repository.fetchNowPlaying()
            _nowPlaying.value = applyFavoriteState(np)
        }
        viewModelScope.launch {
            val up = repository.fetchUpcoming()
            _upcoming.value = applyFavoriteState(up)
        }
    }

    private fun applyFavoriteState(list: List<Movie>): List<Movie> {
        list.forEach { it.isFavorite = FavoritesStore.contains(it.id) }
        return list
    }

    fun toggleFavorite(movie: Movie) {
        FavoritesStore.toggle(movie.id)
    }

    private fun markLists() {
        _popular.value = _popular.value?.map { it.copy(isFavorite = FavoritesStore.contains(it.id)) }
        _nowPlaying.value = _nowPlaying.value?.map { it.copy(isFavorite = FavoritesStore.contains(it.id)) }
        _upcoming.value = _upcoming.value?.map { it.copy(isFavorite = FavoritesStore.contains(it.id)) }
    }

    override fun onCleared() {
        FavoritesStore.favorites.removeObserver(favoritesObserver)
        super.onCleared()
    }
}


