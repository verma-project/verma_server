package org.deraproject.apps.server.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.deraproject.apps.server.enums.CafeEventEnum;
import org.deraproject.apps.server.utils.converters.CafeEventConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

@Table(name = "cafe_events")
@Entity(name = "CafeEvent")
@ToString
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CafeEvent implements Serializable {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(nullable = false)
    @Convert(converter = CafeEventConverter.class)
    private CafeEventEnum cafeEvent;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Cafe cafe;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default
    private ZonedDateTime eventTimestamp = ZonedDateTime.now();
}
