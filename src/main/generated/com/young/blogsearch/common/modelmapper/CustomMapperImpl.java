package com.young.blogsearch.common.modelmapper;

import com.young.blogsearch.domain.BlogDto;
import com.young.blogsearch.domain.BlogDto.BlogDtoBuilder;
import com.young.blogsearch.domain.BlogDto.Documents.DocumentsBuilder;
import com.young.blogsearch.domain.BlogDto.Meta.MetaBuilder;
import com.young.blogsearch.domain.Response;
import com.young.blogsearch.domain.Response.Documents;
import com.young.blogsearch.domain.Response.Meta;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-03-20T23:31:36+0900",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 17.0.6 (Amazon.com Inc.)"
)
@Component
public class CustomMapperImpl implements CustomMapper {

    @Override
    public BlogDto map(Response response) {
        if ( response == null ) {
            return null;
        }

        BlogDtoBuilder blogDto = BlogDto.builder();

        blogDto.meta( map( response.getMeta() ) );
        blogDto.documents( documentsListToDocumentsList( response.getDocuments() ) );

        return blogDto.build();
    }

    @Override
    public com.young.blogsearch.domain.BlogDto.Meta map(Meta meta) {
        if ( meta == null ) {
            return null;
        }

        MetaBuilder meta1 = com.young.blogsearch.domain.BlogDto.Meta.builder();

        meta1.totalCount( meta.getTotalCount() );
        meta1.pageableCount( meta.getPageableCount() );
        meta1.end( meta.isEnd() );

        return meta1.build();
    }

    @Override
    public com.young.blogsearch.domain.BlogDto.Documents map(Documents documents) {
        if ( documents == null ) {
            return null;
        }

        DocumentsBuilder documents1 = com.young.blogsearch.domain.BlogDto.Documents.builder();

        documents1.title( documents.getTitle() );
        documents1.contents( documents.getContents() );
        documents1.url( documents.getUrl() );
        documents1.blogName( documents.getBlogName() );
        documents1.thumbnail( documents.getThumbnail() );
        documents1.datetime( documents.getDatetime() );

        return documents1.build();
    }

    protected List<com.young.blogsearch.domain.BlogDto.Documents> documentsListToDocumentsList(List<Documents> list) {
        if ( list == null ) {
            return null;
        }

        List<com.young.blogsearch.domain.BlogDto.Documents> list1 = new ArrayList<com.young.blogsearch.domain.BlogDto.Documents>( list.size() );
        for ( Documents documents : list ) {
            list1.add( map( documents ) );
        }

        return list1;
    }
}
