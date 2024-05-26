package org.vermaproject.apps.server.db.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;

import java.util.UUID;

@MappedSuperclass
@Getter
public class BaseEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @Version
    private Integer version;
}
