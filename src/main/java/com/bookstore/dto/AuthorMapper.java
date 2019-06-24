package com.bookstore.dto;

import com.bookstore.model.Author;

public class AuthorMapper {

    public static AuthorDto entityToDto(Author author){
        AuthorDto authorDto = new AuthorDto();
        authorDto.setFirstName(author.getFirstName());
        authorDto.setLastName(author.getLastName());
        authorDto.setShortBio(author.getShortBio());
        authorDto.setBooks(author.getBooks());
        authorDto.setId(author.getId());
        return authorDto;
    }

    public static Author dtoToEntity(AuthorDto authorDto){
        Author author = new Author();
        authorDto.setFirstName(authorDto.getFirstName());
        authorDto.setLastName(authorDto.getLastName());
        authorDto.setShortBio(authorDto.getShortBio());
        authorDto.setBooks(authorDto.getBooks());
        authorDto.setId(authorDto.getId());
        return author;
    }
}

