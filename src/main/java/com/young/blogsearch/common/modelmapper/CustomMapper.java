package com.young.blogsearch.common.modelmapper;

import com.young.blogsearch.domain.BlogDto;
import com.young.blogsearch.domain.Response;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel="spring")
public interface CustomMapper {

    CustomMapper INSTANCE = Mappers.getMapper(CustomMapper.class);

    BlogDto map(Response response);
    BlogDto.Meta map(Response.Meta meta);
    BlogDto.Documents map(Response.Documents documents);
}
