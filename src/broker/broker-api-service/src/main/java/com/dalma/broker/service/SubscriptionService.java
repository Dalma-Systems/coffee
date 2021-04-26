package com.dalma.broker.service;

import com.dalma.broker.service.publisher.SubscriptionPublisher;
import com.dalma.contract.dto.subscription.BrokerSubscriptionInputDto;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {
    private final SubscriptionPublisher subscriptionPublisher;

    public SubscriptionService(SubscriptionPublisher subscriptionPublisher) {
        this.subscriptionPublisher = subscriptionPublisher;
    }

    public void createSubscription(BrokerSubscriptionInputDto subscriptionInputDto) {
        subscriptionPublisher.subscribe(subscriptionInputDto);
        // If needed the subscription_id is returned by orion as header:
        // Location: /v2/subscriptions/57458eb60962ef754e7c0998
    }

    public void deleteSubscription(String id) {
        subscriptionPublisher.deleteSubscription(id);
    }
}
