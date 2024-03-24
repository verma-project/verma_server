package org.deraproject.apps.server.db.repositories;

import org.deraproject.apps.server.db.entities.Repair;
import org.deraproject.apps.server.db.entities.RepairEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.UUID;

@Repository
public interface RepairEventRepository extends JpaRepository<RepairEvent, UUID> {
}
