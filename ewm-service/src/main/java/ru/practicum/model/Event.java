package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String annotation;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    @Column(name = "confirmed_requests")
    private Integer confirmedRequests;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User initiator;
    @Embedded
    private Location location;
    private Boolean paid;
    @Column(name = "participant_limit")
    private Integer participantLimit;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Column(name = "request_moderation")
    private Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    private State state;
    private String title;
    @Transient
    private Integer likes;
    @Transient
    private Integer dislikes;

    public Event update(Event event) {
        if (event.getAnnotation() != null) {
            this.annotation = event.annotation;
        }
        if (event.getCategory() != null) {
            this.category = event.category;
        }
        if (event.getEventDate() !=  null) {
            this.eventDate = event.eventDate;
        }
        if (event.getDescription() != null) {
            this.description = event.description;
        }
        if (event.getLocation() != null) {
            this.location = event.location;
        }
        if (event.getParticipantLimit() != null) {
            this.participantLimit = event.participantLimit;
        }
        if (event.getRequestModeration() != null) {
            this.requestModeration = event.requestModeration;
        }
        if (event.getPaid() != null) {
            this.paid = event.paid;
        }
        if (event.getTitle() != null) {
            this.title = event.title;
        }
        if (event.getState() != null) {
            this.state = event.state;
        }
        return this;
    }
}
