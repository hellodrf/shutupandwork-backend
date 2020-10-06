package com.cervidae.shutupandwork.dao;

import com.cervidae.shutupandwork.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository("RankingMapper")
public interface RankingMapper extends IMapper {

    @Select("SELECT * FROM USER ORDER BY score DESC LIMIT #{top}")
    User[] getRankings(@Param("top") int top);

}
