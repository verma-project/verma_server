package io.github.shymega.repaircafe.api.db;

import io.github.shymega.repaircafe.api.domain.Repair;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepairRepository extends JpaRepository<Repair, Long> {
}
