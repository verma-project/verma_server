// SPDX-FileCopyrightText: 2023-2025 Dom Rodriguez (shymega) <shymega@shymega.org.uk>
//
// SPDX-License-Identifier: AGPL-3.0-only

package org.vermaproject.apps.server.db.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.vermaproject.apps.server.utils.converters.StringTrimConverter;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Table(name = "cafes")
@Entity(name = "Cafe")
@ToString
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Cafe extends BaseEntity implements Serializable {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(nullable = false, unique = true)
    @NotEmpty
    @Convert(converter = StringTrimConverter.class)
    private String cafeShortId;

    @Column(nullable = false, unique = true)
    @NotEmpty
    @Convert(converter = StringTrimConverter.class)
    private String cafeName;

    @Column(nullable = false, unique = true)
    @NotEmpty
    @Convert(converter = StringTrimConverter.class)
    private String cafeWebsite;

    @Column(nullable = false, unique = true)
    @Email
    @NotEmpty
    @Convert(converter = StringTrimConverter.class)
    private String cafeContactEmail;

    @Column(nullable = false)
    @OneToMany(mappedBy = "cafe")
    private Set<CafeStateEvent> cafeStateEvents;

    @OneToMany(mappedBy = "cafe")
    private Set<Ticket> tickets;

    @PrePersist
    @JsonIgnore
    private void populateCafeShortId() {
        if (cafeShortId != null) return;

        // Default short ID – four chars.
        cafeShortId = cafeName.trim()
            .substring(0, 3);
    }
}
