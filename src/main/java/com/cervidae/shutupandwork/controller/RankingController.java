package com.cervidae.shutupandwork.controller;

import com.cervidae.shutupandwork.pojo.Ranking;
import com.cervidae.shutupandwork.service.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RankingController {

    final RankingService rankingService;

    @Autowired
    public RankingController(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    /**
     * Get rankings of users, return cached if not expired
     * @param top length of the rankings
     * @return ranking
     */
    @GetMapping(value = "rankings", params = {"top"})
    public Ranking getRanking(int top) {
        return rankingService.getRankings(top);
    }

    /**
     * Forced to pull latest rankings from database, ignore caches
     * Use parameter: 'forced'
     * @param top length of the rankings
     * @return ranking
     */
    @GetMapping(value = "rankings", params = {"top", "forced"})
    public Ranking getForcedRanking(int top) {
        return rankingService.getLatestRankings(top);
    }
}
