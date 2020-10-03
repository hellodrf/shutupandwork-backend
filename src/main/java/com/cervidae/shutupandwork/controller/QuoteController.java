package com.cervidae.shutupandwork.controller;

import com.cervidae.shutupandwork.pojo.Quote;
import com.cervidae.shutupandwork.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class QuoteController {

    private final QuoteService quoteService;

    @Autowired
    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @GetMapping(value = "quotes")
    public Quote getQuote() {
        return quoteService.getRandomQuotes(1).get(0);
    }

    @GetMapping(value = "quotes", params = {"count"})
    public List<Quote> getMultipleQuotes(@RequestParam int count) {
        return quoteService.getRandomQuotes(count);
    }
}