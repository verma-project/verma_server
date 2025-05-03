// SPDX-FileCopyrightText: 2023-2025 Dom Rodriguez (shymega) <shymega@shymega.org.uk>
//
// SPDX-License-Identifier: AGPL-3.0-only

package org.vermaproject.apps.server.db.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.vermaproject.apps.server.enums.PatTestResult;
import org.vermaproject.apps.server.enums.PatTestState;
import org.vermaproject.apps.server.utils.converters.PatTestStateConverter;
import org.vermaproject.apps.server.utils.converters.PatTestResultConverter;

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

    @Convert(converter = PatTestStateConverter.class)
    @Column(nullable = false)
    private PatTestState patTestStateEvent;

    @Column(nullable = false)
    @NotEmpty
    @Convert(converter = PatTestResultConverter.class)
    private PatTestResult patTestResult;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default
    private ZonedDateTime eventTimestamp = ZonedDateTime.now();
}
