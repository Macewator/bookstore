package com.bookstore.repository;

import com.bookstore.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByBook_Isbn(Long isbn);

    @Transactional
    void deleteAllByClient_UserName(String userName);
}
