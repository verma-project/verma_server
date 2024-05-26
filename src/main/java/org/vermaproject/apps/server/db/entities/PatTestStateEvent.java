package org.vermaproject.apps.server.db.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.vermaproject.apps.server.enums.PatTestResultEnum;
import org.vermaproject.apps.server.enums.PatTestStateEventEnum;
import org.vermaproject.apps.server.utils.converters.PatTestStateEventConverter;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Table(name = "pat_test_state_events")
@Entity(name = "PatTestStateEvent")
@ToString
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class PatTestStateEvent extends BaseEntity implements Serializable {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private PatTest patTest;

    @Convert(converter = PatTestStateEventConverter.class)
    @Column(nullable = false)
    private PatTestStateEventEnum patTestStateEvent;

    @Column(nullable = false)
    @NotEmpty
    private PatTestResultEnum patTestResult;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default
    private ZonedDateTime eventTimestamp = ZonedDateTime.now();
}
