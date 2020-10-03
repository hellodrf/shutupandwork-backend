package com.cervidae.shutupandwork.dao;

import com.cervidae.shutupandwork.pojo.Quote;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository("QuoteMapper")
public interface QuoteMapper {

    Quote getQuoteByID();

    Quote[] getQuoteMultiple();

}
