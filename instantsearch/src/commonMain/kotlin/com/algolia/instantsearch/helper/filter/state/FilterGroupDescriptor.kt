package com.algolia.instantsearch.helper.filter.state

import com.algolia.search.model.Attribute

/**
 * A descriptor of a filter group.
 */
public typealias FilterGroupDescriptor = Pair<Attribute, FilterOperator>
