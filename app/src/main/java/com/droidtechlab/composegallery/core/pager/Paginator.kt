package com.droidtechlab.composegallery.core.pager

interface Paginator<Key, Item> {
    suspend fun loadNextItems()
    fun reset()
}