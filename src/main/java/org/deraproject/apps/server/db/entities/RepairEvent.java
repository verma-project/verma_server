package org.deraproject.apps.server.db.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.deraproject.apps.server.enums.RepairEventEnum;
import org.deraproject.apps.server.utils.converters.RepairEventConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

@Table(name = "repair_events")
@Entity(name = "RepairEvent")
@ToString
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class RepairEvent implements Serializable {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Repair repair;

    @Convert(converter = RepairEventConverter.class)
    @Column(nullable = false)
    private RepairEventEnum repairEvent;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default
    private ZonedDateTime eventTimestamp = ZonedDateTime.now();
}
