package com.currencies.checker;

import org.springframework.context.annotation.Bean;

import java.text.SimpleDateFormat;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean("date_bean")
    public SimpleDateFormat simpleDateFormatForDate() {
        return new SimpleDateFormat("yyyy-MM-dd");
    }

    @Bean("time_bean")
    public SimpleDateFormat simpleDateFormatForTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH");
    }

}
