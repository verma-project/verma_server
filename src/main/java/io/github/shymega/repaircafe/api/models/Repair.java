package io.github.shymega.repaircafe.api.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.shymega.repaircafe.api.enums.RepairEventEnum;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

/* Backed by Postgres native */

@Table(name = "repairs")
@Entity
@ToString
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Repair implements Serializable {
    @Id
    @Column(nullable = false)
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(nullable = false)
    private boolean banned;

    @OneToMany(mappedBy = "repair_id")
    private Set<Volunteer> volunteers;

    @OneToMany(mappedBy = "repair_id")
    private Set<RepairEventEnum> events;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ticket_id", referencedColumnName = "id")
    private Ticket ticket;
}
