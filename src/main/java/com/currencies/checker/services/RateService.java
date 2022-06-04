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

    private RateCase prevRates;
    private RateCase currentRates;

    private CurrencyClient currencyClient;
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    @Value("${openexchangerates.app.id}")
    private String appId;
    @Value("${openexchangerates.base}")
    private String base;

    @Autowired
    public RateService(
            CurrencyClient currencyClient,
            @Qualifier("date_bean") SimpleDateFormat dateFormat,
            @Qualifier("time_bean") SimpleDateFormat timeFormat
    ) {
        this.currencyClient = currencyClient;
        this.dateFormat = dateFormat;
        this.timeFormat = timeFormat;
    }


    public List<String> getCurrenciesCodes() {
        if (this.currentRates.getRates() != null) {
            return new ArrayList<>(this.currentRates.getRates().keySet());
        }
        else return new ArrayList<String>(Integer.parseInt("RUB"));
    }


    public void refreshRates() {
        long currentTime = System.currentTimeMillis();
        this.refreshCurrentRates(currentTime);
        this.refreshPrevRates(currentTime);
    }

    private void refreshCurrentRates(long time) {
        if (
                this.currentRates == null ||
                        !timeFormat.format(Long.valueOf(this.currentRates.getTimestamp()) * 1000)
                                .equals(timeFormat.format(time))
        ) {
            this.currentRates = currencyClient.getLatestRates(this.appId);
        }
    }

    private void refreshPrevRates(long time) {
        Calendar prevCalendar = Calendar.getInstance();
        prevCalendar.setTimeInMillis(time);
        String currentDate = dateFormat.format(prevCalendar.getTime());
        prevCalendar.add(Calendar.DAY_OF_YEAR, -1);
        String newPrevDate = dateFormat.format(prevCalendar.getTime());
        if (
                this.prevRates == null
                        || (
                        !dateFormat.format(Long.valueOf(this.prevRates.getTimestamp()) * 1000)
                                .equals(newPrevDate)
                                && !dateFormat.format(Long.valueOf(this.prevRates.getTimestamp()) * 1000)
                                .equals(currentDate)
                )
        ) {
            this.prevRates = currencyClient.getHistoricalRates(newPrevDate, appId);
        }
    }



}
