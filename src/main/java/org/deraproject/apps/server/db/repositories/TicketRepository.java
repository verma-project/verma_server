package org.deraproject.apps.server.db.repositories;

import org.deraproject.apps.server.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {
}
