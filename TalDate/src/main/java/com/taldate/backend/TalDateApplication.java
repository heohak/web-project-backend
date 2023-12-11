package com.taldate.backend;

import com.taldate.backend.kanye.dto.KanyeQuoteDTO;
import com.taldate.backend.kanye.entity.KanyeQuote;
import com.taldate.backend.kanye.repository.QuoteRepository;
import com.taldate.backend.kanye.service.KanyeQuoteService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TalDateApplication implements CommandLineRunner {

    private final KanyeQuoteService kanyeQuoteService;

    private final QuoteRepository quoteRepository;

    public TalDateApplication(KanyeQuoteService kanyeQuoteService, QuoteRepository quoteRepository) {
        this.kanyeQuoteService = kanyeQuoteService;
        this.quoteRepository = quoteRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(TalDateApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        quoteRepository.deleteAll();


        while (quoteRepository.count() < 100) {
            KanyeQuoteDTO quoteDTO = kanyeQuoteService.getKanyeQuote();
            String quoteText = quoteDTO.quote();

            if (!quoteRepository.existsByQuote(quoteText)) {
                KanyeQuote kanyeQuote = new KanyeQuote();
                kanyeQuote.setQuote(quoteText);
                quoteRepository.save(kanyeQuote);
            }
        }
    }
}
