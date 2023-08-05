package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "compilations")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany
    @JoinTable(name = "events_in_compilations", joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private Set<Event> events;
    private Boolean pinned;
    private String title;

    public Compilation update(Compilation compilation) {
        if (compilation.events != null) {
            this.events = compilation.events;
        }
        if (compilation.pinned != null) {
            this.pinned = compilation.pinned;
        }
        if (compilation.title != null) {
            this.title = compilation.title;
        }
        return this;
    }
}
