package com.cervidae.shutupandwork.service;

import com.cervidae.shutupandwork.dao.ICache;
import com.cervidae.shutupandwork.dao.QuoteMapper;
import com.cervidae.shutupandwork.pojo.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @author AaronDu
 */
@Service
public class QuoteService implements IService {

    private final QuoteMapper quoteMapper;

    private final ICache<Integer> integerICache;

    @Autowired
    public QuoteService(QuoteMapper quoteMapper, ICache<Integer> integerICache) {
        this.quoteMapper = quoteMapper;
        this.integerICache = integerICache;
        integerICache.setIdentifier(7);
    }

    /**
     * Get multiple random and unique quotes
     * @param count number of quotes to get
     * @return list of quotes
     */
    public List<Quote> getRandomQuotes(int count) throws DataAccessException {
        List<Quote> quotes = new ArrayList<>();
        count = Math.min(count, getCount());
        for (int i=0; i < count; i++) {
            Quote next = null;
            while (next==null||quotes.contains(next)) {
                next = quoteMapper.getRandom();
            }
            quotes.add(next);
        }
        Assert.notEmpty(quotes, "2002");
        return quotes;
    }

    public Quote getQuoteByID(int id) {
        return quoteMapper.getByID(id);
    }

    public Quote getRandomQuotesByType(int id, int count) {
        return quoteMapper.getByID(id);
    }

    /**
     * Add a quote to database (admin)
     * @param quote the quote
     * @param type type of the quote
     */
    public void addQuote(String quote, int type) {
        Assert.hasText(quote, "2001");
        quoteMapper.add(quote, type);
    }

    /**
     * Remove a quote from database (admin)
     * @param id id of the quote
     */
    public Quote deleteQuote(int id) {
        Quote quote = quoteMapper.getByID(id);
        Assert.notNull(quote, "2002");
        quoteMapper.delete(id);
        return quote;
    }

    private int getCount() {
        if (integerICache.contains("quoteCount")) {
            return integerICache.select("quoteCount");
        } else {
            int count = quoteMapper.count();
            integerICache.put("quoteCount", count);
            integerICache.setExpiry("quoteCount", 15);
            return count;
        }
    }
}
