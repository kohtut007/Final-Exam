package com.homeworks.finalexam

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object FavoritesStore {
    private val _favorites = MutableLiveData<Set<Int>>(emptySet())
    val favorites: LiveData<Set<Int>> = _favorites

    fun toggle(id: Int) {
        val current = _favorites.value ?: emptySet()
        _favorites.value = if (current.contains(id)) current - id else current + id
    }

    fun contains(id: Int): Boolean = _favorites.value?.contains(id) == true
}


