package io.github.shymega.repaircafe.api.db.repositories;

import io.github.shymega.repaircafe.api.entities.Repair;
import io.github.shymega.repaircafe.api.entities.RepairEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;
import java.util.UUID;

public interface RepairEventRepository extends JpaRepository<RepairEvent, UUID> {
    RepairEvent findRepairLatestEvent(Repair repairId, ZonedDateTime latestTimestamp);
}
