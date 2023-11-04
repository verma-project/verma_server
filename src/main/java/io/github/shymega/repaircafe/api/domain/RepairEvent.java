package io.github.shymega.repaircafe.api.domain;

import io.github.shymega.repaircafe.api.domain.converters.RepairEventConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.ZonedDateTime;

@Entity
@Table(name = "events")
@ToString
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepairEvent {
    @Id
    @Column(nullable = false)
    @Setter(AccessLevel.NONE)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "repair_id", nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Repair repair_id;

    @Convert(converter = RepairEventConverter.class)
    @Column(nullable = false)
    private RepairEvent repairEvent;

    @Column(nullable = false)
    private ZonedDateTime eventTimestamp;
}
