package io.github.shymega.repaircafe.api.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.shymega.repaircafe.api.domain.enums.RepairEvent;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

/* Backed by Postgres native */

@Entity
@Table(name = "repairs")
@ToString
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Repair implements Serializable {
    @Id
    @Column(nullable = false)
    @Setter(AccessLevel.NONE)
    private long id;

    @OneToMany(mappedBy = "repair_id")
    private Set<Repairer> repairers;

    @OneToMany(mappedBy = "repair_id")
    private Set<RepairEvent> events;
}
