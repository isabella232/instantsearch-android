package com.algolia.instantsearch.core.subscription

import kotlin.properties.Delegates

public class SubscriptionValue<T>(initialValue: T) : Subscription<T>() {

    public var value: T by Delegates.observable(initialValue) { _, _, newValue ->
        notifyAll(newValue)
    }

    public fun subscribePast(subscription: (T) -> Unit) {
        subscription(value)
        subscriptions += subscription
    }

    public fun notifySubscriptions() {
        notifyAll(value)
    }
}
