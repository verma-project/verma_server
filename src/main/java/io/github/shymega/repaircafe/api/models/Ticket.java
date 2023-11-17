package io.github.shymega.repaircafe.api.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.shymega.repaircafe.api.enums.TicketTypeEnum;
import io.github.shymega.repaircafe.api.utils.converters.TicketTypeConverter;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@Table(name = "tickets")
@Entity
@ToString
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Ticket implements Serializable {
    @Id
    @Column(nullable = false)
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(nullable = false)
    @Convert(converter = TicketTypeConverter.class)
    private TicketTypeEnum ticketType;

    @OneToMany(mappedBy = "visitor_id")
    private Set<Visitor> visitor;

    @OneToOne(mappedBy = "repair")
    private Repair repair;
}
