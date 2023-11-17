package io.github.shymega.repaircafe.api.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@Table(name = "visitors")
@Entity
@ToString
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Visitor implements Serializable {
    @Id
    @Column(nullable = false)
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(nullable = false)
    private String visitorFn;

    @Column(nullable = false)
    private String visitorSn;

    // Null value allowed, only populate for take-home repairs.
    private String visitorAddress;

    /// Null value allowed, only populate for take-home repairs.
    private String visitorTel;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ticket_id", nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Set<Ticket> tickets;

    @Column(nullable = false)
    private boolean banned;
}
