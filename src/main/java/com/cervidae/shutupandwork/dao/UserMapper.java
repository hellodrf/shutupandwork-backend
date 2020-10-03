package com.cervidae.shutupandwork.dao;

import com.cervidae.shutupandwork.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository("UserMapper")
public interface UserMapper {

    @Select("SELECT * FROM USER WHERE username=#{username}")
    User findByUsername(@Param("username") String username);

    @Select("SELECT * FROM USER WHERE id=#{id}")
    User findByID(@Param("id") long id);

    @Insert("INSERT INTO USER VALUES (NULL, #{username}, #{score}")
    boolean add(@Param("username") String username, @Param("score") String score);

}