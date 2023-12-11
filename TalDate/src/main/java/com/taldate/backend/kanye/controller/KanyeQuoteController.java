package com.taldate.backend.kanye.controller;

import com.taldate.backend.kanye.dto.KanyeQuoteDTO;
import com.taldate.backend.kanye.entity.KanyeQuote;
import com.taldate.backend.kanye.repository.QuoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KanyeQuoteController {

    private final QuoteRepository quoteRepository;


    @GetMapping("/api/quote")
    public ResponseEntity<KanyeQuoteDTO> getQuote() {
        KanyeQuote kanyeQuote = quoteRepository.findRandom();
        return ResponseEntity.ok(new KanyeQuoteDTO(kanyeQuote.getQuote()));
    }

}
