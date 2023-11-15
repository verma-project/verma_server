package io.github.shymega.repaircafe.api.db.repositories;

import io.github.shymega.repaircafe.api.models.RepairEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RepairEventRepository extends JpaRepository<RepairEvent, UUID> {
}
