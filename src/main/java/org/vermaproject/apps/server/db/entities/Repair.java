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
import org.vermaproject.apps.server.enums.RepairType;
import org.vermaproject.apps.server.utils.converters.RepairTypeConverter;
import org.vermaproject.apps.server.utils.converters.StringListConverter;
import org.vermaproject.apps.server.utils.converters.StringTrimConverter;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/* Backed by Postgres native */

@EqualsAndHashCode(callSuper = true)
@Table(name = "repairs")
@Entity(name = "Repair")
@ToString
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Repair extends BaseEntity implements Serializable {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(nullable = false)
    @NotEmpty
    @Convert(converter = StringListConverter.class)
    private List<String> details;

    @Column(nullable = false)
    @NotEmpty
    @Convert(converter = StringTrimConverter.class)
    private String itemValue;

    @Column(nullable = false)
    @NotEmpty
    @Convert(converter = StringTrimConverter.class)
    private String itemAge;

    @Column(nullable = false)
    @Convert(converter = RepairTypeConverter.class)
    private RepairType repairType;

    @Column(nullable = false)
    @NotEmpty
    @Convert(converter = StringListConverter.class)
    private String repairTypeCustom;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "repairs")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Volunteer> volunteers;

    @OneToMany(mappedBy = "repair", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<RepairStateEvent> repairStateEvents;

    @ManyToOne
    @JoinColumn(insertable = false, updatable = false)
    private Ticket ticket;

    private boolean isCustomRepairType() {
        return repairType.equals(RepairType.OTHER) && (repairTypeCustom != null && !repairTypeCustom.isEmpty());
    }
}
