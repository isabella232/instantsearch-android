package com.algolia.instantsearch.helper.searcher

import com.algolia.instantsearch.core.ExperimentalInstantSearch
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.core.searcher.Sequencer
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.helper.searcher.internal.SearcherExceptionHandler
import com.algolia.instantsearch.helper.searcher.internal.withUserAgent
import com.algolia.search.client.Index
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.AnswersQuery
import com.algolia.search.transport.RequestOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * The component handling Algolia Answers search requests and managing the search sessions.
 */
@ExperimentalInstantSearch
public class SearcherAnswers(
    public var index: Index,
    public val answersQuery: AnswersQuery = AnswersQuery(""),
    public val requestOptions: RequestOptions? = null,
    override val coroutineScope: CoroutineScope,
) : Searcher<ResponseSearch> {

    override val isLoading: SubscriptionValue<Boolean> = SubscriptionValue(false)
    override val error: SubscriptionValue<Throwable?> = SubscriptionValue(null)
    override val response: SubscriptionValue<ResponseSearch?> = SubscriptionValue(null)

    private val sequencer = Sequencer()
    private val options = requestOptions.withUserAgent()
    private val exceptionHandler = SearcherExceptionHandler(this)

    override fun setQuery(text: String?) {
        text?.let { answersQuery.query = it }
    }

    override fun searchAsync(): Job {
        return coroutineScope.launch(exceptionHandler) {
            isLoading.value = true
            response.value = withContext(Dispatchers.Default) { search() }
            isLoading.value = false
        }.also {
            sequencer.addOperation(it)
        }
    }

    override suspend fun search(): ResponseSearch {
        return index.findAnswers(answersQuery = answersQuery, requestOptions = options)
    }

    override fun cancel() {
        sequencer.cancelAll()
    }
}
