package com.taldate.backend.kanye.service;

import com.taldate.backend.kanye.dto.KanyeQuoteDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class KanyeQuoteService {

    private static final String KANYE_REST_URL = "https://api.kanye.rest/";

    public KanyeQuoteDTO getKanyeQuote() {
        log.debug("Getting Kanye quote from {}", KANYE_REST_URL);
        return (new RestTemplate()).getForObject(KANYE_REST_URL, KanyeQuoteDTO.class);
    }


}
