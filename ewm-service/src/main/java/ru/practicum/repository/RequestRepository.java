package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Event;
import ru.practicum.model.Request;
import ru.practicum.model.User;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public interface RequestRepository extends JpaRepository<Request, Long> {

    Optional<Request> findByRequesterAndEvent(User user, Event event);

    List<Request> findAllByRequester(User requester);

    List<Request> findByEvent(Event event);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Request r SET r.status = REJECTED WHERE r.status = PENDING")
    void rejectLastRequest();
}
