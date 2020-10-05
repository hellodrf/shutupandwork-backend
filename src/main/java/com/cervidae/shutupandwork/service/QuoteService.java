package com.cervidae.shutupandwork.service;

import com.cervidae.shutupandwork.dao.QuoteMapper;
import com.cervidae.shutupandwork.pojo.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuoteService {
    private final QuoteMapper quoteMapper;

    @Autowired
    public QuoteService(QuoteMapper quoteMapper) {
        this.quoteMapper = quoteMapper;
    }

    /**
     * Get multiple random and unique quotes
     * @param count number of quotes to get
     * @return list of quotes
     */
    public List<Quote> getRandomQuotes(int count) throws DataAccessException {
        List<Quote> quotes = new ArrayList<>();
        count = Math.min(count, quoteMapper.count());
        for (int i=0; i < count; i++) {
            Quote next = null;
            while (next==null||quotes.contains(next)) {
                next = quoteMapper.getRandomQuote();
            }
            quotes.add(next);
        }
        return quotes;
    }

    public boolean addQuote(String quote, int type) {
        return quoteMapper.addQuote(quote, type);
    }
}
