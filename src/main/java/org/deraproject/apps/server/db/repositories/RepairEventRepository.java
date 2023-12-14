package org.deraproject.apps.server.db.repositories;

import org.deraproject.apps.server.entities.Repair;
import org.deraproject.apps.server.entities.RepairEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;
import java.util.UUID;

public interface RepairEventRepository extends JpaRepository<RepairEvent, UUID> {
    RepairEvent findRepairLatestEvent(Repair repairId, ZonedDateTime latestTimestamp);
}
