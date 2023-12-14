package io.github.shymega.repaircafe.api.db.repositories;

import io.github.shymega.repaircafe.api.entities.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VisitorRepository extends JpaRepository<Visitor, UUID> {
}
