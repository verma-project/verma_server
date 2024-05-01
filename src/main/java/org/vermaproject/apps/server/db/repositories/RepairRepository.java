package org.vermaproject.apps.server.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vermaproject.apps.server.db.entities.Repair;
import org.vermaproject.apps.server.enums.RepairTypeEnum;

import java.util.Set;
import java.util.UUID;

@Repository
public interface RepairRepository extends JpaRepository<Repair, UUID> {
    Set<Repair> findAllRepairsByRepairType(RepairTypeEnum repairType);
}
