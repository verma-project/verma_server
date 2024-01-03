package org.deraproject.apps.server.db.repositories;

import org.deraproject.apps.server.db.entities.CafeEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CafeEventRepository extends JpaRepository<CafeEvent, UUID> {
}
