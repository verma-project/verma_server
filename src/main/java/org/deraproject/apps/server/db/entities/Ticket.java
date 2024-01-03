package org.deraproject.apps.server.db.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.deraproject.apps.server.enums.TicketTypeEnum;
import org.deraproject.apps.server.utils.converters.TicketTypeConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
public class Ticket implements Serializable {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
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

    @OneToMany(mappedBy = "repairs", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Repair> repairs;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Cafe cafe;

    @OneToMany(mappedBy = "ticket", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<TicketEvent> ticketEvents;

    @Column(nullable = false)
    private Integer associatedItems = repairs.size();
}
