package ru.skypro.homework.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Modifying
    @Transactional
    @Query("update User u " +
            "SET u.password = :newPassword " +
            "where u.email = :email AND u.password = :oldPassword")
    public int updatePassword(@Param("newPassword") String newPassword,
                              @Param("email") String email,
                              @Param("oldPassword") String oldPassword);


    Optional<User> findByEmail(String userName);

}
