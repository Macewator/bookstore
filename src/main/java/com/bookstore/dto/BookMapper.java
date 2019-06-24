package com.bookstore.dto;

import com.bookstore.model.Book;

public class BookMapper {

    public static BookDto entityToDto(Book book){
        BookDto bookDto = new BookDto();
        bookDto.setIsbn(book.getIsbn());
        bookDto.setTitle(book.getTitle());
        bookDto.setDescription(book.getDescription());
        bookDto.setPrice(book.getPrice());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setPublisher(book.getPublisher());
        return bookDto;
    }

    public static Book dtoToEntity(BookDto bookDto){
        Book book = new Book();
        book.setIsbn(bookDto.getIsbn());
        book.setIsbn(bookDto.getIsbn());
        book.setTitle(bookDto.getTitle());
        book.setDescription(bookDto.getDescription());
        book.setPrice(bookDto.getPrice());
        book.setAuthor(bookDto.getAuthor());
        book.setPublisher(bookDto.getPublisher());
        return book;
    }
}
