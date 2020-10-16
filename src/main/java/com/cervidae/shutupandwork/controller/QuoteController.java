package com.cervidae.shutupandwork.controller;

import com.cervidae.shutupandwork.pojo.Quote;
import com.cervidae.shutupandwork.service.QuoteService;
import com.cervidae.shutupandwork.util.Constants;
import com.cervidae.shutupandwork.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author AaronDu
 */
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

    @GetMapping(params = {"count"})
    public Response<List<Quote>> getMultipleByType(@RequestParam int count, @RequestParam int type) {
        List<Quote> quotes = quoteService.getRandomQuotesByType(count, type);
        return Response.success(quotes);
    }

    /**
     * Get a quote by id
     * @return a quote
     */
    @GetMapping(params = {"id"})
    public Response<Quote> getByID(@RequestParam int id) {
        Quote quote = quoteService.getQuoteByID(id);
        return Response.success(quote);
    }

    /**
     * Admin only: add a quote to database
     * @param quote the quote
     * @param type type of quote
     * @return empty success response
     */
    @PostMapping(value = "admin", params = {"quote", "type"})
    public Response<?> add(@RequestParam String quote, @RequestParam int type) {
        quoteService.addQuote(quote, type);
        return Response.success();
    }

    /**
     * Admin only: delete a quote by id
     * @param id quote id
     * @return the deleted quote
     */
    @DeleteMapping(value = "admin", params = {"id"})
    public Response<Quote> delete(@RequestParam int id) {
        Quote quote = quoteService.deleteQuote(id);
        return Response.success(quote);
    }


}