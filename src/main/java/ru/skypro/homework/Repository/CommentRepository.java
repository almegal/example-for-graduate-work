package ru.skypro.homework.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}