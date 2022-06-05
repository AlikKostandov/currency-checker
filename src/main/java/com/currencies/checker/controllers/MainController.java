package com.currencies.checker.controllers;

import com.currencies.checker.services.GIFService;
import com.currencies.checker.services.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/exchange")
public class MainController {

    private final RateService rateService;

    private final GIFService gifService;
    @Value("${giphy.rich}")
    private String richStatus;
    @Value("${giphy.broke}")
    private String brokeStatus;
    @Value("${giphy.idk}")
    private String idkStatus;
    @Value("${giphy.equals}")
    private String equalsStatus;

    @Autowired
    public MainController(RateService rateService, GIFService gifService){
        this.gifService = gifService;
        this.rateService = rateService;
    }


    @GetMapping("/currencies")
    public List<String> getCodes() {
        return rateService.getCurrenciesCodes();
    }


    @GetMapping("/reaction/{code}")
    public ResponseEntity<Map> getGif(@PathVariable String code) {
        int gifKey = 101;
        String gifStatus = this.idkStatus;
        if (code != null) {
            gifKey = rateService.comparisonStatus(code);
        }
        switch (gifKey) {
            case 1:
                gifStatus = this.richStatus;
                break;
            case -1:
                gifStatus = this.brokeStatus;
                break;
            case 0:
                gifStatus = this.equalsStatus;
                break;
        }
        return gifService.getGif(gifStatus);
    }
}
