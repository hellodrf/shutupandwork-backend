package com.cervidae.cbookings.dao;

import com.cervidae.cbookings.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Mapper
@Repository("userMapper")
public interface UserMapper {

    @Select("SELECT * FROM USER WHERE username=#{username}")
    public User findByUsername(@Param("username") String username);

    @Select("SELECT * FROM USER WHERE id=#{id}")
    public User findByID(@Param("id") long id);

    @Insert("INSERT INTO USER VALUES (NULL, #{username}, #{password}, #{name}, " +
            "#{address}, #{phone}, #{email}, #{extra}, #{roleId}, DEFAULT, DEFAULT)")
    public boolean add(@Param("username") String username,@Param("password") String password, @Param("name") String name,
                       @Param("address") String address, @Param("phone") String phone, @Param("email") String email,
                       @Param("extra") String extra, @Param("roleId") int roleId);

    @Select("SELECT R.name FROM USER AS U JOIN ROLE AS R ON U.roleid=R.id WHERE U.username=#{username}")
    public Set<String> getRoles(@Param("username") String username);

}
