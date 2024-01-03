package org.deraproject.apps.server.db.repositories;

import org.deraproject.apps.server.db.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {
}
