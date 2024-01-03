package org.deraproject.apps.server.db.repositories;

import org.deraproject.apps.server.db.entities.TicketEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TicketEventRepository extends JpaRepository<TicketEvent, UUID> {
}
