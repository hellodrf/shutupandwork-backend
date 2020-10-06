package com.cervidae.shutupandwork.service;

import com.cervidae.shutupandwork.dao.RankingMapper;
import com.cervidae.shutupandwork.pojo.Ranking;
import com.cervidae.shutupandwork.util.Constants;
import com.cervidae.shutupandwork.util.ICache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RankingService {

    final RankingMapper rankingMapper;

    final ICache<Integer, Ranking> iCache;

    @Autowired
    public RankingService(RankingMapper rankingMapper, ICache<Integer, Ranking> iCache) {
        this.rankingMapper = rankingMapper;
        this.iCache = iCache;
    }

    /**
     * Get rankings of users, return cached if not expired
     * @param top length of the rankings
     * @return ranking
     */
    public Ranking getRankings(int top) {
        Ranking cache = iCache.select(top);
        if (cache == null || cache.isExpired(Constants.rankingCacheExpiry)) {
            Ranking latest = getLatestRankings(top);
            iCache.put(top, latest);
            return latest;
        } else {
            return cache;
        }
    }

    /**
     * Forced to pull latest rankings from database, ignore caches
     * @param top length of the rankings
     * @return ranking
     */
    public Ranking getLatestRankings(int top) {
        return new Ranking(rankingMapper.getRankings(top));
    }
}
