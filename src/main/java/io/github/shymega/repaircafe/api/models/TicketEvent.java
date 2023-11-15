package io.github.shymega.repaircafe.api.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.shymega.repaircafe.api.enums.TicketEventEnum;
import io.github.shymega.repaircafe.api.utils.converters.RepairEventConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

@Table(name = "repair_events")
@Entity
@ToString
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TicketEvent implements Serializable {
    @Id
    @Column(nullable = false)
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "in_person_ticket_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private InPersonTicket inPersonTicketId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "online_ticket_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private OnlineTicket onlineTicketId;

    @Convert(converter = RepairEventConverter.class)
    @Column(nullable = false)
    private TicketEventEnum ticketEvent;

    @Column(nullable = false)
    private ZonedDateTime eventTimestamp;
}
