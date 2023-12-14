package io.github.shymega.repaircafe.api.db.repositories;

import io.github.shymega.repaircafe.api.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {
}
