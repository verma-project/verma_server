package io.github.shymega.repaircafe.api.db;

import io.github.shymega.repaircafe.api.domain.RepairEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepairEventRepository extends JpaRepository<RepairEvent, Long> {
}
