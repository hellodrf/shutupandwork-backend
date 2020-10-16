package com.cervidae.shutupandwork.service;

import com.cervidae.shutupandwork.dao.RankingMapper;
import com.cervidae.shutupandwork.pojo.Ranking;
import com.cervidae.shutupandwork.util.Constants;
import com.cervidae.shutupandwork.dao.ICache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author AaronDu
 */
@Service
public class RankingService implements IService {

    final RankingMapper rankingMapper;

    final ICache<Ranking> rankingICache;

    @Autowired
    public RankingService(RankingMapper rankingMapper, ICache<Ranking> rankingICache) {
        this.rankingMapper = rankingMapper;
        this.rankingICache = rankingICache;
        this.rankingICache.setIdentifier(1);
    }

    /**
     * Get rankings of users, return cached if not expired
     * @param top length of the rankings
     * @return ranking
     */
    public Ranking getRankings(int top) {
        String key = String.valueOf(top);
        Ranking cache = rankingICache.select(key);
        if (cache == null || cache.isExpired(Constants.RANKING_CACHE_EXPIRY)) {
            Ranking latest = getLatestRankings(top);
            rankingICache.put(key, latest);
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
