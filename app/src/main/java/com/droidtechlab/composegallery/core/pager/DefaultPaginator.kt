package com.droidtechlab.composegallery.core.pager

import com.droidtechlab.composegallery.core.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

class DefaultPaginator<Key, Item>(
    private val initialKey: Key,
    private inline val onLoadUpdated: suspend (Boolean) -> Unit,
    private inline val onRequest: suspend (nextKey: Key) -> Flow<Result<List<Item>>>,
    private inline val getNextKey: suspend (List<Item>) -> Key,
    private inline val onError: suspend (Throwable?) -> Unit,
    private inline val onSuccess: suspend (items: List<Item>, newKey: Key) -> Unit
): Paginator<Key, Item> {

    private var currentKey = initialKey
    private var isMakingRequest = false

    override suspend fun loadNextItems() {
        if(isMakingRequest) {
            return
        }
        isMakingRequest = true
        onLoadUpdated(true)
        val result = onRequest(currentKey)
        isMakingRequest = false
        result.collectLatest {
            if(it.message == null && !it.data.isNullOrEmpty()) {
                currentKey = getNextKey(it.data!!)
                onSuccess(it.data!!, currentKey)
                onLoadUpdated(false)
            } else {
                onError(Throwable(it.message))
                onLoadUpdated(false)
                return@collectLatest
            }
         }
    }

    override fun reset() {
        currentKey = initialKey
    }
}