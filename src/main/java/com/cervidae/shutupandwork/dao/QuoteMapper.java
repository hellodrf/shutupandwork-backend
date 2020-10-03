package com.cervidae.shutupandwork.dao;

import com.cervidae.shutupandwork.pojo.Quote;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository("QuoteMapper")
public interface QuoteMapper {

    @Select("SELECT * FROM QUOTE " +
            "WHERE id=#{id}")
    Quote getQuoteByID(@Param("id") long id);

    @Select("SELECT * FROM QUOTE " +
            "WHERE id=#{id}")
    Quote[] getQuoteMultiple();

    @Select("SELECT QUOTE.* FROM QUOTE " +
            "JOIN (SELECT ROUND(RAND()*(SELECT MAX(id) FROM QUOTE)) AS id) AS t2 " +
            "ON quote.id >= t2.id " +
            "ORDER BY QUOTE.id LIMIT 1")
    Quote getRandomQuote();
}
