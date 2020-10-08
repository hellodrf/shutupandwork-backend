package com.cervidae.shutupandwork.controller;

import com.cervidae.shutupandwork.pojo.Quote;
import com.cervidae.shutupandwork.service.QuoteService;
import com.cervidae.shutupandwork.util.Constants;
import com.cervidae.shutupandwork.util.Response;
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

    /**
     * Get a random quote
     * @return a quote
     */
    @GetMapping
    public Response<Quote> get() {
        Quote quote = quoteService.getRandomQuotes(1).get(0);
        return Response.success(quote);
    }

    /**
     * Get several random quotes
     * @return list of quotes
     */
    @GetMapping(params = {"count"})
    public Response<List<Quote>> getMultiple(@RequestParam int count) {
        List<Quote> quotes = quoteService.getRandomQuotes(count);
        return Response.success(quotes);
    }

    /**
     * Admin only: add a quote to database
     * @param quote the quote
     * @param type type of quote
     * @param password admin password, defined in Util.Constants
     * @return empty success response
     */
    @PostMapping(params = {"quote", "type", "password"})
    public Response<?> add(@RequestParam String quote, @RequestParam int type, @RequestParam String password) {
        if (!password.equals(Constants.adminPassword)) {
            throw new IllegalArgumentException("1001");
        }
        quoteService.addQuote(quote, type);
        return Response.success();
    }

    /**
     * Admin only: delete a quote by id
     * @param id quote id
     * @param password admin password, defined in Util.Constants
     * @return the deleted quote
     */
    @DeleteMapping(params = {"id", "password"})
    public Response<Quote> delete(@RequestParam int id, @RequestParam String password) {
        if (!password.equals(Constants.adminPassword)) {
            throw new IllegalArgumentException("1001");
        }
        Quote quote = quoteService.deleteQuote(id);
        return Response.success(quote);
    }
}