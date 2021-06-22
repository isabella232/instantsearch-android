package com.algolia.instantsearch.compose.relevantsort.internal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.algolia.instantsearch.compose.relevantsort.RelevantSortState

/**
 * Implementation of [RelevantSortState].
 *
 * @param value initial value
 */
internal class RelevantSortStateImpl<T>(value: T) : RelevantSortState<T> {

    override var value: T by mutableStateOf(value)
    override var didToggle: (() -> Unit)? = null

    override fun updateView(input: T) {
        this.value = input
    }
}
