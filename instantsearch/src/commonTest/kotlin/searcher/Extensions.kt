package searcher

import com.algolia.instantsearch.helper.searcher.SearcherAnswers
import com.algolia.instantsearch.helper.searcher.SearcherScope
import com.algolia.instantsearch.helper.searcher.facets.FacetsSearcher
import com.algolia.instantsearch.helper.searcher.hits.HitsSearcher
import com.algolia.search.client.ClientSearch
import com.algolia.search.client.Index
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import kotlinx.coroutines.Dispatchers

val TestCoroutineScope = SearcherScope(Dispatchers.Default)

fun TestSearcherSingle(client: ClientSearch, indexName: IndexName) = HitsSearcher(
    client = client,
    indexName = indexName,
    isDisjunctiveFacetingEnabled = false,
    coroutineScope = TestCoroutineScope
)

fun TestSearcherForFacets(client: ClientSearch, indexName: IndexName, attribute: Attribute) = FacetsSearcher(
    client = client,
    indexName = indexName,
    attribute = attribute,
    coroutineScope = TestCoroutineScope
)

@OptIn(com.algolia.instantsearch.ExperimentalInstantSearch::class)
fun TestSearcherAnswers(index: Index) = SearcherAnswers(
    index = index,
    coroutineScope = TestCoroutineScope
)
