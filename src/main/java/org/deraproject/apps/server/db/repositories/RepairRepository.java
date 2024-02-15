package org.deraproject.apps.server.db.repositories;

import org.deraproject.apps.server.db.entities.Repair;
import org.deraproject.apps.server.enums.RepairTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RepairRepository extends JpaRepository<Repair, UUID> {
    List<Repair> findRepairsByRepairType(RepairTypeEnum repairType);
}
