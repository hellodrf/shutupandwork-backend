package com.cervidae.shutupandwork.controller;

import com.cervidae.shutupandwork.pojo.Quote;
import com.cervidae.shutupandwork.service.QuoteService;
import com.cervidae.shutupandwork.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
    public Response<Quote> getQuote() {
        Quote quote = quoteService.getRandomQuotes(1).get(0);
        return Response.success(quote);
    }

    @GetMapping(params = {"count"})
    public Response<List<Quote>> getMultipleQuotes(@RequestParam int count) {
        List<Quote> quotes = quoteService.getRandomQuotes(count);
        return Response.success(quotes);
    }

    @PostMapping(params = {"quote", "type"})
    public Response<?> addQuote(@RequestParam String quote, @RequestParam int type) {
        boolean success = quoteService.addQuote(quote, type);
        return success ? Response.success() : Response.fail();
    }
}