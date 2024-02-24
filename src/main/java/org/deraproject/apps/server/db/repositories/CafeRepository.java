package org.deraproject.apps.server.db.repositories;

import org.deraproject.apps.server.db.entities.Cafe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CafeRepository extends JpaRepository<Cafe, UUID> {
}
