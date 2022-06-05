package com.currencies.checker.services;

import com.currencies.checker.client.CurrencyClient;
import com.currencies.checker.entities.RateCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



@Service
public class RateService {

    private RateCase previousRates;
    private RateCase currentRates;

    private final CurrencyClient currencyClient;

    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat timeFormat;

    @Value("${openexchangerates.app.id}")
    private String appId;

    @Value("${openexchangerates.base}")
    private String base;

    @Autowired
    public RateService(
            CurrencyClient currencyClient,
            @Qualifier(value = "date_bean") SimpleDateFormat dateFormat,
            @Qualifier(value = "time_bean") SimpleDateFormat timeFormat
    ) {
        this.currencyClient = currencyClient;
        this.dateFormat = dateFormat;
        this.timeFormat = timeFormat;
    }


    public List<String> getCurrenciesCodes() {
        if (this.currentRates.getRates() != null) {
            return new ArrayList<>(this.currentRates.getRates().keySet());
        }
        else return new ArrayList<>();
    }


    public void updateRates() {
        long currentTime = System.currentTimeMillis();
        this.updateCurrentRates(currentTime);
        this.updatePreviousRates(currentTime);
    }


    private void updateCurrentRates(long time) {
        if (
                this.currentRates == null ||
                        !timeFormat.format(Long.valueOf(this.currentRates.getTimestamp()) * 1000)
                                .equals(timeFormat.format(time))
        ) {
            this.currentRates = currencyClient.getLatestRates(this.appId);
        }
    }

    private void updatePreviousRates(long time) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        String today = dateFormat.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        String yesterday = dateFormat.format(calendar.getTime());

        if (
                this.previousRates == null
                        || (
                        !dateFormat.format(Long.valueOf(this.previousRates.getTimestamp()) * 1000)
                                .equals(yesterday)
                                && !dateFormat.format(Long.valueOf(this.previousRates.getTimestamp()) * 1000)
                                .equals(today)
                )
        ) {
            this.previousRates = currencyClient.getHistoricalRates(yesterday, appId);
        }
    }

    public int comparisonStatus(String code) {
        this.updateRates();

        Double previous = previousRates.getRates().get(code);
        Double current = currentRates.getRates().get(code);

        return previousRates.getRates() != null && currentRates.getRates() != null
                ? Double.compare(current, previous)
                : -101;
    }



}
