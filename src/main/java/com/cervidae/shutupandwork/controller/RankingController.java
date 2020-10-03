package com.cervidae.shutupandwork.controller;

import com.cervidae.shutupandwork.pojo.Ranking;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RankingController {

    @GetMapping(value = "/ranking")
    public Ranking getRanking() {
        return new Ranking();
    }
}
