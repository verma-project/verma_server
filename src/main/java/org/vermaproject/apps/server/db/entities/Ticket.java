package org.vermaproject.apps.server.db.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.vermaproject.apps.server.enums.TicketTypeEnum;
import org.vermaproject.apps.server.utils.converters.TicketTypeConverter;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@Table(name = "tickets")
@Entity(name = "Ticket")
@ToString
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Ticket implements Serializable {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(nullable = false)
    @Convert(converter = TicketTypeConverter.class)
    private TicketTypeEnum ticketType;

    @ManyToOne(fetch = FetchType.LAZY,
        cascade = CascadeType.ALL
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Visitor visitor;

    @OneToMany(mappedBy = "ticket", fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Repair> repairs;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Cafe cafe;

    @OneToMany(mappedBy = "ticket", fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<TicketStateEvent> ticketStateEvents;

    @Column(nullable = false)
    private Integer associatedItems;

    @PrePersist
    @JsonIgnore
    @Transient
    public void populateFields() {
        if (this.associatedItems == null) this.associatedItems = repairs.size();
    }
}
