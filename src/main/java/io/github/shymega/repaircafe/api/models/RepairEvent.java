package io.github.shymega.repaircafe.api.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.shymega.repaircafe.api.enums.RepairEventEnum;
import io.github.shymega.repaircafe.api.utils.converters.RepairEventConverter;
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
public class RepairEvent implements Serializable {
    @Id
    @Column(nullable = false)
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "repair_id", nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Repair repair_id;

    @Convert(converter = RepairEventConverter.class)
    @Column(nullable = false)
    private RepairEventEnum repairEvent;

    @Column(nullable = false)
    private ZonedDateTime eventTimestamp;
}
