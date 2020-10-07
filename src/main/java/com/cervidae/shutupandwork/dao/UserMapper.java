package com.cervidae.shutupandwork.dao;

import com.cervidae.shutupandwork.pojo.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository("UserMapper")
public interface UserMapper extends IMapper {

    @Select("SELECT * FROM USER " +
            "WHERE username=#{username}")
    User getByUsername(@Param("username") String username);

    @Insert("INSERT INTO USER " +
            "VALUES (NULL, #{username}, #{score}, 0)")
    void add(@Param("username") String username, @Param("score") int score);

    @Update("UPDATE USER " +
            "SET score=#{score} version=version+1" +
            "WHERE username=#{username}")
    void update(@Param("username") String username, @Param("score") int score);

    @Update("UPDATE USER " +
            "SET score=#{score} version=version+1" +
            "WHERE username=#{username} and version=#{version}")
    void updateOptimistic(@Param("username") String username, @Param("score") int score,
                          @Param("version") long version);
}