package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Like;
import ru.practicum.model.LikeId;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, LikeId> {

    @Query(value = "SELECT * FROM likes WHERE event_id = ? AND is_liked = ?;", nativeQuery = true)
    List<Like> findByEventIdAndIsLiked(Long eventId, Boolean isLiked);
}
