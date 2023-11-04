package io.github.shymega.repaircafe.api.db;

import io.github.shymega.repaircafe.api.domain.Repairer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepairerRepository extends JpaRepository<Repairer, Long> {
}
