package com.bookstore.repository;

import com.bookstore.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query("SELECT r.book.isbn FROM Rating r WHERE r.client.userName=:userName")
    List<Long> findAllIsbnByUserName(@Param("userName") String userName);

    @Query("SELECT r.bookRating FROM Rating r WHERE r.book.isbn=:isbn")
    List<Double> findAllRatingByIsbn(@Param("isbn") Long isbn);

    @Transactional
    void deleteAllByClient_UserName(String userName);
}
