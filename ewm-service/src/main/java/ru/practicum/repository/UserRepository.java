package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT * FROM users ORDER BY id ASC LIMIT ? OFFSET ?;", nativeQuery = true)
    List<User> getUserByLimit(Integer limit, Integer offset);

    @Query(value = "SELECT * FROM users WHERE id IN :ids ORDER BY id LIMIT :limit OFFSET :offset ;", nativeQuery = true)
    List<User> getUserByLimit(@Param("ids") List<Long> ids, @Param("limit") Integer limit,
                              @Param("offset") Integer offset);
}
