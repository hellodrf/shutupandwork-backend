package com.cervidae.shutupandwork.dao;

import com.cervidae.shutupandwork.pojo.Quote;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * @author AaronDu
 */
@Mapper
@Repository("QuoteMapper")
public interface QuoteMapper extends IMapper {

    @Select("SELECT * FROM QUOTE " +
            "WHERE id=#{id}")
    Quote getByID(@Param("id") long id);

    @Select("SELECT QUOTE.* FROM QUOTE " +
            "JOIN (SELECT ROUND(RAND()*(SELECT MAX(id) FROM QUOTE)) AS id) AS t2 " +
            "ON QUOTE.id >= t2.id " +
            "ORDER BY QUOTE.id LIMIT 1")
    Quote getRandom();

    @Select("SELECT QUOTE.* FROM QUOTE " +
            "JOIN (SELECT ROUND(RAND()*(SELECT MAX(id) FROM QUOTE)) AS id) AS t2 " +
            "ON QUOTE.id >= t2.id " +
            "WHERE QUOTE.type = #{type} " +
            "ORDER BY QUOTE.id LIMIT 1")
    Quote getRandomByType(@Param("type") int type);

    @Insert("INSERT INTO QUOTE VALUES (NULL, #{quote}, #{type})")
    void add(@Param("quote") String quote, @Param("type") int type);

    @Select("SELECT COUNT(id) FROM QUOTE")
    int count();

    @Delete("DELETE FROM QUOTE " +
            "WHERE id=#{id}")
    void delete(@Param("id") int id);
}
