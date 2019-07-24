package com.bookstore.repository;

import com.bookstore.model.Favorites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface FavoritesRepository extends JpaRepository<Favorites, Long> {

    List<Favorites> findAllByClient_UserName(String userName);

    @Transactional
    void deleteAllByClient_UserNameAndBook_Isbn(String userName, Long isbn);

    @Transactional
    void deleteAllByClient_UserName(String userName);
}
