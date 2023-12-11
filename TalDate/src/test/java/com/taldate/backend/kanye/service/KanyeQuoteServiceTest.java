package com.taldate.backend.kanye.service;
import com.taldate.backend.kanye.dto.KanyeQuoteDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KanyeQuoteServiceTest {

    private static final String KANYE_REST_URL = "https://api.kanye.rest/";

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private KanyeQuoteService kanyeQuoteService;

    @Test
    void getKanyeQuote() {
        String expectedQuote = "I'm a creative genius";
        KanyeQuoteDTO mockResponse = new KanyeQuoteDTO(expectedQuote);
        lenient().when(restTemplate.getForObject(KANYE_REST_URL, KanyeQuoteDTO.class))
                .thenReturn(mockResponse);

        KanyeQuoteDTO result = kanyeQuoteService.getKanyeQuote();

        assertNotNull(result, "The result should not be null");
        assertNotNull(result.quote(), "The quote should not be null");
        assertTrue(true, "The quote should be a string");

    }
}