package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Event;
import ru.practicum.model.State;
import ru.practicum.model.User;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(value = "SELECT * FROM events WHERE user_id = ? ORDER BY id ASC LIMIT ? OFFSET ?;", nativeQuery = true)
    List<Event> findByUserId(Long id, Integer limit, Integer offset);

    Optional<Event> findByIdAndInitiator(Long id, User initiator);

    @Query(value = "SELECT * FROM events WHERE ((:ids) IS NULL OR user_id IN (:ids)) AND ((:states) IS NULL OR state " +
            "IN (:states)) AND ((:categories) IS NULL OR category_id IN (:categories)) AND (:rangeStart IS NULL OR " +
            "event_date > :rangeStart) AND (:rangeEnd IS NUll OR event_date < :rangeEnd) ORDER BY id LIMIT :limit " +
            "OFFSET :offset ;", nativeQuery = true)
    List<Event> getEvents(@Param("ids") List<Long> ids, @Param("limit") Integer limit,
                          @Param("categories") List<Long> categories,
                          @Param("offset") Integer offset, @Param("states") List<String> states,
                          @Param("rangeStart") LocalDateTime rangeStart, @Param("rangeEnd") LocalDateTime rangeEnd);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Event e SET e.confirmedRequests = e.confirmedRequests + 1 WHERE e.id = :id")
    void increaseConfirmedRequest(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Event e SET e.confirmedRequests = e.confirmedRequests - 1 WHERE e.id = :id")
    void decreaseConfirmedRequest(@Param("id") Long id);

    @Query(value = "SELECT * FROM events WHERE (:text IS NULL OR LOWER(annotation) LIKE :text OR " +
            "LOWER(description) LIKE :text) AND (:categories IS NULL OR category_id IN (:categories)) AND " +
            "(:paid IS NULL OR paid = :paid) AND event_date BETWEEN :rangeStart AND :rangeEnd AND state = 'PUBLISHED'" +
            " ORDER BY event_date LIMIT :limit OFFSET :offset ;", nativeQuery = true)
    List<Event> getPublicEventsSortByDate(@Param("text") String text, @Param("paid") Boolean paid,
                                                       @Param("categories") List<Long> categories,
                                                       @Param("rangeStart") LocalDateTime rangeStart,
                                                       @Param("rangeEnd") LocalDateTime rangeEnd,
                                                       @Param("limit") Integer limit, @Param("offset") Integer size);

    @Query(value = "SELECT * FROM events WHERE (:text IS NULL OR LOWER(annotation) LIKE :text OR " +
            "LOWER(description) LIKE :text) AND (:categories IS NULL OR category_id IN (:categories)) AND " +
            "(:paid IS NULL OR paid = :paid) AND event_date BETWEEN :rangeStart AND :rangeEnd AND state = 'PUBLISHED' " +
            "AND (participant_limit = 0 OR participant_limit > confirmed_requests) ORDER BY event_date LIMIT :limit " +
            "OFFSET :offset ;", nativeQuery = true)
    List<Event> getPublicEventsOnlyAvailableSortByDate(@Param("text") String text, @Param("paid") Boolean paid,
                                          @Param("categories") List<Long> categories,
                                          @Param("rangeStart") LocalDateTime rangeStart,
                                          @Param("rangeEnd") LocalDateTime rangeEnd,
                                          @Param("limit") Integer limit, @Param("offset") Integer size);

    Optional<Event> findByIdAndState(Long id, State state);
}