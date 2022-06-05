package com.currencies.checker.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


@org.springframework.cloud.openfeign.FeignClient(name = "gifsClient", url = "${giphy.url.general}")
public interface GIFSClient {

    @GetMapping("/random")
    ResponseEntity<Map> getRandomGif(
            @RequestParam("api_key") String apiKey,
            @RequestParam("status") String status
    );

}
