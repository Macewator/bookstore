package com.bookstore.dto;

import com.bookstore.model.Publisher;

public class PublisherMapper {

    public static PublisherDto entityToDto(Publisher publisher){
        PublisherDto publisherDto = new PublisherDto();
        publisherDto.setId(publisher.getId());
        publisherDto.setPublisherName(publisher.getPublisherName());
        publisherDto.setPublisherInfo(publisher.getPublisherInfo());
        publisherDto.setWwwPage(publisher.getWwwPage());
        publisherDto.setAddress(publisher.getAddress());
        publisherDto.setBooks(publisher.getBooks());
        return publisherDto;
    }

    public static Publisher dtoToEntity(PublisherDto publisherDto){
        Publisher publisher = new Publisher();
        publisher.setId(publisherDto.getId());
        publisher.setPublisherName(publisherDto.getPublisherName());
        publisher.setPublisherInfo(publisherDto.getPublisherInfo());
        publisher.setWwwPage(publisherDto.getWwwPage());
        publisher.setAddress(publisherDto.getAddress());
        publisher.setBooks(publisherDto.getBooks());
        return publisher;
    }
}
