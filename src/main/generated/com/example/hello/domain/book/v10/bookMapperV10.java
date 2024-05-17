package com.example.hello.domain.book.v10;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-17T16:41:12+0900",
    comments = "version: 1.5.1.Final, compiler: javac, environment: Java 17.0.11 (Amazon.com Inc.)"
)
@Component
public class bookMapperV10 implements BookMapper {

    @Override
    public Book bookPostToBook(BookDto.Post requestBody) {
        if ( requestBody == null ) {
            return null;
        }

        Book book = new Book();

        book.setTitleKorean( requestBody.getTitleKorean() );
        book.setTitleEnglish( requestBody.getTitleEnglish() );
        book.setDescription( requestBody.getDescription() );
        book.setAuthor( requestBody.getAuthor() );
        book.setIsbn( requestBody.getIsbn() );
        book.setPublishDate( requestBody.getPublishDate() );

        return book;
    }

    @Override
    public Book bookPatchToBook(BookDto.Patch requestBody) {
        if ( requestBody == null ) {
            return null;
        }

        Book book = new Book();

        book.setBookId( requestBody.getBookId() );
        book.setTitleKorean( requestBody.getTitleKorean() );
        book.setTitleEnglish( requestBody.getTitleEnglish() );
        book.setDescription( requestBody.getDescription() );
        book.setAuthor( requestBody.getAuthor() );
        book.setIsbn( requestBody.getIsbn() );
        book.setPublishDate( requestBody.getPublishDate() );

        return book;
    }

    @Override
    public BookDto.Response bookToResponse(Book book) {
        if ( book == null ) {
            return null;
        }

        BookDto.Response.ResponseBuilder response = BookDto.Response.builder();

        response.bookId( book.getBookId() );
        response.titleKorean( book.getTitleKorean() );
        response.titleEnglish( book.getTitleEnglish() );
        response.description( book.getDescription() );
        response.author( book.getAuthor() );
        response.isbn( book.getIsbn() );
        response.publishDate( book.getPublishDate() );

        return response.build();
    }

    @Override
    public List<BookDto.Response> booksToResponse(List<Book> books) {
        if ( books == null ) {
            return null;
        }

        List<BookDto.Response> list = new ArrayList<BookDto.Response>( books.size() );
        for ( Book book : books ) {
            list.add( bookToResponse( book ) );
        }

        return list;
    }
}
