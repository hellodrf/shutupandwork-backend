package com.cervidae.shutupandwork.dao;

import com.cervidae.shutupandwork.pojo.Quote;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository("QuoteMapper")
public interface QuoteMapper {

    @Select("SELECT * FROM QUOTE " +
            "WHERE id=#{id}")
    Quote getQuoteByID(@Param("id") long id);

    // WIP: not ready for use
    @Select("SELECT * FROM QUOTE " +
            "WHERE id=#{id}")
    Quote[] getQuoteMultiple();

    @Select("SELECT QUOTE.* FROM QUOTE " +
            "JOIN (SELECT ROUND(RAND()*(SELECT MAX(id) FROM QUOTE)) AS id) AS t2 " +
            "ON QUOTE.id >= t2.id " +
            "ORDER BY QUOTE.id LIMIT 1")
    Quote getRandomQuote();

    @Insert("INSERT INTO QUOTE VALUES (NULL, #{quote}, #{type})")
    void addQuote(@Param("quote") String quote, @Param("type") int type);

    @Select("SELECT COUNT(id) FROM QUOTE")
    int count();

    @Delete("DELETE FROM QUOTE " +
            "WHERE id=#{id}")
    void delete(@Param("id") int id);
}
