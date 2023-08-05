package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Compilation;

import java.util.List;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    @Query(value = "SELECT * FROM compilations WHERE (:pinned IS NULL OR pinned = :pinned) " +
            "ORDER BY id ASC LIMIT :limit OFFSET :offset ;", nativeQuery = true)
    List<Compilation> findByPinned(@Param("pinned") Boolean pinned, @Param("limit") Integer limit,
                             @Param("offset") Integer offset);
}
