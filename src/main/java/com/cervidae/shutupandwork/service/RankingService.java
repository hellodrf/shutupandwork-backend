package com.cervidae.shutupandwork.service;

import com.cervidae.shutupandwork.dao.RankingMapper;
import com.cervidae.shutupandwork.pojo.Ranking;
import com.cervidae.shutupandwork.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RankingService {

    final RankingMapper rankingMapper;

    private final Map<Integer, Ranking> rankingCache = new HashMap<>();

    @Autowired
    public RankingService(RankingMapper rankingMapper) {
        this.rankingMapper = rankingMapper;
    }

    /**
     * Get rankings of users, return cached if not expired
     * @param top length of the rankings
     * @return ranking
     */
    public Ranking getRankings(int top) {
        if (!rankingCache.containsKey(top) || rankingCache.get(top).isExpired(Constants.rankingCacheExpiry)) {
            getLatestRankings(top);
        }
        return rankingCache.get(top);
    }

    /**
     * Forced to pull latest rankings from database, ignore caches
     * @param top length of the rankings
     * @return ranking
     */
    public Ranking getLatestRankings(int top) {
        Ranking ranking = new Ranking(rankingMapper.getRankings(top));
        rankingCache.put(top, ranking);
        return ranking;
    }
}
