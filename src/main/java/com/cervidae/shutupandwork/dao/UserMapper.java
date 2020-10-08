package com.cervidae.shutupandwork.dao;

import com.cervidae.shutupandwork.pojo.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Mapper
@Repository("UserMapper")
public interface UserMapper extends IMapper {

    @Select("SELECT * FROM USER " +
            "WHERE username=#{username}")
    User getByUsername(@Param("username") String username);

    @Insert("INSERT INTO USER " +
            "VALUES (NULL, #{username}, #{score}, #{currentTime})")
    void add(@Param("username") String username, @Param("score") int score, @Param("currentTime") long currentTime);

    @Update("UPDATE USER " +
            "SET score=#{score} " +
            "WHERE username=#{username}")
    void update(@Param("username") String username, @Param("score") int score);

    @Update("UPDATE USER " +
            "SET score=#{score}, updated=#{currentTime} " +
            "WHERE username=#{username} and updated=#{updated}")
    void updateOptimistic(@Param("username") String username, @Param("score") int score,
                          @Param("updated") long updated, @Param("currentTime") long currentTime);
}