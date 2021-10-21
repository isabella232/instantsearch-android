package com.algolia.instantsearch.helper.searcher.multi.hits.internal

import com.algolia.instantsearch.helper.searcher.multi.internal.SearchService
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.filter.FilterGroup
import com.algolia.search.model.multipleindex.IndexQuery
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.response.ResultMultiSearch
import com.algolia.search.transport.RequestOptions

internal class HitsService(
    val client: ClientSearch
) : SearchService<HitsService.Request, ResponseSearch> {

    override suspend fun search(request: Request, requestOptions: RequestOptions?): ResponseSearch {
        val (query, filterGroups) = request
        return when {
            request.filterGroups.isNotEmpty() -> multiSearch(request, requestOptions)
            else -> indexSearch(request.indexQuery, requestOptions)
        }
    }

    private suspend fun multiSearch(request: Request, requestOptions: RequestOptions?): ResponseSearch {
        return advancedSearch(request.indexQuery, request.filterGroups) { queries ->
            val response = client.search(requests = queries, requestOptions = requestOptions)
            response.results.map { (it as ResultMultiSearch.Hits).response }
        }
    }

    private suspend fun indexSearch(indexQuery: IndexQuery, requestOptions: RequestOptions? = null): ResponseSearch {
        val index = client.initIndex(indexQuery.indexName)
        return index.search(indexQuery.query, requestOptions)
    }

    data class Request(
        val indexQuery: IndexQuery,
        val filterGroups: Set<FilterGroup<*>> = emptySet()
    )
}
