package com.cervidae.shutupandwork.controller;

import com.cervidae.shutupandwork.pojo.Quote;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuoteController {

    @GetMapping(name = "/quote")
    public Quote getQuote() {
        return new Quote();
    }
}
