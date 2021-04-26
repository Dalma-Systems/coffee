package com.dalma.broker.api.controller;

import com.dalma.broker.service.SubscriptionService;
import com.dalma.contract.dto.subscription.BrokerSubscriptionInputDto;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.dalma.broker.contract.Paths.BASE_PATH;
import static com.dalma.broker.contract.Paths.BY_ID;
import static com.dalma.broker.contract.Paths.SUBSCRIPTION;

@Validated
@RestController
@RequestMapping(BASE_PATH + SUBSCRIPTION)
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createSubscription(@RequestBody BrokerSubscriptionInputDto subscription) {
        subscriptionService.createSubscription(subscription);
    }

    @DeleteMapping(BY_ID)
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") String id) {
        subscriptionService.deleteSubscription(id);
    }
}
