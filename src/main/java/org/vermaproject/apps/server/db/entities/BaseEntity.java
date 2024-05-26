package org.vermaproject.apps.server.db.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;

@MappedSuperclass
@Getter
public class BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Version
    private Integer version;
}
