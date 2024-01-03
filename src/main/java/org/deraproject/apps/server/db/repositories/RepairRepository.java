package org.deraproject.apps.server.db.repositories;

import org.deraproject.apps.server.db.entities.Repair;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RepairRepository extends JpaRepository<Repair, UUID> {
}
