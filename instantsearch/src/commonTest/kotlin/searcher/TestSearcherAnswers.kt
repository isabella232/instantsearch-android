package searcher

import blocking
import com.algolia.search.model.IndexName
import mockClient
import respondBadRequest
import responseSearch
import shouldBeFalse
import shouldBeNull
import shouldBeTrue
import shouldEqual
import shouldNotBeNull
import kotlin.test.Test

@OptIn(com.algolia.instantsearch.ExperimentalInstantSearch::class)
class TestSearcherAnswers {

    private val client = mockClient()
    private val index = client.initIndex(IndexName("index"))
    private val clientError = respondBadRequest()
    private val indexError = clientError.initIndex(IndexName("index"))

    @Test
    fun searchShouldUpdateLoading() {
        val searcher = TestSearcherAnswers(index)
        var count = 0

        searcher.isLoading.subscribe { if (it) count++ }
        searcher.isLoading.value.shouldBeFalse()
        blocking { searcher.searchAsync().join() }
        count shouldEqual 1
    }

    @Test
    fun searchShouldUpdateResponse() {
        val searcher = TestSearcherAnswers(index)
        var responded = false

        searcher.response.subscribe { responded = true }
        searcher.response.value.shouldBeNull()
        blocking { searcher.searchAsync().join() }
        searcher.response.value shouldEqual responseSearch
        responded.shouldBeTrue()
        searcher.error.value.shouldBeNull()
    }

    @Test
    fun searchShouldUpdateError() {
        val searcher = TestSearcherAnswers(indexError)
        var error = false

        searcher.error.subscribe { error = true }
        searcher.error.value.shouldBeNull()
        blocking { searcher.searchAsync().join() }
        searcher.error.value.shouldNotBeNull()
        searcher.response.value.shouldBeNull()
        error.shouldBeTrue()
    }
}
