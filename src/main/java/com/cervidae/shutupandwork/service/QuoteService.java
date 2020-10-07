package com.cervidae.shutupandwork.service;

import com.cervidae.shutupandwork.dao.QuoteMapper;
import com.cervidae.shutupandwork.pojo.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuoteService implements IService {
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
                next = quoteMapper.getRandom();
            }
            quotes.add(next);
        }
        return quotes;
    }

    public Quote getQuoteByID(int id) {
        return quoteMapper.getByID(id);
    }

    public void addQuote(String quote, int type) {
        if (quote.equals("")) {
            throw new IllegalArgumentException("quote field must not be empty");
        }
        quoteMapper.add(quote, type);
    }

    public Quote deleteQuote(int id) {
        Quote quote = quoteMapper.getByID(id);
        quoteMapper.delete(id);
        return quote;
    }
}
