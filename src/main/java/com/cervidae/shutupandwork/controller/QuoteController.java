package com.cervidae.shutupandwork.controller;

import com.cervidae.shutupandwork.pojo.Quote;
import com.cervidae.shutupandwork.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("quotes")
public class QuoteController {

    private final QuoteService quoteService;

    @Autowired
    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @GetMapping
    public Quote getQuote() {
        return quoteService.getRandomQuotes(1).get(0);
    }

    @GetMapping(params = {"count"})
    public List<Quote> getMultipleQuotes(@RequestParam int count) {
        return quoteService.getRandomQuotes(count);
    }

    @PostMapping(params = {"quote", "type"})
    public boolean addQuote(@RequestParam String quote, @RequestParam int type) {
        return quoteService.addQuote(quote, type);
    }
}