package org.deraproject.apps.server.db.repositories;

import org.deraproject.apps.server.db.entities.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VisitorRepository extends JpaRepository<Visitor, UUID> {
}
