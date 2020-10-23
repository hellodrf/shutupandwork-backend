package com.cervidae.shutupandwork.dao;

import com.cervidae.shutupandwork.pojo.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;


/**
 * @author AaronDu
 */
@Mapper
@Repository("UserMapper")
public interface UserMapper extends IMapper {

    @Select("SELECT * FROM USER " +
            "WHERE username=#{username}")
    User getByUsername(@Param("username") String username);

    @Select("SELECT * FROM USER " +
            "WHERE id=#{id}")
    User getById(@Param("id") int id);

    @Insert("INSERT INTO USER " +
            "VALUES (NULL, #{username}, 0, #{currentTime}, #{password}, #{rule})")
    void register(@Param("username") String username, @Param("password") String password,
                  @Param("currentTime") long currentTime, String rule);

    @Update("UPDATE USER " +
            "SET score=#{score}, updated=#{currentTime} " +
            "WHERE username=#{username} and updated=#{updated}")
    void updateScoreOptimistic(@Param("username") String username, @Param("score") int score,
                          @Param("updated") long updated, @Param("currentTime") long currentTime);
}