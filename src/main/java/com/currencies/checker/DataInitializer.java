package com.currencies.checker;

import com.currencies.checker.services.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DataInitializer {

    private final RateService rateService;

    @Autowired
    public DataInitializer(RateService rateService) {
        this.rateService = rateService;
    }

    @PostConstruct
    public void getDataInit() {
        rateService.refreshRates();
    }
}
