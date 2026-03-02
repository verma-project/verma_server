// SPDX-FileCopyrightText: 2023-2025 Dom Rodriguez (shymega) <shymega@shymega.org.uk>
//
// SPDX-License-Identifier: AGPL-3.0-only

package org.vermaproject.apps.server.db.entities;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;


@MappedSuperclass
@Getter
public class BaseEntity {
    @Version
    private Integer version;
}
