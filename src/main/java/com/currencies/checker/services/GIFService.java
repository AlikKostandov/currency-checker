package com.currencies.checker.services;

import com.currencies.checker.client.GIFSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GIFService {

    private final GIFSClient gifsClient;

    @Value("${giphy.api.key}")
    private String apiKey;


    @Autowired
    public GIFService(GIFSClient gifsClient)
    {
        this.gifsClient = gifsClient;
    }

    public ResponseEntity<Map> getGif(String status) {
        ResponseEntity<Map> resultGif = gifsClient.getRandomGif(this.apiKey, status);
        resultGif.getBody().put("compareResult", status);
        return resultGif;
    }
}
