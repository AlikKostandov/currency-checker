package com.currencies.checker.client;

import com.currencies.checker.entities.RateCase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@org.springframework.cloud.openfeign.FeignClient(name = "CurrencyClient", url = "${openexchangerates.url.general}")
public interface CurrencyClient{

    @GetMapping("/latest.json")
    RateCase getLatestRates(
            @RequestParam("app_id") String appId
    );

    @GetMapping("/historical/{date}.json")
    RateCase getHistoricalRates(
            @PathVariable String date,
            @RequestParam("app_id") String appId
    );

}