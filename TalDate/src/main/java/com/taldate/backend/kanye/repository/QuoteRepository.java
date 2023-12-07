package com.taldate.backend.kanye.repository;

import com.taldate.backend.kanye.entity.KanyeQuote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuoteRepository extends JpaRepository<KanyeQuote, Integer> {

    boolean existsByQuote(String quote);

    @Query(value = "SELECT * FROM kanye_quotes ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    KanyeQuote findRandom();

}
