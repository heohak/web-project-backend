package com.taldate.backend.kanye.controller;

import com.taldate.backend.kanye.dto.KanyeQuoteDTO;
import com.taldate.backend.kanye.service.KanyeQuoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KanyeQuoteController {
    private final KanyeQuoteService kanyeQuoteService;

    @GetMapping("/api/quote")
    public ResponseEntity<KanyeQuoteDTO> getQuote() {
        KanyeQuoteDTO quote = kanyeQuoteService.getKanyeQuote();
        return ResponseEntity.ok(quote);
    }
}
