package com.taldate.backend.kanye.service;

import com.taldate.backend.kanye.dto.KanyeQuoteDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KanyeQuoteService {

    private static final String KANYE_REST_URL = "https://api.kanye.rest/";

    public KanyeQuoteDTO getKanyeQuote() {
        return (new RestTemplate()).getForObject(KANYE_REST_URL, KanyeQuoteDTO.class);
    }



}
